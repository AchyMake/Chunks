package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;

import java.util.ArrayList;
import java.util.List;

public record BlockFertilize(Chunks plugin) implements Listener {
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockFertilize(BlockFertilizeEvent event) {
        if (event.getPlayer() == null)return;
        if (!getChunkdata().disableBlockFertilize())return;
        List<BlockState> blockList = new ArrayList<>();
        Player player = event.getPlayer();
        for (BlockState block : event.getBlocks()) {
            Chunk chunk = block.getChunk();
            if (!getChunkdata().isClaimed(chunk))return;
            if (getChunkdata().hasAccess(player, chunk))return;
            blockList.add(block);
        }
        event.getBlocks().removeAll(blockList);
        getMessage().sendActionBar(player, "&cChunk is owned by&f " + getChunkdata().getOwner(event.getBlock().getChunk()).getName());
    }
}