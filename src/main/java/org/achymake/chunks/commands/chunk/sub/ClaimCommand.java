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

public class ClaimCommand extends ChunkSubCommand {
    private final FileConfiguration config;
    private final Userdata userdata;
    private final ChunkStorage chunkStorage;
    private final Economy economy;
    private final Message message;
    public ClaimCommand(Chunks plugin) {
        config = plugin.getConfig();
        userdata = plugin.getUserdata();
        chunkStorage = plugin.getChunkStorage();
        economy = plugin.getEconomy();
        message = plugin.getMessage();
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
                if (chunkStorage.isClaimed(chunk)) {
                    if (chunkStorage.isOwner(player ,chunk)) {
                        message.send(player, "&c&lHey!&7 Sorry, but you already own current chunk");
                    } else {
                        message.send(player, "&c&lHey!&7 Sorry, but chunk is owned by&f " + chunkStorage.getOwner(chunk).getName());
                    }
                } else if (chunkStorage.isAllowedClaim(chunk)) {
                    if (config.getInt("claim.max-claims") > userdata.getConfig(player).getInt("claimed")) {
                        if (economy.getBalance(player) >= config.getDouble("claim.cost")) {
                            chunkStorage.claim(player, chunk);
                            chunkStorage.claimEffect(player);
                            message.send(player, "&6You bought a chunk for&a " + economy.currencyNamePlural() + " " + economy.format(config.getDouble("claim.cost")));
                        } else {
                            message.send(player, "&c&lHey!&7 Sorry, but you don't have&a " + economy.currencyNamePlural() + " " + economy.format(config.getDouble("claim.cost")) + "&7 to buy a chunk");
                        }
                    } else {
                        message.send(player, "&c&lHey!&7 Sorry, but you have reach your limit of&f " + userdata.getConfig(player).getInt("chunks.claimed") + "&7 claims");
                    }
                } else {
                    message.send(player, "&c&lHey!&7 Sorry, but you are not allowed to claim here");
                }
            }
        }
    }
}
