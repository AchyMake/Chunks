package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.handlers.ChunkHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerUnleashEntity implements Listener {
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
    public PlayerUnleashEntity() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event) {
        var chunk = event.getEntity().getLocation().getChunk();
        if (!getChunkHandler().isClaimed(chunk))return;
        var player = event.getPlayer();
        if (getChunkHandler().hasAccess(chunk, player))return;
        event.setCancelled(true);
        getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
    }
}