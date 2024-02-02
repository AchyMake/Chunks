package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.ChunkStorage;
import org.achymake.chunks.data.Message;
import org.achymake.recovery.Recovery;
import org.achymake.recovery.files.Database;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class PlayerInteractAtEntityRecovery implements Listener {
    private final ChunkStorage chunkStorage;
    private final FileConfiguration config;
    private final Message message;
    private final Database database = Recovery.getInstance().getDatabase();
    public PlayerInteractAtEntityRecovery(Chunks plugin) {
        chunkStorage = plugin.getChunkStorage();
        config = plugin.getConfig();
        message = plugin.getMessage();
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractAtEntityRecovery(PlayerInteractAtEntityEvent event) {
        Chunk chunk = event.getRightClicked().getLocation().getChunk();
        if (!chunkStorage.isClaimed(chunk))return;
        Entity entity = event.getRightClicked();
        if (entity.getType().equals(EntityType.PLAYER))return;
        if (entity.getType().equals(EntityType.MINECART))return;
        if (entity.getType().equals(EntityType.BOAT))return;
        if (entity.isInvulnerable())return;
        if (entity instanceof ArmorStand armorStand) {
            if (database.hasDeathItems(armorStand))return;
        }
        Player player = event.getPlayer();
        if (chunkStorage.hasAccess(player, chunk))return;
        if (config.getBoolean("hostile." + entity.getType()))return;
        event.setCancelled(true);
        message.send(player, "&c&lHey!&7 Sorry, chunk is owned by&f " + chunkStorage.getOwner(chunk).getName());
    }
}