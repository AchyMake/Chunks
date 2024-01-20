package org.achymake.chunks.commands.chunk.sub;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.files.ChunkStorage;
import org.achymake.chunks.files.Database;
import org.achymake.chunks.files.Message;
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
    private ChunkStorage getChunkStorage() {
        return getPlugin().getChunkStorage();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
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
                    getMessage().send(player, "&cError:&7 Chunk protected by&f Server");
                } else if (getChunkStorage().isClaimed(chunk)) {
                    if (getChunkStorage().isOwner(player ,chunk)) {
                        getMessage().send(player, "&cError:&7 You already own current chunk");
                    } else {
                        getMessage().send(player, "&cError:&7 Chunk owned by&f " + getChunkStorage().getOwner(chunk).getName());
                    }
                } else {
                    if (getConfig().getInt("claim.max-claims") > getDatabase().getConfig(player).getInt("claimed")) {
                        if (getEconomy().getBalance(player) >= getConfig().getDouble("claim.cost")) {
                            getChunkStorage().claim(player, chunk);
                            getChunkStorage().claimEffect(player);
                            getMessage().send(player, "&6You bought a chunk for&a " + getEconomy().format(getConfig().getDouble("claim.cost")));
                        } else {
                            getMessage().send(player, "&cError:&7 You don't have&a " + getEconomy().format(getConfig().getDouble("claim.cost")) + "&7 to buy a chunk");
                        }
                    } else {
                        getMessage().send(player, "&cError:&7 You have reach your limit of&f " + getDatabase().getConfig(player).getInt("chunks.claimed") + "&7 claims");
                    }
                }
            }
        }
    }
}
