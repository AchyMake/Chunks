package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.ChunkStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawn implements Listener {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    public CreatureSpawn(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!getChunkStorage().isProtected(event.getEntity().getLocation().getChunk()))return;
        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM))return;
        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.COMMAND))return;
        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER_EGG))return;
        event.setCancelled(true);
    }
}
