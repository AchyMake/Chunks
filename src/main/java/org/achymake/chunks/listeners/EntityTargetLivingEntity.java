package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public record EntityTargetLivingEntity(Chunks plugin) implements Listener {
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() == null)return;
        if (event.getTarget() instanceof Player player) {
            Entity entity = event.getEntity();
            Chunk chunk = entity.getLocation().getChunk();
            if (!getChunkdata().isClaimed(chunk))return;
            if (!getChunkdata().isFriendly(entity))return;
            if (getChunkdata().hasAccess(player, chunk))return;
            event.setCancelled(true);
        }
    }
}