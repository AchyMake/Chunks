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
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTarget implements Listener {
    private final FileConfiguration config;
    private final ChunkStorage chunkStorage;
    public EntityTarget(Chunks plugin) {
        config = plugin.getConfig();
        chunkStorage = plugin.getChunkStorage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player player))return;
        Entity entity = event.getEntity();
        Chunk chunk = entity.getLocation().getChunk();
        if (!chunkStorage.isClaimed(chunk))return;
        if (chunkStorage.hasAccess(player, chunk))return;
        if (config.getBoolean("hostile." + entity.getType()))return;
        event.setCancelled(true);
    }
}
