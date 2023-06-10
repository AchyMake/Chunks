package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;

public class EntityEnterLoveMode implements Listener {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    public EntityEnterLoveMode(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityEnterLoveMode(EntityEnterLoveModeEvent event) {
        if (chunkStorage.isProtected(event.getEntity().getLocation().getChunk())) {
            if (chunkStorage.hasAccess((Player) event.getHumanEntity(), event.getEntity().getLocation().getChunk()))return;
            event.setCancelled(true);
        }
        if (chunkStorage.isClaimed(event.getEntity().getLocation().getChunk())) {
            if (chunkStorage.hasAccess((Player) event.getHumanEntity(), event.getEntity().getLocation().getChunk()))return;
            event.setCancelled(true);
        }
    }
}