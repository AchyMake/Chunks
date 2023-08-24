package net.achymake.chunks.files;

import net.achymake.chunks.Chunks;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class Database {
    private final File dataFolder;
    public Database(File dataFolder) {
        this.dataFolder = dataFolder;
    }
    private FileConfiguration getConfig() {
        return Chunks.getConfiguration();
    }
    public boolean exist(OfflinePlayer offlinePlayer) {
        return new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml").exists();
    }
    public void setup(OfflinePlayer offlinePlayer) {
        if (exist(offlinePlayer)) {
            if (!getConfig(offlinePlayer).getString("name").equals(offlinePlayer.getName())) {
                File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("name", offlinePlayer.getName());
                try {
                    config.save(file);
                } catch (IOException e) {
                    Chunks.sendLog(Level.WARNING, e.getMessage());
                }
            }
        } else {
            File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("name", offlinePlayer.getName());
            config.set("claimed", 0);
            config.createSection("members");
            try {
                config.save(file);
            } catch (IOException e) {
                Chunks.sendLog(Level.WARNING, e.getMessage());
            }
        }
    }
    public FileConfiguration getConfig(OfflinePlayer offlinePlayer) {
        return YamlConfiguration.loadConfiguration(new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml"));
    }
    public void setInt(OfflinePlayer offlinePlayer, String path, int value) {
        File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            Chunks.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public void setStringList(OfflinePlayer offlinePlayer, String path, List<String> value) {
        File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            Chunks.sendLog(Level.WARNING, e.getMessage());
        }
    }
    public List<String> getMembers(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getStringList("members");
    }
    public List<String> getBanned(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getStringList("banned");
    }
    public void reload(OfflinePlayer[] offlinePlayers) {
        for (OfflinePlayer offlinePlayer : offlinePlayers) {
            if (exist(offlinePlayer)) {
                File file = new File(dataFolder, "userdata/" + offlinePlayer.getUniqueId() + ".yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                try {
                    config.load(file);
                } catch (IOException | InvalidConfigurationException e) {
                    Chunks.sendLog(Level.WARNING, e.getMessage());
                }
            }
        }
    }
}