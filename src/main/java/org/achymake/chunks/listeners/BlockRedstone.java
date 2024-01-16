package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.Database;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockRedstone implements Listener {
    private final Chunks plugin;
    private FileConfiguration getConfig() {
        return Chunks.getInstance().getConfig();
    }
    private Database getDatabase() {
        return plugin.getDatabase();
    }
    public BlockRedstone(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockRedstone(BlockRedstoneEvent event) {
        if (!getConfig().getBoolean("claim.redstone-only-inside"))return;
        if (getDatabase().isClaimed(event.getBlock().getChunk()))return;
        event.setNewCurrent(0);
    }
}
