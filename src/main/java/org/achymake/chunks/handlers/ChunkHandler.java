package org.achymake.chunks.handlers;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Userdata;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private UUIDHandler getUUIDHandler() {
        return getInstance().getUUIDHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
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
            for (var uuidString : getConfig(chunk).getStringList("recent-owners").reversed()) {
                listed.add(getInstance().getOfflinePlayer(getUUIDHandler().get(uuidString)));
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
        if (exists(chunk)) {
            var ownerString = getConfig(chunk).getString("owner");
            if (ownerString != null) {
                return getInstance().getOfflinePlayer(getUUIDHandler().get(ownerString));
            } else return null;
        } else return null;
    }
    public boolean setOwner(Chunk chunk, OfflinePlayer offlinePlayer) {
        var worldName = chunk.getWorld().getName();
        var file = getFile(chunk);
        var config = YamlConfiguration.loadConfiguration(file);
        var recentOwners = config.getStringList("recent-owners");
        recentOwners.removeIf(uuidString -> uuidString.equals(offlinePlayer.getUniqueId().toString()));
        config.set("owner", offlinePlayer.getUniqueId().toString());
        config.set("settings.tnt", false);
        config.set("recent-owners", recentOwners);
        var chunks = getUserdata().getChunksStringList(offlinePlayer, worldName);
        chunks.add(String.valueOf(getChunkKey(chunk)));
        try {
            config.save(file);
            getUserdata().setObject(offlinePlayer, "chunks." + worldName, chunks);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    public boolean removeOwner(Chunk chunk) {
        if (isClaimed(chunk)) {
            var owner = getOwner(chunk);
            var file = getFile(chunk);
            var config = YamlConfiguration.loadConfiguration(file);
            getEconomy().depositPlayer(owner, getConfig().getDouble("economy.refund"));
            var chunks = getUserdata().getChunksStringList(owner, chunk.getWorld().getName());
            chunks.remove(String.valueOf(getChunkKey(chunk)));
            getUserdata().setObject(owner, "chunks." + chunk.getWorld().getName(), chunks);
            var recentOwners = config.getStringList("recent-owners");
            recentOwners.add(owner.getUniqueId().toString());
            config.set("owner", null);
            config.set("recent-owners", recentOwners);
            try {
                config.save(file);
                return true;
            } catch (IOException e) {
                getInstance().sendWarning(e.getMessage());
                return false;
            }
        } else return false;
    }
    public boolean isTNTAllowed(Chunk chunk) {
        if (isClaimed(chunk)) {
            return getConfig(chunk).getBoolean("settings.tnt");
        } else return false;
    }
    public boolean toggleTNT(Chunk chunk) {
        if (isClaimed(chunk)) {
            var file = getFile(chunk);
            var config = YamlConfiguration.loadConfiguration(file);
            config.set("settings.tnt", !config.getBoolean("settings.tnt"));
            try {
                config.save(file);
            } catch (IOException e) {
                getInstance().sendWarning(e.getMessage());
            }
            return isTNTAllowed(chunk);
        } else return false;
    }
    public boolean isExpired(Chunk chunk) {
        if (isClaimed(chunk)) {
            return getDateHandler().isExpired(getOwner(chunk));
        } else return false;
    }
    public String getName(Chunk chunk) {
        if (isClaimed(chunk)) {
            return getOwner(chunk).getName();
        } else return "null";
    }
    public boolean isOwner(Chunk chunk, OfflinePlayer offlinePlayer) {
        if (isClaimed(chunk)) {
            return getOwner(chunk) == offlinePlayer;
        } else return true;
    }
    public boolean isMember(Chunk chunk, OfflinePlayer offlinePlayer) {
        if (isClaimed(chunk)) {
            return getUserdata().getMembers(getOwner(chunk)).contains(offlinePlayer);
        } else return true;
    }
    public boolean isBanned(Chunk chunk, OfflinePlayer offlinePlayer) {
        if (isClaimed(chunk)) {
            return getUserdata().getBanned(getOwner(chunk)).contains(offlinePlayer);
        } else return false;
    }
    public boolean hasAccess(Chunk chunk, Player player) {
        if (getWorldHandler().isAllowedClaim(chunk)) {
            if (isClaimed(chunk)) {
                return isOwner(chunk, player) || isMember(chunk, player) || getUserdata().isEditor(player);
            } else return true;
        } else return false;
    }
    public long getChunkKey(Chunk chunk) {
        return (long) chunk.getX() & 4294967295L | ((long) chunk.getZ() & 4294967295L) << 32;
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
    public void scheduleEffect(Player player) {
        getUserdata().playEffect(player, player);
        var taskID = getInstance().getScheduleHandler().runLater(() -> scheduleEffect(player), 40).getTaskId();
        getUserdata().setObject(player, "tasks.effect", taskID);
    }
}