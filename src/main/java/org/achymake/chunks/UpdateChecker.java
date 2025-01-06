package org.achymake.chunks;

import org.achymake.chunks.data.Message;
import org.achymake.chunks.handlers.ScheduleHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private String getName() {
        return getInstance().name();
    }
    private String getVersion() {
        return getInstance().version();
    }
    private boolean notifyUpdate() {
        return getConfig().getBoolean("notify-update");
    }
    public void getUpdate(Player player) {
        if (player.hasPermission("chunks.event.join.update")) {
            if (notifyUpdate()) {
                getScheduler().runLater(new Runnable() {
                    @Override
                    public void run() {
                        getLatest((latest) -> {
                            if (!getVersion().equals(latest)) {
                                player.sendMessage(getMessage().addColor(getName() + "&6 has new update"));
                                player.sendMessage(getMessage().addColor("-&a https://www.spigotmc.org/resources/108772/"));
                            }
                        });
                    }
                }, 3);
            }
        }
    }
    public void getUpdate() {
        if (notifyUpdate()) {
            getScheduler().runAsynchronously(new Runnable() {
                @Override
                public void run() {
                    getLatest((latest) -> {
                        if (!getVersion().equals(latest)) {
                            getInstance().sendInfo(getName() + " has new update:");
                            getInstance().sendInfo("- https://www.spigotmc.org/resources/108772/");
                        }
                    });
                }
            });
        }
    }
    public void getLatest(Consumer<String> consumer) {
        try (var inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + 108772).openStream()) {
            var scanner = new Scanner(inputStream);
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
                scanner.close();
            } else inputStream.close();
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
}