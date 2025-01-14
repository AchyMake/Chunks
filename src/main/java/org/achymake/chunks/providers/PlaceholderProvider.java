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
            switch (params) {
                case "owner" -> {
                    if (instance.getChunkHandler().isClaimed(chunk)) {
                        return instance.getChunkHandler().getName(chunk);
                    } else if (instance.getChunkHandler().hasAccess(chunk, player)) {
                        return "None";
                    } else return "Server";
                }
                case "access" -> {
                    if (instance.getChunkHandler().hasAccess(chunk, player)) {
                        return "&aTrue&f";
                    } else return "&cFalse&f";
                }
                case "claims" -> {
                    return String.valueOf(instance.getUserdata().getClaimCount(player));
                }
                case "claims_left" -> {
                    return String.valueOf(instance.getUserdata().getMaxClaims(player) - instance.getUserdata().getClaimCount(player));
                }
                case "claims_max" -> {
                    return String.valueOf(instance.getUserdata().getMaxClaims(player));
                }
            }
        }
        return super.onPlaceholderRequest(player, params);
    }
}