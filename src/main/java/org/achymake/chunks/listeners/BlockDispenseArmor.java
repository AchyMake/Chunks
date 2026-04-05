package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.WorldHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.plugin.PluginManager;

public class BlockDispenseArmor implements Listener {
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
    public BlockDispenseArmor() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockDispenseArmor(BlockDispenseArmorEvent event) {
        var chunk = event.getBlock().getChunk();
        if (!getWorldHandler().isAllowedClaim(chunk))return;
        if (!getInstance().isRedstoneOnlyInClaims())return;
        if (getChunkHandler().isClaimed(chunk))return;
        event.setCancelled(true);
    }
}