package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.handlers.ChunkHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.PluginManager;

public class EntityChangeBlock implements Listener {
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
    public EntityChangeBlock() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        var chunkHandler = getChunkHandler();
        var chunk = event.getBlock().getChunk();
        if (!chunkHandler.isClaimed(chunk))return;
        if (event.getEntity() instanceof Player player) {
            if (!getChunkHandler().isChangeBlockDisabled(event.getBlock().getType()))return;
            if (chunkHandler.hasAccess(chunk, player))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", chunkHandler.getName(chunk)));
        }
    }
}