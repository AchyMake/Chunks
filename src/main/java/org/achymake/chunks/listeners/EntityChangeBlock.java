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
    private final Chunks plugin;
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    public EntityChangeBlock(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Chunk chunk = event.getBlock().getChunk();
        if (!getChunkStorage().isClaimed(chunk)) return;
        if (event.getEntity() instanceof Player player) {
            if (getChunkStorage().hasAccess(player, chunk))return;
            event.setCancelled(true);
        } else if (getConfig().getBoolean("hostile." + event.getEntity().getType())) {
            event.setCancelled(true);
        }
    }
}
