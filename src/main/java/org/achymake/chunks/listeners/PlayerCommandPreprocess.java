package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.Database;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocess implements Listener {
    private Database getDatabase() {
        return Chunks.getDatabase();
    }
    public PlayerCommandPreprocess(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!getDatabase().isClaimed(event.getPlayer().getLocation().getChunk()))return;
        if (!event.getMessage().startsWith("/sethome"))return;
        if (getDatabase().hasAccess(event.getPlayer(), event.getPlayer().getLocation().getChunk()))return;
        event.setCancelled(true);
        Chunks.send(event.getPlayer(), "&cYou can't&f sethome&c inside&f " + getDatabase().getOwner(event.getPlayer().getLocation().getChunk()).getName() + "&c's Chunk");
    }
}
