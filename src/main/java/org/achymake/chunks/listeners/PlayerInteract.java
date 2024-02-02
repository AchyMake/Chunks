package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
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

public class PlayerInteract implements Listener {
    private final ChunkStorage chunkStorage;
    private final Message message;
    public PlayerInteract(Chunks plugin) {
        chunkStorage = plugin.getChunkStorage();
        message = plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))return;
        if (event.getClickedBlock() == null)return;
        if (event.getHand() != EquipmentSlot.HAND)return;
        Block block = event.getClickedBlock();
        Chunk chunk = block.getChunk();
        if (!chunkStorage.isClaimed(chunk))return;
        if (!isCancelled(block))return;
        Player player = event.getPlayer();
        if (chunkStorage.hasAccess(player, chunk))return;
        event.setCancelled(true);
        message.send(player, "&c&lHey!&7 Sorry, chunk is owned by&f " + chunkStorage.getOwner(chunk).getName());
    }
    private boolean isCancelled(Block block) {
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
