package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EffectCommand extends ChunksSubCommand {
    private final Chunks plugin;
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public EffectCommand(Chunks plugin) {
        this.plugin = plugin;
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
                        getChunkStorage().claimEffect(player, player.getLocation().getChunk());
                        getChunkStorage().claimSound(player);
                        getMessage().sendActionBar(player, "&6Started the effects of Claiming");
                    }
                    if (args[1].equalsIgnoreCase("unclaim")) {
                        getChunkStorage().unclaimEffect(player, player.getLocation().getChunk());
                        getChunkStorage().unclaimSound(player);
                        getMessage().sendActionBar(player, "&6Started the effects of Unclaiming");
                    }
                }
            }
        }
    }
}
