package org.achymake.chunks.commands.chunk.sub;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ClaimCommand extends ChunkSubCommand {
    private final Chunks plugin;
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Economy getEconomy() {
        return plugin.getEconomy();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private boolean isAllowed(Chunk chunk) {
        return plugin.isAllowed(chunk);
    }
    public ClaimCommand(Chunks chunks) {
        plugin = chunks;
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
                if (isAllowed(chunk)) {
                    if (getChunkdata().isClaimed(chunk)) {
                        if (getChunkdata().isOwner(player ,chunk)) {
                            getMessage().send(player, "&cYou already own it");
                        } else {
                            getMessage().send(player, "&cChunk is already owned by " + getChunkdata().getOwner(chunk).getName());
                        }
                    } else {
                        if (getUserdata().getClaimCount(player) >= getConfig().getInt("claim.max-claims")) {
                            getMessage().send(player, "&cYou have reach you're limit of&f " + getUserdata().getClaimCount(player) + "&c claimed chunks");
                        } else {
                            double cost = getConfig().getDouble("economy.cost");
                            int claimed = getUserdata().getClaimCount(player);
                            String currency = getEconomy().currencyNameSingular();
                            if (claimed > 0) {
                                int multiply = getConfig().getInt("economy.multiply");
                                double calculator = multiply * cost / 100 * claimed;
                                double result = cost + calculator;
                                if (getEconomy().has(player, result)) {
                                    getChunkdata().setup(player, chunk);
                                    getChunkdata().claimEffect(player, chunk);
                                    getChunkdata().claimSound(player);
                                    getEconomy().withdrawPlayer(player, result);
                                    getMessage().send(player, "&6You claimed a chunk for&a " + currency + getEconomy().format(result));
                                } else {
                                    getMessage().send(player, "&cYou do not have&a " +  currency + result + "&c to claim it");
                                }
                            } else {
                                if (getEconomy().getBalance(player) >= cost) {
                                    getChunkdata().setup(player, chunk);
                                    getChunkdata().claimEffect(player, chunk);
                                    getChunkdata().claimSound(player);
                                    getEconomy().withdrawPlayer(player, cost);
                                    getMessage().send(player, "&6You claimed a chunk for&a " + currency + getEconomy().format(cost));
                                } else {
                                    String value = currency + getEconomy().format(cost);
                                    getMessage().send(player, "&cYou do not have&a " +  value + "&c to claim it");
                                }
                            }
                        }
                    }
                } else {
                    getMessage().send(player, "&cYou are not allowed to claim in this world");
                }
            }
        }
    }
}
