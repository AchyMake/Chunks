package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Userdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetOwnerCommand extends ChunksSubCommand {
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
    public SetOwnerCommand(Chunks plugin) {
        this.plugin = plugin;
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
                    if (getChunkStorage().isAllowedClaim(chunk)) {
                        if (getUserdata().exist(target)) {
                            getChunkStorage().setOwner(player, target, chunk);
                            getChunkStorage().claimEffect(player);
                            getMessage().send(player, "&6Chunk is now owned by&f " + getChunkStorage().getOwner(chunk).getName());
                        } else {
                            getMessage().send(player, target.getName() + "&c has never joined");
                        }
                    } else {
                        getMessage().send(player, "&c&lHey!&7 Sorry, but you cannot set-owner on current region");
                    }
                }
            }
        }
    }
}
