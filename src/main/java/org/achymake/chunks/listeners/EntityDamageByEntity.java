package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.text.MessageFormat;

public record EntityDamageByEntity(Chunks plugin) implements Listener {
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
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Chunk chunk = event.getEntity().getLocation().getChunk();
        if (!getChunkStorage().isClaimed(chunk))return;
        Entity target = event.getEntity();
        if (target.getType().equals(EntityType.PLAYER))return;
        if (getConfig().getBoolean("hostile." + target.getType()))return;
        Entity damager = event.getDamager();
        if (damager instanceof Arrow arrow) {
            if (!(arrow.getShooter() instanceof Player player))return;
            if (getChunkStorage().hasAccess(player, chunk))return;
            event.setCancelled(true);
            player.sendMessage(MessageFormat.format(getMessage().getString("events.entity-damage-entity"), getChunkStorage().getOwner(chunk).getName()));
        } else if (damager instanceof Player player) {
            if (getChunkStorage().hasAccess(player, chunk))return;
            event.setCancelled(true);
            player.sendMessage(MessageFormat.format(getMessage().getString("events.entity-damage-entity"), getChunkStorage().getOwner(chunk).getName()));
        } else if (damager instanceof Snowball snowball) {
            if (!(snowball.getShooter() instanceof Player player)) return;
            if (getChunkStorage().hasAccess(player, chunk))return;
            event.setCancelled(true);
            player.sendMessage(MessageFormat.format(getMessage().getString("events.entity-damage-entity"), getChunkStorage().getOwner(chunk).getName()));
        } else if (damager instanceof SpectralArrow spectralArrow) {
            if (!(spectralArrow.getShooter() instanceof Player player)) return;
            if (getChunkStorage().hasAccess(player, chunk))return;
            event.setCancelled(true);
            player.sendMessage(MessageFormat.format(getMessage().getString("events.entity-damage-entity"), getChunkStorage().getOwner(chunk).getName()));
        } else if (damager instanceof ThrownPotion thrownPotion) {
            if (!(thrownPotion.getShooter() instanceof Player player))return;
            if (getChunkStorage().hasAccess(player, chunk))return;
            event.setCancelled(true);
            player.sendMessage(MessageFormat.format(getMessage().getString("events.entity-damage-entity"), getChunkStorage().getOwner(chunk).getName()));
        } else if (damager instanceof Trident trident) {
            if (!(trident.getShooter() instanceof Player player))return;
            if (getChunkStorage().hasAccess(player, chunk))return;
            event.setCancelled(true);
            player.sendMessage(MessageFormat.format(getMessage().getString("events.entity-damage-entity"), getChunkStorage().getOwner(chunk).getName()));
        }
    }
}
