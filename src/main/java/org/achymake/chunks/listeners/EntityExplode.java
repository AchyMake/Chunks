package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public record EntityExplode(Chunks plugin) implements Listener {
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!getChunkdata().disableTNTBlockDamage())return;
        List<Block> blockList = new ArrayList<>();
        EntityType entityType = event.getEntityType();
        for (Block block : event.blockList()) {
            Chunk chunk = block.getChunk();
            if (getChunkdata().isClaimed(chunk)) {
                if (entityType.equals(EntityType.TNT) || entityType.equals(EntityType.TNT_MINECART)) {
                    if (getChunkdata().isTNTAllowed(chunk))return;
                    blockList.add(block);
                }
            } else {
                if (entityType.equals(EntityType.TNT) || entityType.equals(EntityType.TNT_MINECART)) {
                    blockList.add(block);
                }
            }
        }
        event.blockList().removeAll(blockList);
        blockList.clear();
    }
}