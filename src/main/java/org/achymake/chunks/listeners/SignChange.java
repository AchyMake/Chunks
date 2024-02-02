package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public record SignChange(Chunks plugin) implements Listener {
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        if (!getChunkStorage().isClaimed(chunk))return;
        if (getChunkStorage().hasAccess(player, chunk))return;
        event.setCancelled(true);
        String owner = getChunkStorage().getOwner(chunk).getName();
        getMessage().send(player, "&c&lHey!&7 Sorry, chunk is owned by&f " + owner);
    }
}
