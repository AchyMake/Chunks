package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode implements Listener {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    public EntityExplode(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (getChunkStorage().isProtected(event.getEntity().getLocation().getChunk())) {
            if (event.blockList().isEmpty())return;
            event.blockList().clear();
        }
        if (getChunkStorage().isClaimed(event.getEntity().getLocation().getChunk())) {
            if (event.getEntity().getType().equals(EntityType.MINECART_TNT)) {
                if (!getChunkStorage().isClaimed(event.getEntity().getLocation().getChunk()))return;
                if (getChunkStorage().TNTAllowed(event.getLocation().getChunk()))return;
                if (event.blockList().isEmpty())return;
                event.blockList().clear();
            }
            if (event.getEntity().getType().equals(EntityType.PRIMED_TNT)) {
                if (!getChunkStorage().isClaimed(event.getEntity().getLocation().getChunk()))return;
                if (getChunkStorage().TNTAllowed(event.getLocation().getChunk()))return;
                if (event.blockList().isEmpty())return;
                event.blockList().clear();
            }
            if (event.getEntity().getType().equals(EntityType.CREEPER)) {
                if (event.blockList().isEmpty())return;
                event.blockList().clear();
            }
        }
    }
}