package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.ChunkStorage;
import org.achymake.chunks.files.Message;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener {
    private final Chunks plugin;
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public EntityDamageByEntity(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity target = event.getEntity();
        Chunk chunk = event.getEntity().getLocation().getChunk();
        if (target.isInvulnerable())return;
        if (target.getType().equals(EntityType.PLAYER))return;
        if (getConfig().getBoolean("hostile." + target.getType()))return;
        if (!getChunkStorage().isClaimed(chunk))return;
        if (damager instanceof Arrow arrow) {
            if (!(arrow.getShooter() instanceof Player player))return;
            if (getChunkStorage().hasAccess(player, chunk))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, "&cError:&7 Chunk owned by&f " + getChunkStorage().getOwner(chunk).getName());
        } else if (damager instanceof Player player) {
            if (getChunkStorage().hasAccess(player, chunk))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, "&cError:&7 Chunk owned by&f " + getChunkStorage().getOwner(chunk).getName());
        } else if (damager instanceof Snowball snowball) {
            if (!(snowball.getShooter() instanceof Player player)) return;
            if (getChunkStorage().hasAccess(player, chunk))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, "&cError:&7 Chunk owned by&f " + getChunkStorage().getOwner(chunk).getName());
        } else if (damager instanceof SpectralArrow spectralArrow) {
            if (!(spectralArrow.getShooter() instanceof Player player)) return;
            if (getChunkStorage().hasAccess(player, chunk))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, "&cError:&7 Chunk owned by&f " + getChunkStorage().getOwner(chunk).getName());
        } else if (damager instanceof ThrownPotion thrownPotion) {
            if (!(thrownPotion.getShooter() instanceof Player player))return;
            if (getChunkStorage().hasAccess(player, chunk))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, "&cError:&7 Chunk owned by&f " + getChunkStorage().getOwner(chunk).getName());
        } else if (damager instanceof Trident trident) {
            if (!(trident.getShooter() instanceof Player player))return;
            if (getChunkStorage().hasAccess(player, chunk))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, "&cError:&7 Chunk owned by&f " + getChunkStorage().getOwner(chunk).getName());
        }
    }
}
