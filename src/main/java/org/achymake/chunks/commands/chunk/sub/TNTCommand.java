package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.files.ChunkStorage;
import org.achymake.chunks.files.Message;
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
    private Message getMessage() {
        return getPlugin().getMessage();
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
                if (getChunkStorage().isClaimed(chunk)) {
                    if (getChunkStorage().isOwner(player, chunk)) {
                        if (getChunkStorage().TNTAllowed(chunk)) {
                            getChunkStorage().getData(chunk).remove(NamespacedKey.minecraft("tnt"));
                            getMessage().send(player, "&6You disabled tnt for this chunk");
                        } else {
                            getChunkStorage().getData(chunk).set(NamespacedKey.minecraft("tnt"), PersistentDataType.STRING, "true");
                            getMessage().send(player, "&6You enabled tnt for this chunk");
                        }
                    } else {
                        if (player.hasPermission("chunks.command.chunks.edit")) {
                            if (getChunkStorage().TNTAllowed(chunk)) {
                                getChunkStorage().getData(chunk).remove(NamespacedKey.minecraft("tnt"));
                                getMessage().send(player, "&6You disabled tnt for this chunk");
                            } else {
                                getChunkStorage().getData(chunk).set(NamespacedKey.minecraft("tnt"), PersistentDataType.STRING, "true");
                                getMessage().send(player, "&6You enabled tnt for this chunk");
                            }
                        } else {
                            getMessage().send(player, "&cError:&7 Chunk is owned by&f " + getChunkStorage().getOwner(chunk).getName());
                        }
                    }
                } else {
                    getMessage().send(player, "&cError:&7 Chunk is unclaimed");
                }
            }
        }
    }
}
