package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.data.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand extends ChunksSubCommand {
    private final Message message;
    public HelpCommand(Chunks plugin) {
        message = plugin.getMessage();
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
        if (sender instanceof Player player) {
            if (player.hasPermission("chunks.command.chunks.help")) {
                if (args.length == 1) {
                    message.send(player, "&6Chunks Help:");
                    if (player.hasPermission("chunks.command.chunks.delete")) {
                        message.send(player, "&f/chunks delete &7- safely unclaims chunk");
                    }
                    if (player.hasPermission("chunks.command.chunks.edit")) {
                        message.send(player, "&f/chunks edit &7- toggle chunk edit");
                    }
                    if (player.hasPermission("chunks.command.chunks.effect")) {
                        message.send(player, "&f/chunks effect &7- effects of claiming");
                    }
                    message.send(player, "&f/chunks help &7- show this list");
                    if (player.hasPermission("chunks.command.chunks.info")) {
                        message.send(player, "&f/chunks info &7- checks info of chunk");
                    }
                    if (player.hasPermission("chunks.command.chunks.protect")) {
                        message.send(player, "&f/chunks protect &7- protects a chunk");
                    }
                    if (player.hasPermission("chunks.command.chunks.reload")) {
                        message.send(player, "&f/chunks reload &7- reload smpchunks plugin");
                    }
                    if (player.hasPermission("chunks.command.chunks.setowner")) {
                        message.send(player, "&f/chunks setowner target &7- sets chunk owner if claimed");
                    }
                }
            }
        }
    }
}
