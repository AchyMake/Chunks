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
        var block = event.getBlock();
        if (!getChunkHandler().isAllowedClaim(block.getChunk()))return;
        if (!getChunkHandler().isRedstoneOnlyInClaims())return;
        if (getChunkHandler().isClaimed(block.getChunk()))return;
        var type = block.getType();
        if (type.equals(getInstance().getMaterialHandler().get("powered_rail")))return;
        if (type.equals(getInstance().getMaterialHandler().get("detector_rail")))return;
        if (type.equals(getInstance().getMaterialHandler().get("activator_rail")))return;
        event.setNewCurrent(event.getOldCurrent());
    }
}