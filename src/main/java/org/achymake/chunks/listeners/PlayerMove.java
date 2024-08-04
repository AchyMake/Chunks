package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public record PlayerMove(Chunks plugin) implements Listener {
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null)return;
        if (to.getChunk() == from.getChunk())return;
        Chunk chunk = to.getChunk();
        if (getChunkdata().isClaimed(chunk)) {
            if (getChunkdata().isBanned(chunk, player)) {
                if (plugin.getChunkEditors().contains(player)) {
                    visit(player, getChunkdata().getOwner(chunk));
                } else {
                    event.setCancelled(true);
                    getMessage().sendActionBar(player, "&cYou are banned from&f " + getChunkdata().getOwner(chunk).getName() + "&c's chunk");
                }
            } else {
                visit(player, getChunkdata().getOwner(chunk));
            }
        } else {
            exit(player);
        }
    }
    private void visit(Player player, OfflinePlayer owner) {
        if (plugin.getUserdata().getConfig(player).isString("visit")) {
            if (!plugin.getUserdata().getConfig(player).getString("visit").equalsIgnoreCase(owner.getUniqueId().toString())) {
                plugin.getUserdata().setString(player, "visit", null);
            }
        } else {
            getMessage().sendActionBar(player, "&6Visiting&f " + owner.getName() + "&6's chunk");
            plugin.getUserdata().setString(player, "visit", owner.getUniqueId().toString());
        }
    }
    private void exit(Player player) {
        if (plugin.getUserdata().getConfig(player).isString("visit")) {
            getMessage().sendActionBar(player, "&6Exited&f " + plugin.getServer().getOfflinePlayer(UUID.fromString(plugin.getUserdata().getConfig(player).getString("visit"))).getName() + "&6's chunk");
            plugin.getUserdata().setString(player, "visit", null);
        }
    }
}