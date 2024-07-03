package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Tag;
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
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null)return;
        Block block = event.getClickedBlock();
        Chunk chunk = block.getChunk();
        if (!getChunkdata().isClaimed(chunk))return;
        Player player = event.getPlayer();
        if (getChunkdata().hasAccess(player, chunk))return;
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getHand() != EquipmentSlot.HAND)return;
            if (!isRightClickBlock(block))return;
            event.setCancelled(true);
            String owner = getChunkdata().getOwner(chunk).getName();
            getMessage().sendActionBar(player, "&cChunk is owned by&f " + owner);
        } else if (event.getAction().equals(Action.PHYSICAL)) {
            if (!getChunkdata().isClaimed(chunk))return;
            if (!isPhysical(block))return;
            event.setCancelled(true);
        }
    }
    private boolean isPhysical(Block block) {
        return block.getType().equals(Material.FARMLAND) || block.getType().equals(Material.TURTLE_EGG) || Tag.PRESSURE_PLATES.isTagged(block.getType());
    }
    private boolean isRightClickBlock(Block block) {
        if (Tag.BEDS.isTagged(block.getType())) {
            return true;
        } else if (Tag.SHULKER_BOXES.isTagged(block.getType())) {
            return true;
        } else if (Tag.FLOWER_POTS.isTagged(block.getType())) {
            return true;
        } else if (Tag.ANVIL.isTagged(block.getType())) {
            return true;
        } else if (Tag.CAMPFIRES.isTagged(block.getType())) {
            return true;
        } else if (Tag.LOGS.isTagged(block.getType())) {
            return true;
        } else if (Tag.TRAPDOORS.isTagged(block.getType())) {
            return true;
        } else if (Tag.DOORS.isTagged(block.getType())) {
            return true;
        } else if (Tag.BUTTONS.isTagged(block.getType())) {
            return true;
        } else if (Tag.FENCE_GATES.isTagged(block.getType())) {
            return true;
        } else if (Tag.CANDLES.isTagged(block.getType())) {
            return true;
        } else if (block.getType().equals(Material.DECORATED_POT)) {
            return true;
        } else if (block.getType().equals(Material.CHISELED_BOOKSHELF)) {
            return true;
        } else if (block.getType().equals(Material.DISPENSER)) {
            return true;
        } else if (block.getType().equals(Material.DROPPER)) {
            return true;
        } else if (block.getType().equals(Material.HOPPER)) {
            return true;
        } else if (block.getType().equals(Material.DAYLIGHT_DETECTOR)) {
            return true;
        } else if (block.getType().equals(Material.LECTERN)) {
            return true;
        } else if (block.getType().equals(Material.COMPARATOR)) {
            return true;
        } else if (block.getType().equals(Material.REPEATER)) {
            return true;
        } else if (block.getType().equals(Material.REDSTONE_WIRE)) {
            return true;
        } else if (block.getType().equals(Material.LEVER)) {
            return true;
        } else if (block.getType().equals(Material.JUKEBOX)) {
            return true;
        } else if (block.getType().equals(Material.NOTE_BLOCK)) {
            return true;
        } else if (block.getType().equals(Material.BEEHIVE)) {
            return true;
        } else if (block.getType().equals(Material.BEE_NEST)) {
            return true;
        } else if (block.getType().equals(Material.RESPAWN_ANCHOR)) {
            return true;
        } else if (block.getType().equals(Material.LODESTONE)) {
            return true;
        } else if (block.getType().equals(Material.BEACON)) {
            return true;
        } else if (block.getType().equals(Material.BELL)) {
            return true;
        } else if (block.getType().equals(Material.BREWING_STAND)) {
            return true;
        } else if (block.getType().equals(Material.SMOKER)) {
            return true;
        } else if (block.getType().equals(Material.BLAST_FURNACE)) {
            return true;
        } else if (block.getType().equals(Material.FURNACE)) {
            return true;
        } else if (block.getType().equals(Material.CHEST)) {
            return true;
        } else if (block.getType().equals(Material.TRAPPED_CHEST)) {
            return true;
        } else return block.getType().equals(Material.BARREL);
    }
}