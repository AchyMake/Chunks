package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.achymake.chunks.handlers.ChunkHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.PluginManager;

public class PlayerToggleFlight implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerToggleFlight() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        if (!getChunkHandler().manipulateFly())return;
        var player = event.getPlayer();
        if (!getUserdata().isSurvival(player))return;
        var chunk = player.getLocation().getChunk();
        if (getChunkHandler().isClaimed(chunk)) {
            if (getChunkHandler().hasAccess(chunk, player))return;
            event.setCancelled(true);
            getUserdata().disableFly(player);
            getMessage().sendActionBar(player, getMessage().get("events.fly.claimed", getChunkHandler().getName(chunk)));
        } else if (!getUserdata().isEditor(player)) {
            event.setCancelled(true);
            getUserdata().disableFly(player);
            getMessage().sendActionBar(player, getMessage().get("events.fly.unclaimed"));
        }
    }
}