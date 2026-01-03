package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.WorldHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.plugin.PluginManager;

public class CrafterCraft implements Listener {
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
    public CrafterCraft() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCrafterCraft(CrafterCraftEvent event) {
        var block = event.getBlock();
        if (!getWorldHandler().isAllowedClaim(block.getChunk()))return;
        if (!getInstance().isRedstoneOnlyInClaims())return;
        if (getChunkHandler().isClaimed(block.getChunk()))return;
        event.setCancelled(true);
    }
}