package net.achymake.chunks.commands.chunks.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunks.ChunksSubCommand;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Effect extends ChunksSubCommand {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    private Message getMessage() {
        return Chunks.getMessage();
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
        if (sender.hasPermission("chunks.command.chunks.effect")) {
            if (args.length == 2) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (args[1].equalsIgnoreCase("claim")) {
                        getChunkStorage().claimEffect(player);
                        getMessage().sendActionBar(player, "&6Started the effects of claiming");
                    }
                    if (args[1].equalsIgnoreCase("unclaim")) {
                        getChunkStorage().unclaimEffect(player);
                        getMessage().sendActionBar(player, "&6Started the effects of unclaiming");
                    }
                }
            }
        }
    }
}