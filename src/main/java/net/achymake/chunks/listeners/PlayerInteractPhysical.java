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
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    public PlayerInteractPhysical(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractPhysical(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.PHYSICAL))return;
        if (event.getClickedBlock() == null)return;
        if (getChunkStorage().isProtected(event.getClickedBlock().getChunk())) {
            if (getChunkStorage().hasAccess(event.getPlayer(), event.getClickedBlock().getChunk()))return;
            if (!physical(event.getClickedBlock()))return;
            event.setCancelled(true);
        }
        if (getChunkStorage().isClaimed(event.getClickedBlock().getChunk())) {
            if (getChunkStorage().hasAccess(event.getPlayer(), event.getClickedBlock().getChunk()))return;
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