package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    private Message getMessage() {
        return Chunks.getMessage();
    }
    public BlockPlace(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (getChunkStorage().isProtected(event.getBlock().getChunk())) {
            if (getChunkStorage().hasAccess(event.getPlayer(), event.getBlock().getChunk()))return;
            event.setCancelled(true);
            getMessage().sendActionBar(event.getPlayer(), "&cChunk is protected by&f Server");
        }
        if (getChunkStorage().isClaimed(event.getBlock().getChunk())) {
            if (getChunkStorage().hasAccess(event.getPlayer(), event.getBlock().getChunk()))return;
            event.setCancelled(true);
            getMessage().sendActionBar(event.getPlayer(), "&cChunk is owned by&f " + getChunkStorage().getOwner(event.getBlock().getChunk()).getName());
        }
    }
}