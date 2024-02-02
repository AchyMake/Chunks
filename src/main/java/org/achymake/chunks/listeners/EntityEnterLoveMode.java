package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;

public class EntityEnterLoveMode implements Listener {
    private final ChunkStorage chunkStorage;
    public EntityEnterLoveMode(Chunks plugin) {
        chunkStorage = plugin.getChunkStorage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityEnterLoveMode(EntityEnterLoveModeEvent event) {
        if (!(event.getHumanEntity() instanceof Player player)) return;
        Chunk chunk = event.getEntity().getLocation().getChunk();
        if (!chunkStorage.isClaimed(chunk))return;
        if (chunkStorage.hasAccess(player, chunk))return;
        event.setCancelled(true);
    }
}