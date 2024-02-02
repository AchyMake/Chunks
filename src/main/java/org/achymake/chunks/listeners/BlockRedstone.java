package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockRedstone implements Listener {
    private final FileConfiguration config;
    private final ChunkStorage chunkStorage;
    public BlockRedstone(Chunks plugin) {
        config = plugin.getConfig();
        chunkStorage = plugin.getChunkStorage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockRedstone(BlockRedstoneEvent event) {
        if (!config.getBoolean("claim.redstone-only-inside"))return;
        if (chunkStorage.isClaimed(event.getBlock().getChunk()))return;
        event.setNewCurrent(0);
    }
}
