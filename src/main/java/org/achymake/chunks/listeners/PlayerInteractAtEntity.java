package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public record PlayerInteractAtEntity(Chunks plugin) implements Listener {
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractAtEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Chunk chunk = entity.getLocation().getChunk();
        if (!getChunkdata().isClaimed(chunk))return;
        if (getChunkdata().isHostile(entity))return;
        if (entity.isInvulnerable())return;
        if (entity.getType().equals(EntityType.PLAYER))return;
        if (entity.getType().equals(EntityType.MINECART))return;
        if (entity.getType().equals(EntityType.BOAT))return;
        if (entity.getType().equals(EntityType.INTERACTION))return;
        Player player = event.getPlayer();
        if (getChunkdata().hasAccess(player, chunk))return;
        event.setCancelled(true);
        getMessage().sendActionBar(player, "&cChunk is owned by&f " + getChunkdata().getOwner(chunk).getName());
    }
}