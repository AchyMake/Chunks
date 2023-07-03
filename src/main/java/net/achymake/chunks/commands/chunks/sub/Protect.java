package net.achymake.chunks.commands.chunks.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunks.ChunksSubCommand;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Protect extends ChunksSubCommand {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
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
                    if (getChunkStorage().isProtected(chunk)) {
                        Chunks.sendActionBar(player, "&cChunk is already protected");
                    } else if (getChunkStorage().isClaimed(chunk)) {
                        Chunks.sendActionBar(player, "&cChunk already owned by &f " + getChunkStorage().getOwner(chunk).getName());
                    } else {
                        Chunks.sendActionBar(player, "&6Chunk is now protected");
                        getChunkStorage().protect(chunk);
                        getChunkStorage().claimEffect(player);
                    }
                }
            }
        }
    }
}