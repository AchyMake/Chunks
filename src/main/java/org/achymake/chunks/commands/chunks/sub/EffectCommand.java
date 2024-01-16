package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.files.Database;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EffectCommand extends ChunksSubCommand {
    private Chunks getPlugin() {
        return Chunks.getInstance();
    }
    private Database getDatabase() {
        return getPlugin().getDatabase();
    }
    @Override
    public String getName() {
        return "effect";
    }
    @Override
    public String getDescription() {
        return "start claim/unclaim effects";
    }
    @Override
    public String getSyntax() {
        return "/chunks effect";
    }
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (sender.hasPermission("chunks.command.chunks.effect")) {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("claim")) {
                        getDatabase().claimEffect(player);
                        getPlugin().sendActionBar(player, "&6Started the effects of claiming");
                    }
                    if (args[1].equalsIgnoreCase("unclaim")) {
                        getDatabase().unclaimEffect(player);
                        getPlugin().sendActionBar(player, "&6Started the effects of unclaiming");
                    }
                }
            }
        }
    }
}
