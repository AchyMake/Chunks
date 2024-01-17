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
    private final Chunks plugin;
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    public CauldronLevelChange(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCauldronLevelChangeProtected(CauldronLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            Chunk chunk = event.getBlock().getChunk();
            if (getChunkStorage().isProtected(chunk)) {
                if (getChunkStorage().hasAccess(player, chunk))return;
                event.setCancelled(true);
                plugin.sendActionBar(player, "&cChunk is protected by&f Server");
            } else if (getChunkStorage().isClaimed(chunk)) {
                if (getChunkStorage().hasAccess(player, chunk))return;
                event.setCancelled(true);
                plugin.sendActionBar(player, "&cChunk is owned by&f " + getChunkStorage().getOwner(chunk).getName());
            }
        }
    }
}
