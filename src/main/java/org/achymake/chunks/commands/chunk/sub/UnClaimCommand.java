package org.achymake.chunks.commands.chunk.sub;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class UnClaimCommand extends ChunkSubCommand {
    private final FileConfiguration config;
    private final ChunkStorage chunkStorage;
    private final Economy economy;
    private final Message message;
    public UnClaimCommand(Chunks plugin) {
        config = plugin.getConfig();
        chunkStorage = plugin.getChunkStorage();
        economy = plugin.getEconomy();
        message = plugin.getMessage();
    }
    @Override
    public String getName() {
        return "unclaim";
    }
    @Override
    public String getDescription() {
        return "unclaims current chunk";
    }
    @Override
    public String getSyntax() {
        return "/chunk unclaim";
    }
    @Override
    public void perform(Player player, String[] args) {
        if (player.hasPermission("chunks.command.chunk.unclaim")) {
            Chunk chunk = player.getLocation().getChunk();
            if (chunkStorage.isClaimed(chunk)) {
                if (chunkStorage.isOwner(player, chunk)){
                    message.send(player, "&6You unclaimed a chunk and got refunded&a " + economy.currencyNamePlural() + " " + economy.format(config.getDouble("unclaim.refund")));
                    chunkStorage.unclaim(chunk);
                    chunkStorage.unclaimEffect(player);
                } else {
                    message.send(player, "&c&lHey!&7 Sorry, chunk is owned by&f " + chunkStorage.getOwner(chunk).getName());
                }
            } else {
                message.send(player, "&c&lHey!&7 Sorry, chunk is already unclaimed");
            }
        }
    }
}
