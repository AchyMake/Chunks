package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityMountEvent;

public class PlayerMount implements Listener {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    private Message getMessage() {
        return Chunks.getMessage();
    }
    public PlayerMount(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMount(EntityMountEvent event) {
        if (!event.getEntity().getType().equals(EntityType.PLAYER))return;
        if (event.getMount().getType().equals(EntityType.ARMOR_STAND))return;
        if (getChunkStorage().isProtected(event.getMount().getLocation().getChunk())) {
            if (getChunkStorage().hasAccess((Player) event.getEntity(), event.getMount().getLocation().getChunk()))return;
            event.setCancelled(true);
            getMessage().sendActionBar((Player) event.getEntity(), "&cChunk is protected by&f Server");
        }
        if (getChunkStorage().isClaimed(event.getMount().getLocation().getChunk())) {
            if (getChunkStorage().hasAccess((Player) event.getEntity(), event.getMount().getLocation().getChunk()))return;
            event.setCancelled(true);
            getMessage().sendActionBar((Player) event.getEntity(), "&cChunk is owned by&f " + getChunkStorage().getOwner(event.getMount().getLocation().getChunk()).getName());
        }
    }
}