package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.Database;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;

public class BlockFertilize implements Listener {
    private final Chunks plugin;
    private Database getDatabase() {
        return plugin.getDatabase();
    }
    public BlockFertilize(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockFertilize(BlockFertilizeEvent event) {
        if (event.getPlayer() == null)return;
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getChunk();
        if (getDatabase().isProtected(chunk)) {
            if (getDatabase().hasAccess(player, chunk))return;
            event.setCancelled(true);
            plugin.sendActionBar(player, "&cChunk is protected by&f Server");
        } else if (getDatabase().isClaimed(chunk)) {
            if (getDatabase().hasAccess(event.getPlayer(), event.getBlock().getChunk()))return;
            event.setCancelled(true);
            plugin.sendActionBar(event.getPlayer(), "&cChunk is owned by&f " + getDatabase().getOwner(event.getBlock().getChunk()).getName());
        }
    }
}
