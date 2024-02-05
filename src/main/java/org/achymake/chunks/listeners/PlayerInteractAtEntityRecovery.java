package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.achymake.recovery.Recovery;
import org.achymake.recovery.files.Database;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.text.MessageFormat;

public record PlayerInteractAtEntityRecovery(Chunks plugin) implements Listener {
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    private Database getDatabase() {
        return Recovery.getInstance().getDatabase();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractAtEntityRecovery(PlayerInteractAtEntityEvent event) {
        Chunk chunk = event.getRightClicked().getLocation().getChunk();
        if (!getChunkStorage().isClaimed(chunk))return;
        Entity entity = event.getRightClicked();
        if (entity.getType().equals(EntityType.PLAYER))return;
        if (entity.getType().equals(EntityType.MINECART))return;
        if (entity.getType().equals(EntityType.BOAT))return;
        if (entity.isInvulnerable())return;
        if (entity instanceof ArmorStand armorStand) {
            if (getDatabase().hasDeathItems(armorStand))return;
        }
        Player player = event.getPlayer();
        if (getChunkStorage().hasAccess(player, chunk))return;
        if (getConfig().getBoolean("hostile." + entity.getType()))return;
        event.setCancelled(true);
        String text = getMessage().getString("events.player-interact-at-entity");
        String message = MessageFormat.format(text, getChunkStorage().getOwner(chunk).getName());
        getMessage().send(player, message);
    }
}