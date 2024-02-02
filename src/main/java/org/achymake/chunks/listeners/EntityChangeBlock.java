package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class EntityChangeBlock implements Listener {
    private final FileConfiguration config;
    private final ChunkStorage chunkStorage;
    public EntityChangeBlock(Chunks plugin) {
        config = plugin.getConfig();
        chunkStorage = plugin.getChunkStorage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Entity entity = event.getEntity();
        Chunk chunk = event.getBlock().getChunk();
        if (!chunkStorage.isClaimed(chunk)) return;
        if (entity instanceof Player player) {
            if (chunkStorage.hasAccess(player, chunk))return;
            event.setCancelled(true);
        } else if (config.getBoolean("hostile." + entity.getType())) {
            event.setCancelled(true);
        }
    }
}
