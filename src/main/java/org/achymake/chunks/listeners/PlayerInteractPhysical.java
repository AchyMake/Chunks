package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.ChunkStorage;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractPhysical implements Listener {
    private final Chunks plugin;
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    public PlayerInteractPhysical(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractPhysical(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.PHYSICAL))return;
        if (event.getClickedBlock() == null)return;
        Chunk chunk = event.getClickedBlock().getChunk();
        if (!getChunkStorage().isClaimed(chunk))return;
        Player player = event.getPlayer();
        if (getChunkStorage().hasAccess(player, chunk))return;
        if (!isPhysical(event.getClickedBlock()))return;
        event.setCancelled(true);
    }
    private boolean isPhysical(Block block) {
        return block.getType().equals(Material.FARMLAND) || block.getType().equals(Material.TURTLE_EGG) || Tag.PRESSURE_PLATES.isTagged(block.getType());
    }
}
