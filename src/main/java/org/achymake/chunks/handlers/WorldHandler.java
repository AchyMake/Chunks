package org.achymake.chunks.handlers;

import org.achymake.chunks.Chunks;
import org.bukkit.Chunk;
import org.bukkit.World;

public class WorldHandler {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    public World get(String worldName) {
        return getInstance().getServer().getWorld(worldName);
    }
    public Chunk getChunk(World world, long chunkKey) {
        return world.getChunkAt((int) chunkKey, (int) (chunkKey >> 32));
    }
}