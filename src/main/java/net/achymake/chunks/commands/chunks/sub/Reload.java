package net.achymake.chunks.commands.chunks.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunks.ChunksSubCommand;
import net.achymake.chunks.files.Message;
import org.bukkit.command.CommandSender;

public class Reload extends ChunksSubCommand {
    private Chunks getPlugin() {
        return Chunks.getInstance();
    }
    private Message getMessage() {
        return Chunks.getMessage();
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
                getPlugin().reload();
                getMessage().send(sender, "&6Chunks:&f config files reloaded");
            }
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("players")) {
                    getPlugin().reloadPlayerFiles();
                    getMessage().send(sender, "&6Chunks:&f player files reloaded");
                }
            }
        }
    }
}