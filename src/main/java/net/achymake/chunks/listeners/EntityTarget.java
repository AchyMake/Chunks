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
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    private final FileConfiguration config = Chunks.getInstance().getConfig();
    public EntityTarget(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() == null)return;
        if (!event.getTarget().getType().equals(EntityType.PLAYER))return;
        if (chunkStorage.isProtected(event.getEntity().getLocation().getChunk())) {
            if (chunkStorage.hasAccess((Player) event.getTarget(), event.getEntity().getLocation().getChunk()))return;
            if (config.getBoolean("is-hostile." + event.getEntity().getType()))return;
            event.setCancelled(true);
        }
        if (chunkStorage.isClaimed(event.getEntity().getLocation().getChunk())) {
            if (chunkStorage.hasAccess((Player) event.getTarget(), event.getEntity().getLocation().getChunk()))return;
            if (config.getBoolean("is-hostile." + event.getEntity().getType()))return;
            event.setCancelled(true);
        }
    }
}