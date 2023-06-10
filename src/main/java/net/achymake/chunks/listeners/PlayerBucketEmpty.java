package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class PlayerBucketEmpty implements Listener {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    private final Message message = Chunks.getMessage();
    public PlayerBucketEmpty(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (chunkStorage.isProtected(event.getBlockClicked().getChunk())) {
            if (chunkStorage.hasAccess(event.getPlayer(), event.getBlockClicked().getChunk()))return;
            event.setCancelled(true);
            message.sendActionBar(event.getPlayer(), "&cChunk is protected by&f Server");
        }
        if (chunkStorage.isClaimed(event.getBlockClicked().getChunk())) {
            if (chunkStorage.hasAccess(event.getPlayer(), event.getBlockClicked().getChunk()))return;
            event.setCancelled(true);
            message.sendActionBar(event.getPlayer(), "&cChunk is owned by&f " + chunkStorage.getOwner(event.getBlockClicked().getChunk()).getName());
        }
    }
}