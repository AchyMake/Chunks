package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.achymake.chunks.events.PlayerChangedChunkEvent;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.GameModeHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class PlayerChangedChunk implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private GameModeHandler getGameModeHandler() {
        return getInstance().getGameModeHandler();
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
        var to = event.getTo();
        var chunkTo = to.getChunk();
        var from = event.getFrom();
        var chunkFrom = from.getChunk();
        if (!event.isCancelled()) {
            if (getChunkHandler().isClaimed(chunkTo)) {
                if (!getChunkHandler().hasAccess(chunkTo, player)) {
                    if (getInstance().manipulateFly()) {
                        if (!player.getGameMode().equals(getGameModeHandler().get("survival")))return;
                        if (!player.isFlying())return;
                        getUserdata().disableFly(player);
                        getMessage().sendActionBar(player, getMessage().get("events.fly.unclaimed"));
                    }
                }
                if (getChunkHandler().isBanned(chunkTo, player)) {
                    if (getUserdata().isEditor(player)) {
                        if (getUserdata().getConfig(player).isString("visit")) {
                            if (getUserdata().getConfig(player).getString("visit").equals(getChunkHandler().getOwner(chunkTo).getUniqueId().toString()))return;
                            getUserdata().setObject(player, "visit", getChunkHandler().getOwner(chunkTo).getUniqueId().toString());
                        } else {
                            getMessage().sendActionBar(player, getMessage().get("events.move.visit", getChunkHandler().getName(chunkTo)));
                            getUserdata().setObject(player, "visit", getChunkHandler().getOwner(chunkTo).getUniqueId().toString());
                        }
                    } else {
                        player.teleport(from);
                        getMessage().sendActionBar(player, getMessage().get("events.move.banned", getChunkHandler().getName(chunkTo)));
                    }
                } else {
                    if (getUserdata().getConfig(player).isString("visit")) {
                        if (getUserdata().getConfig(player).getString("visit").equals(getChunkHandler().getOwner(chunkTo).getUniqueId().toString()))return;
                        getUserdata().setObject(player, "visit", getChunkHandler().getOwner(chunkTo).getUniqueId().toString());
                    } else {
                        getMessage().sendActionBar(player, getMessage().get("events.move.visit", getChunkHandler().getName(chunkTo)));
                        getUserdata().setObject(player, "visit", getChunkHandler().getOwner(chunkTo).getUniqueId().toString());
                    }
                }
            } else if (getChunkHandler().isClaimed(chunkFrom)) {
                if (!getUserdata().getConfig(player).isString("visit"))return;
                getMessage().sendActionBar(player, getMessage().get("events.move.exit", getChunkHandler().getName(chunkFrom)));
                getUserdata().setObject(player, "visit", null);
                if (getInstance().manipulateFly()) {
                    if (!player.getGameMode().equals(getGameModeHandler().get("survival")))return;
                    if (!player.isFlying())return;
                    getUserdata().disableFly(player);
                    getMessage().sendActionBar(player, getMessage().get("events.fly.unclaimed"));
                }
            }
        } else player.teleport(from);
    }
}