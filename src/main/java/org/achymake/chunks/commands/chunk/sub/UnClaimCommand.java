package org.achymake.chunks.commands.chunk.sub;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.files.Database;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class UnClaimCommand extends ChunkSubCommand {
    private Chunks getPlugin() {
        return Chunks.getInstance();
    }
    private FileConfiguration getConfig() {
        return getPlugin().getConfiguration();
    }
    private Database getDatabase() {
        return getPlugin().getDatabase();
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
            if (getDatabase().isProtected(chunk)) {
                getPlugin().send(player, "&cChunk already owned by&f Server");
            } else if (getDatabase().isClaimed(chunk)) {
                if (getDatabase().isOwner(player, chunk)){
                    getPlugin().send(player, "&6You unclaimed a chunk and got refunded&a " + getEconomy().format(getConfig().getDouble("unclaim.refund")));
                    getDatabase().unclaim(chunk);
                    getDatabase().unclaimEffect(player);
                } else {
                    getPlugin().send(player, "&cChunk already owned by&f " + getDatabase().getOwner(chunk).getName());
                }
            } else {
                getPlugin().send(player, "&cChunk already unclaimed");
            }
        }
    }
}
