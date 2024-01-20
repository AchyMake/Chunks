package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.files.Message;
import org.bukkit.entity.Player;

public class HelpCommand extends ChunkSubCommand {
    private Chunks getPlugin() {
        return Chunks.getInstance();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
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
            if (args.length == 1){
                getMessage().send(player, "&6Chunk Help:");
                if (player.hasPermission("chunks.command.chunk.claim")) {
                    getMessage().send(player, "/chunk claim&7 - claims current chunk");
                }
                getMessage().send(player, "/chunk help&7 - show this list");
                if (player.hasPermission("chunks.command.chunk.members")) {
                    getMessage().send(player, "/chunk members&7 - check member list");
                    getMessage().send(player, "/chunk members add target&7 - add member");
                    getMessage().send(player, "/chunk members remove target&7 - remove member");
                }
                if (player.hasPermission("chunks.command.chunk.tnt")) {
                    getMessage().send(player, "/chunk tnt&7 - toggle tnt for the chunk");
                }
                if (player.hasPermission("chunks.command.chunk.unclaim")) {
                    getMessage().send(player, "/chunk unclaim&7 - unclaims current chunk");
                }
            }
        }
    }
}
