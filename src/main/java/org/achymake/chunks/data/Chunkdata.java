package org.achymake.chunks.data;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.achymake.chunks.Chunks;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public record Chunkdata(Chunks plugin) {
    private File getDataFolder() {
        return plugin.getDataFolder();
    }
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    public File getFile(Chunk chunk) {
        return new File(getDataFolder(), "database/" + chunk.getWorld().getName() + "/" + getChunkKey(chunk) + ".yml");
    }
    public File getFile(String worldName, String longString) {
        return new File(getDataFolder(), "database/" + worldName + "/" + longString + ".yml");
    }
    public boolean exist(Chunk chunk) {
        return getFile(chunk).exists();
    }
    public FileConfiguration getConfig(Chunk chunk) {
        return YamlConfiguration.loadConfiguration(getFile(chunk));
    }
    public FileConfiguration getConfig(String worldName, String longString) {
        return YamlConfiguration.loadConfiguration(getFile(worldName, longString));
    }
    public StateFlag getFlag() {
        return plugin.getFlagChunksClaim();
    }
    public boolean isAllowedWorld(World world) {
        return getConfig().getStringList("worlds").contains(world.getName());
    }
    public boolean isAllowedClaim(Chunk chunk) {
        try {
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(chunk.getWorld()));
            if (regionManager != null) {
                int x = chunk.getX() << 4;
                int z = chunk.getZ() << 4;
                ProtectedCuboidRegion protectedCuboidRegion = new ProtectedCuboidRegion("_", BlockVector3.at(x, -64, z), BlockVector3.at(x + 15, 320, z + 15));
                for (ProtectedRegion regionIn : regionManager.getApplicableRegions(protectedCuboidRegion)) {
                    StateFlag.State flag = regionIn.getFlag(getFlag());
                    if (flag == StateFlag.State.ALLOW) {
                        return true;
                    } else if (flag == StateFlag.State.DENY) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
            return false;
        }
    }
    public boolean isDisableBlockPlace() {
        return getConfig().getBoolean("settings.disable-block-place");
    }
    public boolean isDisableBlockFertilize() {
        return getConfig().getBoolean("settings.disable-block-fertilize");
    }
    public boolean isDisableBlockBreak() {
        return getConfig().getBoolean("settings.disable-block-break");
    }
    public boolean isDisabledHarvestBlocks(Block block) {
        return getConfig().getStringList("settings.disabled-harvest-blocks").contains(block.getType().toString());
    }
    public boolean isDisabledInteractPhysicalBlocks(Block block) {
        return getConfig().getStringList("settings.disabled-interact-physical-blocks").contains(block.getType().toString());
    }
    public boolean isDisabledInteractBlocks(Block block) {
        return getConfig().getStringList("settings.disabled-interact-blocks").contains(block.getType().toString());
    }
    public boolean isDisableCauldronLevelChange() {
        return getConfig().getBoolean("settings.disable-cauldron-level-change");
    }
    public boolean isDisabledChangeBlocks(Block block) {
        return getConfig().getStringList("settings.disabled-change-blocks").contains(block.getType().toString());
    }
    public boolean isDisableBuckets(Material material) {
        return getConfig().getStringList("settings.disable-buckets").contains(material.toString());
    }
    public boolean isDisableShearBlocks(Block block) {
        return getConfig().getStringList("settings.disable-shear-blocks").contains(block.getType().toString());
    }
    public boolean isDisableSignChange() {
        return getConfig().getBoolean("settings.disable-sign-change");
    }
    public boolean isHostile(Entity entity) {
        return getConfig().getStringList("settings.hostile").contains(entity.getType().toString());
    }
    public boolean isFriendly(Entity entity) {
        return !getConfig().getStringList("settings.hostile").contains(entity.getType().toString());
    }
    public boolean isDisableEntityExplodeBlocks() {
        return getConfig().getBoolean("settings.disable-entity-explode-blocks");
    }
    public void setup(OfflinePlayer offlinePlayer, Chunk chunk) {
        File file = getFile(chunk);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("owner", offlinePlayer.getUniqueId().toString());
        config.set("date-claimed", offlinePlayer.getLastPlayed());
        config.set("settings.tnt", false);
        config.set("x", chunk.getX());
        config.set("z", chunk.getZ());
        try {
            getUserdata().addClaim(offlinePlayer, chunk);
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void remove(OfflinePlayer offlinePlayer, Chunk chunk) {
        getUserdata().removeClaim(offlinePlayer, chunk);
        getFile(chunk).delete();
    }
    public void removeAll(OfflinePlayer offlinePlayer) {
        for (String worldName : getUserdata().getConfig(offlinePlayer).getConfigurationSection("chunks").getKeys(false)) {
            for (String longString : getUserdata().getConfig(offlinePlayer).getStringList("chunks." + worldName)) {
                int x = getConfig(worldName, longString).getInt("x");
                int z = getConfig(worldName, longString).getInt("z");
                Chunk chunk = getServer().getWorld(worldName).getChunkAt(x, z);
                remove(offlinePlayer, chunk);
            }
        }
    }
    public void setOwner(Player player, OfflinePlayer offlinePlayer, Chunk chunk) {
        File file = getFile(chunk);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("owner", offlinePlayer.getUniqueId().toString());
        config.set("date-claimed", player.getLastPlayed());
        config.set("settings.tnt", false);
        config.set("x", chunk.getX());
        config.set("z", chunk.getZ());
        try {
            getUserdata().addClaim(offlinePlayer, chunk);
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
    public long getChunkKey(Chunk chunk) {
        return (long) chunk.getX() & 4294967295L | ((long) chunk.getZ() & 4294967295L) << 32;
    }
    public OfflinePlayer getOwner(Chunk chunk) {
        return getServer().getOfflinePlayer(UUID.fromString(getConfig(chunk).getString("owner")));
    }
    public int getClaimCount(Chunk chunk) {
        return getUserdata().getClaimCount(getOwner(chunk));
    }
    public String getDateClaimed(Chunk chunk) {
        return SimpleDateFormat.getDateInstance().format(Long.parseLong(getConfig(chunk).getString("date-claimed")));
    }
    public boolean isClaimed(Chunk chunk) {
        return exist(chunk);
    }
    public boolean isTNTAllowed(Chunk chunk) {
        return getConfig(chunk).getBoolean("settings.tnt");
    }
    public void toggleTNT(Chunk chunk, boolean value) {
        File file = getFile(chunk);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("settings.tnt", value);
        try {
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
    public boolean isOwner(OfflinePlayer offlinePlayer, Chunk chunk) {
        return offlinePlayer == getOwner(chunk);
    }
    public boolean isMember(OfflinePlayer offlinePlayer, Chunk chunk) {
        return getUserdata().getMembers(getOwner(chunk)).contains(offlinePlayer);
    }
    public boolean hasAccess(Player player, Chunk chunk) {
        if (isClaimed(chunk)) {
            return isOwner(player, chunk) || isMember(player, chunk) || plugin.isEditor(player);
        } else {
            return true;
        }
    }
    public List<String> getBanned(Chunk chunk) {
        if (isClaimed(chunk)) {
            return getConfig(chunk).getStringList("banned");
        } else {
            return new ArrayList<>();
        }
    }
    public boolean isBanned(Chunk chunk, Player player) {
        return getBanned(chunk).contains(player.getUniqueId().toString());
    }
    public void claimEffect(Player player, Chunk chunk) {
        Location location = player.getLocation();
        Particle particle = Particle.valueOf(getConfig().getString("claim.particle"));
        player.spawnParticle(particle, chunk.getBlock(8, 0, 0).getX(), location.getBlockY() - 1, chunk.getBlock(8, 0, 0).getZ(), 250, 4, 12, 0, 0);
        player.spawnParticle(particle, chunk.getBlock(0, 0, 8).getX(), location.getBlockY() - 1, chunk.getBlock(0, 0, 8).getZ(), 250, 0, 12, 4, 0);
        player.spawnParticle(particle, chunk.getBlock(15, 0, 8).getX() + 1, location.getBlockY() - 1, chunk.getBlock(15, 0, 8).getZ(), 250, 0, 12, 4, 0);
        player.spawnParticle(particle, chunk.getBlock(8, 0, 15).getX(), location.getBlockY() - 1, chunk.getBlock(8, 0, 15).getZ() + 1, 250, 4, 12, 0, 0);
    }
    public void claimSound(Player player) {
        player.playSound(player, Sound.valueOf(getConfig().getString("claim.sound.type")), (float) getConfig().getDouble("claim.sound.volume"), (float) getConfig().getDouble("claim.sound.pitch"));
    }
    public void unclaimEffect(Player player, Chunk chunk) {
        Location location = player.getLocation();
        Particle particle = Particle.valueOf(getConfig().getString("unclaim.particle"));
        player.spawnParticle(particle, chunk.getBlock(8, 0, 0).getX(), location.getBlockY() - 1, chunk.getBlock(8, 0, 0).getZ(), 250, 4, 12, 0, 0);
        player.spawnParticle(particle, chunk.getBlock(0, 0, 8).getX(), location.getBlockY() - 1, chunk.getBlock(0, 0, 8).getZ(), 250, 0, 12, 4, 0);
        player.spawnParticle(particle, chunk.getBlock(15, 0, 8).getX() + 1, location.getBlockY() - 1, chunk.getBlock(15, 0, 8).getZ(), 250, 0, 12, 4, 0);
        player.spawnParticle(particle, chunk.getBlock(8, 0, 15).getX(), location.getBlockY() - 1, chunk.getBlock(8, 0, 15).getZ() + 1, 250, 4, 12, 0, 0);
    }
    public void unclaimSound(Player player) {
        player.playSound(player, Sound.valueOf(getConfig().getString("unclaim.sound.type")), (float) getConfig().getDouble("unclaim.sound.volume"), (float) getConfig().getDouble("unclaim.sound.pitch"));
    }
    public void claimEffect(Player player, Chunk chunk, OfflinePlayer offlinePlayer) {
        Location location = player.getLocation();
        Particle particle = Particle.valueOf(getConfig().getString("claim.particle"));
        Chunk chunkNorth = player.getWorld().getChunkAt(chunk.getX(), chunk.getZ() - 1);
        Chunk chunkEast = player.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ());
        Chunk chunkSouth = player.getWorld().getChunkAt(chunk.getX(), chunk.getZ() + 1);
        Chunk chunkWest = player.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ());
        if (isClaimed(chunkNorth)) {
            if (!getOwner(chunkNorth).equals(offlinePlayer)) {
                player.spawnParticle(particle, chunk.getBlock(8, 0, 0).getX(), location.getBlockY() - 1, chunk.getBlock(8, 0, 0).getZ(), 250, 4, 12, 0, 0);
            }
        } else {
            player.spawnParticle(particle, chunk.getBlock(8, 0, 0).getX(), location.getBlockY() - 1, chunk.getBlock(8, 0, 0).getZ(), 250, 4, 12, 0, 0);
        }
        if (isClaimed(chunkEast)) {
            if (!getOwner(chunkEast).equals(offlinePlayer)) {
                player.spawnParticle(particle, chunk.getBlock(15, 0, 8).getX() + 1, location.getBlockY() - 1, chunk.getBlock(15, 0, 8).getZ(), 250, 0, 12, 4, 0);
            }
        } else {
            player.spawnParticle(particle, chunk.getBlock(15, 0, 8).getX() + 1, location.getBlockY() - 1, chunk.getBlock(15, 0, 8).getZ(), 250, 0, 12, 4, 0);
        }
        if (isClaimed(chunkSouth)) {
            if (!getOwner(chunkSouth).equals(offlinePlayer)) {
                player.spawnParticle(particle, chunk.getBlock(8, 0, 15).getX(), location.getBlockY() - 1, chunk.getBlock(8, 0, 15).getZ() + 1, 250, 4, 12, 0, 0);
            }
        } else {
            player.spawnParticle(particle, chunk.getBlock(8, 0, 15).getX(), location.getBlockY() - 1, chunk.getBlock(8, 0, 15).getZ() + 1, 250, 4, 12, 0, 0);
        }
        if (isClaimed(chunkWest)) {
            if (!getOwner(chunkWest).equals(offlinePlayer)) {
                player.spawnParticle(particle, chunk.getBlock(0, 0, 8).getX(), location.getBlockY() - 1, chunk.getBlock(0, 0, 8).getZ(), 250, 0, 12, 4, 0);
            }
        } else {
            player.spawnParticle(particle, chunk.getBlock(0, 0, 8).getX(), location.getBlockY() - 1, chunk.getBlock(0, 0, 8).getZ(), 250, 0, 12, 4, 0);
        }
    }
    public void reload() {
        File folder = new File(getDataFolder(), "database");
        if (folder.exists() | folder.isDirectory()) {
            for (String worlds : folder.list()) {
                File folders = new File(getDataFolder(), "database/" + worlds);
                if (folders.exists() | folders.isDirectory()) {
                    for (File files : folders.listFiles()) {
                        if (files.exists() | files.isFile()) {
                            FileConfiguration config = YamlConfiguration.loadConfiguration(files);
                            try {
                                config.load(files);
                            } catch (IOException | InvalidConfigurationException e) {
                                getMessage().sendLog(Level.WARNING, e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }
}