package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.WorldHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerInteract implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
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
    public PlayerInteract() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        var block = event.getClickedBlock();
        if (block == null)return;
        var chunk = block.getChunk();
        if (!getWorldHandler().isAllowedClaim(chunk))return;
        if (!getChunkHandler().isClaimed(chunk))return;
        var player = event.getPlayer();
        if (getChunkHandler().hasAccess(chunk, player))return;
        if (event.getAction().equals(Action.PHYSICAL)) {
            if (!getInstance().isPhysicalBlockDisabled(block.getType()))return;
            event.setCancelled(true);
        } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (getInstance().isInteractBlockDisabled(block.getType())) {
                event.setCancelled(true);
                getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
            } else if (getInstance().isBedDisabled(block.getType())) {
                event.setCancelled(true);
                getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
            } else if (getInstance().isHarvestBlockDisabled(block.getType())) {
                event.setCancelled(true);
                getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
            }
        }
    }
}