package org.achymake.chunks.commands.chunk.sub;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class ClaimCommand extends ChunkSubCommand {
    private final Chunks plugin;
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    private Economy getEconomy() {
        return plugin.getEconomy();
    }
    private Message getMessage() {
        return plugin.getMessage();
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
                if (getChunkStorage().isAllowedClaim(chunk)) {
                    if (getChunkStorage().isClaimed(chunk)) {
                        if (getChunkStorage().isOwner(player ,chunk)) {
                            player.sendMessage(getMessage().getString("commands.chunk.claim.already-owned"));
                        } else {
                            player.sendMessage(MessageFormat.format(getMessage().getString("commands.chunk.claim.already-claimed"), getChunkStorage().getOwner(chunk).getName()));
                        }
                    } else {
                        if (getUserdata().getClaimCount(player) >= getConfig().getInt("claim.max-claims")) {
                            if (getEconomy().getBalance(player) >= getConfig().getDouble("claim.cost")) {
                                getChunkStorage().claim(player, chunk);
                                getChunkStorage().claimEffect(player, chunk);
                                getChunkStorage().claimSound(player);
                                player.sendMessage(MessageFormat.format(getMessage().getString("commands.chunk.claim.success"), getEconomy().format(getConfig().getDouble("claim.cost"))));
                            } else {
                                String value = getEconomy().format(getConfig().getDouble("claim.cost"));
                                player.sendMessage(MessageFormat.format(getMessage().getString("commands.chunk.claim.insufficient-funds"), value));
                            }
                        } else {
                            player.sendMessage(MessageFormat.format(getMessage().getString("commands.chunk.claim.insufficient-claims"), getUserdata().getConfig(player).getInt("claimed")));
                        }
                    }
                } else {
                    player.sendMessage(getMessage().getString("commands.chunk.claim.worldguard"));
                }
            }
        }
    }
}
