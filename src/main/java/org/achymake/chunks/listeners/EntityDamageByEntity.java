package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public record EntityDamageByEntity(Chunks plugin) implements Listener {
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Chunk chunk = entity.getLocation().getChunk();
        if (!getChunkdata().isClaimed(chunk))return;
        if (!getChunkdata().isFriendly(entity))return;
        Entity damager = event.getDamager();
        String owner = getChunkdata().getOwner(chunk).getName();
        switch (damager) {
            case Arrow arrow -> {
                if (arrow.getShooter() instanceof Player player) {
                    if (entity instanceof Player) {
                        if (!getChunkdata().pvpInsideClaims()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&cHey!&7 Sorry but PVP is disabled inside claims");
                        }
                    } else {
                        if (getChunkdata().hasAccess(player, chunk))return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&cChunk is owned by&f " + owner);
                    }
                }
            }
            case Player player -> {
                if (entity instanceof Player) {
                    if (!getChunkdata().pvpInsideClaims()) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&cHey!&7 Sorry but PVP is disabled inside claims");
                    }
                } else {
                    if (getChunkdata().hasAccess(player, chunk))return;
                    event.setCancelled(true);
                    getMessage().sendActionBar(player, "&cChunk is owned by&f " + owner);
                }
            }
            case Snowball snowball -> {
                if (snowball.getShooter() instanceof Player player) {
                    if (entity instanceof Player) {
                        if (!getChunkdata().pvpInsideClaims()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&cHey!&7 Sorry but PVP is disabled inside claims");
                        }
                    } else {
                        if (getChunkdata().hasAccess(player, chunk))return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&cChunk is owned by&f " + owner);
                    }
                }
            }
            case SpectralArrow spectralArrow -> {
                if (spectralArrow.getShooter() instanceof Player player) {
                    if (entity instanceof Player) {
                        if (!getChunkdata().pvpInsideClaims()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&cHey!&7 Sorry but PVP is disabled inside claims");
                        }
                    } else {
                        if (getChunkdata().hasAccess(player, chunk))return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&cChunk is owned by&f " + owner);
                    }
                }
            }
            case ThrownPotion thrownPotion -> {
                if (thrownPotion.getShooter() instanceof Player player) {
                    if (entity instanceof Player) {
                        if (!getChunkdata().pvpInsideClaims()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&cHey!&7 Sorry but PVP is disabled inside claims");
                        }
                    } else {
                        if (getChunkdata().hasAccess(player, chunk))return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&cChunk is owned by&f " + owner);
                    }
                }
            }
            case Trident trident -> {
                if (trident.getShooter() instanceof Player player) {
                    if (entity instanceof Player) {
                        if (!getChunkdata().pvpInsideClaims()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&cHey!&7 Sorry but PVP is disabled inside claims");
                        }
                    } else {
                        if (getChunkdata().hasAccess(player, chunk))return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&cChunk is owned by&f " + owner);
                    }
                }
            }
            case WindCharge windCharge -> {
                if (windCharge.getShooter() instanceof Player player) {
                    if (entity instanceof Player) {
                        if (!getChunkdata().pvpInsideClaims()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&cHey!&7 Sorry but PVP is disabled inside claims");
                        }
                    } else {
                        if (getChunkdata().hasAccess(player, chunk))return;
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&cChunk is owned by&f " + owner);
                    }
                }
            }
            default -> {
            }
        }
    }
}