package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class TNTCommand extends ChunkSubCommand {
    private final ChunkStorage chunkStorage;
    private final Message message;
    public TNTCommand(Chunks plugin) {
        chunkStorage = plugin.getChunkStorage();
        message = plugin.getMessage();
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
                if (chunkStorage.isClaimed(chunk)) {
                    if (chunkStorage.isOwner(player, chunk)) {
                        if (chunkStorage.TNTAllowed(chunk)) {
                            chunkStorage.getData(chunk).remove(NamespacedKey.minecraft("tnt"));
                            message.send(player, "&6You disabled tnt for this chunk");
                        } else {
                            chunkStorage.getData(chunk).set(NamespacedKey.minecraft("tnt"), PersistentDataType.STRING, "true");
                            message.send(player, "&6You enabled tnt for this chunk");
                        }
                    } else {
                        if (player.hasPermission("chunks.command.chunks.edit")) {
                            if (chunkStorage.TNTAllowed(chunk)) {
                                chunkStorage.getData(chunk).remove(NamespacedKey.minecraft("tnt"));
                                message.send(player, "&6You disabled tnt for this chunk");
                            } else {
                                chunkStorage.getData(chunk).set(NamespacedKey.minecraft("tnt"), PersistentDataType.STRING, "true");
                                message.send(player, "&6You enabled tnt for this chunk");
                            }
                        } else {
                            message.send(player, "&c&lHey!&7 Sorry, but chunk is owned by&f " + chunkStorage.getOwner(chunk).getName());
                        }
                    }
                } else {
                    message.send(player, "&c&lHey!&7 Sorry, but chunk is unclaimed");
                }
            }
        }
    }
}
