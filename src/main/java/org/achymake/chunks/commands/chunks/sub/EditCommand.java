package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.files.ChunkStorage;
import org.achymake.chunks.files.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditCommand extends ChunksSubCommand {
    private Chunks getPlugin() {
        return Chunks.getInstance();
    }
    private ChunkStorage getChunkStorage() {
        return getPlugin().getChunkStorage();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
    }
    @Override
    public String getName() {
        return "edit";
    }
    @Override
    public String getDescription() {
        return "allow to edit chunk";
    }
    @Override
    public String getSyntax() {
        return "/chunks edit";
    }
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("chunks.command.chunks.edit")) {
                    if (getChunkStorage().hasChunkEdit(player)) {
                        getChunkStorage().getChunkEditors().remove(player);
                        getMessage().sendActionBar(player, "&6&lChunk Edit:&c Disabled");
                    } else {
                        getChunkStorage().getChunkEditors().add(player);
                        getMessage().sendActionBar(player, "&6&lChunk Edit:&a Enabled");
                    }
                }
            }
        }
    }
}
