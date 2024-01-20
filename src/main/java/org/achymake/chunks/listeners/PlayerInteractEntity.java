package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.ChunkStorage;
import org.achymake.chunks.files.Message;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntity implements Listener {
    private final Chunks plugin;
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public PlayerInteractEntity(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getRightClicked().getLocation().getChunk();
        Entity entity = event.getRightClicked();
        if (getChunkStorage().isProtected(chunk)) {
            if (entity.getType().equals(EntityType.PLAYER))return;
            if (entity.getType().equals(EntityType.MINECART))return;
            if (entity.getType().equals(EntityType.BOAT))return;
            if (entity.isInvulnerable())return;
            if (getChunkStorage().hasAccess(player, chunk))return;
            if (getConfig().getBoolean("hostile." + entity.getType()))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, "&cError:&7 Chunk protected by&f Server");
        } else if (getChunkStorage().isClaimed(chunk)) {
            if (entity.getType().equals(EntityType.PLAYER))return;
            if (entity.getType().equals(EntityType.MINECART))return;
            if (entity.getType().equals(EntityType.BOAT))return;
            if (entity.isInvulnerable())return;
            if (getChunkStorage().hasAccess(player, chunk))return;
            if (getConfig().getBoolean("hostile." + entity.getType()))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, "&cError:&7 Chunk owned by&f " + getChunkStorage().getOwner(chunk).getName());
        }
    }
}
