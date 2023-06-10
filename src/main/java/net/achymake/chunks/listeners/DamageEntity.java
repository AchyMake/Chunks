package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageEntity implements Listener {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    private final Message message = Chunks.getMessage();
    private final FileConfiguration config = Chunks.getInstance().getConfig();
    public DamageEntity(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        if (!event.getDamager().getType().equals(EntityType.PLAYER))return;
        if (chunkStorage.isProtected(event.getEntity().getLocation().getChunk())) {
            if (chunkStorage.hasAccess((Player) event.getDamager(), event.getEntity().getLocation().getChunk()))return;
            if (event.getEntity().isInvulnerable())return;
            if (event.getEntity().getType().equals(EntityType.PLAYER))return;
            if (config.getBoolean("is-hostile." + event.getEntity().getType()))return;
            event.setCancelled(true);
            message.sendActionBar((Player) event.getDamager(), "&cChunk is protected by&f Server");
        }
        if (chunkStorage.isClaimed(event.getEntity().getLocation().getChunk())) {
            if (chunkStorage.hasAccess((Player) event.getDamager(), event.getEntity().getLocation().getChunk()))return;
            if (event.getEntity().isInvulnerable())return;
            if (event.getEntity().getType().equals(EntityType.PLAYER))return;
            if (config.getBoolean("is-hostile." + event.getEntity().getType()))return;
            event.setCancelled(true);
            message.sendActionBar((Player) event.getDamager(), "&cChunk is owned by&f " + chunkStorage.getOwner(event.getEntity().getLocation().getChunk()).getName());
        }
    }
}