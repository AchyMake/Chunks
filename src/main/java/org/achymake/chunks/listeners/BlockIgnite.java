package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

public record BlockIgnite(Chunks plugin) implements Listener {
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockRedstone(BlockIgniteEvent event) {
        if (event.getIgnitingBlock() == null)return;
        Entity entity = event.getIgnitingEntity();
        if (entity instanceof Player player) {
            Block block = event.getIgnitingBlock();
            Chunk chunk = block.getChunk();
            if (!getChunkdata().isClaimed(chunk))return;
            if (getChunkdata().hasAccess(player, chunk))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, "&cChunk is owned by&f " + getChunkdata().getOwner(chunk).getName());
        }
    }
}