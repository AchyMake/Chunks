package net.achymake.chunks.commands.chunks.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunks.ChunksSubCommand;
import net.achymake.chunks.files.Message;
import org.bukkit.command.CommandSender;

public class Help extends ChunksSubCommand {
    private Message getMessage() {
        return Chunks.getMessage();
    }
    @Override
    public String getName() {
        return "help";
    }
    @Override
    public String getDescription() {
        return "checks chunk help";
    }
    @Override
    public String getSyntax() {
        return "/chunk help";
    }
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender.hasPermission("chunks.command.chunks.help")) {
            if (args.length == 1) {
                getMessage().send(sender, "&6Chunks Help:");
                if (sender.hasPermission("chunks.command.chunks.delete")) {
                    getMessage().send(sender, "&f/chunks delete &7- safely unclaims chunk");
                }
                if (sender.hasPermission("chunks.command.chunks.edit")) {
                    getMessage().send(sender, "&f/chunks edit &7- toggle chunk edit");
                }
                if (sender.hasPermission("chunks.command.chunks.effect")) {
                    getMessage().send(sender, "&f/chunks effect &7- effects of claiming");
                }
                getMessage().send(sender, "&f/chunks help &7- show this list");
                if (sender.hasPermission("chunks.command.chunks.info")) {
                    getMessage().send(sender, "&f/chunks info &7- checks info of chunk");
                }
                if (sender.hasPermission("chunks.command.chunks.protect")) {
                    getMessage().send(sender, "&f/chunks protect &7- protects a chunk");
                }
                if (sender.hasPermission("chunks.command.chunks.reload")) {
                    getMessage().send(sender, "&f/chunks reload &7- reload smpchunks plugin");
                }
                if (sender.hasPermission("chunks.command.chunks.setowner")) {
                    getMessage().send(sender, "&f/chunks setowner target &7- sets chunk owner if claimed");
                }
            }
        }
    }
}