package net.achymake.chunks.commands.chunks.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload extends ChunksSubCommand {
    private Chunks getPlugin() {
        return Chunks.getInstance();
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
        if (sender.hasPermission("chunks.command.chunks.reload")) {
            if (args.length == 1) {
                Player player = (Player) sender;
                getPlugin().reload();
                Chunks.send(player, "&6Chunks:&f files reloaded");
            }
        }
    }
}