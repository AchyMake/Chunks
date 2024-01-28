package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.ChunkStorage;
import org.achymake.chunks.files.Message;
import org.bukkit.Chunk;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityMountEvent;

public class EntityMount implements Listener {
    private final Chunks plugin;
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public EntityMount(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityMount(EntityMountEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getMount() instanceof ArmorStand)return;
            if (event.getMount() instanceof Boat)return;
            if (event.getMount() instanceof Minecart)return;
            Chunk chunk = event.getMount().getLocation().getChunk();
            if (!getChunkStorage().isClaimed(chunk))return;
            if (getChunkStorage().hasAccess(player, chunk))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, "&cError:&7 Chunk owned by&f " + getChunkStorage().getOwner(chunk).getName());
        }
    }
}
