package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.plugin.PluginManager;

public class BlockFromTo implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockFromTo() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockFromTo(BlockFromToEvent event) {
        var block = event.getBlock();
        if (!getChunkHandler().isAllowedClaim(block.getChunk()))return;
        if (!block.isLiquid())return;
        if (!getChunkHandler().isFluidFromOutsideDisabled())return;
        var blockFace = event.getFace();
        var chunk = block.getChunk();
        if (getChunkHandler().isClaimed(chunk)) {
            if (blockFace.equals(BlockFace.SOUTH)) {
                if (getChunkHandler().getOwner(block.getLocation().add(0, 0, 1).getBlock().getChunk()) == getChunkHandler().getOwner(chunk))return;
                event.setCancelled(true);
            } else if (blockFace.equals(BlockFace.EAST)) {
                if (getChunkHandler().getOwner(block.getLocation().add(1, 0, 0).getBlock().getChunk()) == getChunkHandler().getOwner(chunk))return;
                event.setCancelled(true);
            } else if (blockFace.equals(BlockFace.NORTH)) {
                if (getChunkHandler().getOwner(block.getLocation().add(0, 0, -1).getBlock().getChunk()) == getChunkHandler().getOwner(chunk))return;
                event.setCancelled(true);
            } else if (blockFace.equals(BlockFace.WEST)) {
                if (getChunkHandler().getOwner(block.getLocation().add(-1, 0, 0).getBlock().getChunk()) == getChunkHandler().getOwner(chunk))return;
                event.setCancelled(true);
            }
        } else if (blockFace.equals(BlockFace.SOUTH)) {
            if (getChunkHandler().isClaimed(block.getLocation().add(0, 0, 1).getBlock().getChunk())) {
                event.setCancelled(true);
            }
        } else if (blockFace.equals(BlockFace.EAST)) {
            if (getChunkHandler().isClaimed(block.getLocation().add(1, 0, 0).getBlock().getChunk())) {
                event.setCancelled(true);
            }
        } else if (blockFace.equals(BlockFace.NORTH)) {
            if (getChunkHandler().isClaimed(block.getLocation().add(0, 0, -1).getBlock().getChunk())) {
                event.setCancelled(true);
            }
        } else if (blockFace.equals(BlockFace.WEST)) {
            if (getChunkHandler().isClaimed(block.getLocation().add(-1, 0, 0).getBlock().getChunk())) {
                event.setCancelled(true);
            }
        }
    }
}