package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends ChunksSubCommand {
    @Override
    public String getName() {
        return "reload";
    }
    @Override
    public String getDescription() {
        return "reload config files";
    }
    @Override
    public String getSyntax() {
        return "/chunks reload";
    }
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("chunks.command.chunks.reload")) {
                if (args.length == 1) {
                    Chunks.getInstance().reload();
                    Chunks.send(player, "&6Chunks:&f reloaded");
                }
            }
        }
    }
}
