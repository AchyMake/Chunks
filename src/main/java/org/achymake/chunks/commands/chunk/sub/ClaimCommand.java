package org.achymake.chunks.commands.chunk.sub;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.files.Database;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ClaimCommand extends ChunkSubCommand {
    private Chunks getPlugin() {
        return Chunks.getInstance();
    }
    private FileConfiguration getConfig() {
        return getPlugin().getConfig();
    }
    private Economy getEconomy() {
        return getPlugin().getEconomy();
    }
    private Database getDatabase() {
        return getPlugin().getDatabase();
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
                if (getDatabase().isProtected(chunk)) {
                    getPlugin().send(player, "&cChunk is protected by&f Server");
                } else if (getDatabase().isClaimed(chunk)) {
                    if (getDatabase().isOwner(player ,chunk)) {
                        getPlugin().send(player, "&cYou already own this chunk");
                    } else {
                        getPlugin().send(player, "&cChunk already owned by " + getDatabase().getOwner(chunk).getName());
                    }
                } else {
                    if (getConfig().getInt("claim.max-claims") > getDatabase().getConfig(player).getInt("claimed")) {
                        if (getEconomy().getBalance(player) >= getConfig().getDouble("claim.cost")) {
                            getDatabase().claim(player, chunk);
                            getDatabase().claimEffect(player);
                            getPlugin().send(player, "&6You bought a chunk for&a " + getEconomy().format(getConfig().getDouble("claim.cost")));
                        } else {
                            getPlugin().send(player, "&cYou don't have&a " + getEconomy().format(getConfig().getDouble("claim.cost")) + "&c to buy a chunk");
                        }
                    } else {
                        getPlugin().send(player, "&cYou have reach your limit of&f " + getDatabase().getConfig(player).getInt("chunks.claimed") + "&c claims");
                    }
                }
            }
        }
    }
}
