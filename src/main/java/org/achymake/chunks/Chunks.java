package org.achymake.chunks;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.api.PlaceholderProvider;
import org.achymake.chunks.commands.chunk.ChunkCommand;
import org.achymake.chunks.commands.chunks.ChunksCommand;
import org.achymake.chunks.files.*;
import org.achymake.chunks.listeners.*;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Chunks extends JavaPlugin {
    private static Chunks instance;
    public static Chunks getInstance() {
        return instance;
    }
    private static FileConfiguration configuration;
    public static FileConfiguration getConfiguration() {
        return configuration;
    }
    private static File folder;
    public static File getFolder() {
        return folder;
    }
    private static Logger logger;
    public static void sendLog(Level level, String message) {
        logger.log(level, message);
    }
    private static Database database;
    public static Database getDatabase() {
        return database;
    }
    private static Economy economy = null;
    public static Economy getEconomy() {
        return economy;
    }
    @Override
    public void onEnable() {
        instance = this;
        configuration = getConfig();
        folder = getDataFolder();
        logger = getLogger();
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            sendLog(Level.WARNING, "You have to install 'Vault'");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            sendLog(Level.INFO, "Hooked to 'Vault'");
            if (isEconomyInstalled()) {
                sendLog(Level.INFO, "Economy hooked from 'Vault'");
            } else {
                sendLog(Level.WARNING, "'Vault' does not have any 'Economy' installed");
                getServer().getPluginManager().disablePlugin(this);
            }
        }
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            sendLog(Level.WARNING, "You have to install 'PlaceholderAPI'");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            new PlaceholderProvider().register();
            sendLog(Level.INFO, "Hooked to 'PlaceholderAPI'");
        }
        database = new Database();
        commands();
        events();
        reload();
        sendLog(Level.INFO, "Enabled " + getName() + " " + getDescription().getVersion());
        getUpdate();
    }
    @Override
    public void onDisable() {
        if (new PlaceholderProvider().isRegistered()) {
            new PlaceholderProvider().unregister();
        }
        sendLog(Level.INFO, "Disabled " + getName() + " " + getDescription().getVersion());
    }
    private boolean isEconomyInstalled() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
    private void commands() {
        getCommand("chunk").setExecutor(new ChunkCommand());
        getCommand("chunks").setExecutor(new ChunksCommand());
    }
    private void events() {
        new BlockBreak(this);
        new BlockFertilize(this);
        new BlockPlace(this);
        new BlockRedstone(this);
        new CauldronLevelChange(this);
        new CreatureSpawn(this);
        new EntityBlockForm(this);
        new EntityDamageByEntity(this);
        new EntityChangeBlock(this);
        new EntityEnterLoveMode(this);
        new EntityExplode(this);
        new EntityMount(this);
        new EntityTarget(this);
        new NotifyUpdate(this);
        new PlayerBucketEmpty(this);
        new PlayerBucketEntity(this);
        new PlayerBucketFill(this);
        new PlayerCommandPreprocess(this);
        new PlayerInteract(this);
        new PlayerLeashEntity(this);
        new PlayerLogin(this);
        new PlayerMove(this);
        new PlayerQuit(this);
        new PlayerShearEntity(this);
        new SignChange(this);
    }
    public void getUpdate(Player player) {
        if (notifyUpdate()) {
            getLatest((latest) -> {
                if (!getDescription().getVersion().equals(latest)) {
                    send(player,"&6" + getName() + " Update:&f " + latest);
                    send(player,"&6Current Version: &f" + getDescription().getVersion());
                }
            });
        }
    }
    public void getUpdate() {
        if (notifyUpdate()) {
            getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
                @Override
                public void run() {
                    getLatest((latest) -> {
                        sendLog(Level.INFO, "Checking latest release");
                        if (getDescription().getVersion().replace("-Spigot", "").equals(latest)) {
                            sendLog(Level.INFO, "You are using the latest version");
                        } else {
                            sendLog(Level.INFO, "New Update: " + latest);
                            sendLog(Level.INFO, "Current Version: " + getDescription().getVersion().replace("-Spigot", ""));
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
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            sendLog(Level.WARNING, e.getMessage());
        }
    }
    private boolean notifyUpdate() {
        return getConfig().getBoolean("notify-update");
    }
    public void reload() {
        File file = new File(getDataFolder(), "config.yml");
        if (file.exists()) {
            try {
                getConfig().load(file);
            } catch (IOException | InvalidConfigurationException e) {
                sendLog(Level.WARNING, e.getMessage());
            }
            saveConfig();
        } else {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        getDatabase().reload(getServer().getOfflinePlayers());
    }
    public static void send(ConsoleCommandSender sender, String message) {
        sender.sendMessage(message);
    }
    public static void send(Player player, String message) {
        player.sendMessage(addColor(message));
    }
    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(addColor(message)));
    }
    public static String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
