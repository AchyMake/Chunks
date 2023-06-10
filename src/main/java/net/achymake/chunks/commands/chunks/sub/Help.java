package net.achymake.chunks.commands.chunks.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunks.ChunksSubCommand;
import net.achymake.chunks.files.Message;
import org.bukkit.command.CommandSender;

public class Help extends ChunksSubCommand {
    private final Message message = Chunks.getMessage();
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
                message.send(sender, "&6Chunks Help:");
                if (sender.hasPermission("chunks.command.chunks.delete")) {
                    message.send(sender, "&f/chunks delete &7- safely unclaims chunk");
                }
                if (sender.hasPermission("chunks.command.chunks.edit")) {
                    message.send(sender, "&f/chunks edit &7- toggle chunk edit");
                }
                if (sender.hasPermission("chunks.command.chunks.effect")) {
                    message.send(sender, "&f/chunks effect &7- effects of claiming");
                }
                message.send(sender, "&f/chunks help &7- show this list");
                if (sender.hasPermission("chunks.command.chunks.info")) {
                    message.send(sender, "&f/chunks info &7- checks info of chunk");
                }
                if (sender.hasPermission("chunks.command.chunks.protect")) {
                    message.send(sender, "&f/chunks protect &7- protects a chunk");
                }
                if (sender.hasPermission("chunks.command.chunks.reload")) {
                    message.send(sender, "&f/chunks reload &7- reload smpchunks plugin");
                }
                if (sender.hasPermission("chunks.command.chunks.setowner")) {
                    message.send(sender, "&f/chunks setowner target &7- sets chunk owner if claimed");
                }
            }
        }
    }
}