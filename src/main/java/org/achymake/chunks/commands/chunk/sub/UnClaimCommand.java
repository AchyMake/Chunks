package org.achymake.chunks.commands.chunk.sub;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.files.ChunkStorage;
import org.achymake.chunks.files.Message;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class UnClaimCommand extends ChunkSubCommand {
    private Chunks getPlugin() {
        return Chunks.getInstance();
    }
    private FileConfiguration getConfig() {
        return getPlugin().getConfig();
    }
    private ChunkStorage getChunkStorage() {
        return getPlugin().getChunkStorage();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
    }
    private Economy getEconomy() {
        return getPlugin().getEconomy();
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
                getMessage().send(player, "&cError:&7 Chunk protected by&f Server");
            } else if (getChunkStorage().isClaimed(chunk)) {
                if (getChunkStorage().isOwner(player, chunk)){
                    getMessage().send(player, "&6You unclaimed a chunk and got refunded&a " + getEconomy().format(getConfig().getDouble("unclaim.refund")));
                    getChunkStorage().unclaim(chunk);
                    getChunkStorage().unclaimEffect(player);
                } else {
                    getMessage().send(player, "&cError:&7 Chunk owned by&f " + getChunkStorage().getOwner(chunk).getName());
                }
            } else {
                getMessage().send(player, "&cError:&7 Chunk already unclaimed");
            }
        }
    }
}
