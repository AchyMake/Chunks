package net.achymake.chunks.commands.chunks.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunks.ChunksSubCommand;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetOwner extends ChunksSubCommand {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    private Database getDatabase() {
        return Chunks.getDatabase();
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
        if (sender.hasPermission("chunks.command.chunks.setowner")) {
            if (sender instanceof Player) {
                if (args.length == 2) {
                    Player player = (Player) sender;
                    if (getChunkStorage().isProtected(((Player) sender).getLocation().getChunk())) {
                        Chunks.send(player, "&cChunk is protected by&f Server");
                    } else {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                        if (getDatabase().exist(target)) {
                            getChunkStorage().setOwner((Player) sender, target, ((Player) sender).getLocation().getChunk());
                            getChunkStorage().claimEffect((Player) sender);
                            Chunks.send(player, "&6Chunk is now owned by&f " + getChunkStorage().getOwner(((Player) sender).getLocation().getChunk()).getName());
                        } else {
                            Chunks.send(player, target.getName() + "&c has never joined");
                        }
                    }
                }
            }
        }
    }
}