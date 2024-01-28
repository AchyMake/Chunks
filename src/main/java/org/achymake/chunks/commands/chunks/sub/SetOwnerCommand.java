package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.files.ChunkStorage;
import org.achymake.chunks.files.Database;
import org.achymake.chunks.files.Message;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetOwnerCommand extends ChunksSubCommand {
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
        return "setowner";
    }
    @Override
    public String getDescription() {
        return "sets owner of the chunk";
    }
    @Override
    public String getSyntax() {
        return "/chunks setowner";
    }
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("chunks.command.chunks.setowner")) {
                if (args.length == 2) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    Chunk chunk = player.getLocation().getChunk();
                    if (getChunkStorage().isAllowedClaim(getPlugin(), chunk)) {
                        if (getDatabase().exist(target)) {
                            getChunkStorage().setOwner(player, target, chunk);
                            getChunkStorage().claimEffect(player);
                            getMessage().send(player, "&6Chunk is now owned by&f " + getChunkStorage().getOwner(chunk).getName());
                        } else {
                            getMessage().send(player, "&cError:&f " + target.getName() + "&7 has never joined");
                        }
                    } else {
                        getMessage().send(player, "&cError:&7 You cannot setowner on current region");
                    }
                }
            }
        }
    }
}
