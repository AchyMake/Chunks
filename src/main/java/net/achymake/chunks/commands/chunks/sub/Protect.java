package net.achymake.chunks.commands.chunks.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunks.ChunksSubCommand;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Protect extends ChunksSubCommand {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    private final Message message = Chunks.getMessage();
    @Override
    public String getName() {
        return "protect";
    }
    @Override
    public String getDescription() {
        return "claims the chunk";
    }
    @Override
    public String getSyntax() {
        return "/chunk claim";
    }
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                if (player.hasPermission("chunks.command.chunks.protect")) {
                    Chunk chunk = player.getLocation().getChunk();
                    if (chunkStorage.isProtected(chunk)) {
                        message.sendActionBar(player, "&cChunk is already protected");
                    } else if (chunkStorage.isClaimed(chunk)) {
                        message.sendActionBar(player, "&cChunk already owned by &f " + chunkStorage.getOwner(chunk).getName());
                    } else {
                        message.sendActionBar(player, "&6Chunk is now protected");
                        chunkStorage.protect(chunk);
                        chunkStorage.claimEffect(player);
                    }
                }
            }
        }
    }
}