package org.achymake.chunks.providers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.achymake.chunks.Chunks;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

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
        return Chunks.getInstance().version();
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
            var instance = Chunks.getInstance();
            var chunk = player.getLocation().getChunk();
            var userdata = instance.getUserdata();
            var chunkHandler = instance.getChunkHandler();
            switch (params) {
                case "owner" -> {
                    if (chunkHandler.isClaimed(chunk)) {
                        return chunkHandler.getName(chunk);
                    } else if (chunkHandler.hasAccess(chunk, player)) {
                        return "None";
                    } else return "Server";
                }
                case "access" -> {
                    if (chunkHandler.hasAccess(chunk, player)) {
                        return "&aTrue&f";
                    } else return "&cFalse&f";
                }
                case "claims" -> {
                    return String.valueOf(userdata.getClaimCount(player));
                }
                case "claims_left" -> {
                    return String.valueOf(userdata.getMaxClaims(player) - userdata.getClaimCount(player));
                }
                case "claims_max" -> {
                    return String.valueOf(userdata.getMaxClaims(player));
                }
            }
        }
        return super.onPlaceholderRequest(player, params);
    }
}