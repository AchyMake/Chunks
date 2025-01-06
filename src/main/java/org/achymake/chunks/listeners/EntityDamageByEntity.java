package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.EntityHandler;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.PluginManager;

public class EntityDamageByEntity implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public EntityDamageByEntity() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        var entity = event.getEntity();
        var chunk = entity.getLocation().getChunk();
        if (!getChunkHandler().isClaimed(chunk))return;
        var damager = event.getDamager();
        switch (damager) {
            case Arrow arrow -> {
                if (arrow.getShooter() instanceof Player player) {
                    if (entity instanceof Player target) {
                        if (getChunkHandler().isPvpInsideClaims())return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, getMessage().get("events.cancelled.pvp"));
                    } else if (getEntityHandler().isFriendly(entity.getType())) {
                        if (getChunkHandler().hasAccess(chunk, player))return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
                    }
                }
            }
            case Player player -> {
                if (entity instanceof Player target) {
                    if (getChunkHandler().isPvpInsideClaims())return;
                    event.setCancelled(true);
                    getMessage().sendActionBar(player, getMessage().get("events.cancelled.pvp"));
                } else if (getEntityHandler().isFriendly(entity.getType())) {
                    if (getChunkHandler().hasAccess(chunk, player))return;
                    event.setCancelled(true);
                    getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
                }
            }
            case Snowball snowball -> {
                if (snowball.getShooter() instanceof Player player) {
                    if (entity instanceof Player target) {
                        if (getChunkHandler().isPvpInsideClaims())return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, getMessage().get("events.cancelled.pvp"));
                    } else if (getEntityHandler().isFriendly(entity.getType())) {
                        if (getChunkHandler().hasAccess(chunk, player))return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
                    }
                }
            }
            case SpectralArrow spectralArrow -> {
                if (spectralArrow.getShooter() instanceof Player player) {
                    if (entity instanceof Player target) {
                        if (getChunkHandler().isPvpInsideClaims())return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, getMessage().get("events.cancelled.pvp"));
                    } else if (getEntityHandler().isFriendly(entity.getType())) {
                        if (getChunkHandler().hasAccess(chunk, player))return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
                    }
                }
            }
            case ThrownPotion thrownPotion -> {
                if (thrownPotion.getShooter() instanceof Player player) {
                    if (entity instanceof Player target) {
                        if (getChunkHandler().isPvpInsideClaims())return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, getMessage().get("events.cancelled.pvp"));
                    } else if (getEntityHandler().isFriendly(entity.getType())) {
                        if (getChunkHandler().hasAccess(chunk, player))return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
                    }
                }
            }
            case Trident trident -> {
                if (trident.getShooter() instanceof Player player) {
                    if (entity instanceof Player target) {
                        if (getChunkHandler().isPvpInsideClaims())return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, getMessage().get("events.cancelled.pvp"));
                    } else if (getEntityHandler().isFriendly(entity.getType())) {
                        if (getChunkHandler().hasAccess(chunk, player))return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
                    }
                }
            }
            case WindCharge windCharge -> {
                if (windCharge.getShooter() instanceof Player player) {
                    if (entity instanceof Player target) {
                        if (getChunkHandler().isPvpInsideClaims())return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, getMessage().get("events.cancelled.pvp"));
                    } else if (getEntityHandler().isFriendly(entity.getType())) {
                        if (getChunkHandler().hasAccess(chunk, player))return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
                    }
                }
            }
            default -> {
            }
        }
    }
}