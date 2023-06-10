package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageEntityWithSnowball implements Listener {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    private final Message message = Chunks.getMessage();
    private final FileConfiguration config = Chunks.getInstance().getConfig();
    public DamageEntityWithSnowball(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageEntityWithSnowball(EntityDamageByEntityEvent event) {
        if (!event.getDamager().getType().equals(EntityType.SNOWBALL))return;
        if (chunkStorage.isProtected(event.getEntity().getLocation().getChunk())) {
            Snowball damager = (Snowball) event.getDamager();
            if (damager.getShooter() instanceof Player) {
                if (chunkStorage.hasAccess((Player) damager.getShooter(), event.getEntity().getLocation().getChunk()))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (config.getBoolean("is-hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                if (damager.getShooter() == null)return;
                message.sendActionBar((Player) damager.getShooter(), "&cChunk is protected by&f Server");
            }
        }
        if (chunkStorage.isClaimed(event.getEntity().getLocation().getChunk())) {
            Snowball damager = (Snowball) event.getDamager();
            if (damager.getShooter() instanceof Player) {
                if (chunkStorage.hasAccess((Player) damager.getShooter(), event.getEntity().getLocation().getChunk()))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (config.getBoolean("is-hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                if (damager.getShooter() == null)return;
                message.sendActionBar((Player) damager.getShooter(), "&cChunk is claimed by&f " + chunkStorage.getOwner(event.getEntity().getLocation().getChunk()).getName());
            }
        }
    }
}