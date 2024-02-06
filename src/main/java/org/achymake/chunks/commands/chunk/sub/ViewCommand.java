package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.Message;
import org.bukkit.entity.Player;

public class ViewCommand extends ChunkSubCommand {
    private final Chunks plugin;
    private Message getMessage() {
        return plugin.getMessage();
    }
    public ViewCommand(Chunks plugin) {
        this.plugin = plugin;
    }
    @Override
    public String getName() {
        return "view";
    }
    @Override
    public String getDescription() {
        return "view claimed chunks";
    }
    @Override
    public String getSyntax() {
        return "/chunk view";
    }
    @Override
    public void perform(Player player, String[] args) {
        if (player.hasPermission("chunks.command.chunk.view")) {
            if (args.length == 1) {
                plugin.getUserdata().chunkView(player);
                plugin.getChunkStorage().claimSound(player);
            }
        }
    }
}