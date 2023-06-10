package net.achymake.chunks.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.achymake.chunks.Chunks;
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
        return "1.10.0";
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
        }
        if (params.equals("owner")) {
            if (Chunks.getChunkStorage().isProtected(player.getLocation().getChunk())) {
                return "Server";
            }
            if (Chunks.getChunkStorage().isClaimed(player.getLocation().getChunk())) {
                return Chunks.getChunkStorage().getOwner(player.getLocation().getChunk()).getName();
            }
            return "None";
        }
        if (params.equals("access")) {
            if (Chunks.getChunkStorage().isProtected(player.getLocation().getChunk()) || Chunks.getChunkStorage().isClaimed(player.getLocation().getChunk())) {
                if (Chunks.getChunkStorage().hasAccess(player, player.getLocation().getChunk())) {
                    return "True";
                }else {
                    return "False";
                }
            }
            return "True";
        }
        if (params.equals("claimed")) {
            return String.valueOf(Chunks.getChunkStorage().getClaimedCount(player));
        }
        if (params.equals("max_claims")) {
            return String.valueOf(Chunks.getInstance().getConfig().getInt("claim.max-claims"));
        }
        return super.onPlaceholderRequest(player, params);
    }
}