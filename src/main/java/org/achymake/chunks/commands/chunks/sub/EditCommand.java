package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditCommand extends ChunksSubCommand {
    private final ChunkStorage chunkStorage;
    private final Message message;
    public EditCommand(Chunks plugin) {
        chunkStorage = plugin.getChunkStorage();
        message = plugin.getMessage();
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
                    if (chunkStorage.hasChunkEdit(player)) {
                        chunkStorage.getChunkEditors().remove(player);
                        message.sendActionBar(player, "&6&lChunk Edit:&c Disabled");
                    } else {
                        chunkStorage.getChunkEditors().add(player);
                        message.sendActionBar(player, "&6&lChunk Edit:&a Enabled");
                    }
                }
            }
        }
    }
}
