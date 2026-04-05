package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.WorldHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.plugin.PluginManager;

public class BlockPistonExtend implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockPistonExtend() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        var chunk = event.getBlock().getChunk();
        if (!getWorldHandler().isAllowedClaim(chunk))return;
        if (getInstance().isRedstoneOnlyInClaims()) {
            if (getChunkHandler().isClaimed(chunk))return;
            event.setCancelled(true);
        }
        if (getInstance().isPistonFromOutsideDisabled()) {
            var blocks = event.getBlocks();
            if (getChunkHandler().isClaimed(chunk)) {
                for (var block : blocks) {
                    var blocksChunk = block.getChunk();
                    if (getChunkHandler().isClaimed(blocksChunk)) {
                        if (getChunkHandler().getOwner(chunk) == getChunkHandler().getOwner(blocksChunk))return;
                        event.setCancelled(true);
                    }
                }
            } else {
                for (var block : blocks) {
                    var blocksChunk = block.getChunk();
                    if (!getChunkHandler().isClaimed(blocksChunk))return;
                    event.setCancelled(true);
                }
            }
        }
    }
}