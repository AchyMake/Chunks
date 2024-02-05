package org.achymake.chunks.data;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public record ChunkStorage(Chunks plugin) {
    private Economy getEconomy() {
        return plugin.getEconomy();
    }
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    public List<Player> getChunkEditors() {
        return plugin.getChunkEditors();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public double getClaimCost() {
        return getConfig().getDouble("claim.cost");
    }
    public int getMaxClaims() {
        return getConfig().getInt("max-claims");
    }
    public PersistentDataContainer getData(Chunk chunk) {
        return chunk.getPersistentDataContainer();
    }
    public boolean isAllowedClaim(Chunk chunk) {
        try {
            int bx = chunk.getX() << 4;
            int bz = chunk.getZ() << 4;
            BlockVector3 pt1 = BlockVector3.at(bx, -64, bz);
            BlockVector3 pt2 = BlockVector3.at(bx + 15, 320, bz + 15);
            ProtectedCuboidRegion region = new ProtectedCuboidRegion("_", pt1, pt2);
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(chunk.getWorld()));
            if (regionManager != null) {
                for (ProtectedRegion regionIn : regionManager.getApplicableRegions(region)) {
                    StateFlag.State flag = regionIn.getFlag(plugin.getFlagChunksClaim());
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
    public boolean hasAccess(Player player, Chunk chunk) {
        if (isClaimed(chunk)) {
            return isOwner(player, chunk) || isMember(player, chunk) || hasChunkEdit(player);
        } else {
            return true;
        }
    }
    public boolean isOwner(Player player, Chunk chunk) {
        return getOwner(chunk) == player;
    }
    public boolean isMember(Player player, Chunk chunk) {
        return getMembers(chunk).contains(player.getUniqueId().toString());
    }
    public boolean hasChunkEdit(Player player) {
        return getChunkEditors().contains(player);
    }
    public boolean isClaimed(Chunk chunk) {
        return getData(chunk).has(NamespacedKey.minecraft("owner"), PersistentDataType.STRING);
    }
    public void setOwner(Player player, OfflinePlayer target, Chunk chunk) {
        String uuidString = target.getUniqueId().toString();
        String date = SimpleDateFormat.getDateInstance().format(player.getLastPlayed());
        int targetClaimed = getUserdata().getConfig(target).getInt("claimed") + 1;
        if (isClaimed(chunk)) {
            OfflinePlayer owner = getOwner(chunk);
            int ownerClaimed = getUserdata().getConfig(owner).getInt("claimed") - 1;
            getUserdata().setInt(owner,"claimed", ownerClaimed);
            getData(chunk).set(NamespacedKey.minecraft("owner"), PersistentDataType.STRING, uuidString);
            getData(chunk).set(NamespacedKey.minecraft("date-claimed"), PersistentDataType.STRING, date);
            getUserdata().setInt(target,"claimed", targetClaimed);
        } else {
            getUserdata().setInt(target,"claimed", targetClaimed);
            getData(chunk).set(NamespacedKey.minecraft("owner"), PersistentDataType.STRING, uuidString);
            getData(chunk).set(NamespacedKey.minecraft("date-claimed"), PersistentDataType.STRING, date);
        }
    }
    public boolean TNTAllowed(Chunk chunk) {
        return getData(chunk).has(NamespacedKey.minecraft("tnt"), PersistentDataType.STRING);
    }
    public OfflinePlayer getOwner(Chunk chunk) {
        String uuidString = getData(chunk).get(NamespacedKey.minecraft("owner"), PersistentDataType.STRING);
        UUID uuid = UUID.fromString(uuidString);
        return plugin.getServer().getOfflinePlayer(uuid);
    }
    public String getDateClaimed(Chunk chunk) {
        return getData(chunk).get(NamespacedKey.minecraft("date-claimed"), PersistentDataType.STRING);
    }
    public int getClaimedCount(Chunk chunk) {
        return getUserdata().getConfig(getOwner(chunk)).getInt("claimed");
    }
    public int getClaimedCount(OfflinePlayer offlinePlayer) {
        return getUserdata().getConfig(offlinePlayer).getInt("claimed");
    }
    public List<String> getBanned(Chunk chunk) {
        if (isClaimed(chunk)) {
            return getUserdata().getBanned(getOwner(chunk));
        } else {
            return new ArrayList<>();
        }
    }
    public List<UUID> getMembersUUID(Chunk chunk) {
        List<UUID> uuids = new ArrayList<>();
        if (isClaimed(chunk)) {
            for (String uuidString : getMembers(chunk)) {
                uuids.add(UUID.fromString(uuidString));
            }
        }
        return uuids;
    }
    public List<String> getMembers(Chunk chunk) {
        if (isClaimed(chunk)) {
            return getMembers(getOwner(chunk));
        } else {
            return new ArrayList<>();
        }
    }
    public List<String> getMembers(OfflinePlayer offlinePlayer) {
        if (getUserdata().exist(offlinePlayer)) {
            return getUserdata().getMembers(offlinePlayer);
        } else {
            return new ArrayList<>();
        }
    }
    public List<UUID> getMembersUUID(OfflinePlayer offlinePlayer) {
        List<UUID> uuids = new ArrayList<>();
        if (getUserdata().exist(offlinePlayer)) {
            for (String uuidString : getMembers(offlinePlayer)) {
                uuids.add(UUID.fromString(uuidString));
            }
        }
        return uuids;
    }
    public void claimEffect(Player player) {
        Location location = player.getLocation();
        Particle particle = Particle.valueOf(getConfig().getString("claim.particle"));
        Location locationSouth = new Location(player.getWorld(), location.getChunk().getBlock(15, 0, 8).getX(), location.getBlockY()-3, location.getChunk().getBlock(15, 0, 8).getZ());
        Location locationEast = new Location(player.getWorld(), location.getChunk().getBlock(8, 0, 15).getX(), location.getBlockY()-3, location.getChunk().getBlock(8, 0, 15).getZ());
        String soundType = getConfig().getString("claim.sound.type");
        float volume = getConfig().getLong("claim.sound.volume");
        float pitch = getConfig().getLong("claim.sound.pitch");
        player.playSound(player, Sound.valueOf(soundType), volume, pitch);
        player.spawnParticle(particle, location.getChunk().getBlock(8, 0, 0).getX(), location.getBlockY()-3, location.getChunk().getBlock(8, 0, 0).getZ(), 250, 4, 12, 0, 0);
        player.spawnParticle(particle, location.getChunk().getBlock(0, 0, 8).getX(), location.getBlockY()-3, location.getChunk().getBlock(0, 0, 8).getZ(), 250, 0, 12, 4, 0);
        player.spawnParticle(particle,locationSouth.add(1, 0, 0), 250, 0, 12, 4, 0);
        player.spawnParticle(particle,locationEast.add(0, 0, 1), 250, 4, 12, 0, 0);
    }
    public void unclaimEffect(Player player) {
        Location location = player.getLocation();
        Particle particle = Particle.valueOf(getConfig().getString("unclaim.particle"));
        Location locationSouth = new Location(player.getWorld(), location.getChunk().getBlock(15, 0, 8).getX(), location.getBlockY()-3, location.getChunk().getBlock(15, 0, 8).getZ());
        Location locationEast = new Location(player.getWorld(), location.getChunk().getBlock(8, 0, 15).getX(), location.getBlockY()-3, location.getChunk().getBlock(8, 0, 15).getZ());
        String soundType = getConfig().getString("unclaim.sound.type");
        float volume = getConfig().getLong("unclaim.sound.volume");
        float pitch = getConfig().getLong("unclaim.sound.pitch");
        player.playSound(player, Sound.valueOf(soundType), volume, pitch);
        player.spawnParticle(particle, location.getChunk().getBlock(8, 0, 0).getX(), location.getBlockY()-3, location.getChunk().getBlock(8, 0, 0).getZ(), 250, 4, 12, 0, 0);
        player.spawnParticle(particle, location.getChunk().getBlock(0, 0, 8).getX(), location.getBlockY()-3, location.getChunk().getBlock(0, 0, 8).getZ(), 250, 0, 12, 4, 0);
        player.spawnParticle(particle,locationSouth.add(1, 0, 0), 250, 0, 12, 4, 0);
        player.spawnParticle(particle,locationEast.add(0, 0, 1), 250, 4, 12, 0, 0);
    }
    public void claim(Player player, Chunk chunk) {
        getEconomy().withdrawPlayer(player, getConfig().getDouble("claim.cost"));
        String uuidString = player.getUniqueId().toString();
        getData(chunk).set(NamespacedKey.minecraft("owner"), PersistentDataType.STRING, uuidString);
        String date = SimpleDateFormat.getDateInstance().format(player.getLastPlayed());
        getData(chunk).set(NamespacedKey.minecraft("date-claimed"), PersistentDataType.STRING, date);
        int newValue = getUserdata().getConfig(player).getInt("claimed") + 1;
        getUserdata().setInt(player,"claimed", newValue);
    }
    public void unclaim(Chunk chunk) {
        OfflinePlayer offlinePlayer = getOwner(chunk);
        int newValue = getUserdata().getConfig(offlinePlayer).getInt("claimed") - 1;
        getUserdata().setInt(offlinePlayer,"claimed", newValue);
        getEconomy().depositPlayer(offlinePlayer, getConfig().getDouble("unclaim.refund"));
        getData(chunk).remove(NamespacedKey.minecraft("date-claimed"));
        getData(chunk).remove(NamespacedKey.minecraft("owner"));
    }
    public boolean isBanned(Chunk chunk, Player player) {
        return getBanned(chunk).contains(player.getUniqueId().toString());
    }
}