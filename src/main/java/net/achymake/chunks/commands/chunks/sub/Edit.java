package net.achymake.chunks.commands.chunks.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunks.ChunksSubCommand;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Edit extends ChunksSubCommand {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
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
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player player = (Player) sender;
                if (player.hasPermission("chunks.command.chunks.edit")) {
                    if (getChunkStorage().hasChunkEdit(player)) {
                        getChunkStorage().getChunkEditors().remove(player);
                        Chunks.sendActionBar(player, "&6&lChunk Edit:&c Disabled");
                    } else {
                        getChunkStorage().getChunkEditors().add(player);
                        Chunks.sendActionBar(player, "&6&lChunk Edit:&a Enabled");
                    }
                }
            }
        }
    }
}