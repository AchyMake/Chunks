package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.Database;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode implements Listener {
    private Database getDatabase() {
        return Chunks.getDatabase();
    }
    public EntityExplode(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (getDatabase().isProtected(event.getEntity().getLocation().getChunk())) {
            if (event.blockList().isEmpty())return;
            event.blockList().clear();
        } else if (getDatabase().isClaimed(event.getEntity().getLocation().getChunk())) {
            if (event.getEntity().getType().equals(EntityType.MINECART_TNT)) {
                if (!getDatabase().isClaimed(event.getEntity().getLocation().getChunk()))return;
                if (getDatabase().TNTAllowed(event.getLocation().getChunk()))return;
                if (event.blockList().isEmpty())return;
                event.blockList().clear();
            }
            if (event.getEntity().getType().equals(EntityType.PRIMED_TNT)) {
                if (!getDatabase().isClaimed(event.getEntity().getLocation().getChunk()))return;
                if (getDatabase().TNTAllowed(event.getLocation().getChunk()))return;
                if (event.blockList().isEmpty())return;
                event.blockList().clear();
            }
            if (event.getEntity().getType().equals(EntityType.CREEPER)) {
                if (event.blockList().isEmpty())return;
                event.blockList().clear();
            }
        }
    }
}
