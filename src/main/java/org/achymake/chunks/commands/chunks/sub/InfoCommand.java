package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommand extends ChunksSubCommand {
    private final Chunks plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public InfoCommand(Chunks plugin) {
        this.plugin = plugin;
    }
    @Override
    public String getName() {
        return "info";
    }
    @Override
    public String getDescription() {
        return "checks chunk info";
    }
    @Override
    public String getSyntax() {
        return "/chunks info";
    }
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("chunks.command.chunks.info")) {
                if (args.length == 1) {
                    Chunk chunk = player.getLocation().getChunk();
                    if (getChunkdata().isClaimed(chunk)) {
                        getMessage().send(player, "&6Chunks Info:&f Chunk");
                        getMessage().send(player, "&6Owner:&f " + getChunkdata().getOwner(chunk).getName());
                        getMessage().send(player, "&6Date claimed:&f " + getChunkdata().getDateClaimed(chunk));
                        getMessage().send(player, "&6Chunks claimed:&f " + getChunkdata().getClaimCount(chunk));
                        if (getUserdata().getMembers(getChunkdata().getOwner(chunk)).isEmpty()) {
                            getMessage().send(player, getChunkdata().getOwner(chunk).getName() + "&6 has no members");
                        } else {
                            getMessage().send(player, getChunkdata().getOwner(chunk).getName()+"&6 members:");
                            for (OfflinePlayer offlinePlayer : getUserdata().getMembers(getChunkdata().getOwner(chunk))) {
                                getMessage().send(player, "- " + offlinePlayer.getName());
                            }
                        }
                    }
                }
                if (args.length == 2) {
                    OfflinePlayer target = player.getServer().getOfflinePlayer(args[1]);
                    if (getUserdata().exist(target)) {
                        getMessage().send(player, "&6Chunks Info:&f "+target.getName());
                        getMessage().send(player, "&6Chunks claimed:&f " + getUserdata().getClaimCount(target));
                        if (getUserdata().getMembers(target).isEmpty()) {
                            getMessage().send(player, target.getName() + "&6 has no members");
                        } else {
                            getMessage().send(player, "&6Members:");
                            for (OfflinePlayer offlinePlayer : getUserdata().getMembers(target)) {
                                getMessage().send(player, "- " + offlinePlayer.getName());
                            }
                        }
                    }
                }
            }
        }
    }
}
