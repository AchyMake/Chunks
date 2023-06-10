package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractPhysical implements Listener {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    public PlayerInteractPhysical(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractPhysical(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.PHYSICAL))return;
        if (event.getClickedBlock() == null)return;
        if (chunkStorage.isProtected(event.getClickedBlock().getChunk())) {
            if (chunkStorage.hasAccess(event.getPlayer(), event.getClickedBlock().getChunk()))return;
            if (!physical(event.getClickedBlock()))return;
            event.setCancelled(true);
        }
        if (chunkStorage.isClaimed(event.getClickedBlock().getChunk())) {
            if (chunkStorage.hasAccess(event.getPlayer(), event.getClickedBlock().getChunk()))return;
            if (!physical(event.getClickedBlock()))return;
            event.setCancelled(true);
        }
    }
    private boolean physical(Block block) {
        if (Tag.PRESSURE_PLATES.isTagged(block.getType())) {
            return true;
        }
        if (block.getType().equals(Material.FARMLAND)) {
            return true;
        }
        if (block.getType().equals(Material.TURTLE_EGG)) {
            return true;
        }
        return false;
    }
}