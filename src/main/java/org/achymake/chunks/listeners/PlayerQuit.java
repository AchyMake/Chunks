package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    public PlayerQuit(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!Chunks.getChunkEditors().contains(event.getPlayer()))return;
        Chunks.getChunkEditors().remove(event.getPlayer());
    }
}
