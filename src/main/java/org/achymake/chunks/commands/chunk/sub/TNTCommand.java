package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.text.MessageFormat;

public class TNTCommand extends ChunkSubCommand {
    private final Chunks plugin;
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public TNTCommand(Chunks plugin) {
        this.plugin = plugin;
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
                            player.sendMessage(getMessage().getString("commands.chunk.tnt.disable"));
                        } else {
                            getChunkStorage().getData(chunk).set(NamespacedKey.minecraft("tnt"), PersistentDataType.STRING, "true");
                            player.sendMessage(getMessage().getString("commands.chunk.tnt.enable"));
                        }
                    } else {
                        if (player.hasPermission("chunks.command.chunks.edit")) {
                            if (getChunkStorage().TNTAllowed(chunk)) {
                                getChunkStorage().getData(chunk).remove(NamespacedKey.minecraft("tnt"));
                                player.sendMessage(getMessage().getString("commands.chunk.tnt.disable"));
                            } else {
                                getChunkStorage().getData(chunk).set(NamespacedKey.minecraft("tnt"), PersistentDataType.STRING, "true");
                                player.sendMessage(getMessage().getString("commands.chunk.tnt.enable"));
                            }
                        } else {
                            player.sendMessage(MessageFormat.format(getMessage().getString("commands.chunk.tnt.claimed"), getChunkStorage().getOwner(chunk).getName()));
                        }
                    }
                } else {
                    player.sendMessage(getMessage().getString("commands.chunk.tnt.unclaimed"));
                }
            }
        }
    }
}
