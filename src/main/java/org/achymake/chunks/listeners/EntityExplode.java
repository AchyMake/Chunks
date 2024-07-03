package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
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

public record EntityExplode(Chunks plugin) implements Listener {
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityExplode(EntityExplodeEvent event) {
        Chunk chunk = event.getLocation().getChunk();
        if (!getChunkdata().isClaimed(chunk))return;
        Entity entity = event.getEntity();
        switch (entity) {
            case Creeper creeper -> event.blockList().clear();
            case WitherSkull witherSkull -> event.blockList().clear();
            case TNTPrimed tntPrimed -> {
                if (getChunkdata().isTNTAllowed(chunk)) return;
                event.blockList().clear();
            }
            case ExplosiveMinecart explosiveMinecart -> {
                if (getChunkdata().isTNTAllowed(chunk)) return;
                event.blockList().clear();
            }
            default -> {
            }
        }
    }
}