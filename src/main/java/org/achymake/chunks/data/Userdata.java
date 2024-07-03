package org.achymake.chunks.data;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
            playerConfig.set("claimed", 0);
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
    public void addClaim(OfflinePlayer offlinePlayer, Chunk chunk) {
        String worldName = chunk.getWorld().getName();
        int result = getClaimCount(offlinePlayer) + 1;
        List<String> longList = getConfig(offlinePlayer).getStringList("chunks." + worldName);
        longList.add(String.valueOf(getChunkdata().getChunkKey(chunk.getX(), chunk.getZ())));
        setInt(offlinePlayer, "claimed", result);
        setStringList(offlinePlayer, "chunks." + worldName, longList);
    }
    public void removeClaim(OfflinePlayer offlinePlayer, Chunk chunk) {
        getEconomy().depositPlayer(offlinePlayer, getConfig().getDouble("economy.refund"));
        String worldName = chunk.getWorld().getName();
        List<String> longList = getConfig(offlinePlayer).getStringList("chunks." + worldName);
        longList.remove(String.valueOf(getChunkdata().getChunkKey(chunk.getX(), chunk.getZ())));
        setStringList(offlinePlayer, "chunks." + worldName, longList);
        int result = getClaimCount(offlinePlayer) - 1;
        if (!(result <= -1)) {
            setInt(offlinePlayer, "claimed", result);
        }
    }
    public int getClaimCount(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getInt("claimed");
    }
    public List<OfflinePlayer> getMembers(OfflinePlayer offlinePlayer) {
        List<OfflinePlayer> offlinePlayerList = new ArrayList<>();
        for (String uuidString : getConfig(offlinePlayer).getStringList("members")) {
            UUID uuid = UUID.fromString(uuidString);
            OfflinePlayer member = getServer().getOfflinePlayer(uuid);
            offlinePlayerList.add(member);
        }
        return offlinePlayerList;
    }
    public List<String> getMembersUUIDString(OfflinePlayer offlinePlayer) {
        List<String> uuidStringList = new ArrayList<>();
        uuidStringList.addAll(getConfig(offlinePlayer).getStringList("members"));
        return uuidStringList;
    }
    public List<OfflinePlayer> getBanned(OfflinePlayer offlinePlayer) {
        List<OfflinePlayer> offlinePlayerList = new ArrayList<>();
        for (String uuidString : getConfig(offlinePlayer).getStringList("banned")) {
            UUID uuid = UUID.fromString(uuidString);
            OfflinePlayer member = getServer().getOfflinePlayer(uuid);
            offlinePlayerList.add(member);
        }
        return offlinePlayerList;
    }
    public List<String> getBannedUUIDString(OfflinePlayer offlinePlayer) {
        return new ArrayList<>(getConfig(offlinePlayer).getStringList("banned"));
    }
    public void chunkView(Player player, OfflinePlayer offlinePlayer) {
        String worldName = player.getWorld().getName();
        Configuration config = getConfig(offlinePlayer);
        if (config.isList("chunks." + worldName)) {
            for (String longString : config.getStringList("chunks." + worldName)) {
                int x = getChunkdata().getConfig(worldName, longString).getInt("x");
                int z = getChunkdata().getConfig(worldName, longString).getInt("z");
                Chunk chunk = player.getWorld().getChunkAt(x, z);
                if (chunk.isLoaded()) {
                    getChunkdata().claimEffect(player, chunk, offlinePlayer);
                }
            }
        }
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