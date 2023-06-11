package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractBlocks implements Listener {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    private Message getMessage() {
        return Chunks.getMessage();
    }
    public PlayerInteractBlocks(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractBlocks(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))return;
        if (event.getClickedBlock() == null)return;
        if (getChunkStorage().isProtected(event.getClickedBlock().getChunk())) {
            if (getChunkStorage().hasAccess(event.getPlayer(), event.getClickedBlock().getChunk()))return;
            if (!isCancelledProtected(event.getClickedBlock()))return;
            event.setCancelled(true);
            getMessage().sendActionBar(event.getPlayer(), "&cChunk is protected by&f Server");
        }
        if (getChunkStorage().isClaimed(event.getClickedBlock().getChunk())) {
            if (getChunkStorage().hasAccess(event.getPlayer(), event.getClickedBlock().getChunk()))return;
            if (!isCancelledClaimed(event.getClickedBlock()))return;
            event.setCancelled(true);
            getMessage().sendActionBar(event.getPlayer(), "&cChunk is owned by&f " + getChunkStorage().getOwner(event.getClickedBlock().getChunk()).getName());
        }
    }
    public static boolean isCancelledClaimed(Block block) {
        if (Tag.BEDS.isTagged(block.getType())) {
            return true;
        }
        if (Tag.CROPS.isTagged(block.getType())) {
            return true;
        }
        if (Tag.SHULKER_BOXES.isTagged(block.getType())) {
            return true;
        }
        if (Tag.FLOWER_POTS.isTagged(block.getType())) {
            return true;
        }
        if (Tag.ANVIL.isTagged(block.getType())) {
            return true;
        }
        if (Tag.CAMPFIRES.isTagged(block.getType())) {
            return true;
        }
        if (Tag.LOGS.isTagged(block.getType())) {
            return true;
        }
        if (Tag.TRAPDOORS.isTagged(block.getType())) {
            return true;
        }
        if (Tag.DOORS.isTagged(block.getType())) {
            return true;
        }
        if (Tag.BUTTONS.isTagged(block.getType())) {
            return true;
        }
        if (Tag.FENCE_GATES.isTagged(block.getType())) {
            return true;
        }
        if (Tag.CANDLES.isTagged(block.getType())) {
            return true;
        }
        if (block.getType().equals(Material.DISPENSER)) {
            return true;
        }
        if (block.getType().equals(Material.DROPPER)) {
            return true;
        }
        if (block.getType().equals(Material.HOPPER)) {
            return true;
        }
        if (block.getType().equals(Material.DAYLIGHT_DETECTOR)) {
            return true;
        }
        if (block.getType().equals(Material.LECTERN)) {
            return true;
        }
        if (block.getType().equals(Material.COMPARATOR)) {
            return true;
        }
        if (block.getType().equals(Material.REPEATER)) {
            return true;
        }
        if (block.getType().equals(Material.REDSTONE_WIRE)) {
            return true;
        }
        if (block.getType().equals(Material.LEVER)) {
            return true;
        }
        if (block.getType().equals(Material.JUKEBOX)) {
            return true;
        }
        if (block.getType().equals(Material.NOTE_BLOCK)) {
            return true;
        }
        if (block.getType().equals(Material.BEEHIVE)) {
            return true;
        }
        if (block.getType().equals(Material.BEE_NEST)) {
            return true;
        }
        if (block.getType().equals(Material.RESPAWN_ANCHOR)) {
            return true;
        }
        if (block.getType().equals(Material.LODESTONE)) {
            return true;
        }
        if (block.getType().equals(Material.BEACON)) {
            return true;
        }
        if (block.getType().equals(Material.BELL)) {
            return true;
        }
        if (block.getType().equals(Material.BREWING_STAND)) {
            return true;
        }
        if (block.getType().equals(Material.SMOKER)) {
            return true;
        }
        if (block.getType().equals(Material.BLAST_FURNACE)) {
            return true;
        }
        if (block.getType().equals(Material.FURNACE)) {
            return true;
        }
        if (block.getType().equals(Material.CHEST)) {
            return true;
        }
        if (block.getType().equals(Material.TRAPPED_CHEST)) {
            return true;
        }
        if (block.getType().equals(Material.BARREL)) {
            return true;
        }
        return false;
    }
    private boolean isCancelledProtected(Block block) {
        if (Tag.CROPS.isTagged(block.getType())) {
            return true;
        }
        if (Tag.FENCE_GATES.isTagged(block.getType())) {
            return true;
        }
        if (Tag.FLOWER_POTS.isTagged(block.getType())) {
            return true;
        }
        if (Tag.ANVIL.isTagged(block.getType())) {
            return true;
        }
        if (Tag.CANDLES.isTagged(block.getType())) {
            return true;
        }
        if (Tag.LOGS.isTagged(block.getType())) {
            return true;
        }
        if (Tag.TRAPDOORS.isTagged(block.getType())) {
            return true;
        }
        return false;
    }
}