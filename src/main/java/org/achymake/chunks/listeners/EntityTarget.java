package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.ChunkStorage;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTarget implements Listener {
    private final Chunks plugin;
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    public EntityTarget(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() == null)return;
        if (event.getTarget() instanceof Player player) {
            Entity entity = event.getEntity();
            Chunk chunk = entity.getLocation().getChunk();
            if (!getChunkStorage().isClaimed(chunk))return;
            if (getChunkStorage().hasAccess(player, chunk))return;
            if (getConfig().getBoolean("hostile." + entity.getType()))return;
            event.setCancelled(true);
        }
    }
}
