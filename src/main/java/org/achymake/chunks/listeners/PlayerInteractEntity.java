package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.EntityHandler;
import org.achymake.chunks.handlers.WorldHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerInteractEntity implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerInteractEntity() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        var chunk = event.getRightClicked().getLocation().getChunk();
        if (!getWorldHandler().isAllowedClaim(chunk))return;
        if (!getChunkHandler().isClaimed(chunk))return;
        var player = event.getPlayer();
        if (getChunkHandler().hasAccess(chunk, player))return;
        if (event.getRightClicked().getType().equals(getEntityHandler().getEntityType("player")))return;
        if (event.getRightClicked().getType().equals(getEntityHandler().getEntityType("minecart")))return;
        if (event.getRightClicked().getType().equals(getEntityHandler().getEntityType("boat")))return;
        event.setCancelled(true);
        getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
    }
}