package org.achymake.chunks.data;

import org.achymake.chunks.Chunks;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
    public List<Player> getChunkEditors() {
        return plugin.getChunkEditors();
    }
    public File getFile(Chunk chunk) {
        return new File(getDataFolder(), "database/" + chunk.getWorld().getName() + "/" + getChunkKey(chunk.getX(), chunk.getZ()) + ".yml");
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
    public long getChunkKey(int x, int z) {
        return (long)x & 4294967295L | ((long)z & 4294967295L) << 32;
    }
    public OfflinePlayer getOwner(Chunk chunk) {
        return getServer().getOfflinePlayer(UUID.fromString(getConfig(chunk).getString("owner")));
    }
    public int getClaimCount(Chunk chunk) {
        return getUserdata().getClaimCount(getOwner(chunk));
    }
    public String getDateClaimed(Chunk chunk) {
        String date = SimpleDateFormat.getDateInstance().format(getConfig(chunk).getLong("date-claimed"));
        return getConfig(chunk).getString(date);
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
    public boolean isChunkEditor(Player player) {
        return getChunkEditors().contains(player);
    }
    public boolean hasAccess(Player player, Chunk chunk) {
        if (isClaimed(chunk)) {
            return isOwner(player, chunk) || isMember(player, chunk) || getChunkEditors().contains(player);
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
        String soundType = getConfig().getString("claim.sound.type");
        float volume = (float) getConfig().getDouble("claim.sound.volume");
        float pitch = (float) getConfig().getDouble("claim.sound.pitch");
        player.playSound(player, Sound.valueOf(soundType), volume, pitch);
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
        String soundType = getConfig().getString("unclaim.sound.type");
        float volume = (float) getConfig().getDouble("unclaim.sound.volume");
        float pitch = (float) getConfig().getDouble("unclaim.sound.pitch");
        player.playSound(player, Sound.valueOf(soundType), volume, pitch);
    }
    public void claimEffect(Player player, Chunk chunk, OfflinePlayer offlinePlayer) {
        Location location = player.getLocation();
        Particle particle = Particle.valueOf(getConfig().getString("claim.particle"));
        Location north = new Location(player.getWorld(), chunk.getBlock(0, 0, 0).getX() + 0.5, location.getBlockY() - 1, chunk.getBlock(0, 0, 0).getZ() + 0.5 - 1);
        Location east = new Location(player.getWorld(), chunk.getBlock(0, 0, 0).getX() + 0.5 + 16, location.getBlockY() - 1, chunk.getBlock(0, 0, 0).getZ() + 0.5);
        Location south = new Location(player.getWorld(), chunk.getBlock(0, 0, 0).getX() + 0.5, location.getBlockY() - 1, chunk.getBlock(0, 0, 0).getZ() + 0.5 + 16);
        Location west = new Location(player.getWorld(), chunk.getBlock(0, 0, 0).getX() + 0.5 - 1, location.getBlockY() - 1, chunk.getBlock(0, 0, 0).getZ() + 0.5);
        if (isClaimed(north.getChunk())) {
            if (!getOwner(north.getChunk()).equals(offlinePlayer)) {
                player.spawnParticle(particle, chunk.getBlock(8, 0, 0).getX(), location.getBlockY() - 1, chunk.getBlock(8, 0, 0).getZ(), 250, 4, 12, 0, 0);
            }
        } else {
            player.spawnParticle(particle, chunk.getBlock(8, 0, 0).getX(), location.getBlockY() - 1, chunk.getBlock(8, 0, 0).getZ(), 250, 4, 12, 0, 0);
        }
        if (isClaimed(east.getChunk())) {
            if (!getOwner(east.getChunk()).equals(offlinePlayer)) {
                player.spawnParticle(particle, chunk.getBlock(15, 0, 8).getX() + 1, location.getBlockY() - 1, chunk.getBlock(15, 0, 8).getZ(), 250, 0, 12, 4, 0);
            }
        } else {
            player.spawnParticle(particle, chunk.getBlock(15, 0, 8).getX() + 1, location.getBlockY() - 1, chunk.getBlock(15, 0, 8).getZ(), 250, 0, 12, 4, 0);
        }
        if (isClaimed(south.getChunk())) {
            if (!getOwner(south.getChunk()).equals(offlinePlayer)) {
                player.spawnParticle(particle, chunk.getBlock(8, 0, 15).getX(), location.getBlockY() - 1, chunk.getBlock(8, 0, 15).getZ() + 1, 250, 4, 12, 0, 0);
            }
        } else {
            player.spawnParticle(particle, chunk.getBlock(8, 0, 15).getX(), location.getBlockY() - 1, chunk.getBlock(8, 0, 15).getZ() + 1, 250, 4, 12, 0, 0);
        }
        if (isClaimed(west.getChunk())) {
            if (!getOwner(west.getChunk()).equals(offlinePlayer)) {
                player.spawnParticle(particle, chunk.getBlock(0, 0, 8).getX(), location.getBlockY() - 1, chunk.getBlock(0, 0, 8).getZ(), 250, 0, 12, 4, 0);
            }
        } else {
            player.spawnParticle(particle, chunk.getBlock(0, 0, 8).getX(), location.getBlockY() - 1, chunk.getBlock(0, 0, 8).getZ(), 250, 0, 12, 4, 0);
        }
    }
    public void reload() {
        for (World worlds : getServer().getWorlds()) {
            File worldFolder = new File(getDataFolder(), "database/" + worlds);
            if (worldFolder.exists()) {
                for (File files : worldFolder.listFiles()) {
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
