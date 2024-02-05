package org.achymake.chunks.commands.chunk.sub;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

public class UnClaimCommand extends ChunkSubCommand {
    private final Chunks plugin;
    private FileConfiguration getConfig() {
        return plugin.getConfig();
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
            Chunk chunk = player.getLocation().getChunk();
            if (getChunkStorage().isClaimed(chunk)) {
                if (getChunkStorage().isOwner(player, chunk)) {
                    player.sendMessage(MessageFormat.format(getMessage().getString("commands.chunk.unclaim.success"), getEconomy().format(getConfig().getDouble("unclaim.refund"))));
                    getChunkStorage().unclaim(chunk);
                    getChunkStorage().unclaimEffect(player);
                } else {
                    player.sendMessage(MessageFormat.format(getMessage().getString("commands.chunk.unclaim.claimed"), getChunkStorage().getOwner(chunk).getName()));
                }
            } else {
                player.sendMessage(getMessage().getString("commands.chunk.unclaim.unclaimed"));
            }
        }
    }
}
