package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.text.MessageFormat;

public record PlayerCommandPreprocess(Chunks plugin) implements Listener {
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = player.getLocation().getChunk();
        if (!getChunkStorage().isClaimed(chunk))return;
        if (!event.getMessage().startsWith("/sethome"))return;
        if (getChunkStorage().hasAccess(player, chunk))return;
        event.setCancelled(true);
        player.sendMessage(MessageFormat.format(getMessage().getString("events.player-command-preprocess"), getChunkStorage().getOwner(chunk).getName()));
    }
}