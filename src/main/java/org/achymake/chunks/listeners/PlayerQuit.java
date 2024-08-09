package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Userdata;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public record PlayerQuit(Chunks plugin) implements Listener {
    private List<Player> getChunkEditors() {
        return plugin.getChunkEditors();
    }
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (getUserdata().hasTaskID(player, "effect")) {
            if (getServer().getScheduler().isCurrentlyRunning(getUserdata().getTaskID(player, "effect"))) {
                getServer().getScheduler().cancelTask(getUserdata().getTaskID(player, "effect"));
            }
            getUserdata().removeTaskID(player, "effect");
            getUserdata().setString(player, "counter", null);
        }
        if (getChunkEditors().contains(player)) {
            getChunkEditors().remove(player);
        }
    }
}