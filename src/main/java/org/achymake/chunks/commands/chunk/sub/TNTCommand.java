package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class TNTCommand extends ChunkSubCommand {
    private final Chunks plugin;
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private boolean isAllowed(Chunk chunk) {
        return plugin.isAllowed(chunk);
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
                if (isAllowed(chunk)) {
                    if (getChunkdata().isClaimed(chunk)) {
                        if (getChunkdata().isOwner(player, chunk)) {
                            if (getChunkdata().isTNTAllowed(chunk)) {
                                getChunkdata().toggleTNT(chunk, false);
                                getMessage().send(player, "&6Disabled&f TNT&6 for current chunk");
                            } else {
                                getChunkdata().toggleTNT(chunk, true);
                                getMessage().send(player, "&6Enabled&f TNT&6 for current chunk");
                            }
                        } else {
                            if (player.hasPermission("chunks.command.chunks.edit")) {
                                if (getChunkdata().isTNTAllowed(chunk)) {
                                    getChunkdata().toggleTNT(chunk, false);
                                    getMessage().send(player, "&6Disabled&f TNT&6 for current chunk");
                                } else {
                                    getChunkdata().toggleTNT(chunk, true);
                                    getMessage().send(player, "&6Enabled&f TNT&6 for current chunk");
                                }
                            } else {
                                getMessage().send(player, "&cChunk is owned by&f " + getChunkdata().getOwner(chunk).getName());
                            }
                        }
                    } else {
                        getMessage().send(player, "&cChunk is unclaimed");
                    }
                } else {
                    getMessage().send(player, "&cYou are not allowed to toggle tnt in this world");
                }
            }
        }
    }
}
