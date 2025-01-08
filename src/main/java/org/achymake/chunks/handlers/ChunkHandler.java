package org.achymake.chunks.handlers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Userdata;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChunkHandler {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private DateHandler getDateHandler() {
        return getInstance().getDateHandler();
    }
    private Economy getEconomy() {
        return getInstance().getEconomy();
    }
    public File getFile(Chunk chunk) {
        return new File(chunk.getWorld().getWorldFolder(), getInstance().name() + "/" + getChunkKey(chunk) + ".yml");
    }
    public boolean exists(Chunk chunk) {
        return getFile(chunk).exists();
    }
    public FileConfiguration getConfig(Chunk chunk) {
        return YamlConfiguration.loadConfiguration(getFile(chunk));
    }
    public List<OfflinePlayer> getRecentOwners(Chunk chunk) {
        var listed = new ArrayList<OfflinePlayer>();
        if (exists(chunk)) {
            for (var uuidString : getConfig(chunk).getStringList("recent-owners")) {
                listed.add(getInstance().getOfflinePlayer(UUID.fromString(uuidString)));
            }
        }
        return listed;
    }
    public boolean isClaimed(Chunk chunk) {
        if (exists(chunk)) {
            return getOwner(chunk) != null;
        } else return false;
    }
    public OfflinePlayer getOwner(Chunk chunk) {
        var config = getConfig(chunk);
        if (config.isString("owner")) {
            return getInstance().getOfflinePlayer(UUID.fromString(config.getString("owner")));
        } else return null;
    }
    public void setOwner(Chunk chunk, OfflinePlayer offlinePlayer) {
        var file = getFile(chunk);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("name", offlinePlayer.getName());
        config.set("owner", offlinePlayer.getUniqueId().toString());
        config.set("settings.tnt", false);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
        var chunks = getUserdata().getConfig(offlinePlayer).getStringList("chunks." + chunk.getWorld().getName());
        chunks.add(String.valueOf(getChunkKey(chunk)));
        getUserdata().setStringList(offlinePlayer, "chunks." + chunk.getWorld().getName(), chunks);
    }
    public boolean isTNTAllowed(Chunk chunk) {
        return getConfig(chunk).getBoolean("settings.tnt");
    }
    public boolean toggleTNT(Chunk chunk) {
        var file = getFile(chunk);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set("settings.tnt", !config.getBoolean("settings.tnt"));
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
        return isTNTAllowed(chunk);
    }
    public boolean isExpired(Chunk chunk) {
        if (isClaimed(chunk)) {
            return getDateHandler().isExpired(getOwner(chunk));
        } else return false;
    }
    public String getName(Chunk chunk) {
        var config = getConfig(chunk);
        if (config.isString("name")) {
            return config.getString("name");
        } else return getOwner(chunk).getName();
    }
    public boolean isOwner(Chunk chunk, OfflinePlayer offlinePlayer) {
        return getOwner(chunk) == offlinePlayer;
    }
    public boolean isMember(Chunk chunk, OfflinePlayer offlinePlayer) {
        return getUserdata().getMembers(getOwner(chunk)).contains(offlinePlayer);
    }
    public boolean isBanned(Chunk chunk, OfflinePlayer offlinePlayer) {
        return getUserdata().getBanned(getOwner(chunk)).contains(offlinePlayer);
    }
    public boolean hasAccess(Chunk chunk, OfflinePlayer offlinePlayer) {
        return isOwner(chunk, offlinePlayer) || isMember(chunk, offlinePlayer) || getUserdata().isEditor(offlinePlayer);
    }
    public void removeOwner(Chunk chunk) {
        if (isClaimed(chunk)) {
            var owner = getOwner(chunk);
            var file = getFile(chunk);
            var config = YamlConfiguration.loadConfiguration(file);
            getEconomy().depositPlayer(owner, getConfig().getDouble("economy.refund"));
            var chunks = getUserdata().getConfig(owner).getStringList("chunks." + chunk.getWorld().getName());
            chunks.remove(String.valueOf(getChunkKey(chunk)));
            getUserdata().setStringList(owner, "chunks." + chunk.getWorld().getName(), chunks);
            var recentOwners = config.getStringList("recent-owners");
            recentOwners.remove(owner.getUniqueId().toString());
            recentOwners.add(owner.getUniqueId().toString());
            config.set("owner", null);
            config.set("recent-owners", recentOwners);
            try {
                config.save(file);
            } catch (IOException e) {
                getInstance().sendWarning(e.getMessage());
            }
        }
    }
    public long getChunkKey(Chunk chunk) {
        return (long) chunk.getX() & 4294967295L | ((long) chunk.getZ() & 4294967295L) << 32;
    }
    private boolean isAllowed(ApplicableRegionSet applicableRegionSet) {
        for (var regionIn : applicableRegionSet) {
            var flag = regionIn.getFlag(getInstance().getFlag());
            if (flag == StateFlag.State.ALLOW) {
                return true;
            } else if (flag == StateFlag.State.DENY) {
                return false;
            }
        }
        return false;
    }
    public boolean isAllowedClaim(Chunk chunk) {
        if (getConfig().getStringList("worlds").contains(chunk.getWorld().getName())) {
            var regionManager = getInstance().getWorldGuard().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(chunk.getWorld()));
            if (regionManager != null) {
                var x = chunk.getX() << 4;
                var z = chunk.getZ() << 4;
                var protectedCuboidRegion = new ProtectedCuboidRegion("_", BlockVector3.at(x, -64, z), BlockVector3.at(x + 15, 320, z + 15));
                if (regionManager.getApplicableRegions(protectedCuboidRegion).getRegions().isEmpty()) {
                    return true;
                } else return isAllowed(regionManager.getApplicableRegions(protectedCuboidRegion));
            } else return true;
        } else return false;
    }
    public void reload(Chunk chunk) {
        var file = getFile(chunk);
        if (file.exists()) {
            var config = YamlConfiguration.loadConfiguration(file);
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                getInstance().sendWarning(e.getMessage());
            }
            if (isExpired(chunk)) {
                removeOwner(chunk);
            }
        }
    }
    public boolean isPvpInsideClaims() {
        return getConfig().getBoolean("pvp-inside-claims");
    }
    public boolean isTNTBlockDamageDisabled() {
        return getConfig().getBoolean("disable-tnt-block-damage");
    }
    public boolean isRedstoneOnlyInClaims() {
        return getConfig().getBoolean("redstone-only-in-claims");
    }
    public boolean isBlockPlaceDisabled() {
        return getConfig().getBoolean("disable-block-place");
    }
    public boolean isBlockFertilizeDisabled() {
        return getConfig().getBoolean("disable-block-fertilize");
    }
    public boolean isBlockBreakDisabled() {
        return getConfig().getBoolean("disable-block-break");
    }
    public boolean isCauldronLevelChangeDisabled() {
        return getConfig().getBoolean("disable-cauldron-level-change");
    }
    public boolean isSignChangeDisabled() {
        return getConfig().getBoolean("disable-sign-change");
    }
    public boolean isFluidFromOutsideDisabled() {
        return getConfig().getBoolean("disable-fluid-from-outside");
    }
    public boolean isPistonFromOutsideDisabled() {
        return getConfig().getBoolean("disable-piston-from-outside");
    }
    public boolean isHarvestBlockDisabled(Material blockType) {
        return getConfig().getBoolean("disable-harvest-blocks." + blockType);
    }
    public boolean isPhysicalBlockDisabled(Material blockType) {
        return getConfig().getBoolean("disable-physical-blocks." + blockType);
    }
    public boolean isInteractBlockDisabled(Material blockType) {
        return getConfig().getBoolean("disable-interact-blocks." + blockType);
    }
    public boolean isChangeBlockDisabled(Material blockType) {
        return getConfig().getBoolean("disable-change-blocks." + blockType);
    }
    public boolean isBucketDisabled(Material material) {
        return getConfig().getBoolean("disable-buckets." + material);
    }
    public boolean isBedDisabled(Material blockType) {
        return getConfig().getBoolean("disable-beds." + blockType);
    }
    public boolean isFlyAllowed() {
        return getConfig().getBoolean("claim.manipulate-fly");
    }
}