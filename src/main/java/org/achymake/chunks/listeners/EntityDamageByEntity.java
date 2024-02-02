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

public class EntityDamageByEntity implements Listener {
    private final FileConfiguration config;
    private final ChunkStorage chunkStorage;
    private final Message message;
    public EntityDamageByEntity(Chunks plugin) {
        config = plugin.getConfig();
        chunkStorage = plugin.getChunkStorage();
        message = plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity target = event.getEntity();
        Chunk chunk = event.getEntity().getLocation().getChunk();
        if (target.isInvulnerable())return;
        if (target.getType().equals(EntityType.PLAYER))return;
        if (config.getBoolean("hostile." + target.getType()))return;
        if (!chunkStorage.isClaimed(chunk))return;
        if (damager instanceof Arrow arrow) {
            if (!(arrow.getShooter() instanceof Player player))return;
            if (chunkStorage.hasAccess(player, chunk))return;
            event.setCancelled(true);
            message.send(player, "&c&lHey!&7 Sorry, chunk is owned by&f " + chunkStorage.getOwner(chunk).getName());
        } else if (damager instanceof Player player) {
            if (chunkStorage.hasAccess(player, chunk))return;
            event.setCancelled(true);
            message.send(player, "&c&lHey!&7 Sorry, chunk is owned by&f " + chunkStorage.getOwner(chunk).getName());
        } else if (damager instanceof Snowball snowball) {
            if (!(snowball.getShooter() instanceof Player player)) return;
            if (chunkStorage.hasAccess(player, chunk))return;
            event.setCancelled(true);
            message.send(player, "&c&lHey!&7 Sorry, chunk is owned by&f " + chunkStorage.getOwner(chunk).getName());
        } else if (damager instanceof SpectralArrow spectralArrow) {
            if (!(spectralArrow.getShooter() instanceof Player player)) return;
            if (chunkStorage.hasAccess(player, chunk))return;
            event.setCancelled(true);
            message.send(player, "&c&lHey!&7 Sorry, chunk is owned by&f " + chunkStorage.getOwner(chunk).getName());
        } else if (damager instanceof ThrownPotion thrownPotion) {
            if (!(thrownPotion.getShooter() instanceof Player player))return;
            if (chunkStorage.hasAccess(player, chunk))return;
            event.setCancelled(true);
            message.send(player, "&c&lHey!&7 Sorry, chunk is owned by&f " + chunkStorage.getOwner(chunk).getName());
        } else if (damager instanceof Trident trident) {
            if (!(trident.getShooter() instanceof Player player))return;
            if (chunkStorage.hasAccess(player, chunk))return;
            event.setCancelled(true);
            message.send(player, "&c&lHey!&7 Sorry, chunk is owned by&f " + chunkStorage.getOwner(chunk).getName());
        }
    }
}
