package org.achymake.chunks.commands.chunk.sub;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.files.ChunkStorage;
import org.achymake.chunks.files.Database;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ClaimCommand extends ChunkSubCommand {
    private FileConfiguration getConfig() {
        return Chunks.getConfiguration();
    }
    private Economy getEconomy() {
        return Chunks.getEconomy();
    }
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    private Database getDatabase() {
        return Chunks.getDatabase();
    }
    @Override
    public String getName() {
        return "claim";
    }
    @Override
    public String getDescription() {
        return "claims the chunk";
    }
    @Override
    public String getSyntax() {
        return "/chunk claim";
    }
    @Override
    public void perform(Player player, String[] args) {
        if (player.hasPermission("chunks.command.chunk.claim")) {
            if (args.length == 1) {
                Chunk chunk = player.getLocation().getChunk();
                if (getChunkStorage().isProtected(chunk)) {
                    Chunks.send(player, "&cChunk is protected by&f Server");
                } else if (getChunkStorage().isClaimed(chunk)) {
                    if (getChunkStorage().isOwner(player ,chunk)) {
                        Chunks.send(player, "&cYou already own this chunk");
                    } else {
                        Chunks.send(player, "&cChunk already owned by " + getChunkStorage().getOwner(chunk).getName());
                    }
                } else {
                    if (getConfig().getInt("claim.max-claims") > getDatabase().getConfig(player).getInt("claimed")) {
                        if (getEconomy().getBalance(player) >= getConfig().getDouble("claim.cost")) {
                            getChunkStorage().claim(player, chunk);
                            getChunkStorage().claimEffect(player);
                            Chunks.send(player, "&6You bought a chunk for&a " + getEconomy().format(getConfig().getDouble("claim.cost")));
                        } else {
                            Chunks.send(player, "&cYou don't have&a " + getEconomy().format(getConfig().getDouble("claim.cost")) + "&c to buy a chunk");
                        }
                    } else {
                        Chunks.send(player, "&cYou have reach your limit of&f " + getDatabase().getConfig(player).getInt("chunks.claimed") + "&c claims");
                    }
                }
            }
        }
    }
}
