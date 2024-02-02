package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;

public class BlockFertilize implements Listener {
    private final ChunkStorage chunkStorage;
    private final Message message;
    public BlockFertilize(Chunks plugin) {
        chunkStorage = plugin.getChunkStorage();
        message = plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockFertilize(BlockFertilizeEvent event) {
        if (event.getPlayer() == null)return;
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        if (!chunkStorage.isClaimed(chunk))return;
        if (chunkStorage.hasAccess(player, chunk))return;
        event.setCancelled(true);
        message.send(player, "&c&lHey!&7 Sorry, chunk is owned by&f " + chunkStorage.getOwner(chunk).getName());
    }
}
