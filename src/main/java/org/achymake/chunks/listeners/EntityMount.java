package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
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
    private final ChunkStorage chunkStorage;
    private final Message message;
    public EntityMount(Chunks plugin) {
        chunkStorage = plugin.getChunkStorage();
        message = plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityMount(EntityMountEvent event) {
        if (!(event.getEntity() instanceof Player player))return;
        if (event.getMount() instanceof ArmorStand)return;
        if (event.getMount() instanceof Boat)return;
        if (event.getMount() instanceof Minecart)return;
        Chunk chunk = event.getMount().getLocation().getChunk();
        if (!chunkStorage.isClaimed(chunk))return;
        if (chunkStorage.hasAccess(player, chunk))return;
        event.setCancelled(true);
        message.send(player, "&c&lHey!&7 Sorry, chunk is owned by&f " + chunkStorage.getOwner(chunk).getName());
    }
}