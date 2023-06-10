package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CauldronLevelChangeEvent;

public class CauldronLevelChange implements Listener {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    private final Message message = Chunks.getMessage();
    public CauldronLevelChange(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCauldronLevelChange(CauldronLevelChangeEvent event) {
        if (chunkStorage.isProtected(event.getBlock().getChunk())) {
            if (chunkStorage.hasAccess((Player) event.getEntity(), event.getBlock().getChunk()))return;
            event.setCancelled(true);
            message.sendActionBar((Player) event.getEntity(), "&cChunk is protected by&f Server");
        }
        if (chunkStorage.isClaimed(event.getBlock().getChunk())) {
            if (chunkStorage.hasAccess((Player) event.getEntity(), event.getBlock().getChunk()))return;
            event.setCancelled(true);
            message.sendActionBar((Player) event.getEntity(), "&cChunk is owned by&f " + chunkStorage.getOwner(event.getBlock().getChunk()).getName());
        }
    }
}