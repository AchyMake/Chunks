package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.EntityHandler;
import org.achymake.chunks.handlers.WorldHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.plugin.PluginManager;

public class EntityTargetLivingEntity implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public EntityTargetLivingEntity() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() instanceof Player player) {
            var chunk = event.getEntity().getLocation().getChunk();
            if (!getWorldHandler().isAllowedClaim(chunk))return;
            if (!getChunkHandler().isClaimed(chunk))return;
            if (!getEntityHandler().isFriendly(event.getEntityType()))return;
            if (getChunkHandler().hasAccess(chunk, player))return;
            event.setCancelled(true);
        }
    }
}