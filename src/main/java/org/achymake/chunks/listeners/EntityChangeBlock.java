package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
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
    private boolean isAllowed(Chunk chunk) {
        return plugin.isAllowed(chunk);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Chunk chunk = event.getBlock().getChunk();
        if (!isAllowed(chunk))return;
        if (!getChunkdata().isClaimed(chunk))return;
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (getChunkdata().hasAccess(player, chunk))return;
            event.setCancelled(true);
            String owner = getChunkdata().getOwner(chunk).getName();
            getMessage().sendActionBar(player, "&cChunk is owned by&f " + owner);
        } else if (plugin.getConfig().getBoolean("hostile." + entity.getType())) {
            event.setCancelled(true);
        }
    }
}