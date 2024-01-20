package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.files.ChunkStorage;
import org.achymake.chunks.files.Message;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProtectCommand extends ChunksSubCommand {
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
        if (sender instanceof Player player) {
            if (player.hasPermission("chunks.command.chunks.protect")) {
                if (args.length == 1) {
                    Chunk chunk = player.getLocation().getChunk();
                    if (getChunkStorage().isProtected(chunk)) {
                        getMessage().sendActionBar(player, "&cError:&7 Chunk already protected");
                    } else if (getChunkStorage().isClaimed(chunk)) {
                        getMessage().sendActionBar(player, "&cError:&7 Chunk owned by &f " + getChunkStorage().getOwner(chunk).getName());
                    } else {
                        getMessage().sendActionBar(player, "&6Chunk is now protected");
                        getChunkStorage().protect(chunk);
                        getChunkStorage().claimEffect(player);
                    }
                }
            }
        }
    }
}
