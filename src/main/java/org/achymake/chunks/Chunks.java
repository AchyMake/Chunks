package org.achymake.chunks;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.api.PlaceholderProvider;
import org.achymake.chunks.commands.chunk.ChunkCommand;
import org.achymake.chunks.commands.chunks.ChunksCommand;
import org.achymake.chunks.files.*;
import org.achymake.chunks.listeners.*;
import org.bukkit.configuration.InvalidConfigurationException;
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

public final class Chunks extends JavaPlugin {
    private static Chunks instance;
    private static Database database;
    private static Message message;
    private static ChunkStorage chunkStorage;
    private static Economy economy = null;
    public static StateFlag FLAG_CHUNKS_CLAIM;
    @Override
    public void onLoad() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("chunks-claim", false);
            registry.register(flag);
        } catch (FlagConflictException ignored) {
            Flag<?> existing = registry.get("chunks-claim");
            if (existing instanceof StateFlag) {
                FLAG_CHUNKS_CLAIM = (StateFlag) existing;
            }
        } catch (Exception e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
    @Override
    public void onEnable() {
        instance = this;
        message = new Message(this);
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getMessage().sendLog(Level.WARNING, "You have to install 'Vault'");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            getMessage().sendLog(Level.INFO, "Hooked to 'Vault'");
            if (isEconomyInstalled()) {
                getMessage().sendLog(Level.INFO, "Economy hooked from 'Vault'");
            } else {
                getMessage().sendLog(Level.WARNING, "'Vault' does not have any 'Economy' installed");
                getServer().getPluginManager().disablePlugin(this);
            }
        }
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getMessage().sendLog(Level.WARNING, "You have to install 'PlaceholderAPI'");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            new PlaceholderProvider().register();
            getMessage().sendLog(Level.INFO, "Hooked to 'PlaceholderAPI'");
        }
        database = new Database(this);
        chunkStorage = new ChunkStorage(this);
        commands();
        events();
        reload();
        getMessage().sendLog(Level.INFO, "Enabled " + getName() + " " + getDescription().getVersion());
        getUpdate();
    }
    @Override
    public void onDisable() {
        if (new PlaceholderProvider().isRegistered()) {
            new PlaceholderProvider().unregister();
        }
        if (!getChunkStorage().getChunkEditors().isEmpty()) {
            getChunkStorage().getChunkEditors().clear();
        }
        getMessage().sendLog(Level.INFO, "Disabled " + getName() + " " + getDescription().getVersion());
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
        if (getServer().getPluginManager().isPluginEnabled("Recovery")) {
            new PlayerInteractAtEntityRecovery(this);
        } else {
            new PlayerInteractAtEntity(this);
        }
        new PlayerInteractPhysical(this);
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
                    getMessage().send(player,"&6" + getName() + " Update:&f " + latest);
                    getMessage().send(player,"&6Current Version: &f" + getDescription().getVersion());
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
                        getMessage().sendLog(Level.INFO, "Checking latest release");
                        if (getDescription().getVersion().equals(latest)) {
                            getMessage().sendLog(Level.INFO, "You are using the latest version");
                        } else {
                            getMessage().sendLog(Level.INFO, "New Update: " + latest);
                            getMessage().sendLog(Level.INFO, "Current Version: " + getDescription().getVersion());
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
            getMessage().sendLog(Level.WARNING, e.getMessage());
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
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
            saveConfig();
        } else {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        getDatabase().reload(getServer().getOfflinePlayers());
    }
    public Economy getEconomy() {
        return economy;
    }
    public Message getMessage() {
        return message;
    }
    public ChunkStorage getChunkStorage() {
        return chunkStorage;
    }
    public Database getDatabase() {
        return database;
    }
    public static Chunks getInstance() {
        return instance;
    }
}
