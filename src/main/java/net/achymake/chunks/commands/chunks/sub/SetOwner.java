package net.achymake.chunks.commands.chunks.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunks.ChunksSubCommand;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Database;
import net.achymake.chunks.files.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetOwner extends ChunksSubCommand {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    private final Message message = Chunks.getMessage();
    private final Database database = Chunks.getDatabase();
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
                    if (chunkStorage.isProtected(((Player) sender).getLocation().getChunk())) {
                        message.send(sender, "&cChunk is protected by&f Server");
                    } else {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                        if (database.exist(target)) {
                            chunkStorage.setOwner((Player) sender, target, ((Player) sender).getLocation().getChunk());
                            chunkStorage.claimEffect((Player) sender);
                            message.send(sender, "&6Chunk is now owned by&f " + chunkStorage.getOwner(((Player) sender).getLocation().getChunk()).getName());
                        } else {
                            message.send(sender, target.getName() + "&c has never joined");
                        }
                    }
                }
            }
        }
    }
}