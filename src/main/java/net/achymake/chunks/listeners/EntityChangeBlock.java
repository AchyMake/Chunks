package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityChangeBlock implements Listener {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    public EntityChangeBlock(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (chunkStorage.isProtected(event.getBlock().getChunk())) {
            event.setCancelled(true);
        }
        if (chunkStorage.isClaimed(event.getBlock().getChunk())) {
            if (!Chunks.getInstance().getConfig().getBoolean("is-hostile." + event.getEntity()))return;
            event.setCancelled(true);
        }
    }
}