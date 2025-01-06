package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.handlers.ChunkHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.plugin.PluginManager;

public class HangingBreakByEntity implements Listener {
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
    public HangingBreakByEntity() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        var chunk = event.getEntity().getLocation().getChunk();
        if (event.getRemover() instanceof Player player) {
            if (!getChunkHandler().isClaimed(chunk))return;
            if (getChunkHandler().hasAccess(chunk, player))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
        } else event.setCancelled(true);
    }
}