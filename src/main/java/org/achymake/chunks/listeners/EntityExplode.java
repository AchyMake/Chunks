package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.ChunkStorage;
import org.bukkit.Chunk;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode implements Listener {
    private final Chunks plugin;
    private ChunkStorage getChunkStorage()
    {
        return plugin.getChunkStorage();
    }
    public EntityExplode(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityExplode(EntityExplodeEvent event) {
        Chunk chunk = event.getLocation().getChunk();
        if (!getChunkStorage().isClaimed(chunk))return;
        Entity entity = event.getEntity();
        if (entity instanceof Creeper) {
            event.blockList().clear();
        } else if (entity instanceof WitherSkull) {
            event.blockList().clear();
        } else if (entity instanceof TNTPrimed) {
            if (getChunkStorage().TNTAllowed(chunk))return;
            event.blockList().clear();
        } else if (entity instanceof ExplosiveMinecart) {
            if (getChunkStorage().TNTAllowed(chunk))return;
            event.blockList().clear();
        }
    }
}
