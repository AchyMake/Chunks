package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.files.Database;
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

public class PlayerInteract implements Listener {
    private Database getDatabase() {
        return Chunks.getDatabase();
    }
    public PlayerInteract(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getClickedBlock() == null)return;
            Player player = event.getPlayer();
            Chunk chunk = event.getClickedBlock().getChunk();
            if (getDatabase().isProtected(chunk)) {
                if (getDatabase().hasAccess(player, chunk))return;
                if (!isCancelledProtected(event.getClickedBlock()))return;
                event.setCancelled(true);
                Chunks.sendActionBar(player, "&cChunk is protected by&f Server");
            } else if (getDatabase().isClaimed(chunk)) {
                if (getDatabase().hasAccess(player, chunk))return;
                if (!isCancelledClaimed(event.getClickedBlock()))return;
                event.setCancelled(true);
                Chunks.sendActionBar(player, "&cChunk is owned by&f " + getDatabase().getOwner(chunk).getName());

            }
        } else if (event.getAction().equals(Action.PHYSICAL)) {
            if (event.getClickedBlock() == null)return;
            Player player = event.getPlayer();
            Chunk chunk = event.getClickedBlock().getChunk();
            if (getDatabase().isProtected(chunk)) {
                if (getDatabase().hasAccess(player, chunk))return;
                if (!physical(event.getClickedBlock()))return;
                event.setCancelled(true);
            } else if (getDatabase().isClaimed(chunk)) {
                if (getDatabase().hasAccess(player, chunk))return;
                if (!physical(event.getClickedBlock()))return;
                event.setCancelled(true);
            }
        }
    }
    private boolean physical(Block block) {
        return Tag.PRESSURE_PLATES.isTagged(block.getType()) || block.getType().equals(Material.TURTLE_EGG) || block.getType().equals(Material.FARMLAND);
    }
    public static boolean isCancelledClaimed(Block block) {
        if (Tag.BEDS.isTagged(block.getType())) {
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
        if (block.getType().equals(Material.DECORATED_POT)) {
            return true;
        }
        if (block.getType().equals(Material.CHISELED_BOOKSHELF)) {
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
        if (block.getType().equals(Material.DECORATED_POT)) {
            return true;
        }
        if (block.getType().equals(Material.CHISELED_BOOKSHELF)) {
            return true;
        }
        return false;
    }
}
