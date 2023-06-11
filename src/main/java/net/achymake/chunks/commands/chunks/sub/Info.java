package net.achymake.chunks.commands.chunks.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunks.ChunksSubCommand;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Database;
import net.achymake.chunks.files.Message;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Info extends ChunksSubCommand {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    private Database getDatabase() {
        return Chunks.getDatabase();
    }
    private Message getMessage() {
        return Chunks.getMessage();
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
        if (sender.hasPermission("chunks.command.chunks.info")) {
            if (sender instanceof Player) {
                if (args.length == 1){
                    Player player = (Player) sender;
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
                                getMessage().send(player, "- " + sender.getServer().getOfflinePlayer(uuid).getName());
                            }
                        }
                    } else if (getChunkStorage().isProtected(chunk)) {
                        getMessage().send(player, "&6Chunks Info:&f Chunk");
                        getMessage().send(player, "&6Owner:&f Server");
                    }
                }
                if (args.length == 2) {
                    OfflinePlayer target = sender.getServer().getOfflinePlayer(args[1]);
                    if (getDatabase().exist(target)) {
                        getMessage().send(sender, "&6Chunks Info:&f "+target.getName());
                        getMessage().send(sender, "&6Chunks claimed:&f " + getChunkStorage().getClaimedCount(target));
                        if (getChunkStorage().getMembers(target).isEmpty()) {
                            getMessage().send(sender, target.getName() + "&6 has no members");
                        } else {
                            getMessage().send(sender, "&6Members:");
                            for (UUID uuid : getChunkStorage().getMembersUUID(target)) {
                                getMessage().send(sender, "- " + sender.getServer().getOfflinePlayer(uuid).getName());
                            }
                        }
                    }
                }
            }
        }
    }
}