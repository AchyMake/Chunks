package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerMove implements Listener {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    public PlayerMove(Chunks plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null)return;
        if (event.getFrom().getChunk() == event.getTo().getChunk())return;
        Chunk chunk = event.getTo().getChunk();
        if (getChunkStorage().isProtected(chunk)) {
            if (event.getPlayer().getPersistentDataContainer().has(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING)) {
                if (!event.getPlayer().getPersistentDataContainer().get(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING).equals("Server")) {
                    event.getPlayer().getPersistentDataContainer().remove(NamespacedKey.minecraft("chunk-visitor"));
                }
            } else {
                Chunks.sendActionBar(event.getPlayer(), "&6Visiting&f Server&6's Chunk");
                event.getPlayer().getPersistentDataContainer().set(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING, "Server");
            }
        } else if (getChunkStorage().isClaimed(chunk)) {
            if (getChunkStorage().isBanned(chunk, event.getPlayer())) {
                event.setCancelled(true);
                Chunks.send(event.getPlayer(), "&cError:&7 You are banned from&f " + getChunkStorage().getOwner(chunk));
            }
            if (event.getPlayer().getPersistentDataContainer().has(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING)) {
                if (!event.getPlayer().getPersistentDataContainer().get(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING).equals(getChunkStorage().getOwner(chunk).getName())) {
                    event.getPlayer().getPersistentDataContainer().remove(NamespacedKey.minecraft("chunk-visitor"));
                }
            } else {
                Chunks.sendActionBar(event.getPlayer(), "&6Visiting&f " + getChunkStorage().getOwner(chunk).getName() + "&6's Chunk");
                event.getPlayer().getPersistentDataContainer().set(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING, getChunkStorage().getOwner(chunk).getName());
            }
        } else {
            if (event.getPlayer().getPersistentDataContainer().has(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING)) {
                String lastChunkOwner = event.getPlayer().getPersistentDataContainer().get(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING);
                Chunks.sendActionBar(event.getPlayer(), "&6Exiting&f " + lastChunkOwner + "&6's Chunk");
                event.getPlayer().getPersistentDataContainer().remove(NamespacedKey.minecraft("chunk-visitor"));
            }
        }
    }
}