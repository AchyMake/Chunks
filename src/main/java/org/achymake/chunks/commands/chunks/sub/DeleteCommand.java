package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.files.Database;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteCommand extends ChunksSubCommand {
    private Chunks getPlugin() {
        return Chunks.getInstance();
    }
    private Database getDatabase() {
        return getPlugin().getDatabase();
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
                    if (getDatabase().isProtected(chunk)) {
                        getDatabase().unprotect(chunk);
                        getDatabase().unclaimEffect(player);
                        getPlugin().send(player, "&6Chunk is now unprotected");
                    } else if (getDatabase().isClaimed(chunk)) {
                        getPlugin().send(player, "&6You safely unclaimed&f " + getDatabase().getOwner(chunk).getName() + "&6 chunk");
                        getDatabase().unclaim(chunk);
                        getDatabase().unclaimEffect(player);
                    } else {
                        getPlugin().send(player, "&cChunk is already unclaimed");
                    }
                }
            }
        }
    }
}
