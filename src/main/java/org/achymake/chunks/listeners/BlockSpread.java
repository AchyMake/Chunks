package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.plugin.PluginManager;

public class BlockSpread implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockSpread() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockSpread(BlockSpreadEvent event) {
        var sourceChunk = event.getSource().getChunk();
        if (!getChunkHandler().isAllowedClaim(sourceChunk))return;
        var blockChunk = event.getBlock().getChunk();
        if (event.getSource().getType().equals(Material.FIRE)) {
            if (getChunkHandler().isClaimed(sourceChunk)) {
                if (getChunkHandler().isClaimed(blockChunk)) {
                    if (getChunkHandler().getOwner(sourceChunk) == getChunkHandler().getOwner(blockChunk))return;
                    event.setCancelled(true);
                }
            } else if (getChunkHandler().isClaimed(blockChunk)) {
                event.setCancelled(true);
            }
        } else if (event.getSource().getType().equals(Material.LAVA)) {
            if (getChunkHandler().isClaimed(sourceChunk)) {
                if (getChunkHandler().isClaimed(blockChunk)) {
                    if (getChunkHandler().getOwner(sourceChunk) == getChunkHandler().getOwner(blockChunk))return;
                    event.setCancelled(true);
                }
            } else if (getChunkHandler().isClaimed(blockChunk)) {
                event.setCancelled(true);
            }
        }
    }
}