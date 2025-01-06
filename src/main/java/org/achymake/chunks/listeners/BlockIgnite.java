package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.handlers.ChunkHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.plugin.PluginManager;

public class BlockIgnite implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockIgnite() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getIgnitingBlock() == null)return;
        if (event.getIgnitingEntity() instanceof Player player) {
            var chunk = event.getIgnitingBlock().getChunk();
            if (!getChunkHandler().isClaimed(chunk))return;
            if (getChunkHandler().hasAccess(chunk, player))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
        }
    }
}