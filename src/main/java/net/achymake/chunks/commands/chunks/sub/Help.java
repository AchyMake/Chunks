package net.achymake.chunks.commands.chunks.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help extends ChunksSubCommand {
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
                Player player = (Player) sender;
                Chunks.send(player, "&6Chunks Help:");
                if (sender.hasPermission("chunks.command.chunks.delete")) {
                    Chunks.send(player, "&f/chunks delete &7- safely unclaims chunk");
                }
                if (sender.hasPermission("chunks.command.chunks.edit")) {
                    Chunks.send(player, "&f/chunks edit &7- toggle chunk edit");
                }
                if (sender.hasPermission("chunks.command.chunks.effect")) {
                    Chunks.send(player, "&f/chunks effect &7- effects of claiming");
                }
                Chunks.send(player, "&f/chunks help &7- show this list");
                if (sender.hasPermission("chunks.command.chunks.info")) {
                    Chunks.send(player, "&f/chunks info &7- checks info of chunk");
                }
                if (sender.hasPermission("chunks.command.chunks.protect")) {
                    Chunks.send(player, "&f/chunks protect &7- protects a chunk");
                }
                if (sender.hasPermission("chunks.command.chunks.reload")) {
                    Chunks.send(player, "&f/chunks reload &7- reload smpchunks plugin");
                }
                if (sender.hasPermission("chunks.command.chunks.setowner")) {
                    Chunks.send(player, "&f/chunks setowner target &7- sets chunk owner if claimed");
                }
            }
        }
    }
}