package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.achymake.chunks.events.PlayerChangedChunkEvent;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.WorldHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class PlayerChangedChunk implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerChangedChunk() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangedChunk(PlayerChangedChunkEvent event) {
        var player = event.getPlayer();
        if (!getWorldHandler().isAllowedClaim(event.getTo().getChunk()))return;
        if (event.isCancelled()) {
            player.teleport(event.getFrom());
        } else {
            var to = event.getTo().getChunk();
            if (getChunkHandler().isClaimed(to)) {
                if (!getChunkHandler().hasAccess(to, player)) {
                    if (getInstance().manipulateFly()) {
                        if (player.isFlying() && getUserdata().isSurvival(player)) {
                            getUserdata().disableFly(player);
                            getMessage().sendActionBar(player, getMessage().get("events.fly.unclaimed"));
                        }
                    }
                }
                if (getChunkHandler().isBanned(to, player)) {
                    if (getUserdata().isEditor(player)) {
                        if (getUserdata().getConfig(player).isString("visit")) {
                            if (getUserdata().getConfig(player).getString("visit").equals(getChunkHandler().getOwner(to).getUniqueId().toString()))return;
                            getUserdata().setObject(player, "visit", getChunkHandler().getOwner(to).getUniqueId().toString());
                        } else {
                            getMessage().sendActionBar(player, getMessage().get("events.move.visit", getChunkHandler().getName(to)));
                            getUserdata().setObject(player, "visit", getChunkHandler().getOwner(to).getUniqueId().toString());
                        }
                    } else {
                        player.teleport(event.getFrom());
                        getMessage().sendActionBar(player, getMessage().get("events.move.banned", getChunkHandler().getName(to)));
                    }
                } else {
                    if (getUserdata().getConfig(player).isString("visit")) {
                        if (getUserdata().getConfig(player).getString("visit").equals(getChunkHandler().getOwner(to).getUniqueId().toString()))return;
                        getUserdata().setObject(player, "visit", getChunkHandler().getOwner(to).getUniqueId().toString());
                    } else {
                        getMessage().sendActionBar(player, getMessage().get("events.move.visit", getChunkHandler().getName(to)));
                        getUserdata().setObject(player, "visit", getChunkHandler().getOwner(to).getUniqueId().toString());
                    }
                }
            } else {
                var from = event.getFrom().getChunk();
                if (!getChunkHandler().isClaimed(from))return;
                if (!getUserdata().getConfig(player).isString("visit"))return;
                getMessage().sendActionBar(player, getMessage().get("events.move.exit", getChunkHandler().getName(from)));
                getUserdata().setObject(player, "visit", null);
                if (getInstance().manipulateFly()) {
                    if (player.isFlying() && getUserdata().isSurvival(player)) {
                        getUserdata().disableFly(player);
                        getMessage().sendActionBar(player, getMessage().get("events.fly.unclaimed"));
                    }
                }
            }
        }
    }
}