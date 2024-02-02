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

public class PlayerMove implements Listener {
    private final ChunkStorage chunkStorage;
    private final Message message;
    public PlayerMove(Chunks plugin) {
        chunkStorage = plugin.getChunkStorage();
        message = plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null)return;
        Chunk chunk = event.getTo().getChunk();
        if (event.getFrom().getChunk() == chunk)return;
        Player player = event.getPlayer();
        if (chunkStorage.isClaimed(chunk)) {
            if (chunkStorage.isBanned(chunk, player)) {
                event.setCancelled(true);
                message.sendActionBar(player, "&c&lHey!&7 Sorry, but you are banned from&f " + chunkStorage.getOwner(chunk).getName());
            }
            if (player.getPersistentDataContainer().has(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING)) {
                if (!player.getPersistentDataContainer().get(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING).equals(chunkStorage.getOwner(chunk).getName())) {
                    player.getPersistentDataContainer().remove(NamespacedKey.minecraft("chunk-visitor"));
                }
            } else {
                message.sendActionBar(player, "&6Visiting&f " + chunkStorage.getOwner(chunk).getName() + "&6's Chunk");
                player.getPersistentDataContainer().set(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING, chunkStorage.getOwner(chunk).getName());
            }
        } else {
            if (player.getPersistentDataContainer().has(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING)) {
                String lastChunkOwner = player.getPersistentDataContainer().get(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING);
                message.sendActionBar(player, "&6Exiting&f " + lastChunkOwner + "&6's Chunk");
                player.getPersistentDataContainer().remove(NamespacedKey.minecraft("chunk-visitor"));
            }
        }
    }
}
