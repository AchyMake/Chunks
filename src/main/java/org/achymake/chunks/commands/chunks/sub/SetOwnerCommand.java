package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.files.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetOwnerCommand extends ChunksSubCommand {
    private Database getDatabase() {
        return Chunks.getDatabase();
    }
    @Override
    public String getName() {
        return "setowner";
    }
    @Override
    public String getDescription() {
        return "sets owner of the chunk";
    }
    @Override
    public String getSyntax() {
        return "/chunks setowner";
    }
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("chunks.command.chunks.setowner")) {
                if (args.length == 2) {
                    if (getDatabase().isProtected(player.getLocation().getChunk())) {
                        Chunks.send(player, "&cChunk is protected by&f Server");
                    } else {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                        if (getDatabase().exist(target)) {
                            getDatabase().setOwner(player, target, player.getLocation().getChunk());
                            getDatabase().claimEffect(player);
                            Chunks.send(player, "&6Chunk is now owned by&f " + getDatabase().getOwner(player.getLocation().getChunk()).getName());
                        } else {
                            Chunks.send(player, target.getName() + "&c has never joined");
                        }
                    }
                }
            }
        }
    }
}
