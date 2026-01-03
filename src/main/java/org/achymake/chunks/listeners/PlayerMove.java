package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.events.PlayerChangedChunkEvent;
import org.achymake.chunks.handlers.WorldHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerMove implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerMove() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null)return;
        if (event.getFrom().getChunk() == event.getTo().getChunk())return;
        if (!getWorldHandler().isAllowedClaim(event.getTo().getChunk()))return;
        getPluginManager().callEvent(new PlayerChangedChunkEvent(event.getPlayer(), event.getFrom(), event.getTo()));
    }
}