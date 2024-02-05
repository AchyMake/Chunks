package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

import java.text.MessageFormat;

public record PlayerShearEntity(Chunks plugin) implements Listener {
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        Chunk chunk = event.getEntity().getLocation().getChunk();
        if (!getChunkStorage().isClaimed(chunk))return;
        Player player = event.getPlayer();
        if (getChunkStorage().hasAccess(player, chunk))return;
        event.setCancelled(true);
        String text = getMessage().getString("events.player-shear-entity");
        String message = MessageFormat.format(text, getChunkStorage().getOwner(chunk).getName());
        getMessage().send(player, message);
    }
}
