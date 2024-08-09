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
import org.bukkit.event.entity.EntityChangeBlockEvent;

public record EntityChangeBlock(Chunks plugin) implements Listener {
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Block block = event.getBlock();
        Chunk chunk = block.getChunk();
        if (!getChunkdata().isClaimed(chunk))return;
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (!getChunkdata().disabledChangeBlocks(block))return;
            if (getChunkdata().hasAccess(player, chunk))return;
            event.setCancelled(true);
            String owner = getChunkdata().getOwner(chunk).getName();
            getMessage().sendActionBar(player, "&cChunk is owned by&f " + owner);
        } else if (getChunkdata().isHostile(entity)) {
            event.setCancelled(true);
        }
    }
}