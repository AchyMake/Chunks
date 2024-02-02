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

import static org.achymake.chunks.Chunks.FLAG_CHUNKS_CLAIM;

public class ChunkStorage {
    private final Chunks plugin;
    private final List<Player> chunkEditors = new ArrayList<>();
    private final FileConfiguration config;
    private final Userdata userdata;
    private final Economy economy;
    public ChunkStorage(Chunks plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
        userdata = plugin.getUserdata();
        economy = plugin.getEconomy();
    }
    public PersistentDataContainer getData(Chunk chunk) {
        return chunk.getPersistentDataContainer();
    }
    public boolean isAllowedClaim(Chunk chunk) {
        try {
            int bx = chunk.getX() << 4;
            int bz = chunk.getZ() << 4;
            BlockVector3 pt1 = BlockVector3.at(bx, 0, bz);
            BlockVector3 pt2 = BlockVector3.at(bx + 15, 256, bz + 15);
            ProtectedCuboidRegion region = new ProtectedCuboidRegion("_", pt1, pt2);
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(chunk.getWorld()));
            if (regionManager != null) {
                for (ProtectedRegion regionIn : regionManager.getApplicableRegions(region)) {
                    StateFlag.State flag = regionIn.getFlag(FLAG_CHUNKS_CLAIM);
                    if (flag == StateFlag.State.ALLOW) {
                        return true;
                    } else if (flag == StateFlag.State.DENY) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            plugin.getMessage().sendLog(Level.WARNING, e.getMessage());
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
        if (isClaimed(chunk)) {
            userdata.setInt(getOwner(chunk),"claimed", userdata.getConfig(getOwner(chunk)).getInt("claimed") - 1);
            getData(chunk).set(NamespacedKey.minecraft("owner"), PersistentDataType.STRING, target.getUniqueId().toString());
            getData(chunk).set(NamespacedKey.minecraft("date-claimed"), PersistentDataType.STRING, SimpleDateFormat.getDateInstance().format(player.getLastPlayed()));
            userdata.setInt(target,"claimed", userdata.getConfig(target).getInt("claimed") + 1);
        } else {
            userdata.setInt(target,"claimed", userdata.getConfig(target).getInt("claimed") + 1);
            getData(chunk).set(NamespacedKey.minecraft("owner"), PersistentDataType.STRING, target.getUniqueId().toString());
            getData(chunk).set(NamespacedKey.minecraft("date-claimed"), PersistentDataType.STRING, SimpleDateFormat.getDateInstance().format(player.getLastPlayed()));
        }
    }
    public boolean TNTAllowed(Chunk chunk) {
        return getData(chunk).has(NamespacedKey.minecraft("tnt"), PersistentDataType.STRING);
    }
    public OfflinePlayer getOwner(Chunk chunk) {
        return Chunks.getInstance().getServer().getOfflinePlayer(UUID.fromString(getData(chunk).get(NamespacedKey.minecraft("owner"), PersistentDataType.STRING)));
    }
    public String getDateClaimed(Chunk chunk) {
        return getData(chunk).get(NamespacedKey.minecraft("date-claimed"), PersistentDataType.STRING);
    }
    public int getClaimedCount(Chunk chunk) {
        return userdata.getConfig(getOwner(chunk)).getInt("claimed");
    }
    public int getClaimedCount(OfflinePlayer offlinePlayer) {
        return userdata.getConfig(offlinePlayer).getInt("claimed");
    }
    public List<String> getBanned(Chunk chunk) {
        if (isClaimed(chunk)) {
            return userdata.getBanned(getOwner(chunk));
        } else {
            return new ArrayList<>();
        }
    }
    public List<UUID> getMembersUUID(Chunk chunk) {
        List<UUID> uuids = new ArrayList<>();
        if (isClaimed(chunk)){
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
        if (userdata.exist(offlinePlayer)) {
            return userdata.getMembers(offlinePlayer);
        } else {
            return new ArrayList<>();
        }
    }
    public List<UUID> getMembersUUID(OfflinePlayer offlinePlayer) {
        List<UUID> uuids = new ArrayList<>();
        if (userdata.exist(offlinePlayer)){
            for (String uuidString : getMembers(offlinePlayer)) {
                uuids.add(UUID.fromString(uuidString));
            }
        }
        return uuids;
    }
    public void claimEffect(Player player) {
        Particle particle = Particle.valueOf(config.getString("claim.particle"));
        Location locationSouth = new Location(player.getWorld(), player.getLocation().getChunk().getBlock(15, 0, 8).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(15, 0, 8).getZ());
        Location locationEast = new Location(player.getWorld(), player.getLocation().getChunk().getBlock(8, 0, 15).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(8, 0, 15).getZ());
        player.playSound(player, Sound.valueOf(config.getString("claim.sound.type")), Float.parseFloat(config.getString("claim.sound.volume")), Float.parseFloat(config.getString("claim.sound.pitch")));
        player.spawnParticle(particle, player.getLocation().getChunk().getBlock(8, 0, 0).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(8, 0, 0).getZ(), 250, 4, 12, 0, 0);
        player.spawnParticle(particle, player.getLocation().getChunk().getBlock(0, 0, 8).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(0, 0, 8).getZ(), 250, 0, 12, 4, 0);
        player.spawnParticle(particle,locationSouth.add(1, 0, 0), 250, 0, 12, 4, 0);
        player.spawnParticle(particle,locationEast.add(0, 0, 1), 250, 4, 12, 0, 0);
    }
    public void unclaimEffect(Player player) {
        Particle particle = Particle.valueOf(config.getString("unclaim.particle"));
        Location locationSouth = new Location(player.getWorld(), player.getLocation().getChunk().getBlock(15, 0, 8).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(15, 0, 8).getZ());
        Location locationEast = new Location(player.getWorld(), player.getLocation().getChunk().getBlock(8, 0, 15).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(8, 0, 15).getZ());
        player.playSound(player, Sound.valueOf(config.getString("unclaim.sound.type")),Float.parseFloat(config.getString("unclaim.sound.volume")), Float.parseFloat(config.getString("unclaim.sound.pitch")));
        player.spawnParticle(particle, player.getLocation().getChunk().getBlock(8, 0, 0).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(8, 0, 0).getZ(), 250, 4, 12, 0, 0);
        player.spawnParticle(particle, player.getLocation().getChunk().getBlock(0, 0, 8).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(0, 0, 8).getZ(), 250, 0, 12, 4, 0);
        player.spawnParticle(particle,locationSouth.add(1, 0, 0), 250, 0, 12, 4, 0);
        player.spawnParticle(particle,locationEast.add(0, 0, 1), 250, 4, 12, 0, 0);
    }
    public void claim(Player player, Chunk chunk) {
        economy.withdrawPlayer(player, config.getDouble("claim.cost"));
        getData(chunk).set(NamespacedKey.minecraft("owner"), PersistentDataType.STRING,player.getUniqueId().toString());
        getData(chunk).set(NamespacedKey.minecraft("date-claimed"), PersistentDataType.STRING, SimpleDateFormat.getDateInstance().format(player.getLastPlayed()));
        userdata.setInt(player,"claimed", userdata.getConfig(player).getInt("claimed") + 1);
    }
    public void unclaim(Chunk chunk) {
        OfflinePlayer offlinePlayer = getOwner(chunk);
        userdata.setInt(offlinePlayer,"claimed", userdata.getConfig(offlinePlayer).getInt("claimed") - 1);
        economy.depositPlayer(offlinePlayer, config.getDouble("unclaim.refund"));
        getData(chunk).remove(NamespacedKey.minecraft("date-claimed"));
        getData(chunk).remove(NamespacedKey.minecraft("owner"));
    }
    public boolean isBanned(Chunk chunk, Player player) {
        return getBanned(chunk).contains(player.getUniqueId().toString());
    }
    public List<Player> getChunkEditors() {
        return chunkEditors;
    }
}
