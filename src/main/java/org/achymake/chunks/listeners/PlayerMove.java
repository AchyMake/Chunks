package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;

public record PlayerMove(Chunks plugin) implements Listener {
    private ChunkStorage getChunkStorage() {
        return plugin.getChunkStorage();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null)return;
        Chunk chunk = event.getTo().getChunk();
        if (event.getFrom().getChunk() == chunk)return;
        Player player = event.getPlayer();
        if (getChunkStorage().isClaimed(chunk)) {
            String owner = getChunkStorage().getOwner(chunk).getName();
            if (getChunkStorage().isBanned(chunk, player)) {
                event.setCancelled(true);
                getMessage().sendActionBar(player, "&c&lHey!&7 Sorry, but you are banned from&f " + owner);
            }
            if (player.getPersistentDataContainer().has(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING)) {
                if (!player.getPersistentDataContainer().get(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING).equals(owner)) {
                    player.getPersistentDataContainer().remove(NamespacedKey.minecraft("chunk-visitor"));
                }
            } else {
                getMessage().sendActionBar(player, "&6Visiting&f " + owner + "&6's Chunk");
                player.getPersistentDataContainer().set(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING, owner);
            }
        } else {
            if (player.getPersistentDataContainer().has(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING)) {
                String lastChunkOwner = player.getPersistentDataContainer().get(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING);
                getMessage().sendActionBar(player, "&6Exiting&f " + lastChunkOwner + "&6's Chunk");
                player.getPersistentDataContainer().remove(NamespacedKey.minecraft("chunk-visitor"));
            }
        }
    }
}
