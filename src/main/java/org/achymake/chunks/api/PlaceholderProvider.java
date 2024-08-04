package org.achymake.chunks.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.achymake.chunks.Chunks;
import org.bukkit.Chunk;
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
        return Chunks.getInstance().getDescription().getVersion();
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
            Chunk chunk = player.getLocation().getChunk();
            switch (params) {
                case "owner" -> {
                    if (chunks.getChunkdata().isClaimed(chunk)) {
                        return chunks.getChunkdata().getOwner(chunk).getName();
                    }
                    return "None";
                }
                case "access" -> {
                    if (chunks.getChunkdata().isClaimed(chunk)) {
                        if (chunks.getChunkdata().hasAccess(player, chunk)) {
                            return "True";
                        } else {
                            return "False";
                        }
                    }
                    return "True";
                }
                case "claimed" -> {
                    return String.valueOf(chunks.getUserdata().getClaimCount(player));
                }
                case "max_claims" -> {
                    return String.valueOf(chunks.getConfig().getInt("claim.max-claims"));
                }
                case "claims_left" -> {
                    return String.valueOf(chunks.getConfig().getInt("claim.max-claims") - chunks.getUserdata().getClaimCount(player));
                }
            }
        }
        return super.onPlaceholderRequest(player, params);
    }
}