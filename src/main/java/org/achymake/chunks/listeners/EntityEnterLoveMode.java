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
    private final Chunks plugin;
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    public EntityEnterLoveMode(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityEnterLoveMode(EntityEnterLoveModeEvent event) {
        if (event.getHumanEntity() instanceof Player player) {
            Chunk chunk = event.getEntity().getLocation().getChunk();
            if (!getChunkStorage().isClaimed(chunk))return;
            if (getChunkStorage().hasAccess(player, chunk))return;
            event.setCancelled(true);
        }
    }
}
