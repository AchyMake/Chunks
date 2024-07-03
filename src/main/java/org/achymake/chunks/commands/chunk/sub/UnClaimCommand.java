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

public class UnClaimCommand extends ChunkSubCommand {
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
    public UnClaimCommand(Chunks plugin) {
        this.plugin = plugin;
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
            if (args.length == 1) {
                Chunk chunk = player.getLocation().getChunk();
                if (isAllowed(chunk)) {
                    if (getChunkdata().isClaimed(chunk)) {
                        if (getChunkdata().isOwner(player, chunk)) {
                            getMessage().send(player, "&6You unclaimed current chunk and got refunded&a " + getEconomy().currencyNameSingular() + getEconomy().format(getConfig().getDouble("economy.refund")));
                            getChunkdata().remove(player, chunk);
                            getChunkdata().unclaimEffect(player, chunk);
                            getChunkdata().unclaimSound(player);
                        } else {
                            getMessage().send(player, "&cChunk is owned by&f " + getChunkdata().getOwner(chunk).getName());
                        }
                    } else {
                        getMessage().send(player, "&cChunk is already unclaimed");
                    }
                } else {
                    getMessage().send(player, "&cYou are not allowed to unclaim in this world");
                }
            }
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("all")) {
                    if (getUserdata().getClaimCount(player) > 0) {
                        getChunkdata().removeAll(player);
                        getMessage().send(player, "&6You unclaimed all chunks and got refunded each for&a " + getEconomy().currencyNameSingular() + getEconomy().format(getConfig().getDouble("economy.refund")));
                    } else {
                        getMessage().send(player, "&cYou do not have any claimed chunks");
                    }
                }
            }
        }
    }
}
