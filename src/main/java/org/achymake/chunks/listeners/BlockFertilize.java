package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.WorldHandler;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;

public class BlockFertilize implements Listener {
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
    public BlockFertilize() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockFertilize(BlockFertilizeEvent event) {
        if (!getWorldHandler().isAllowedClaim(event.getBlock().getChunk()))return;
        if (!getInstance().isBlockFertilizeDisabled())return;
        var player = event.getPlayer();
        if (player == null)return;
        var blockList = new ArrayList<BlockState>();
        var blocks = event.getBlocks();
        blocks.forEach(blockState -> {
            var chunk = blockState.getChunk();
            if (!getChunkHandler().isClaimed(chunk))return;
            if (getChunkHandler().hasAccess(chunk, player))return;
            blockList.add(blockState);
        });
        if (blockList.isEmpty())return;
        blocks.removeAll(blockList);
    }
}