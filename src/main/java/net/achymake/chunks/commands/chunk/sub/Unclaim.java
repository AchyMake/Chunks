package net.achymake.chunks.commands.chunk.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunk.ChunkSubCommand;
import net.achymake.chunks.files.ChunkStorage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Unclaim extends ChunkSubCommand {
    private FileConfiguration getConfig() {
        return Chunks.getConfiguration();
    }
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    private Economy getEconomy() {
        return Chunks.getEconomy();
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
            if (getChunkStorage().isProtected(chunk)) {
                Chunks.send(player, "&cChunk already owned by&f Server");
            } else if (getChunkStorage().isClaimed(chunk)) {
                if (getChunkStorage().isOwner(player, chunk)){
                    Chunks.send(player, "&6You unclaimed a chunk and got refunded&a " + getEconomy().currencyNameSingular() + getEconomy().format(getConfig().getDouble("unclaim.refund")));
                    getChunkStorage().unclaim(chunk);
                    getChunkStorage().unclaimEffect(player);
                } else {
                    Chunks.send(player, "&cChunk already owned by&f " + getChunkStorage().getOwner(chunk).getName());
                }
            } else {
                Chunks.send(player, "&cChunk already unclaimed");
            }
        }
    }
}