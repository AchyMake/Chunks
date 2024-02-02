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
    private final Userdata userdata;
    private final ChunkStorage chunkStorage;
    private final Message message;
    public SetOwnerCommand(Chunks plugin) {
        userdata = plugin.getUserdata();
        chunkStorage = plugin.getChunkStorage();
        message = plugin.getMessage();
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
                    if (chunkStorage.isAllowedClaim(chunk)) {
                        if (userdata.exist(target)) {
                            chunkStorage.setOwner(player, target, chunk);
                            chunkStorage.claimEffect(player);
                            message.send(player, "&6Chunk is now owned by&f " + chunkStorage.getOwner(chunk).getName());
                        } else {
                            message.send(player, "&c&lHey&7 Sorry, but&f " + target.getName() + "&7 has never joined");
                        }
                    } else {
                        message.send(player, "&c&lHey!&7 Sorry, but you cannot set-owner on current region");
                    }
                }
            }
        }
    }
}
