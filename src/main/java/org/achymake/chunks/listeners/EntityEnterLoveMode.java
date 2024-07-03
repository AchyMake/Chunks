package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;

public record EntityEnterLoveMode(Chunks plugin) implements Listener {
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
    public void onEntityEnterLoveMode(EntityEnterLoveModeEvent event) {
        if (!(event.getHumanEntity() instanceof Player player))return;
        Chunk chunk = event.getEntity().getLocation().getChunk();
        if (!isAllowed(chunk))return;
        if (!getChunkdata().isClaimed(chunk))return;
        if (getChunkdata().hasAccess(player, chunk))return;
        event.setCancelled(true);
        String owner = getChunkdata().getOwner(chunk).getName();
        getMessage().sendActionBar(player, "&cChunk is owned by&f " + owner);
    }
}