package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.files.Database;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProtectCommand extends ChunksSubCommand {
    private Database getDatabase() {
        return Chunks.getDatabase();
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
                    if (getDatabase().isProtected(chunk)) {
                        Chunks.sendActionBar(player, "&cChunk is already protected");
                    } else if (getDatabase().isClaimed(chunk)) {
                        Chunks.sendActionBar(player, "&cChunk already owned by &f " + getDatabase().getOwner(chunk).getName());
                    } else {
                        Chunks.sendActionBar(player, "&6Chunk is now protected");
                        getDatabase().protect(chunk);
                        getDatabase().claimEffect(player);
                    }
                }
            }
        }
    }
}
