package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntity implements Listener {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    private final Message message = Chunks.getMessage();
    public PlayerInteractEntity(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (chunkStorage.isProtected(event.getRightClicked().getLocation().getChunk())) {
            if (event.getRightClicked().getType().equals(EntityType.PLAYER))return;
            if (event.getRightClicked().getType().equals(EntityType.MINECART))return;
            if (event.getRightClicked().getType().equals(EntityType.BOAT))return;
            if (event.getRightClicked().isInvulnerable())return;
            if (chunkStorage.hasAccess(event.getPlayer(), event.getRightClicked().getLocation().getChunk()))return;
            if (Chunks.getInstance().getConfig().getBoolean("is-hostile." + event.getRightClicked().getType()))return;
            event.setCancelled(true);
            message.sendActionBar(event.getPlayer(), "&cChunk is protected by&f Server");
        }
        if (chunkStorage.isClaimed(event.getRightClicked().getLocation().getChunk())) {
            if (event.getRightClicked().getType().equals(EntityType.PLAYER))return;
            if (event.getRightClicked().getType().equals(EntityType.MINECART))return;
            if (event.getRightClicked().getType().equals(EntityType.BOAT))return;
            if (event.getRightClicked().isInvulnerable())return;
            if (chunkStorage.hasAccess(event.getPlayer(), event.getRightClicked().getLocation().getChunk()))return;
            if (Chunks.getInstance().getConfig().getBoolean("is-hostile." + event.getRightClicked().getType()))return;
            event.setCancelled(true);
            message.sendActionBar(event.getPlayer(), "&cChunk is owned by&f " + chunkStorage.getOwner(event.getRightClicked().getLocation().getChunk()).getName());
        }
    }
}