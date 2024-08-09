package org.achymake.chunks.data;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public record Userdata(Chunks plugin) {
    private File getDataFolder() {
        return plugin.getDataFolder();
    }
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Economy getEconomy() {
        return plugin.getEconomy();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public File getFile(OfflinePlayer offlinePlayer) {
        return new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
    }
    public boolean exist(OfflinePlayer offlinePlayer) {
        return getFile(offlinePlayer).exists();
    }
    public FileConfiguration getConfig(OfflinePlayer offlinePlayer) {
        return YamlConfiguration.loadConfiguration(getFile(offlinePlayer));
    }
    public void setup(OfflinePlayer offlinePlayer) {
        if (exist(offlinePlayer)) {
            if (!getConfig(offlinePlayer).getString("name").equals(offlinePlayer.getName())) {
                File file = getFile(offlinePlayer);
                FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
                playerConfig.set("name", offlinePlayer.getName());
                try {
                    playerConfig.save(file);
                } catch (IOException e) {
                    getMessage().sendLog(Level.WARNING, e.getMessage());
                }
            }
        } else {
            File file = getFile(offlinePlayer);
            FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
            playerConfig.set("name", offlinePlayer.getName());
            playerConfig.createSection("members");
            playerConfig.createSection("banned");
            playerConfig.createSection("chunks");
            try {
                playerConfig.save(file);
            } catch (IOException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
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
    public void addInt(OfflinePlayer offlinePlayer, String path, int value) {
        File file = getFile(offlinePlayer);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, config.getInt(path) + value);
        try {
            config.save(file);
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void addTaskID(Player player, String task, int value) {
        setInt(player, "tasks." + task, value);
    }
    public boolean hasTaskID(Player player, String task) {
        return getConfig(player).isInt("tasks." + task);
    }
    public int getTaskID(Player player, String task) {
        return getConfig(player).getInt("tasks." + task);
    }
    public void removeTaskID(Player player, String task) {
        setString(player, "tasks." + task, null);
    }
    public void addClaim(OfflinePlayer offlinePlayer, Chunk chunk) {
        List<String> longList = getConfig(offlinePlayer).getStringList("chunks." + chunk.getWorld().getName());
        longList.add(String.valueOf(getChunkdata().getChunkKey(chunk)));
        setStringList(offlinePlayer, "chunks." + chunk.getWorld().getName(), longList);
    }
    public void removeClaim(OfflinePlayer offlinePlayer, Chunk chunk) {
        getEconomy().depositPlayer(offlinePlayer, getConfig().getDouble("economy.refund"));
        List<String> longList = getConfig(offlinePlayer).getStringList("chunks." + chunk.getWorld().getName());
        longList.remove(String.valueOf(getChunkdata().getChunkKey(chunk)));
        setStringList(offlinePlayer, "chunks." + chunk.getWorld().getName(), longList);
    }
    public int getClaimCount(OfflinePlayer offlinePlayer) {
        Set<String> worlds = getConfig(offlinePlayer).getConfigurationSection("chunks").getKeys(false);
        if (worlds.isEmpty()) {
            return 0;
        } else {
            List<Integer> test = new ArrayList<>();
            for (String world : worlds) {
                int size = getConfig(offlinePlayer).getStringList("chunks." + world).size();
                if (test.isEmpty()) {
                    test.addFirst(size);
                } else {
                    int tests = test.getFirst() + size;
                    test.addFirst(tests);
                }
            }
            return test.getFirst();
        }
    }
    public List<OfflinePlayer> getMembers(OfflinePlayer offlinePlayer) {
        List<OfflinePlayer> offlinePlayerList = new ArrayList<>();
        for (String uuidString : getConfig(offlinePlayer).getStringList("members")) {
            offlinePlayerList.add(getServer().getOfflinePlayer(UUID.fromString(uuidString)));
        }
        return offlinePlayerList;
    }
    public List<String> getMembersUUIDString(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getStringList("members");
    }
    public List<OfflinePlayer> getBanned(OfflinePlayer offlinePlayer) {
        List<OfflinePlayer> offlinePlayerList = new ArrayList<>();
        for (String uuidString : getConfig(offlinePlayer).getStringList("banned")) {
            offlinePlayerList.add(getServer().getOfflinePlayer(UUID.fromString(uuidString)));
        }
        return offlinePlayerList;
    }
    public List<String> getBannedUUIDString(OfflinePlayer offlinePlayer) {
        return new ArrayList<>(getConfig(offlinePlayer).getStringList("banned"));
    }
    public void chunkView(Player player, OfflinePlayer offlinePlayer) {
        String worldName = player.getWorld().getName();
        if (getConfig(offlinePlayer).isList("chunks." + worldName)) {
            for (String longString : getConfig(offlinePlayer).getStringList("chunks." + worldName)) {
                int x = getChunkdata().getConfig(worldName, longString).getInt("x");
                int z = getChunkdata().getConfig(worldName, longString).getInt("z");
                Chunk chunk = player.getWorld().getChunkAt(x, z);
                if (chunk.isLoaded()) {
                    getChunkdata().playEffect(player, chunk, "claim");
                }
            }
        }
    }
    public void reload() {
        File folder = new File(getDataFolder(), "userdata");
        if (folder.exists() | folder.isDirectory()) {
            for (File files : folder.listFiles()) {
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