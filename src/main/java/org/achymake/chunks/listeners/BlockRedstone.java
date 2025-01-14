package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.PluginManager;

public class BlockRedstone implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockRedstone() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockRedstone(BlockRedstoneEvent event) {
        if (!getChunkHandler().isRedstoneOnlyInClaims())return;
        if (getChunkHandler().isClaimed(event.getBlock().getChunk()))return;
        if (event.getBlock().getType().equals(getInstance().getMaterialHandler().get("powered_rail")))return;
        if (event.getBlock().getType().equals(getInstance().getMaterialHandler().get("detector_rail")))return;
        if (event.getBlock().getType().equals(getInstance().getMaterialHandler().get("activator_rail")))return;
        event.setNewCurrent(event.getOldCurrent());
    }
}