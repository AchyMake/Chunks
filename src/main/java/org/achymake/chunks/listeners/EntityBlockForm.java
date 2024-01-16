package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.Database;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;

public class EntityBlockForm implements Listener {
    private Database getDatabase() {
        return Chunks.getDatabase();
    }
    public EntityBlockForm(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityBlockForm(EntityBlockFormEvent event) {
        if (!getDatabase().isProtected(event.getBlock().getChunk()))return;
        if (event.getEntity() instanceof Player player) {
            if (getDatabase().hasAccess(player, event.getBlock().getChunk()))return;
            event.setCancelled(true);
        } else {
            event.setCancelled(true);
        }
    }
}
