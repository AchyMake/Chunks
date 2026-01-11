package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.EntityHandler;
import org.achymake.chunks.handlers.WorldHandler;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;

public class EntityExplode implements Listener {
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
    public EntityExplode() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityExplode(EntityExplodeEvent event) {
        var chunk = event.getLocation().getChunk();
        if (!getWorldHandler().isAllowedClaim(chunk))return;
        if (!getInstance().isTNTBlockDamageDisabled())return;
        var blockList = event.blockList();
        var newBlockList = new ArrayList<Block>();
        var entityType = event.getEntityType();
        blockList.forEach(block -> {
            var blockChunk = block.getChunk();
            if (getChunkHandler().isClaimed(blockChunk)) {
                if (!getEntityHandler().isTNT(entityType))return;
                if (getChunkHandler().isTNTAllowed(blockChunk))return;
                newBlockList.add(block);
            } else newBlockList.add(block);
        });
        if (newBlockList.isEmpty())return;
        blockList.removeAll(newBlockList);
    }
}