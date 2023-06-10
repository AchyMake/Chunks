package net.achymake.chunks.commands.chunk.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunk.ChunkSubCommand;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class Unclaim extends ChunkSubCommand {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    private final Economy economy = Chunks.getEconomy();
    private final Message message = Chunks.getMessage();
    private final Chunks chunks = Chunks.getInstance();
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
            if (chunkStorage.isProtected(chunk)) {
                message.send(player, "&cChunk already owned by&f Server");
            } else if (chunkStorage.isClaimed(chunk)) {
                if (chunkStorage.isOwner(player, chunk)){
                    message.send(player, "&6You unclaimed a chunk and got refunded&a " + economy.currencyNameSingular() + economy.format(chunks.getConfig().getDouble("unclaim.refund")));
                    chunkStorage.unclaim(chunk);
                    chunkStorage.unclaimEffect(player);
                } else {
                    message.send(player, "&cChunk already owned by&f " + chunkStorage.getOwner(chunk).getName());
                }
            } else {
                message.send(player, "&cChunk already unclaimed");
            }
        }
    }
}