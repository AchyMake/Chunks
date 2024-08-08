package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.ExplosiveMinecart;
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
        Chunk chunk = event.getLocation().getChunk();
        if (!getChunkdata().isDisableEntityExplodeBlocks())return;
        List<Block> blockList = new ArrayList<>();
        for (Block block : event.blockList()) {
            if (!getChunkdata().isClaimed(block.getChunk()))return;
            blockList.add(block);
        }
        Entity entity = event.getEntity();
        switch (entity) {
            case TNTPrimed tntPrimed -> {
                if (getChunkdata().isTNTAllowed(chunk))return;
                event.blockList().removeAll(blockList);
                blockList.clear();
            }
            case ExplosiveMinecart explosiveMinecart -> {
                if (getChunkdata().isTNTAllowed(chunk))return;
                event.blockList().removeAll(blockList);
                blockList.clear();
            }
            default -> {
                event.blockList().removeAll(blockList);
                blockList.clear();
            }
        }
    }
}