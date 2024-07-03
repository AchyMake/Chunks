package org.achymake.chunks.commands.chunks.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.ChunksSubCommand;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EffectCommand extends ChunksSubCommand {
    private final Chunks plugin;
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
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
                        Chunk chunk = player.getLocation().getChunk();
                        getChunkdata().claimEffect(player, chunk);
                        getChunkdata().claimSound(player);
                        getMessage().sendActionBar(player, "&6Started the effects of Claiming");
                    }
                    if (args[1].equalsIgnoreCase("unclaim")) {
                        Chunk chunk = player.getLocation().getChunk();
                        getChunkdata().unclaimEffect(player, chunk);
                        getChunkdata().unclaimSound(player);
                        getMessage().sendActionBar(player, "&6Started the effects of Unclaiming");
                    }
                }
            }
        }
    }
}
