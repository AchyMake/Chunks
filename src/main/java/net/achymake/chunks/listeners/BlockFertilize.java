package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;

public class BlockFertilize implements Listener {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    public BlockFertilize(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockFertilize(BlockFertilizeEvent event) {
        if (event.getPlayer() == null)return;
        if (getChunkStorage().isProtected(event.getBlock().getChunk())) {
            if (getChunkStorage().hasAccess(event.getPlayer(), event.getBlock().getChunk()))return;
            event.setCancelled(true);
            Chunks.sendActionBar(event.getPlayer(), "&cChunk is protected by&f Server");
        }
        if (getChunkStorage().isClaimed(event.getBlock().getChunk())) {
            if (getChunkStorage().hasAccess(event.getPlayer(), event.getBlock().getChunk()))return;
            event.setCancelled(true);
            Chunks.sendActionBar(event.getPlayer(), "&cChunk is owned by&f " + getChunkStorage().getOwner(event.getBlock().getChunk()).getName());
        }
    }
}