package org.achymake.chunks.listeners;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.handlers.ChunkHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.plugin.PluginManager;

public class NotePlay implements Listener {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public NotePlay() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onNotePlay(NotePlayEvent event) {
        var block = event.getBlock();
        if (!getChunkHandler().isAllowedClaim(block.getChunk()))return;
        if (!getChunkHandler().isRedstoneOnlyInClaims())return;
        if (getChunkHandler().isClaimed(block.getChunk()))return;
        event.setCancelled(true);
    }
}