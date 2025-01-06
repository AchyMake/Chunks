package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockShearEntityEvent;
import org.bukkit.plugin.PluginManager;

public class BlockShearEntity implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockShearEntity() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockShearEntity(BlockShearEntityEvent event) {
        if (!getChunkHandler().isRedstoneOnlyInClaims())return;
        var block = event.getBlock();
        var blockChunk = block.getChunk();
        var entity = event.getEntity();
        var entityChunk = entity.getLocation().getChunk();
        if (blockChunk == entityChunk)return;
        if (getChunkHandler().isClaimed(entityChunk)) {
            if (getChunkHandler().isClaimed(blockChunk)) {
                if (getChunkHandler().getOwner(blockChunk) == getChunkHandler().getOwner(entityChunk))return;
                event.setCancelled(true);
            } else event.setCancelled(true);
        } else event.setCancelled(true);
    }
}