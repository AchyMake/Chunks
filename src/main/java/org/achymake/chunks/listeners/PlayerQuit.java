package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    private Database getDatabase() {
        return Chunks.getDatabase();
    }
    public PlayerQuit(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!getDatabase().getConfig(event.getPlayer()).getBoolean("settings.chunk-edit"))return;
        getDatabase().setBoolean(event.getPlayer(), "settings.chunk-edit", false);
    }
}
