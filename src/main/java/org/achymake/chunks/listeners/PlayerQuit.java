package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public record PlayerQuit(Chunks plugin) implements Listener {
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!getChunkStorage().hasChunkEdit(player))return;
        getChunkStorage().getChunkEditors().remove(player);
    }
}