package net.achymake.chunks.commands.chunk.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunk.ChunkSubCommand;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Database;
import net.achymake.chunks.files.Message;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public class Claim extends ChunkSubCommand {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    private final Database database = Chunks.getDatabase();
    private final Economy economy = Chunks.getEconomy();
    private final Message message = Chunks.getMessage();
    private final Chunks chunks = Chunks.getInstance();
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
                if (chunkStorage.isProtected(chunk)) {
                    message.send(player, "&cChunk is protected by&f Server");
                } else if (chunkStorage.isClaimed(chunk)) {
                    if (chunkStorage.isOwner(player ,chunk)) {
                        message.send(player, "&cYou already own this chunk");
                    } else {
                        message.send(player, "&cChunk already owned by " + chunkStorage.getOwner(chunk).getName());
                    }
                } else {
                    if (chunks.getConfig().getInt("claim.max-claims") > database.get(player).getInt("chunks.claimed")) {
                        if (economy.getBalance(player) >= chunks.getConfig().getDouble("claim.cost")) {
                            chunkStorage.claim(player, chunk);
                            chunkStorage.claimEffect(player);
                            message.send(player, "&6You bought a chunk for&a " + economy.currencyNameSingular() + economy.format(chunks.getConfig().getDouble("claim.cost")));
                        } else {
                            message.send(player, "&cYou don't have&a " + economy.currencyNameSingular() + economy.format(chunks.getConfig().getDouble("claim.cost")) + "&c to buy a chunk");
                        }
                    } else {
                        message.send(player, "&cYou have reach your limit of&f " + database.get(player).getInt("chunks.claimed") + "&c claims");
                    }
                }
            }
        }
    }
}