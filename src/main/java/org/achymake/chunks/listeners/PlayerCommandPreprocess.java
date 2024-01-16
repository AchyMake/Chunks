package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.Database;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocess implements Listener {
    private final Chunks plugin;
    private Database getDatabase() {
        return plugin.getDatabase();
    }
    public PlayerCommandPreprocess(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getPlayer().getLocation().getChunk();
        if (!getDatabase().isClaimed(chunk))return;
        if (!event.getMessage().startsWith("/sethome"))return;
        if (getDatabase().hasAccess(player, chunk))return;
        event.setCancelled(true);
        plugin.send(player, "&cYou can't&f sethome&c inside&f " + getDatabase().getOwner(chunk).getName() + "&c's Chunk");
    }
}
