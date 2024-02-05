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
                player.sendMessage(getMessage().getString("commands.chunk.help.title"));
                if (player.hasPermission("chunks.command.chunk.claim")) {
                    player.sendMessage(getMessage().getString("commands.chunk.help.claim"));
                }
                player.sendMessage(getMessage().getString("commands.chunk.help.help"));
                if (player.hasPermission("chunks.command.chunk.members")) {
                    for (String messages : getMessage().getConfig().getStringList("commands.chunk.help.members")) {
                        player.sendMessage(getMessage().addColor(messages));
                    }
                }
                if (player.hasPermission("chunks.command.chunk.tnt")) {
                    player.sendMessage(getMessage().getString("commands.chunk.help.tnt"));
                }
                if (player.hasPermission("chunks.command.chunk.unclaim")) {
                    player.sendMessage(getMessage().getString("commands.chunk.help.unclaim"));
                }
            }
        }
    }
}
