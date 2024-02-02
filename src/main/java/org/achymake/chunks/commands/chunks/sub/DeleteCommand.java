package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteCommand extends ChunksSubCommand {
    private final ChunkStorage chunkStorage;
    private final Message message;
    public DeleteCommand(Chunks plugin) {
        chunkStorage = plugin.getChunkStorage();
        message = plugin.getMessage();
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
                    if (chunkStorage.isClaimed(chunk)) {
                        message.send(player, "&6You safely unclaimed&f " + chunkStorage.getOwner(chunk).getName() + "&6 chunk");
                        chunkStorage.unclaim(chunk);
                        chunkStorage.unclaimEffect(player);
                    } else {
                        message.send(player, "&c&lHey!&7 Sorry, but chunk is already unclaimed");
                    }
                }
            }
        }
    }
}
