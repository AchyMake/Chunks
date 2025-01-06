package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerCommandPreprocess implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerCommandPreprocess() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        var player = event.getPlayer();
        var chunk = player.getLocation().getChunk();
        if (!getChunkHandler().isClaimed(chunk))return;
        if (!event.getMessage().toLowerCase().startsWith("/sethome"))return;
        if (getChunkHandler().hasAccess(chunk, player))return;
        event.setCancelled(true);
        player.sendMessage(getInstance().getMessage().get("events.cancelled.command", event.getMessage()));
    }
}