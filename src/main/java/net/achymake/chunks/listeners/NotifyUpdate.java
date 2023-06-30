package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.version.UpdateChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NotifyUpdate implements Listener {
    public NotifyUpdate(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onNotifyUpdate(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("chunks.command.chunks.reload"))return;
        new UpdateChecker().sendMessage(event.getPlayer());
    }
}