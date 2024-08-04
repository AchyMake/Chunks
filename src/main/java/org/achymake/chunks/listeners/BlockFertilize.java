package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;

public record BlockFertilize(Chunks plugin) implements Listener {
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockFertilize(BlockFertilizeEvent event) {
        if (event.getPlayer() == null)return;
        Chunk chunk = event.getBlock().getChunk();
        if (!getChunkdata().isClaimed(chunk))return;
        if (!getChunkdata().isDisableBlockFertilize())return;
        Player player = event.getPlayer();
        if (getChunkdata().hasAccess(player, chunk))return;
        event.setCancelled(true);
        getMessage().sendActionBar(player, "&cChunk is owned by&f " + getChunkdata().getOwner(chunk).getName());
    }
}