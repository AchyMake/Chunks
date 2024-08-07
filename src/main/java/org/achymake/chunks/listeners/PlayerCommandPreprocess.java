package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public record PlayerCommandPreprocess(Chunks plugin) implements Listener {
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = player.getLocation().getChunk();
        if (!getChunkdata().isClaimed(chunk))return;
        if (!event.getMessage().toLowerCase().startsWith("/sethome"))return;
        if (getChunkdata().hasAccess(player, chunk))return;
        event.setCancelled(true);
        getMessage().send(player, "&cYou are not allowed to sethome inside&f " + getChunkdata().getOwner(chunk).getName() + "&c's chunk");
    }
}