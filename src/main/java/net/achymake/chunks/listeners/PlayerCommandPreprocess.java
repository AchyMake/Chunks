package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocess implements Listener {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    private final Message message = Chunks.getMessage();
    public PlayerCommandPreprocess (Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess (PlayerCommandPreprocessEvent event) {
        if (!chunkStorage.isClaimed(event.getPlayer().getLocation().getChunk()))return;
        if (!event.getMessage().startsWith("/sethome"))return;
        if (chunkStorage.hasAccess(event.getPlayer(), event.getPlayer().getLocation().getChunk()))return;
        event.setCancelled(true);
        message.send(event.getPlayer(), "&cYou can't&f sethome&c inside&f " + chunkStorage.getOwner(event.getPlayer().getLocation().getChunk()).getName() + "&c's Chunk");
    }
}