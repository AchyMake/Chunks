package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NotifyUpdate implements Listener {
    private Chunks getPlugin() {
        return Chunks.getInstance();
    }
    public NotifyUpdate(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onNotifyUpdate(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("chunks.command.chunks.reload"))return;
        getPlugin().getUpdate(event.getPlayer());
    }
}