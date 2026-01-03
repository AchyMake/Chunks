package org.achymake.chunks.data;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.WorldHandler;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Userdata {
    private final List<Player> editors = new ArrayList<>();
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private Chunk getChunk(World world, String longString) {
        return getWorldHandler().getChunk(world, Long.parseLong(longString));
    }
    public File getFile(OfflinePlayer offlinePlayer) {
        return new File(getInstance().getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
    }
    public boolean exists(OfflinePlayer offlinePlayer) {
        return getFile(offlinePlayer).exists();
    }
    public FileConfiguration getConfig(OfflinePlayer offlinePlayer) {
        return YamlConfiguration.loadConfiguration(getFile(offlinePlayer));
    }
    public void setObject(OfflinePlayer offlinePlayer, String path, Object value) {
        var file = getFile(offlinePlayer);
        var config = YamlConfiguration.loadConfiguration(file);
        config.set(path, value);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public boolean isEditor(Player player) {
        return getEditors().contains(player);
    }
    public boolean hasReachedChunkLimit(Player player) {
        return getClaimCount(player) >= getMaxClaims(player);
    }
    public List<String> getMembersStringList(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getStringList("members");
    }
    public List<OfflinePlayer> getMembers(OfflinePlayer offlinePlayer) {
        var members = new ArrayList<OfflinePlayer>();
        var membersStringList = getMembersStringList(offlinePlayer);
        if (!membersStringList.isEmpty()) {
            for (var member : getMembersStringList(offlinePlayer)) {
                members.add(getInstance().getOfflinePlayer(UUID.fromString(member)));
            }
        }
        return members;
    }
    public List<String> getBannedStringList(OfflinePlayer offlinePlayer) {
        return getConfig(offlinePlayer).getStringList("banned");
    }
    public List<OfflinePlayer> getBanned(OfflinePlayer offlinePlayer) {
        var banned = new ArrayList<OfflinePlayer>();
        var bannedStringList = getBannedStringList(offlinePlayer);
        if (!bannedStringList.isEmpty()) {
            for (var ban : bannedStringList) {
                banned.add(getInstance().getOfflinePlayer(UUID.fromString(ban)));
            }
        }
        return banned;
    }
    public List<String> getChunksStringList(OfflinePlayer offlinePlayer, String worldName) {
        return getConfig(offlinePlayer).getStringList("chunks." + worldName);
    }
    public List<Chunk> getChunks(OfflinePlayer offlinePlayer, World world) {
        var chunks = new ArrayList<Chunk>();
        var chunksStringList = getChunksStringList(offlinePlayer, world.getName());
        if (!chunksStringList.isEmpty()) {
            chunksStringList.forEach(longString -> chunks.add(getChunk(world, longString)));
        }
        return chunks;
    }
    public int getClaimCount(OfflinePlayer offlinePlayer) {
        var worldNames = getConfig(offlinePlayer).getConfigurationSection("chunks").getKeys(false);
        if (!worldNames.isEmpty()) {
            var integers = new ArrayList<Integer>();
            for (var worldName : worldNames) {
                if (!integers.isEmpty()) {
                    integers.set(0, integers.getFirst() + getConfig(offlinePlayer).getStringList("chunks." + worldName).size());
                } else integers.addFirst(getConfig(offlinePlayer).getStringList("chunks." + worldName).size());
            }
            return integers.getFirst();
        } else return 0;
    }
    public int getMaxClaims(Player player) {
        if (!player.isOp()) {
            var listed = new ArrayList<Integer>();
            for (var rank : getConfig().getConfigurationSection("max-claims").getKeys(false)) {
                if (player.hasPermission("chunks.command.chunk.claim.multiple." + rank)) {
                    listed.add(getConfig().getInt("max-claims." + rank));
                }
            }
            listed.sort(Integer::compareTo);
            if (!listed.isEmpty()) {
                 return listed.getLast();
            } else return getConfig().getInt("max-claims.default");
        } else return getConfig().getInt("max-claims.op");
    }
    public void playEffect(Player player, OfflinePlayer offlinePlayer) {
        chunkView(player, offlinePlayer);
        claimSound(player);
    }
    public void chunkView(Player player, OfflinePlayer offlinePlayer) {
        for (var chunk : getChunks(offlinePlayer, player.getWorld())) {
            if (chunk.isLoaded()) {
                claimEffect(player, chunk, offlinePlayer);
            }
        }
    }
    public void claimSound(Player player) {
        playSound(player, getConfig().getString("claim.sound.type"), (float) getConfig().getDouble("claim.sound.volume"), (float) getConfig().getDouble("claim.sound.pitch"));
    }
    public void claimEffect(Player player, Chunk chunk) {
        var location = player.getLocation();
        var particle = Particle.valueOf(getConfig().getString("claim.particle.type"));
        player.spawnParticle(particle, chunk.getBlock(7, 0, 0).getX(), location.getY() + 4, chunk.getBlock(7, 0, 0).getZ(), getConfig().getInt("claim.particle.amount"), getConfig().getInt("claim.particle.spread"), getConfig().getInt("claim.particle.height"), 0, 0);
        player.spawnParticle(particle, chunk.getBlock(15, 0, 7).getX() + 1, location.getY() + 4, chunk.getBlock(15, 0, 7).getZ(), getConfig().getInt("claim.particle.amount"), 0, getConfig().getInt("claim.particle.height"), getConfig().getInt("claim.particle.spread"), 0);
        player.spawnParticle(particle, chunk.getBlock(7, 0, 15).getX(), location.getY() + 4, chunk.getBlock(7, 0, 15).getZ() + 1, getConfig().getInt("claim.particle.amount"), getConfig().getInt("claim.particle.spread"), getConfig().getInt("claim.particle.height"), 0, 0);
        player.spawnParticle(particle, chunk.getBlock(0, 0, 7).getX(), location.getY() + 4, chunk.getBlock(0, 0, 7).getZ(), getConfig().getInt("claim.particle.amount"), 0, getConfig().getInt("claim.particle.height"), getConfig().getInt("claim.particle.spread"), 0);
    }
    public void claimEffect(Player player, Chunk chunk, OfflinePlayer offlinePlayer) {
        var location = player.getLocation();
        var particle = Particle.valueOf(getConfig().getString("claim.particle.type"));
        var north = chunk.getBlock(1,0,0).getLocation().add(0,0,-1).getChunk();
        var east = chunk.getBlock(15,0,1).getLocation().add(1,0,0).getChunk();
        var south = chunk.getBlock(1,0,15).getLocation().add(0,0,1).getChunk();
        var west = chunk.getBlock(0,0,1).getLocation().add(-1,0,0).getChunk();
        if (getChunkHandler().isClaimed(north)) {
            if (getChunkHandler().getOwner(north) != offlinePlayer) {
                player.spawnParticle(particle, chunk.getBlock(7, 0, 0).getX(), location.getY() + 4, chunk.getBlock(7, 0, 0).getZ(), getConfig().getInt("claim.particle.amount"), getConfig().getInt("claim.particle.spread"), getConfig().getInt("claim.particle.height"), 0, 0);
            }
        } else player.spawnParticle(particle, chunk.getBlock(7, 0, 0).getX(), location.getY() + 4, chunk.getBlock(7, 0, 0).getZ(), getConfig().getInt("claim.particle.amount"), getConfig().getInt("claim.particle.spread"), getConfig().getInt("claim.particle.height"), 0, 0);
        if (getChunkHandler().isClaimed(east)) {
            if (getChunkHandler().getOwner(east) != offlinePlayer) {
                player.spawnParticle(particle, chunk.getBlock(15, 0, 7).getX() + 1, location.getY() + 4, chunk.getBlock(15, 0, 7).getZ(), getConfig().getInt("claim.particle.amount"), 0, getConfig().getInt("claim.particle.height"), getConfig().getInt("claim.particle.spread"), 0);
            }
        } else player.spawnParticle(particle, chunk.getBlock(15, 0, 7).getX() + 1, location.getY() + 4, chunk.getBlock(15, 0, 7).getZ(), getConfig().getInt("claim.particle.amount"), 0, getConfig().getInt("claim.particle.height"), getConfig().getInt("claim.particle.spread"), 0);
        if (getChunkHandler().isClaimed(south)) {
            if (getChunkHandler().getOwner(south) != offlinePlayer) {
                player.spawnParticle(particle, chunk.getBlock(7, 0, 15).getX(), location.getY() + 4, chunk.getBlock(7, 0, 15).getZ() + 1, getConfig().getInt("claim.particle.amount"), getConfig().getInt("claim.particle.spread"), getConfig().getInt("claim.particle.height"), 0, 0);
            }
        } else player.spawnParticle(particle, chunk.getBlock(7, 0, 15).getX(), location.getY() + 4, chunk.getBlock(7, 0, 15).getZ() + 1, getConfig().getInt("claim.particle.amount"), getConfig().getInt("claim.particle.spread"), getConfig().getInt("claim.particle.height"), 0, 0);
        if (getChunkHandler().isClaimed(west)) {
            if (getChunkHandler().getOwner(west) != offlinePlayer) {
                player.spawnParticle(particle, chunk.getBlock(0, 0, 7).getX(), location.getY() + 4, chunk.getBlock(0, 0, 7).getZ(), getConfig().getInt("claim.particle.amount"), 0, getConfig().getInt("claim.particle.height"), getConfig().getInt("claim.particle.spread"), 0);
            }
        } else player.spawnParticle(particle, chunk.getBlock(0, 0, 7).getX(), location.getY() + 4, chunk.getBlock(0, 0, 7).getZ(), getConfig().getInt("claim.particle.amount"), 0, getConfig().getInt("claim.particle.height"), getConfig().getInt("claim.particle.spread"), 0);
    }
    public void unclaimSound(Player player) {
        playSound(player, getConfig().getString("unclaim.sound.type"), (float) getConfig().getDouble("unclaim.sound.volume"), (float) getConfig().getDouble("unclaim.sound.pitch"));
    }
    public void unclaimEffect(Player player, Chunk chunk) {
        var location = player.getLocation();
        var particle = Particle.valueOf(getConfig().getString("unclaim.particle.type"));
        player.spawnParticle(particle, chunk.getBlock(7, 0, 0).getX(), location.getY() + 4, chunk.getBlock(7, 0, 0).getZ(), getConfig().getInt("unclaim.particle.amount"), getConfig().getInt("unclaim.particle.spread"), getConfig().getInt("unclaim.particle.height"), 0, 0);
        player.spawnParticle(particle, chunk.getBlock(15, 0, 7).getX() + 1, location.getY() + 4, chunk.getBlock(15, 0, 7).getZ(), getConfig().getInt("unclaim.particle.amount"), 0, getConfig().getInt("unclaim.particle.height"), getConfig().getInt("unclaim.particle.spread"), 0);
        player.spawnParticle(particle, chunk.getBlock(7, 0, 15).getX(), location.getY() + 4, chunk.getBlock(7, 0, 15).getZ() + 1, getConfig().getInt("unclaim.particle.amount"), getConfig().getInt("unclaim.particle.spread"), getConfig().getInt("unclaim.particle.height"), 0, 0);
        player.spawnParticle(particle, chunk.getBlock(0, 0, 7).getX(), location.getY() + 4, chunk.getBlock(0, 0, 7).getZ(), getConfig().getInt("unclaim.particle.amount"), 0, getConfig().getInt("unclaim.particle.height"), getConfig().getInt("unclaim.particle.spread"), 0);
    }
    public boolean removeAll(OfflinePlayer offlinePlayer) {
        var config = getConfig(offlinePlayer);
        var worlds = config.getConfigurationSection("chunks").getKeys(false);
        if (!worlds.isEmpty()) {
            worlds.forEach(worldName -> {
                var world = getWorldHandler().get(worldName);
                config.getStringList("chunks." + worldName).forEach(longString -> {
                    var chunkKey = Long.parseLong(longString);
                    var x = (int) chunkKey;
                    var z = (int) (chunkKey >> 32);
                    var chunk = world.getChunkAt(x, z);
                    getChunkHandler().removeOwner(chunk);
                });
            });
            return true;
        } else return false;
    }
    public void playSound(Player player, String soundName, float volume, float pitch) {
        player.playSound(player, Sound.valueOf(soundName.toUpperCase()), volume, pitch);
    }
    private void setup(OfflinePlayer offlinePlayer) {
        setObject(offlinePlayer, "name", offlinePlayer.getName());
    }
    public void reload(Player player) {
        if (exists(player)) {
            var file = getFile(player);
            var config = YamlConfiguration.loadConfiguration(file);
            try {
                config.load(file);
                if (!player.getName().equals(config.getString("name"))) {
                    setObject(player, "name", player.getName());
                }
            } catch (IOException | InvalidConfigurationException e) {
                getInstance().sendWarning(e.getMessage());
            }
        } else setup(player);
    }
    public boolean isSurvival(Player player) {
        return player.getGameMode().equals(GameMode.SURVIVAL);
    }
    public void disableFly(Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
    }
    public void disable() {
        if (getEditors().isEmpty())return;
        getEditors().clear();
    }
    public List<OfflinePlayer> getOfflinePlayers() {
        var listed = new ArrayList<OfflinePlayer>();
        var folder = new File(getInstance().getDataFolder(), "userdata");
        if (folder.exists() && folder.isDirectory()) {
            for (var file : folder.listFiles()) {
                if (file.exists() && file.isFile()) {
                    listed.add(getInstance().getOfflinePlayer(UUID.fromString(file.getName().replace(".yml", ""))));
                }
            }
        }
        return listed;
    }
    public void reload() {
        var folder = new File(getInstance().getDataFolder(), "userdata");
        if (folder.exists() && folder.isDirectory()) {
            for (var file : folder.listFiles()) {
                if (file.exists() && file.isFile()) {
                    var config = YamlConfiguration.loadConfiguration(file);
                    try {
                        config.load(file);
                    } catch (IOException | InvalidConfigurationException e) {
                        getInstance().sendWarning(e.getMessage());
                    }
                }
            }
        }
    }
    public List<Player> getEditors() {
        return editors;
    }
}