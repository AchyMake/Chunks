package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocess implements Listener {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    private Message getMessage() {
        return Chunks.getMessage();
    }
    public PlayerCommandPreprocess (Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess (PlayerCommandPreprocessEvent event) {
        if (!getChunkStorage().isClaimed(event.getPlayer().getLocation().getChunk()))return;
        if (!event.getMessage().startsWith("/sethome"))return;
        if (getChunkStorage().hasAccess(event.getPlayer(), event.getPlayer().getLocation().getChunk()))return;
        event.setCancelled(true);
        getMessage().send(event.getPlayer(), "&cYou can't&f sethome&c inside&f " + getChunkStorage().getOwner(event.getPlayer().getLocation().getChunk()).getName() + "&c's Chunk");
    }
}