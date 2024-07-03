package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;

public record PlayerMove(Chunks plugin) implements Listener {
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().getChunk() == event.getFrom().getChunk())return;
        Player player = event.getPlayer();
        Chunk chunk = event.getTo().getChunk();
        if (getChunkdata().isClaimed(chunk)) {
            OfflinePlayer owner = getChunkdata().getOwner(chunk);
            if (getChunkdata().isBanned(chunk, player)) {
                if (getChunkdata().getChunkEditors().contains(player)) {
                    visit(player, owner);
                } else {
                    event.setCancelled(true);
                    getMessage().sendActionBar(player, "&cYou are banned from&f " + owner.getName() + "&c's chunk");
                }
            } else {
                visit(player, owner);
            }
        } else {
            exit(player);
        }
    }
    private void visit(Player player, OfflinePlayer owner) {
        if (player.getPersistentDataContainer().has(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING)) {
            if (!player.getPersistentDataContainer().get(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING).equals(owner.getName())) {
                player.getPersistentDataContainer().remove(NamespacedKey.minecraft("chunk-visitor"));
            }
        } else {
            getMessage().sendActionBar(player, "&6Visiting&f " + owner.getName() + "&6's chunk");
            player.getPersistentDataContainer().set(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING, owner.getName());
        }
    }
    private void exit(Player player) {
        if (player.getPersistentDataContainer().has(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING)) {
            String lastChunkOwner = player.getPersistentDataContainer().get(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING);
            getMessage().sendActionBar(player, "&6Exited&f " + lastChunkOwner + "&6's chunk");
            player.getPersistentDataContainer().remove(NamespacedKey.minecraft("chunk-visitor"));
        }
    }
}