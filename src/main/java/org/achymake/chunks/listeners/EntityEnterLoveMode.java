package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.Database;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEnterLoveModeEvent;

public class EntityEnterLoveMode implements Listener {
    private Database getDatabase() {
        return Chunks.getDatabase();
    }
    public EntityEnterLoveMode(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityEnterLoveMode(EntityEnterLoveModeEvent event) {
        if (event.getHumanEntity() instanceof Player player) {
            Chunk chunk = event.getEntity().getLocation().getChunk();
            if (getDatabase().isProtected(chunk)) {
                if (getDatabase().hasAccess(player, chunk))return;
                event.setCancelled(true);
            } else if (getDatabase().isClaimed(chunk)) {
                if (getDatabase().hasAccess(player, chunk))return;
                event.setCancelled(true);
            }
        }
    }
}
