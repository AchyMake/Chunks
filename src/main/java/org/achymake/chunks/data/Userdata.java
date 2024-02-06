package org.achymake.chunks.data;

import org.achymake.chunks.Chunks;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public record Userdata(Chunks plugin) {
    private File getDataFolder() {
        return plugin.getDataFolder();
    }
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public boolean exist(OfflinePlayer offlinePlayer) {
        return new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml").exists();
    }
    public File getFile(OfflinePlayer offlinePlayer) {
        return new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
    }
    public void setup(OfflinePlayer offlinePlayer) {
        if (exist(offlinePlayer)) {
            if (!getConfig(offlinePlayer).getString("name").equals(offlinePlayer.getName())) {
                File file = getFile(offlinePlayer);
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("name", offlinePlayer.getName());
                try {
                    config.save(file);
                } catch (IOException e) {
                    getMessage().sendLog(Level.WARNING, e.getMessage());
                }
            }
        } else {
            File file = getFile(offlinePlayer);
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("name", offlinePlayer.getName());
            config.set("claimed", 0);
            config.createSection("members");
            try {
                config.save(file);
            } catch (IOException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        }
    }
    public FileConfiguration getConfig(OfflinePlayer offlinePlayer) {
        return YamlConfiguration.loadConfiguration(getFile(offlinePlayer));
    }
    public void setBoolean(OfflinePlayer offlinePlayer, String path, boolean value) {
        File file = getFile(offlinePlayer);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setString(OfflinePlayer offlinePlayer, String path, String value) {
        File file = getFile(offlinePlayer);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setInt(OfflinePlayer offlinePlayer, String path, int value) {
        File file = getFile(offlinePlayer);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setStringList(OfflinePlayer offlinePlayer, String path, List<String> value) {
        File file = getFile(offlinePlayer);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void chunkView(Player player) {
        String worldName = player.getWorld().getName();
        if (getConfig(player).isConfigurationSection(worldName)) {
            for (String chunkLocations : getConfig(player).getConfigurationSection(worldName).getKeys(false)) {
                int x = getConfig(player).getInt(worldName + "." + chunkLocations + ".x");
                int z = getConfig(player).getInt(worldName + "." + chunkLocations + ".z");
                Chunk chunk = player.getWorld().getChunkAt(x,z);
                if (chunk.isLoaded()) {
                    claimEffect(player, chunk);
                }
            }
        }
    }
    public void claimEffect(Player player, Chunk chunk) {
        Location location = player.getLocation();
        Particle particle = Particle.valueOf(getConfig().getString("claim.particle"));
        Location locationSouth = new Location(player.getWorld(), chunk.getBlock(15, 0, 8).getX(), location.getBlockY() - 1, chunk.getBlock(15, 0, 8).getZ());
        Location locationEast = new Location(player.getWorld(), chunk.getBlock(8, 0, 15).getX(), location.getBlockY() - 1, chunk.getBlock(8, 0, 15).getZ());
        Location west = new Location(player.getWorld(), chunk.getBlock(0, 0, 0).getX() + 0.5 - 1, location.getBlockY()-1, chunk.getBlock(0, 0, 0).getZ() + 0.5);
        Location north = new Location(player.getWorld(), chunk.getBlock(0, 0, 0).getX() + 0.5, location.getBlockY()-1, chunk.getBlock(0, 0, 0).getZ() + 0.5 - 1);
        Location south = new Location(player.getWorld(), chunk.getBlock(0, 0, 0).getX() + 0.5, location.getBlockY()-1, chunk.getBlock(0, 0, 0).getZ() + 0.5 + 16);
        Location east = new Location(player.getWorld(), chunk.getBlock(0, 0, 0).getX() + 0.5 + 16, location.getBlockY()-1, chunk.getBlock(0, 0, 0).getZ() + 0.5);
        if (plugin.getChunkStorage().isClaimed(north.getChunk())) {
            if (!plugin.getChunkStorage().isOwner(player, north.getChunk())) {
                player.spawnParticle(particle, chunk.getBlock(8, 0, 0).getX(), location.getBlockY() - 1, chunk.getBlock(8, 0, 0).getZ(), 250, 4, 12, 0, 0);
            }
        } else {
            player.spawnParticle(particle, chunk.getBlock(8, 0, 0).getX(), location.getBlockY() - 1, chunk.getBlock(8, 0, 0).getZ(), 250, 4, 12, 0, 0);
        }
        if (plugin.getChunkStorage().isClaimed(west.getChunk())) {
            if (!plugin.getChunkStorage().isOwner(player, west.getChunk())) {
                player.spawnParticle(particle, chunk.getBlock(0, 0, 8).getX(), location.getBlockY() - 1, chunk.getBlock(0, 0, 8).getZ(), 250, 0, 12, 4, 0);
            }
        } else {
            player.spawnParticle(particle, chunk.getBlock(0, 0, 8).getX(), location.getBlockY() - 1, chunk.getBlock(0, 0, 8).getZ(), 250, 0, 12, 4, 0);
        }
        if (plugin.getChunkStorage().isClaimed(east.getChunk())) {
            if (!plugin.getChunkStorage().isOwner(player, east.getChunk())) {
                player.spawnParticle(particle,locationSouth.add(1, 0, 0), 250, 0, 12, 4, 0);
            }
        } else {
            player.spawnParticle(particle,locationSouth.add(1, 0, 0), 250, 0, 12, 4, 0);
        }
        if (plugin.getChunkStorage().isClaimed(south.getChunk())) {
            if (!plugin.getChunkStorage().isOwner(player, south.getChunk())) {
                player.spawnParticle(particle,locationEast.add(0, 0, 1), 250, 4, 12, 0, 0);
            }
        } else {
            player.spawnParticle(particle,locationEast.add(0, 0, 1), 250, 4, 12, 0, 0);
        }
    }
    public List<String> getMembers(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getStringList("members");
    }
    public List<String> getBanned(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getStringList("banned");
    }
    public int getClaimCount(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getInt("claimed");
    }
    public void reload(OfflinePlayer[] offlinePlayers) {
        for (OfflinePlayer offlinePlayer : offlinePlayers) {
            if (exist(offlinePlayer)) {
                File file = getFile(offlinePlayer);
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                try {
                    config.load(file);
                } catch (IOException | InvalidConfigurationException e) {
                    getMessage().sendLog(Level.WARNING, e.getMessage());
                }
            }
        }
    }
}