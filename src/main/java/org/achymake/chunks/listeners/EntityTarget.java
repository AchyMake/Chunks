package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.Database;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTarget implements Listener {
    private FileConfiguration getConfig() {
        return Chunks.getInstance().getConfig();
    }
    private Database getDatabase() {
        return Chunks.getDatabase();
    }
    public EntityTarget(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() == null)return;
        if (event.getTarget() instanceof Player player) {
            Chunk chunk = event.getEntity().getLocation().getChunk();
            if (getDatabase().isProtected(chunk)) {
                if (getDatabase().hasAccess(player, chunk))return;
                if (getConfig().getBoolean("hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
            } else if (getDatabase().isClaimed(chunk)) {
                if (getDatabase().hasAccess(player, chunk))return;
                if (getConfig().getBoolean("hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
            }
        }
    }
}
