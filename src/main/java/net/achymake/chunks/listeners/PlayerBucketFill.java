package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class PlayerBucketFill implements Listener {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    public PlayerBucketFill(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        if (getChunkStorage().isProtected(event.getBlockClicked().getChunk())) {
            if (getChunkStorage().hasAccess(event.getPlayer(), event.getBlockClicked().getChunk()))return;
            event.setCancelled(true);
            Chunks.sendActionBar(event.getPlayer(), "&cChunk is protected by&f Server");
        }
        if (getChunkStorage().isClaimed(event.getBlockClicked().getChunk())) {
            if (getChunkStorage().hasAccess(event.getPlayer(), event.getBlockClicked().getChunk()))return;
            event.setCancelled(true);
            Chunks.sendActionBar(event.getPlayer(), "&cChunk is owned by&f " + getChunkStorage().getOwner(event.getBlockClicked().getChunk()).getName());
        }
    }
}