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
    private final Userdata userdata;
    private final ChunkStorage chunkStorage;
    private final Message message;
    public InfoCommand(Chunks plugin) {
        userdata = plugin.getUserdata();
        chunkStorage = plugin.getChunkStorage();
        message = plugin.getMessage();
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
                    if (chunkStorage.isClaimed(chunk)) {
                        message.send(player, "&6Chunks Info:&f Chunk");
                        message.send(player, "&6Owner:&f " + chunkStorage.getOwner(chunk).getName());
                        message.send(player, "&6Date claimed:&f " + chunkStorage.getDateClaimed(chunk));
                        message.send(player, "&6Chunks claimed:&f " + chunkStorage.getClaimedCount(chunk));
                        if (chunkStorage.getMembers(chunk).isEmpty()) {
                            message.send(player, chunkStorage.getOwner(chunk).getName() + "&6 has no members");
                        } else {
                            message.send(player, chunkStorage.getOwner(chunk).getName()+"&6 members:");
                            for (UUID uuid : chunkStorage.getMembersUUID(chunk)) {
                                message.send(player, "- " + player.getServer().getOfflinePlayer(uuid).getName());
                            }
                        }
                    }
                }
                if (args.length == 2) {
                    OfflinePlayer target = player.getServer().getOfflinePlayer(args[1]);
                    if (userdata.exist(target)) {
                        message.send(player, "&6Chunks Info:&f "+target.getName());
                        message.send(player, "&6Chunks claimed:&f " + chunkStorage.getClaimedCount(target));
                        if (userdata.getMembers(target).isEmpty()) {
                            message.send(player, target.getName() + "&6 has no members");
                        } else {
                            message.send(player, "&6Members:");
                            for (UUID uuid : chunkStorage.getMembersUUID(target)) {
                                message.send(player, "- " + player.getServer().getOfflinePlayer(uuid).getName());
                            }
                        }
                    }
                }
            }
        }
    }
}
