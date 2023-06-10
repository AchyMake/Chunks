package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawn implements Listener {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    public CreatureSpawn(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (chunkStorage.isProtected(event.getEntity().getLocation().getChunk())) {
            if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM))return;
            if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.COMMAND))return;
            if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER_EGG))return;
            event.setCancelled(true);
        }
    }
}