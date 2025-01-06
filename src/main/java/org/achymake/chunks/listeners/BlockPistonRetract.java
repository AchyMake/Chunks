package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.plugin.PluginManager;

public class BlockPistonRetract implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockPistonRetract() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        var chunk = event.getBlock().getChunk();
        if (getChunkHandler().isRedstoneOnlyInClaims()) {
            if (getChunkHandler().isClaimed(chunk))return;
            event.setCancelled(true);
        } else if (getChunkHandler().isRedstoneOnlyInClaims()) {
            if (getChunkHandler().isClaimed(chunk)) {
                for (var block : event.getBlocks()) {
                    if (getChunkHandler().isClaimed(block.getChunk())) {
                        if (getChunkHandler().getOwner(chunk) == getChunkHandler().getOwner(block.getChunk()))return;
                        event.setCancelled(true);
                    }
                }
            } else {
                for (var block : event.getBlocks()) {
                    if (!getChunkHandler().isClaimed(block.getChunk()))return;
                    event.setCancelled(true);
                }
            }
        }
    }
}