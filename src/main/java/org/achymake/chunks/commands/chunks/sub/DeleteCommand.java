package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.files.ChunkStorage;
import org.achymake.chunks.files.Message;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteCommand extends ChunksSubCommand {
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
                    if (getChunkStorage().isProtected(chunk)) {
                        getChunkStorage().unprotect(chunk);
                        getChunkStorage().unclaimEffect(player);
                        getMessage().send(player, "&6Chunk is now unprotected");
                    } else if (getChunkStorage().isClaimed(chunk)) {
                        getMessage().send(player, "&6You safely unclaimed&f " + getChunkStorage().getOwner(chunk).getName() + "&6 chunk");
                        getChunkStorage().unclaim(chunk);
                        getChunkStorage().unclaimEffect(player);
                    } else {
                        getMessage().send(player, "&cError:&7 Chunk already unclaimed");
                    }
                }
            }
        }
    }
}
