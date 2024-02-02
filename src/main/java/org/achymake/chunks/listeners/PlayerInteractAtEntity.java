package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public record PlayerInteractAtEntity(Chunks plugin) implements Listener {
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        Chunk chunk = entity.getLocation().getChunk();
        if (!getChunkStorage().isClaimed(chunk))return;
        if (getConfig().getBoolean("hostile." + entity.getType()))return;
        if (entity.getType().equals(EntityType.PLAYER))return;
        if (entity.getType().equals(EntityType.MINECART))return;
        if (entity.getType().equals(EntityType.BOAT))return;
        if (entity.getType().equals(EntityType.INTERACTION))return;
        if (entity.isInvulnerable())return;
        Player player = event.getPlayer();
        if (getChunkStorage().hasAccess(player, chunk))return;
        event.setCancelled(true);
        String owner = getChunkStorage().getOwner(chunk).getName();
        getMessage().send(player, "&c&lHey!&7 Sorry, chunk is owned by&f " + owner);
    }
}
