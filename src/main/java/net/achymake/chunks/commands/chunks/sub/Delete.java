package net.achymake.chunks.commands.chunks.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunks.ChunksSubCommand;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Delete extends ChunksSubCommand {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    private Message getMessage() {
        return Chunks.getMessage();
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
            if (sender instanceof Player) {
                if (args.length == 1){
                    Player player = (Player) sender;
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
                        getMessage().send(player, "&cChunk is already unclaimed");
                    }
                }
            }
        }
    }
}