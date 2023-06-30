package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityTarget implements Listener {
    private FileConfiguration getConfig() {
        return Chunks.getInstance().getConfig();
    }
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    public EntityTarget(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() == null)return;
        if (!event.getTarget().getType().equals(EntityType.PLAYER))return;
        if (getChunkStorage().isProtected(event.getEntity().getLocation().getChunk())) {
            if (getChunkStorage().hasAccess((Player) event.getTarget(), event.getEntity().getLocation().getChunk()))return;
            if (getConfig().getBoolean("is-hostile." + event.getEntity().getType()))return;
            event.setCancelled(true);
        }
        if (getChunkStorage().isClaimed(event.getEntity().getLocation().getChunk())) {
            if (getChunkStorage().hasAccess((Player) event.getTarget(), event.getEntity().getLocation().getChunk()))return;
            if (getConfig().getBoolean("is-hostile." + event.getEntity().getType()))return;
            event.setCancelled(true);
        }
    }
}