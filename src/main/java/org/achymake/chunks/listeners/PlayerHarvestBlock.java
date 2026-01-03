package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.WorldHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerHarvestBlock implements Listener {
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
    public PlayerHarvestBlock() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerHarvestBlock(PlayerHarvestBlockEvent event) {
        var chunk = event.getHarvestedBlock().getChunk();
        if (!getWorldHandler().isAllowedClaim(chunk))return;
        if (!getChunkHandler().isClaimed(chunk))return;
        if (!getInstance().isHarvestBlockDisabled(event.getHarvestedBlock().getType()))return;
        var player = event.getPlayer();
        if (getChunkHandler().hasAccess(chunk, player))return;
        event.setCancelled(true);
        getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
    }
}