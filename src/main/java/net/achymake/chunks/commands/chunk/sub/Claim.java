package net.achymake.chunks.commands.chunk.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunk.ChunkSubCommand;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Database;
import net.achymake.chunks.files.Message;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Claim extends ChunkSubCommand {
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
    private Message getMessage() {
        return Chunks.getMessage();
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
                    getMessage().send(player, "&cChunk is protected by&f Server");
                } else if (getChunkStorage().isClaimed(chunk)) {
                    if (getChunkStorage().isOwner(player ,chunk)) {
                        getMessage().send(player, "&cYou already own this chunk");
                    } else {
                        getMessage().send(player, "&cChunk already owned by " + getChunkStorage().getOwner(chunk).getName());
                    }
                } else {
                    if (getConfig().getInt("claim.max-claims") > getDatabase().getConfig(player).getInt("chunks.claimed")) {
                        if (getEconomy().getBalance(player) >= getConfig().getDouble("claim.cost")) {
                            getChunkStorage().claim(player, chunk);
                            getChunkStorage().claimEffect(player);
                            getMessage().send(player, "&6You bought a chunk for&a " + getEconomy().currencyNameSingular() + getEconomy().format(getConfig().getDouble("claim.cost")));
                        } else {
                            getMessage().send(player, "&cYou don't have&a " + getEconomy().currencyNameSingular() + getEconomy().format(getConfig().getDouble("claim.cost")) + "&c to buy a chunk");
                        }
                    } else {
                        getMessage().send(player, "&cYou have reach your limit of&f " + getDatabase().getConfig(player).getInt("chunks.claimed") + "&c claims");
                    }
                }
            }
        }
    }
}