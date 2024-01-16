package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.ChunkStorage;
import org.bukkit.Chunk;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityMountEvent;

public class EntityMount implements Listener {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    public EntityMount(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityMount(EntityMountEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getMount() instanceof ArmorStand)return;
            Chunk chunk = event.getMount().getLocation().getChunk();
            if (getChunkStorage().isProtected(chunk)) {
                if (getChunkStorage().hasAccess(player, chunk))return;
                event.setCancelled(true);
                Chunks.sendActionBar(player, "&cChunk is protected by&f Server");
            } else if (getChunkStorage().isClaimed(chunk)) {
                if (getChunkStorage().hasAccess(player, chunk))return;
                event.setCancelled(true);
                Chunks.sendActionBar(player, "&cChunk is owned by&f " + getChunkStorage().getOwner(chunk).getName());
            }
        }
    }
}
