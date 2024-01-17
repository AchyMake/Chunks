package org.achymake.chunks.files;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Database {
    private final Chunks plugin;
    public Database(Chunks plugin) {
        this.plugin = plugin;
    }
    private File getFolder() {
        return plugin.getDataFolder();
    }
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Economy getEconomy() {
        return plugin.getEconomy();
    }
    public boolean exist(OfflinePlayer offlinePlayer) {
        return new File(getFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml").exists();
    }
    public void setup(OfflinePlayer offlinePlayer) {
        if (exist(offlinePlayer)) {
            if (!getConfig(offlinePlayer).getString("name").equals(offlinePlayer.getName())) {
                File file = new File(getFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("name", offlinePlayer.getName());
                try {
                    config.save(file);
                } catch (IOException e) {
                    plugin.sendLog(Level.WARNING, e.getMessage());
                }
            }
        } else {
            File file = new File(getFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("name", offlinePlayer.getName());
            config.set("claimed", 0);
            config.createSection("members");
            try {
                config.save(file);
            } catch (IOException e) {
                plugin.sendLog(Level.WARNING, e.getMessage());
            }
        }
    }
    public FileConfiguration getConfig(OfflinePlayer offlinePlayer) {
        return YamlConfiguration.loadConfiguration(new File(getFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml"));
    }
    public void setBoolean(OfflinePlayer offlinePlayer, String path, boolean value) {
        File file = new File(getFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setInt(OfflinePlayer offlinePlayer, String path, int value) {
        File file = new File(getFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setStringList(OfflinePlayer offlinePlayer, String path, List<String> value) {
        File file = new File(getFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public List<String> getBanned(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getStringList("banned");
    }
    public void reload(OfflinePlayer[] offlinePlayers) {
        for (OfflinePlayer offlinePlayer : offlinePlayers) {
            if (exist(offlinePlayer)) {
                File file = new File(getFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                try {
                    config.load(file);
                } catch (IOException | InvalidConfigurationException e) {
                    plugin.sendLog(Level.WARNING, e.getMessage());
                }
            }
        }
    }
    public PersistentDataContainer getData(Chunk chunk) {
        return chunk.getPersistentDataContainer();
    }
    public boolean hasAccess(Player player, Chunk chunk) {
        if (isProtected(chunk)) {
            return hasChunkEdit(player);
        }
        if (isClaimed(chunk)) {
            return isOwner(player, chunk) || isMember(player, chunk) || hasChunkEdit(player);
        }
        return true;
    }
    public boolean isOwner(Player player, Chunk chunk) {
        return getOwner(chunk) == player;
    }
    public boolean isMember(Player player, Chunk chunk) {
        return getMembers(chunk).contains(player.getUniqueId().toString());
    }
    public boolean hasChunkEdit(Player player) {
        return getConfig(player).getBoolean("settings.chunk-edit");
    }
    public boolean isClaimed(Chunk chunk) {
        return getData(chunk).has(NamespacedKey.minecraft("owner"), PersistentDataType.STRING);
    }
    public void setOwner(Player player, OfflinePlayer target, Chunk chunk) {
        if (isClaimed(chunk)) {
            setInt(getOwner(chunk),"claimed", getConfig(getOwner(chunk)).getInt("claimed") - 1);
            getData(chunk).set(NamespacedKey.minecraft("owner"), PersistentDataType.STRING, target.getUniqueId().toString());
            getData(chunk).set(NamespacedKey.minecraft("date-claimed"), PersistentDataType.STRING, SimpleDateFormat.getDateInstance().format(player.getLastPlayed()));
            setInt(target,"claimed", getConfig(target).getInt("claimed") + 1);
        } else {
            setInt(target,"claimed", getConfig(target).getInt("claimed") + 1);
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
        return getConfig(getOwner(chunk)).getInt("claimed");
    }
    public int getClaimedCount(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getInt("claimed");
    }
    public List<String> getBanned(Chunk chunk) {
        if (isClaimed(chunk)) {
            return getBanned(getOwner(chunk));
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
        if (exist(offlinePlayer)) {
            return getConfig(offlinePlayer).getStringList("members");
        } else {
            return new ArrayList<>();
        }
    }
    public List<UUID> getMembersUUID(OfflinePlayer offlinePlayer) {
        List<UUID> uuids = new ArrayList<>();
        if (exist(offlinePlayer)){
            for (String uuidString : getMembers(offlinePlayer)) {
                uuids.add(UUID.fromString(uuidString));
            }
        }
        return uuids;
    }
    public void claimEffect(Player player) {
        Particle particle = Particle.valueOf(getConfig().getString("claim.particle"));
        Location locationSouth = new Location(player.getWorld(), player.getLocation().getChunk().getBlock(15, 0, 8).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(15, 0, 8).getZ());
        Location locationEast = new Location(player.getWorld(), player.getLocation().getChunk().getBlock(8, 0, 15).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(8, 0, 15).getZ());
        player.playSound(player.getLocation(), Sound.valueOf(getConfig().getString("claim.sound.type")), Float.parseFloat(getConfig().getString("claim.sound.volume")), Float.parseFloat(getConfig().getString("claim.sound.pitch")));
        player.spawnParticle(particle, player.getLocation().getChunk().getBlock(8, 0, 0).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(8, 0, 0).getZ(), 250, 4, 12, 0, 0);
        player.spawnParticle(particle, player.getLocation().getChunk().getBlock(0, 0, 8).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(0, 0, 8).getZ(), 250, 0, 12, 4, 0);
        player.spawnParticle(particle,locationSouth.add(1, 0, 0), 250, 0, 12, 4, 0);
        player.spawnParticle(particle,locationEast.add(0, 0, 1), 250, 4, 12, 0, 0);
    }
    public void unclaimEffect(Player player) {
        Particle particle = Particle.valueOf(getConfig().getString("unclaim.particle"));
        Location locationSouth = new Location(player.getWorld(), player.getLocation().getChunk().getBlock(15, 0, 8).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(15, 0, 8).getZ());
        Location locationEast = new Location(player.getWorld(), player.getLocation().getChunk().getBlock(8, 0, 15).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(8, 0, 15).getZ());
        player.playSound(player.getLocation(), Sound.valueOf(getConfig().getString("unclaim.sound.type")),Float.parseFloat(getConfig().getString("unclaim.sound.volume")), Float.parseFloat(getConfig().getString("unclaim.sound.pitch")));
        player.spawnParticle(particle, player.getLocation().getChunk().getBlock(8, 0, 0).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(8, 0, 0).getZ(), 250, 4, 12, 0, 0);
        player.spawnParticle(particle, player.getLocation().getChunk().getBlock(0, 0, 8).getX(), player.getLocation().getBlockY()-3, player.getLocation().getChunk().getBlock(0, 0, 8).getZ(), 250, 0, 12, 4, 0);
        player.spawnParticle(particle,locationSouth.add(1, 0, 0), 250, 0, 12, 4, 0);
        player.spawnParticle(particle,locationEast.add(0, 0, 1), 250, 4, 12, 0, 0);
    }
    public void claim(Player player, Chunk chunk) {
        getEconomy().withdrawPlayer(player, getConfig().getDouble("claim.cost"));
        getData(chunk).set(NamespacedKey.minecraft("owner"), PersistentDataType.STRING,player.getUniqueId().toString());
        getData(chunk).set(NamespacedKey.minecraft("date-claimed"), PersistentDataType.STRING, SimpleDateFormat.getDateInstance().format(player.getLastPlayed()));
        setInt(player,"claimed", getConfig(player).getInt("claimed") + 1);
    }
    public void unclaim(Chunk chunk) {
        OfflinePlayer offlinePlayer = getOwner(chunk);
        setInt(offlinePlayer,"claimed", getConfig(offlinePlayer).getInt("claimed") - 1);
        getEconomy().depositPlayer(offlinePlayer, getConfig().getDouble("unclaim.refund"));
        getData(chunk).remove(NamespacedKey.minecraft("date-claimed"));
        getData(chunk).remove(NamespacedKey.minecraft("owner"));
    }
    public boolean isProtected(Chunk chunk) {
        return getData(chunk).has(NamespacedKey.minecraft("protected"), PersistentDataType.STRING);
    }
    public void protect(Chunk chunk) {
        getData(chunk).set(NamespacedKey.minecraft("protected"), PersistentDataType.STRING, "true");
    }
    public void unprotect(Chunk chunk) {
        getData(chunk).remove(NamespacedKey.minecraft("protected"));
    }
    public boolean isBanned(Chunk chunk, Player player) {
        return getBanned(chunk).contains(player.getUniqueId().toString());
    }
}
