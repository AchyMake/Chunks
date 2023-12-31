package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
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
    public void onCauldronLevelChange(CauldronLevelChangeEvent event) {
        if (event.getEntity() == null)return;
        if (getChunkStorage().isProtected(event.getBlock().getChunk())) {
            if (getChunkStorage().hasAccess((Player) event.getEntity(), event.getBlock().getChunk()))return;
            event.setCancelled(true);
            Chunks.sendActionBar((Player) event.getEntity(), "&cChunk is protected by&f Server");
        }
        if (getChunkStorage().isClaimed(event.getBlock().getChunk())) {
            if (getChunkStorage().hasAccess((Player) event.getEntity(), event.getBlock().getChunk()))return;
            event.setCancelled(true);
            Chunks.sendActionBar((Player) event.getEntity(), "&cChunk is owned by&f " + getChunkStorage().getOwner(event.getBlock().getChunk()).getName());
        }
    }
}