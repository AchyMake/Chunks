package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.text.MessageFormat;

public record BlockPlace(Chunks plugin) implements Listener {
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        Chunk chunk = event.getBlock().getChunk();
        if (!getChunkStorage().isClaimed(chunk))return;
        Player player = event.getPlayer();
        if (getChunkStorage().hasAccess(player, chunk))return;
        event.setCancelled(true);
        player.sendMessage(MessageFormat.format(getMessage().getString("events.block-place"), getChunkStorage().getOwner(chunk).getName()));
    }
}
