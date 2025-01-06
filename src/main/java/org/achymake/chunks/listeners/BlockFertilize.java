package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Userdata;
import org.achymake.chunks.handlers.ChunkHandler;
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
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockFertilize() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockFertilize(BlockFertilizeEvent event) {
        if (!getChunkHandler().isBlockFertilizeDisabled())return;
        var player = event.getPlayer();
        if (player == null)return;
        if (getUserdata().isEditor(player))return;
        var blockList = new ArrayList<BlockState>();
        event.getBlocks().forEach(blockState -> {
            if (!getChunkHandler().isClaimed(blockState.getChunk()))return;
            blockList.add(blockState);
        });
        if (blockList.isEmpty())return;
        event.getBlocks().removeAll(blockList);
        blockList.clear();
    }
}