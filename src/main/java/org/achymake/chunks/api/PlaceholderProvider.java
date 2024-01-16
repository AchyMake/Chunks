package org.achymake.chunks.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.achymake.chunks.Chunks;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderProvider extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "chunks";
    }
    @Override
    public String getAuthor() {
        return "AchyMake";
    }
    @Override
    public String getVersion() {
        return "1.20.4";
    }
    @Override
    public boolean canRegister() {
        return true;
    }
    @Override
    public boolean register() {
        return super.register();
    }
    @Override
    public boolean persist() {
        return true;
    }
    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        } else {
            Chunks chunks = Chunks.getInstance();
            if (params.equals("owner")) {
                if (chunks.getDatabase().isProtected(player.getLocation().getChunk())) {
                    return "Server";
                }
                if (chunks.getDatabase().isClaimed(player.getLocation().getChunk())) {
                    return chunks.getDatabase().getOwner(player.getLocation().getChunk()).getName();
                }
                return "None";
            }
            if (params.equals("access")) {
                if (chunks.getDatabase().isProtected(player.getLocation().getChunk()) || chunks.getDatabase().isClaimed(player.getLocation().getChunk())) {
                    if (chunks.getDatabase().hasAccess(player, player.getLocation().getChunk())) {
                        return "True";
                    }else {
                        return "False";
                    }
                }
                return "True";
            }
            if (params.equals("claimed")) {
                return String.valueOf(chunks.getDatabase().getClaimedCount(player));
            }
            if (params.equals("max_claims")) {
                return String.valueOf(chunks.getConfiguration().getInt("claim.max-claims"));
            }
        }
        return super.onPlaceholderRequest(player, params);
    }
}
