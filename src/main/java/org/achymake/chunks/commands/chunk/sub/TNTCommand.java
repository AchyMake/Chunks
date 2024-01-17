package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.files.ChunkStorage;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class TNTCommand extends ChunkSubCommand {
    private Chunks getPlugin() {
        return Chunks.getInstance();
    }
    private ChunkStorage getChunkStorage() {
        return getPlugin().getChunkStorage();
    }
    @Override
    public String getName() {
        return "tnt";
    }
    @Override
    public String getDescription() {
        return "toggle tnt";
    }
    @Override
    public String getSyntax() {
        return "/chunk tnt";
    }
    @Override
    public void perform(Player player, String[] args) {
        if (player.hasPermission("chunks.command.chunk.tnt")) {
            if (args.length == 1) {
                Chunk chunk = player.getLocation().getChunk();
                if (getChunkStorage().isProtected(chunk)) {
                    getPlugin().send(player, "&cChunk already owned by&f Server");
                } else if (getChunkStorage().isClaimed(chunk)) {
                    if (getChunkStorage().isOwner(player, chunk)) {
                        if (getChunkStorage().TNTAllowed(chunk)) {
                            getChunkStorage().getData(chunk).remove(NamespacedKey.minecraft("tnt"));
                            getPlugin().send(player, "&6You disabled tnt for this chunk");
                        } else {
                            getChunkStorage().getData(chunk).set(NamespacedKey.minecraft("tnt"), PersistentDataType.STRING, "true");
                            getPlugin().send(player, "&6You enabled tnt for this chunk");
                        }
                    } else {
                        if (player.hasPermission("chunks.command.chunks.edit")) {
                            if (getChunkStorage().TNTAllowed(chunk)) {
                                getChunkStorage().getData(chunk).remove(NamespacedKey.minecraft("tnt"));
                                getPlugin().send(player, "&6You disabled tnt for this chunk");
                            } else {
                                getChunkStorage().getData(chunk).set(NamespacedKey.minecraft("tnt"), PersistentDataType.STRING, "true");
                                getPlugin().send(player, "&6You enabled tnt for this chunk");
                            }
                        } else {
                            getPlugin().send(player, "&cChunk already owned by&f " + getChunkStorage().getOwner(chunk).getName());
                        }
                    }
                } else {
                    getPlugin().send(player, "&cChunk is already unclaimed");
                }
            }
        }
    }
}
