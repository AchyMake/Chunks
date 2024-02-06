package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteCommand extends ChunksSubCommand {
    private final Chunks plugin;
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public DeleteCommand(Chunks plugin) {
        this.plugin = plugin;
    }
    @Override
    public String getName() {
        return "delete";
    }
    @Override
    public String getDescription() {
        return "deletes the chunk";
    }
    @Override
    public String getSyntax() {
        return "/chunks delete";
    }
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender.hasPermission("chunks.command.chunks.delete")) {
            if (sender instanceof Player player) {
                if (args.length == 1){
                    Chunk chunk = player.getLocation().getChunk();
                    if (getChunkStorage().isClaimed(chunk)) {
                        getMessage().send(player, "&6You safely unclaimed&f " + getChunkStorage().getOwner(chunk).getName() + "&6 chunk");
                        getChunkStorage().unclaim(chunk);
                        getChunkStorage().unclaimEffect(player, chunk);
                        getChunkStorage().unclaimSound(player);
                    } else {
                        getMessage().send(player, "&cCurrent chunk is already unclaimed");
                    }
                }
            }
        }
    }
}
