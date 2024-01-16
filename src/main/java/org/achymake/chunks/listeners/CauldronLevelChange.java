package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.Database;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CauldronLevelChangeEvent;

public class CauldronLevelChange implements Listener {
    private Database getDatabase() {
        return Chunks.getDatabase();
    }
    public CauldronLevelChange(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onCauldronLevelChangeProtected(CauldronLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            Chunk chunk = event.getBlock().getChunk();
            if (getDatabase().isProtected(chunk)) {
                if (getDatabase().hasAccess(player, chunk))return;
                event.setCancelled(true);
                Chunks.sendActionBar(player, "&cChunk is protected by&f Server");
            } else if (getDatabase().isClaimed(chunk)) {
                if (getDatabase().hasAccess(player, chunk))return;
                event.setCancelled(true);
                Chunks.sendActionBar(player, "&cChunk is owned by&f " + getDatabase().getOwner(chunk).getName());
            }
        }
    }
}
