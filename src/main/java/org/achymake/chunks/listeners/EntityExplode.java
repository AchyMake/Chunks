package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.EntityHandler;
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
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public EntityExplode() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!getChunkHandler().isTNTBlockDamageDisabled())return;
        var blockList = new ArrayList<Block>();
        for (var block : event.blockList()) {
            if (getChunkHandler().isClaimed(block.getChunk())) {
                if (event.getEntityType().equals(getEntityHandler().getEntityType("tnt")) || event.getEntityType().equals(getEntityHandler().getEntityType("tnt_minecart"))) {
                    if (getChunkHandler().isTNTAllowed(block.getChunk()))return;
                    blockList.add(block);
                }
            } else blockList.add(block);
        }
        event.blockList().removeAll(blockList);
        blockList.clear();
    }
}