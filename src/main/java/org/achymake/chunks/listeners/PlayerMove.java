package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
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

import java.text.MessageFormat;

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
        if (event.getTo().getChunk() == event.getFrom().getChunk())return;
        Player player = event.getPlayer();
        Chunk chunk = event.getTo().getChunk();
        if (getChunkStorage().isClaimed(chunk)) {
            OfflinePlayer owner = getChunkStorage().getOwner(chunk);
            if (getChunkStorage().isBanned(chunk, player)) {
                if (getChunkStorage().hasChunkEdit(player)) {
                    visit(player, owner);
                } else {
                    event.setCancelled(true);
                    getMessage().sendActionBar(player, MessageFormat.format(getMessage().getString("events.player-visit-chunk-banned"), owner.getName()));
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
            getMessage().sendActionBar(player, MessageFormat.format(getMessage().getString("events.player-visit-chunk"), owner.getName()));
            player.getPersistentDataContainer().set(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING, owner.getName());
        }
    }
    private void exit(Player player) {
        if (player.getPersistentDataContainer().has(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING)) {
            String lastChunkOwner = player.getPersistentDataContainer().get(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING);
            getMessage().sendActionBar(player, MessageFormat.format(getMessage().getString("events.player-exit-chunk"), lastChunkOwner));
            player.getPersistentDataContainer().remove(NamespacedKey.minecraft("chunk-visitor"));
        }
    }
}
