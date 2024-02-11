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
        return "105";
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
            if (params.equals("owner")) {
                if (chunks.getChunkStorage().isClaimed(chunk)) {
                    return chunks.getChunkStorage().getOwner(chunk).getName();
                }
                return "None";
            }
            if (params.equals("access")) {
                if (chunks.getChunkStorage().isClaimed(chunk)) {
                    if (chunks.getChunkStorage().hasAccess(player, chunk)) {
                        return "True";
                    }else {
                        return "False";
                    }
                }
                return "True";
            }
            if (params.equals("claimed")) {
                return String.valueOf(chunks.getChunkStorage().getClaimedCount(player));
            }
            if (params.equals("max_claims")) {
                return String.valueOf(chunks.getConfig().getInt("claim.max-claims"));
            }
            if (params.equals("claims_left")) {
                return String.valueOf(chunks.getConfig().getInt("claim.max-claims") - chunks.getChunkStorage().getClaimedCount(player));
            }
        }
        return super.onPlaceholderRequest(player, params);
    }
}
