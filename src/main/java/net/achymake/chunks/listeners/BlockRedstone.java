package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockRedstone implements Listener {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    private final Chunks chunks = Chunks.getInstance();
    public BlockRedstone(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockRedstone(BlockRedstoneEvent event) {
        if (!chunks.getConfig().getBoolean("claim.redstone-only-inside"))return;
        if (chunkStorage.isClaimed(event.getBlock().getChunk()))return;
        event.setNewCurrent(0);
    }
}