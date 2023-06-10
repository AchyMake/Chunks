package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode implements Listener {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    public EntityExplode(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (chunkStorage.isProtected(event.getEntity().getLocation().getChunk())) {
            event.setCancelled(true);
        }
        if (chunkStorage.isClaimed(event.getEntity().getLocation().getChunk())) {
            if (event.getEntity().getType().equals(EntityType.MINECART_TNT)) {
                if (!chunkStorage.isClaimed(event.getEntity().getLocation().getChunk()))return;
                if (chunkStorage.TNTAllowed(event.getLocation().getChunk()))return;
                event.setCancelled(true);
            }
            if (event.getEntity().getType().equals(EntityType.PRIMED_TNT)) {
                if (!chunkStorage.isClaimed(event.getEntity().getLocation().getChunk()))return;
                if (chunkStorage.TNTAllowed(event.getLocation().getChunk()))return;
                event.setCancelled(true);
            }
            if (event.getEntity().getType().equals(EntityType.CREEPER)) {
                event.setCancelled(true);
            }
        }
    }
}