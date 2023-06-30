package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
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
        if (getChunkStorage().isProtected(event.getEntity().getLocation().getChunk())) {
            if (getChunkStorage().hasAccess((Player) event.getHumanEntity(), event.getEntity().getLocation().getChunk()))return;
            event.setCancelled(true);
        }
        if (getChunkStorage().isClaimed(event.getEntity().getLocation().getChunk())) {
            if (getChunkStorage().hasAccess((Player) event.getHumanEntity(), event.getEntity().getLocation().getChunk()))return;
            event.setCancelled(true);
        }
    }
}