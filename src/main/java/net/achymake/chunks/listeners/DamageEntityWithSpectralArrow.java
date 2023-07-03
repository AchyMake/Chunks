package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageEntityWithSpectralArrow implements Listener {
    private FileConfiguration getConfig() {
        return Chunks.getInstance().getConfig();
    }
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    public DamageEntityWithSpectralArrow(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamageEntityWithSpectralArrow(EntityDamageByEntityEvent event) {
        if (!event.getDamager().getType().equals(EntityType.SPECTRAL_ARROW))return;
        if (getChunkStorage().isProtected(event.getEntity().getLocation().getChunk())) {
            SpectralArrow damager = (SpectralArrow) event.getDamager();
            if (damager.getShooter() instanceof Player) {
                if (getChunkStorage().hasAccess((Player) damager.getShooter(), event.getEntity().getLocation().getChunk()))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (getConfig().getBoolean("is-hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                if (damager.getShooter() == null)return;
                Chunks.sendActionBar((Player) damager.getShooter(), "&cChunk is protected by&f Server");
            }
        }
        if (getChunkStorage().isClaimed(event.getEntity().getLocation().getChunk())) {
            SpectralArrow damager = (SpectralArrow) event.getDamager();
            if (damager.getShooter() instanceof Player) {
                if (getChunkStorage().hasAccess((Player) damager.getShooter(), event.getEntity().getLocation().getChunk()))return;
                if (event.getEntity().isInvulnerable())return;
                if (event.getEntity().getType().equals(EntityType.PLAYER))return;
                if (getConfig().getBoolean("is-hostile." + event.getEntity().getType()))return;
                event.setCancelled(true);
                if (damager.getShooter() == null)return;
                Chunks.sendActionBar((Player) damager.getShooter(), "&cChunk is claimed by&f " + getChunkStorage().getOwner(event.getEntity().getLocation().getChunk()).getName());
            }
        }
    }
}