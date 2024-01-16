package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.ChunkStorage;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CauldronLevelChangeEvent;

public class CauldronLevelChange implements Listener {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    public CauldronLevelChange(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCauldronLevelChangeProtected(CauldronLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            Chunk chunk = event.getBlock().getChunk();
            if (getChunkStorage().isProtected(chunk)) {
                if (getChunkStorage().hasAccess(player, chunk))return;
                event.setCancelled(true);
                Chunks.sendActionBar(player, "&cChunk is protected by&f Server");
            } else if (getChunkStorage().isClaimed(chunk)) {
                if (getChunkStorage().hasAccess(player, event.getBlock().getChunk()))return;
                event.setCancelled(true);
                Chunks.sendActionBar(player, "&cChunk is owned by&f " + getChunkStorage().getOwner(event.getBlock().getChunk()).getName());
            }
        }
    }
}
