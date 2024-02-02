package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEntityEvent;

public class PlayerBucketEntity implements Listener {
    private final ChunkStorage chunkStorage;
    private final Message message;
    public PlayerBucketEntity(Chunks plugin) {
        chunkStorage = plugin.getChunkStorage();
        message = plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBucketEntity(PlayerBucketEntityEvent event) {
        Chunk chunk = event.getEntity().getLocation().getChunk();
        if (!chunkStorage.isClaimed(chunk))return;
        Player player = event.getPlayer();
        if (chunkStorage.hasAccess(player, chunk))return;
        event.setCancelled(true);
        message.send(player, "&c&lHey!&7 Sorry, chunk is owned by&f " + chunkStorage.getOwner(chunk).getName());
    }
}
