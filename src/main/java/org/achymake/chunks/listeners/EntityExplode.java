package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.ChunkStorage;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode implements Listener {
    private final Chunks plugin;
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    public EntityExplode(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (getChunkStorage().isProtected(event.getEntity().getLocation().getChunk())) {
            if (event.blockList().isEmpty())return;
            event.blockList().clear();
        } else if (getChunkStorage().isClaimed(event.getEntity().getLocation().getChunk())) {
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
