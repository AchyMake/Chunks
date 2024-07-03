package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ViewCommand extends ChunkSubCommand {
    private final Chunks plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
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
                getUserdata().chunkView(player, player);
                getChunkdata().claimSound(player);
            }
            if (args.length == 2) {
                if (player.hasPermission("chunks.command.chunk.view.others")) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                    if (getUserdata().exist(offlinePlayer)) {
                        getUserdata().chunkView(player, offlinePlayer);
                        getChunkdata().claimSound(player);
                    }
                }
            }
        }
    }
}