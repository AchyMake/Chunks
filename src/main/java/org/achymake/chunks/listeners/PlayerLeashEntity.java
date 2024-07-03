package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerLeashEntityEvent;

public record PlayerLeashEntity(Chunks plugin) implements Listener {
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        Chunk chunk = event.getEntity().getLocation().getChunk();
        if (!getChunkdata().isClaimed(chunk))return;
        Player player = event.getPlayer();
        if (getChunkdata().hasAccess(player, chunk))return;
        event.setCancelled(true);
        String owner = getChunkdata().getOwner(chunk).getName();
        getMessage().sendActionBar(player, "&cChunk is owned by&f " + owner);
    }
}