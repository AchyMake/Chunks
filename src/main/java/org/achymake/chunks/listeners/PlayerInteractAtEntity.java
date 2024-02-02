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

public class PlayerInteractAtEntity implements Listener {
    private final ChunkStorage chunkStorage;
    private final FileConfiguration config;
    private final Message message;
    public PlayerInteractAtEntity(Chunks plugin) {
        chunkStorage = plugin.getChunkStorage();
        config = plugin.getConfig();
        message = plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Entity entity = event.getRightClicked();
        Chunk chunk = entity.getLocation().getChunk();
        if (!chunkStorage.isClaimed(chunk))return;
        if (config.getBoolean("hostile." + entity.getType()))return;
        if (entity.getType().equals(EntityType.PLAYER))return;
        if (entity.getType().equals(EntityType.MINECART))return;
        if (entity.getType().equals(EntityType.BOAT))return;
        if (entity.getType().equals(EntityType.INTERACTION))return;
        if (entity.isInvulnerable())return;
        Player player = event.getPlayer();
        if (chunkStorage.hasAccess(player, chunk))return;
        event.setCancelled(true);
        message.send(player, "&c&lHey!&7 Sorry, chunk is owned by&f " + chunkStorage.getOwner(chunk).getName());
    }
}
