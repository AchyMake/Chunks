package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.Database;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener {
    private final Chunks plugin;
    private Database getDatabase() {
        return plugin.getDatabase();
    }
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    public EntityDamageByEntity(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Chunk chunk = event.getEntity().getLocation().getChunk();
        if (event.getDamager() instanceof Arrow arrow) {
            if (getDatabase().isProtected(chunk)) {
                if (!(arrow.getShooter() instanceof Player player))return;
                if (getDatabase().hasAccess(player, chunk))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (getConfig().getBoolean("hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                plugin.sendActionBar(player, "&cChunk is protected by&f Server");
            } else if (getDatabase().isClaimed(chunk)) {
                if (!(arrow.getShooter() instanceof Player player))return;
                if (getDatabase().hasAccess(player, chunk))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (getConfig().getBoolean("hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                plugin.sendActionBar(player, "&cChunk is claimed by&f " + getDatabase().getOwner(chunk).getName());

            }
        } else if (event.getDamager() instanceof Player player) {
            if (getDatabase().isProtected(chunk)) {
                if (getDatabase().hasAccess(player, chunk))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (getConfig().getBoolean("hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                plugin.sendActionBar(player, "&cChunk is protected by&f Server");
            } else if (getDatabase().isClaimed(chunk)) {
                if (getDatabase().hasAccess(player, chunk))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (getConfig().getBoolean("hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                plugin.sendActionBar(player, "&cChunk is owned by&f " + getDatabase().getOwner(chunk).getName());
            }
        } else if (event.getDamager() instanceof Snowball snowball) {
            if (getDatabase().isProtected(chunk)) {
                if (!(snowball.getShooter() instanceof Player player))return;
                if (getDatabase().hasAccess(player, chunk))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (getConfig().getBoolean("hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                plugin.sendActionBar(player, "&cChunk is protected by&f Server");
            } else if (getDatabase().isClaimed(chunk)) {
                if (!(snowball.getShooter() instanceof Player player)) return;
                if (getDatabase().hasAccess(player, chunk))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (getConfig().getBoolean("hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                plugin.sendActionBar(player, "&cChunk is claimed by&f " + getDatabase().getOwner(chunk).getName());
            }
        } else if (event.getDamager() instanceof SpectralArrow spectralArrow) {
            if (getDatabase().isProtected(chunk)) {
                if (!(spectralArrow.getShooter() instanceof Player player))return;
                if (getDatabase().hasAccess(player, chunk))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (getConfig().getBoolean("hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                plugin.sendActionBar(player, "&cChunk is protected by&f Server");
            } else if (getDatabase().isClaimed(chunk)) {
                if (!(spectralArrow.getShooter() instanceof Player player)) return;
                if (getDatabase().hasAccess(player, chunk))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (getConfig().getBoolean("hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                plugin.sendActionBar(player, "&cChunk is claimed by&f " + getDatabase().getOwner(chunk).getName());
            }
        } else if (event.getDamager() instanceof ThrownPotion thrownPotion) {
            if (getDatabase().isProtected(chunk)) {
                if (!(thrownPotion.getShooter() instanceof Player player)) return;
                if (getDatabase().hasAccess(player, chunk))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (getConfig().getBoolean("hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                plugin.sendActionBar(player, "&cChunk is protected by&f Server");
            } else if (getDatabase().isClaimed(chunk)) {
                if (!(thrownPotion.getShooter() instanceof Player player))return;
                if (getDatabase().hasAccess(player, chunk))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (getConfig().getBoolean("hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                plugin.sendActionBar(player, "&cChunk is claimed by&f " + getDatabase().getOwner(chunk).getName());
            }
        } else if (event.getDamager() instanceof Trident trident) {
            if (getDatabase().isProtected(chunk)) {
                if (!(trident.getShooter() instanceof Player player))return;
                if (getDatabase().hasAccess(player, chunk))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (getConfig().getBoolean("hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                plugin.sendActionBar(player, "&cChunk is protected by&f Server");
            } else if (getDatabase().isClaimed(chunk)) {
                if (!(trident.getShooter() instanceof Player player))return;
                if (getDatabase().hasAccess(player, chunk))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (getConfig().getBoolean("hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                plugin.sendActionBar(player, "&cChunk is claimed by&f " + getDatabase().getOwner(chunk).getName());
            }
        }
    }
}
