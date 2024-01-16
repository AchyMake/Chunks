package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.ChunkStorage;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;

public class EntityEnterLoveMode implements Listener {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    public EntityEnterLoveMode(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityEnterLoveMode(EntityEnterLoveModeEvent event) {
        if (event.getHumanEntity() instanceof Player player) {
            Chunk chunk = event.getEntity().getLocation().getChunk();
            if (getChunkStorage().isProtected(chunk)) {
                if (getChunkStorage().hasAccess(player, chunk))return;
                event.setCancelled(true);
            } else if (getChunkStorage().isClaimed(chunk)) {
                if (getChunkStorage().hasAccess(player, chunk))return;
                event.setCancelled(true);
            }
        }
    }
}
