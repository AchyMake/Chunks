package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.handlers.ChunkHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.plugin.PluginManager;

public class CauldronLevelChange implements Listener {
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
    public CauldronLevelChange() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCauldronLevelChange(CauldronLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            var chunk = event.getBlock().getChunk();
            if (!getChunkHandler().isClaimed(chunk))return;
            if (!getChunkHandler().isCauldronLevelChangeDisabled())return;
            if (getChunkHandler().hasAccess(chunk, player))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
        }
    }
}