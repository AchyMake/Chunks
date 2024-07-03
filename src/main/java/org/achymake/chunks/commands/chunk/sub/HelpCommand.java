package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.Message;
import org.bukkit.entity.Player;

public class HelpCommand extends ChunkSubCommand {
    private final Chunks plugin;
    private Message getMessage() {
        return plugin.getMessage();
    }
    public HelpCommand(Chunks plugin) {
        this.plugin = plugin;
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
    public void perform(Player player, String[] args) {
        if (player.hasPermission("chunks.command.chunk.help")) {
            if (args.length == 1) {
                getMessage().send(player, "&6Chunk Help:");
                if (player.hasPermission("chunks.command.chunk.ban")) {
                    getMessage().send(player, "&f/chunk ban player&7 - bans target from chunk");
                }
                if (player.hasPermission("chunks.command.chunk.banned")) {
                    getMessage().send(player, "&f/chunk banned&7 - check list of banned");
                }
                if (player.hasPermission("chunks.command.chunk.claim")) {
                    getMessage().send(player, "&f/chunk claim&7 - claim current chunk");
                }
                getMessage().send(player, "&f/chunk help&7 - show this list");
                if (player.hasPermission("chunks.command.chunk.members")) {
                    getMessage().send(player, "&f/chunk members add player&7 - adds chunk member");
                    getMessage().send(player, "&f/chunk members remove player&7 - removes chunk member");
                }
                if (player.hasPermission("chunks.command.chunk.tnt")) {
                    getMessage().send(player, "&f/chunk tnt&7 - toggle tnt");
                }
                if (player.hasPermission("chunks.command.chunk.unban")) {
                    getMessage().send(player, "&f/chunk unban player&7 - unbans target");
                }
                if (player.hasPermission("chunks.command.chunk.unclaim")) {
                    getMessage().send(player, "&f/chunk unclaim&7 - unclaim current chunk");
                }
                if (player.hasPermission("chunks.command.chunk.view")) {
                    getMessage().send(player, "&f/chunk view&7 - view claimed chunks");
                }
                if (player.hasPermission("chunks.command.chunk.view.others")) {
                    getMessage().send(player, "&f/chunk view target&7 - view target claimed chunks");
                }
            }
        }
    }
}
