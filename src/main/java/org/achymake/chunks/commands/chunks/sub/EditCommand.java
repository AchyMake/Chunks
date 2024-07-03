package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.data.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditCommand extends ChunksSubCommand {
    private final Chunks plugin;
    private Message getMessage() {
        return plugin.getMessage();
    }
    public EditCommand(Chunks plugin) {
        this.plugin = plugin;
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
                    if (plugin.isEditor(player)) {
                        plugin.getChunkEditors().remove(player);
                        getMessage().sendActionBar(player, "&6Chunk Edit:&c Disabled");
                    } else {
                        plugin.getChunkEditors().add(player);
                        getMessage().sendActionBar(player, "&6Chunk Edit:&a Enabled");
                    }
                }
            }
        }
    }
}
