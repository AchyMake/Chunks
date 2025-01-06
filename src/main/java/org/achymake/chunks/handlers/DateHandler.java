package org.achymake.chunks.handlers;

import org.achymake.chunks.Chunks;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Date;

public class DateHandler {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    public Date getDate(long date) {
        return new Date(date);
    }
    public boolean isExpired(OfflinePlayer offlinePlayer) {
        if (getConfig().getInt("claim.expires") > 0) {
            var expired = getDate(offlinePlayer.getLastPlayed());
            expired.setDate(expired.getDate() + getConfig().getInt("claim.expires"));
            return getDate(offlinePlayer.getLastPlayed()).after(expired);
        } else return false;
    }
}