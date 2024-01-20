package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.files.ChunkStorage;
import org.achymake.chunks.files.Database;
import org.achymake.chunks.files.Message;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class InfoCommand extends ChunksSubCommand {
    private Chunks getPlugin() {
        return Chunks.getInstance();
    }
    private Database getDatabase() {
        return getPlugin().getDatabase();
    }
    private ChunkStorage getChunkStorage() {
        return getPlugin().getChunkStorage();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
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
                    } else if (getChunkStorage().isProtected(chunk)) {
                        getMessage().send(player, "&6Chunks Info:&f Chunk");
                        getMessage().send(player, "&6Owner:&f Server");
                    }
                }
                if (args.length == 2) {
                    OfflinePlayer target = player.getServer().getOfflinePlayer(args[1]);
                    if (getDatabase().exist(target)) {
                        getMessage().send(player, "&6Chunks Info:&f "+target.getName());
                        getMessage().send(player, "&6Chunks claimed:&f " + getChunkStorage().getClaimedCount(target));
                        if (getDatabase().getMembers(target).isEmpty()) {
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
