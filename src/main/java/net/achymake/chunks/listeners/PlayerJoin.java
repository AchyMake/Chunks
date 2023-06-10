package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.version.UpdateChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    public PlayerJoin(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Chunks.getDatabase().setup(event.getPlayer());
        if (!event.getPlayer().hasPermission("chunks.command.chunks.reload"))return;
        new UpdateChecker(Chunks.getInstance(), 108772).sendMessage(event.getPlayer());
    }
}