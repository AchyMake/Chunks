package org.achymake.chunks.net;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;

public record UpdateChecker(Chunks plugin) {
    private Message getMessage() {
        return plugin.getMessage();
    }
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private String getPluginName() {
        return plugin.getDescription().getName();
    }
    private String getPluginVersion() {
        return plugin.getDescription().getVersion();
    }
    public void getUpdate(Player player) {
        if (notifyUpdate()) {
            if (player.hasPermission("chunks.event.join.update")) {
                getLatest((latest) -> {
                    if (!getPluginVersion().equals(latest)) {
                        getMessage().send(player, getPluginName() + "&6 has new update:");
                        getMessage().send(player, "-&a https://www.spigotmc.org/resources/108772/");
                    }
                });
            }
        }
    }
    public void getUpdate() {
        if (notifyUpdate()) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    getLatest((latest) -> {
                        if (getPluginVersion().equals(latest)) {
                            getMessage().sendLog(Level.INFO, "You are using the latest version");
                        } else {
                            getMessage().sendLog(Level.INFO, getPluginName() + " has new update:");
                            getMessage().sendLog(Level.INFO, "- https://www.spigotmc.org/resources/108772/");
                        }
                    });
                }
            });
        }
    }
    public void getLatest(Consumer<String> consumer) {
        try {
            InputStream inputStream = (new URL("https://api.spigotmc.org/legacy/update.php?resource=" + 108772)).openStream();
            Scanner scanner = new Scanner(inputStream);
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
                scanner.close();
            }
            inputStream.close();
        } catch (IOException e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
    private boolean notifyUpdate() {
        return getConfig().getBoolean("notify-update");
    }
}