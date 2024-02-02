package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EffectCommand extends ChunksSubCommand {
    private final ChunkStorage chunkStorage;
    private final Message message;
    public EffectCommand(Chunks plugin) {
        chunkStorage = plugin.getChunkStorage();
        message = plugin.getMessage();
    }
    @Override
    public String getName() {
        return "effect";
    }
    @Override
    public String getDescription() {
        return "start claim/unclaim effects";
    }
    @Override
    public String getSyntax() {
        return "/chunks effect";
    }
    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (sender.hasPermission("chunks.command.chunks.effect")) {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("claim")) {
                        chunkStorage.claimEffect(player);
                        message.sendActionBar(player, "&6Started the effects of Claiming");
                    }
                    if (args[1].equalsIgnoreCase("unclaim")) {
                        chunkStorage.unclaimEffect(player);
                        message.sendActionBar(player, "&6Started the effects of Unclaiming");
                    }
                }
            }
        }
    }
}
