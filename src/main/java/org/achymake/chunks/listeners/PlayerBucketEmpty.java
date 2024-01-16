package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.Database;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class PlayerBucketEmpty implements Listener {
    private Database getDatabase() {
        return Chunks.getDatabase();
    }
    public PlayerBucketEmpty(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlockClicked().getChunk();
        if (getDatabase().isProtected(chunk)) {
            if (getDatabase().hasAccess(player, chunk))return;
            event.setCancelled(true);
            Chunks.sendActionBar(player, "&cChunk is protected by&f Server");
        } else if (getDatabase().isClaimed(chunk)) {
            if (getDatabase().hasAccess(player, chunk))return;
            event.setCancelled(true);
            Chunks.sendActionBar(player, "&cChunk is owned by&f " + getDatabase().getOwner(chunk).getName());
        }
    }
}
