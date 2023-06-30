package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEntityEvent;

public class PlayerBucketEntity implements Listener {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    private Message getMessage() {
        return Chunks.getMessage();
    }
    public PlayerBucketEntity(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBucketEntity(PlayerBucketEntityEvent event) {
        if (getChunkStorage().isProtected(event.getEntity().getLocation().getChunk())) {
            if (getChunkStorage().hasAccess(event.getPlayer(), event.getEntity().getLocation().getChunk()))return;
            event.setCancelled(true);
            getMessage().sendActionBar(event.getPlayer(), "&cChunk is protected by&f Server");
        }
        if (getChunkStorage().isClaimed(event.getEntity().getLocation().getChunk())) {
            if (getChunkStorage().hasAccess(event.getPlayer(), event.getEntity().getLocation().getChunk()))return;
            event.setCancelled(true);
            getMessage().sendActionBar(event.getPlayer(), "&cChunk is owned by&f " + getChunkStorage().getOwner(event.getEntity().getLocation().getChunk()).getName());
        }
    }
}