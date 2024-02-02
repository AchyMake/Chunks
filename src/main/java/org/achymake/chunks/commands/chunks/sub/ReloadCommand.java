package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.data.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends ChunksSubCommand {
    private final Chunks plugin;
    private final Message message;
    public ReloadCommand(Chunks plugin) {
        this.plugin = plugin;
        message = plugin.getMessage();
    }
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
                    plugin.reload();
                    message.send(player, "&6Chunks:&f reloaded");
                }
            }
        }
    }
}
