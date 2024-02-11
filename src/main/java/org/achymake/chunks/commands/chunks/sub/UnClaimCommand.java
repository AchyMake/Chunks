package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnClaimCommand extends ChunksSubCommand {
    private final Chunks plugin;
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public UnClaimCommand(Chunks plugin) {
        this.plugin = plugin;
    }
    @Override
    public String getName() {
        return "unclaim";
    }
    @Override
    public String getDescription() {
        return "unclaim current chunk";
    }
    @Override
    public String getSyntax() {
        return "/chunks unclaim";
    }
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (sender.hasPermission("chunks.command.chunks.unclaim")) {
                if (args.length == 1) {
                    Chunk chunk = player.getLocation().getChunk();
                    if (getChunkStorage().isClaimed(chunk)) {
                        getMessage().send(player, "&6You safely unclaimed&f " + getChunkStorage().getOwner(chunk).getName() + "&6 chunk");
                        getChunkStorage().unclaim(chunk);
                        getChunkStorage().unclaimEffect(player, chunk);
                        getChunkStorage().unclaimSound(player);
                    } else {
                        getMessage().send(player, "&cCurrent chunk is already unclaimed");
                    }
                }
                if (args.length == 3) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                    if (args[2].equalsIgnoreCase("all")) {
                        getUserdata().unclaimALL(offlinePlayer);
                        getMessage().send(player, "&6You safely unclaimed all&f " + offlinePlayer.getName() + "&6 chunks");
                    }
                }
            }
        }
    }
}
