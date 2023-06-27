package net.achymake.chunks;

import net.achymake.chunks.api.PlaceholderProvider;
import net.achymake.chunks.commands.chunk.ChunkCommand;
import net.achymake.chunks.commands.chunks.ChunksCommand;
import net.achymake.chunks.files.*;
import net.achymake.chunks.listeners.*;
import net.achymake.chunks.version.UpdateChecker;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class Chunks extends JavaPlugin {
    private static Chunks instance;
    public static Chunks getInstance() {
        return instance;
    }
    private static FileConfiguration configuration;
    public static FileConfiguration getConfiguration() {
        return configuration;
    }
    private static Message message;
    public static Message getMessage() {
        return message;
    }
    private static ChunkStorage chunkStorage;
    public static ChunkStorage getChunkStorage() {
        return chunkStorage;
    }
    private static Database database;
    public static Database getDatabase() {
        return database;
    }
    private static Economy economy = null;
    public static Economy getEconomy() {
        return economy;
    }
    private void start() {
        instance = this;
        configuration = getConfig();
        message = new Message(getLogger());
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getMessage().sendLog(Level.WARNING, "You have to install 'Vault'");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            if (isEconomyInstalled()) {
                getMessage().sendLog(Level.INFO, "Hooked to 'Vault'");
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
        chunkStorage = new ChunkStorage(this);
        database = new Database(getDataFolder());
        commands();
        events();
        reload();
        getMessage().sendLog(Level.INFO, "Enabled " + getName() + " " + getDescription().getVersion());
        new UpdateChecker(this, 108772).getUpdate();
    }
    private void stop() {
        if (!getChunkStorage().getChunkEditors().isEmpty()) {
            getChunkStorage().getChunkEditors().clear();
        }
        if (new PlaceholderProvider().isRegistered()) {
            new PlaceholderProvider().unregister();
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
    @Override
    public void onEnable() {
        start();
    }
    @Override
    public void onDisable() {
        stop();
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
        new DamageEntity(this);
        new DamageEntityWithArrow(this);
        new DamageEntityWithSnowball(this);
        new DamageEntityWithSpectralArrow(this);
        new DamageEntityWithThrownPotion(this);
        new DamageEntityWithTrident(this);
        new EntityBlockForm(this);
        new EntityChangeBlock(this);
        new EntityEnterLoveMode(this);
        new EntityExplode(this);
        new EntityTarget(this);
        new PlayerBucketEmpty(this);
        new PlayerBucketEntity(this);
        new PlayerBucketFill(this);
        new PlayerCommandPreprocess(this);
        new PlayerInteractBlocks(this);
        new PlayerInteractEntity(this);
        new PlayerInteractPhysical(this);
        new PlayerJoin(this);
        new PlayerLeashEntity(this);
        new PlayerMount(this);
        new PlayerMove(this);
        new PlayerQuit(this);
        new PlayerShearEntity(this);
        new SignChange(this);
    }
    public void reload() {
        File file = new File(getDataFolder(), "config.yml");
        if (file.exists()) {
            try {
                getConfig().load(file);
                getMessage().sendLog(Level.INFO, "reloaded config.yml");
            } catch (IOException | InvalidConfigurationException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
            saveConfig();
        } else {
            getMessage().sendLog(Level.INFO, "creating config.yml");
            getConfig().options().copyDefaults(true);
            saveConfig();
            getMessage().sendLog(Level.INFO, "created config.yml");
        }
    }
    public void reloadPlayerFiles() {
        for (OfflinePlayer offlinePlayer : getServer().getOfflinePlayers()) {
            if (getDatabase().exist(offlinePlayer)) {
                File file = new File(getDataFolder(), "userdata/" + offlinePlayer.getUniqueId() + ".yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                try {
                    config.load(file);
                } catch (IOException | InvalidConfigurationException e) {
                    getMessage().sendLog(Level.WARNING, e.getMessage());
                }
            }
        }
    }
}