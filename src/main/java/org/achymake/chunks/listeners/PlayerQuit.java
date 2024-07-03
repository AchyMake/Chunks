package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public record PlayerQuit(Chunks plugin) implements Listener {
    private List<Player> getChunkEditors() {
        return plugin.getChunkEditors();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!getChunkEditors().contains(player))return;
        getChunkEditors().remove(player);
    }
}