package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditCommand extends ChunksSubCommand {
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
                    if (Chunks.getChunkStorage().hasChunkEdit(player)) {
                        Chunks.getDatabase().setBoolean(player, "settings.chunk-edit", false);
                        Chunks.sendActionBar(player, "&6&lChunk Edit:&c Disabled");
                    } else {
                        Chunks.getDatabase().setBoolean(player, "settings.chunk-edit", true);
                        Chunks.sendActionBar(player, "&6&lChunk Edit:&a Enabled");
                    }
                }
            }
        }
    }
}
