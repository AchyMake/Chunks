package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public record PlayerInteract(Chunks plugin) implements Listener {
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null)return;
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (getChunkdata().isDisabledInteractBlocks(block)) {
                if (event.getHand() != EquipmentSlot.HAND)return;
                Chunk chunk = block.getChunk();
                if (!getChunkdata().isClaimed(chunk))return;
                if (getChunkdata().hasAccess(player, chunk))return;
                event.setCancelled(true);
                getMessage().sendActionBar(player, "&cChunk is owned by&f " + getChunkdata().getOwner(chunk).getName());
            }
        } else if (event.getAction().equals(Action.PHYSICAL)) {
            Chunk chunk = block.getChunk();
            if (!getChunkdata().isClaimed(chunk))return;
            if (!getChunkdata().isDisabledInteractPhysicalBlocks(block))return;
            if (getChunkdata().hasAccess(player, chunk))return;
            event.setCancelled(true);
        }
    }
}