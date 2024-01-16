package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.ChunkStorage;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityChangeBlock implements Listener {
    private FileConfiguration getConfig() {
        return Chunks.getInstance().getConfig();
    }
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    public EntityChangeBlock(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Chunk chunk = event.getBlock().getChunk();
        if (getChunkStorage().isProtected(chunk)) {
            if (event.getEntity() instanceof Player player) {
                if (getChunkStorage().hasAccess(player, chunk))return;
                event.setCancelled(true);
            } else {
                event.setCancelled(true);
            }
            event.setCancelled(true);
        } else if (getChunkStorage().isClaimed(chunk)) {
            if (event.getEntity() instanceof Player player) {
                if (getChunkStorage().hasAccess(player, chunk))return;
                event.setCancelled(true);
            } else if (getConfig().getBoolean("hostile." + event.getEntity().getType())) {
                event.setCancelled(true);
            }
        }
    }
}
