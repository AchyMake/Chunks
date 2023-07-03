package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntity implements Listener {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    public PlayerInteractEntity(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (getChunkStorage().isProtected(event.getRightClicked().getLocation().getChunk())) {
            if (event.getRightClicked().getType().equals(EntityType.PLAYER))return;
            if (event.getRightClicked().getType().equals(EntityType.MINECART))return;
            if (event.getRightClicked().getType().equals(EntityType.BOAT))return;
            if (event.getRightClicked().isInvulnerable())return;
            if (getChunkStorage().hasAccess(event.getPlayer(), event.getRightClicked().getLocation().getChunk()))return;
            if (Chunks.getInstance().getConfig().getBoolean("is-hostile." + event.getRightClicked().getType()))return;
            event.setCancelled(true);
            Chunks.sendActionBar(event.getPlayer(), "&cChunk is protected by&f Server");
        }
        if (getChunkStorage().isClaimed(event.getRightClicked().getLocation().getChunk())) {
            if (event.getRightClicked().getType().equals(EntityType.PLAYER))return;
            if (event.getRightClicked().getType().equals(EntityType.MINECART))return;
            if (event.getRightClicked().getType().equals(EntityType.BOAT))return;
            if (event.getRightClicked().isInvulnerable())return;
            if (getChunkStorage().hasAccess(event.getPlayer(), event.getRightClicked().getLocation().getChunk()))return;
            if (Chunks.getInstance().getConfig().getBoolean("is-hostile." + event.getRightClicked().getType()))return;
            event.setCancelled(true);
            Chunks.sendActionBar(event.getPlayer(), "&cChunk is owned by&f " + getChunkStorage().getOwner(event.getRightClicked().getLocation().getChunk()).getName());
        }
    }
}