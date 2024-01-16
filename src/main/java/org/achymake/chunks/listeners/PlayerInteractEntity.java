package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.Database;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntity implements Listener {
    private Database getDatabase() {
        return Chunks.getDatabase();
    }
    public PlayerInteractEntity(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getRightClicked().getLocation().getChunk();
        Entity entity = event.getRightClicked();
        if (getDatabase().isProtected(chunk)) {
            if (entity.getType().equals(EntityType.PLAYER))return;
            if (entity.getType().equals(EntityType.MINECART))return;
            if (entity.getType().equals(EntityType.BOAT))return;
            if (entity.isInvulnerable())return;
            if (getDatabase().hasAccess(player, chunk))return;
            if (Chunks.getInstance().getConfig().getBoolean("hostile." + entity.getType()))return;
            event.setCancelled(true);
            Chunks.sendActionBar(player, "&cChunk is protected by&f Server");
        } else if (getDatabase().isClaimed(chunk)) {
            if (entity.getType().equals(EntityType.PLAYER))return;
            if (entity.getType().equals(EntityType.MINECART))return;
            if (entity.getType().equals(EntityType.BOAT))return;
            if (entity.isInvulnerable())return;
            if (getDatabase().hasAccess(player, chunk))return;
            if (Chunks.getInstance().getConfig().getBoolean("hostile." + entity.getType()))return;
            event.setCancelled(true);
            Chunks.sendActionBar(player, "&cChunk is owned by&f " + getDatabase().getOwner(chunk).getName());
        }
    }
}
