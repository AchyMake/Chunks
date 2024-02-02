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

public class PlayerCommandPreprocess implements Listener {
    private final ChunkStorage chunkStorage;
    private final Message message;
    public PlayerCommandPreprocess(Chunks plugin) {
        chunkStorage = plugin.getChunkStorage();
        message = plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getPlayer().getLocation().getChunk();
        if (!chunkStorage.isClaimed(chunk))return;
        if (!event.getMessage().startsWith("/sethome"))return;
        if (chunkStorage.hasAccess(player, chunk))return;
        event.setCancelled(true);
        message.send(player, "&c&lHey!&7 Sorry, but you can't&f sethome&7 inside&f " + chunkStorage.getOwner(chunk).getName() + "&7's chunk");
    }
}
