package net.achymake.chunks.listeners;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.files.ChunkStorage;
import net.achymake.chunks.files.Message;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerMove implements Listener {
    private final ChunkStorage chunkStorage = Chunks.getChunkStorage();
    private final Message message = Chunks.getMessage();
    public PlayerMove(Chunks chunks) {
        chunks.getServer().getPluginManager().registerEvents(this, chunks);
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() == null)return;
        if (event.getFrom().getChunk() == event.getTo().getChunk())return;
        if (chunkStorage.isProtected(event.getPlayer().getLocation().getChunk())) {
            if (event.getPlayer().getPersistentDataContainer().has(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING)) {
                if (!event.getPlayer().getPersistentDataContainer().get(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING).equals("Server")) {
                    event.getPlayer().getPersistentDataContainer().remove(NamespacedKey.minecraft("chunk-visitor"));
                }
            } else {
                message.sendActionBar(event.getPlayer(), "&6Visiting&f Server&6's Chunk");
                event.getPlayer().getPersistentDataContainer().set(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING, "Server");
            }
        } else if (chunkStorage.isClaimed(event.getPlayer().getLocation().getChunk())) {
            if (event.getPlayer().getPersistentDataContainer().has(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING)) {
                if (!event.getPlayer().getPersistentDataContainer().get(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING).equals(chunkStorage.getOwner(event.getPlayer().getLocation().getChunk()).getName())) {
                    event.getPlayer().getPersistentDataContainer().remove(NamespacedKey.minecraft("chunk-visitor"));
                }
            } else {
                message.sendActionBar(event.getPlayer(), "&6Visiting&f " + chunkStorage.getOwner(event.getPlayer().getLocation().getChunk()).getName() + "&6's Chunk");
                event.getPlayer().getPersistentDataContainer().set(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING, chunkStorage.getOwner(event.getPlayer().getLocation().getChunk()).getName());
            }
        } else {
            if (event.getPlayer().getPersistentDataContainer().has(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING)) {
                String lastChunkOwner = event.getPlayer().getPersistentDataContainer().get(NamespacedKey.minecraft("chunk-visitor"), PersistentDataType.STRING);
                message.sendActionBar(event.getPlayer(), "&6Exiting&f " + lastChunkOwner + "&6's Chunk");
                event.getPlayer().getPersistentDataContainer().remove(NamespacedKey.minecraft("chunk-visitor"));
            }
        }
    }
}