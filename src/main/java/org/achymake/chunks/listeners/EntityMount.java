package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.EntityHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityMountEvent;
import org.bukkit.plugin.PluginManager;

public class EntityMount implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public EntityMount() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityMount(EntityMountEvent event) {
        var chunk = event.getMount().getLocation().getChunk();
        if (!getChunkHandler().isClaimed(chunk))return;
        if (event.getEntity() instanceof Player player) {
            if (getChunkHandler().hasAccess(chunk, player))return;
            if (event.getMount().getType().equals(getEntityHandler().getEntityType("armor_stand")))return;
            if (event.getMount().getType().equals(getEntityHandler().getEntityType("boat")))return;
            if (event.getMount().getType().equals(getEntityHandler().getEntityType("minecart")))return;
            event.setCancelled(true);
            getMessage().sendActionBar(player, getMessage().get("events.cancelled.claimed", getChunkHandler().getName(chunk)));
        }
    }
}