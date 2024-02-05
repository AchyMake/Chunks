package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Userdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InfoCommand extends ChunksSubCommand {
    private final Chunks plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
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
                    if (getChunkStorage().isClaimed(chunk)) {
                        getMessage().send(player, "&6Chunks Info:&f Chunk");
                        getMessage().send(player, "&6Owner:&f " + getChunkStorage().getOwner(chunk).getName());
                        getMessage().send(player, "&6Date claimed:&f " + getChunkStorage().getDateClaimed(chunk));
                        getMessage().send(player, "&6Chunks claimed:&f " + getChunkStorage().getClaimedCount(chunk));
                        if (getChunkStorage().getMembers(chunk).isEmpty()) {
                            getMessage().send(player, getChunkStorage().getOwner(chunk).getName() + "&6 has no members");
                        } else {
                            getMessage().send(player, getChunkStorage().getOwner(chunk).getName()+"&6 members:");
                            for (UUID uuid : getChunkStorage().getMembersUUID(chunk)) {
                                getMessage().send(player, "- " + player.getServer().getOfflinePlayer(uuid).getName());
                            }
                        }
                    }
                }
                if (args.length == 2) {
                    OfflinePlayer target = player.getServer().getOfflinePlayer(args[1]);
                    if (getUserdata().exist(target)) {
                        getMessage().send(player, "&6Chunks Info:&f "+target.getName());
                        getMessage().send(player, "&6Chunks claimed:&f " + getChunkStorage().getClaimedCount(target));
                        if (getUserdata().getMembers(target).isEmpty()) {
                            getMessage().send(player, target.getName() + "&6 has no members");
                        } else {
                            getMessage().send(player, "&6Members:");
                            for (UUID uuid : getChunkStorage().getMembersUUID(target)) {
                                getMessage().send(player, "- " + player.getServer().getOfflinePlayer(uuid).getName());
                            }
                        }
                    }
                }
            }
        }
    }
}
