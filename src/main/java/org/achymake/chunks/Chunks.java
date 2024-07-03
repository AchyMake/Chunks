package org.achymake.chunks;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.api.PlaceholderProvider;
import org.achymake.chunks.commands.chunk.ChunkCommand;
import org.achymake.chunks.commands.chunks.ChunksCommand;
import org.achymake.chunks.data.Chunkdata;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.achymake.chunks.listeners.*;
import org.achymake.chunks.net.UpdateChecker;
import org.bukkit.Chunk;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class Chunks extends JavaPlugin {
    private static Chunks instance;
    private static Message message;
    private static Chunkdata chunkdata;
    private static Userdata userdata;
    private static Economy economy = null;
    private static UpdateChecker updateChecker;
    private final List<Player> chunkEditors = new ArrayList<>();
    @Override
    public void onEnable() {
        instance = this;
        message = new Message(this);
        chunkdata = new Chunkdata(this);
        userdata = new Userdata(this);
        updateChecker = new UpdateChecker(this);
        if (isEconomyInstalled()) {
            getMessage().sendLog(Level.INFO, "Chunks is hooked with 'Vault'");
        } else {
            getMessage().sendLog(Level.WARNING, "'Vault' does not have any 'Economy' installed");
            getManager().disablePlugin(this);
        }
        new PlaceholderProvider().register();
        commands();
        events();
        reload();
        getMessage().sendLog(Level.INFO, "Enabled " + getDescription().getName() + " " + getDescription().getVersion());
        getUpdateChecker().getUpdate();
    }
    @Override
    public void onDisable() {
        new PlaceholderProvider().unregister();
        if (!getChunkEditors().isEmpty()) {
            getChunkEditors().clear();
        }
        getMessage().sendLog(Level.INFO, "Disabled " + getDescription().getName() + " " + getDescription().getVersion());
    }
    private void commands() {
        getCommand("chunk").setExecutor(new ChunkCommand(this));
        getCommand("chunks").setExecutor(new ChunksCommand(this));
    }
    private void events() {
        getManager().registerEvents(new BlockBreak(this), this);
        getManager().registerEvents(new BlockFertilize(this), this);
        getManager().registerEvents(new BlockPlace(this), this);
        getManager().registerEvents(new BlockRedstone(this), this);
        getManager().registerEvents(new CauldronLevelChange(this), this);
        getManager().registerEvents(new EntityChangeBlock(this), this);
        getManager().registerEvents(new EntityDamageByEntity(this), this);
        getManager().registerEvents(new EntityEnterLoveMode(this), this);
        getManager().registerEvents(new EntityExplode(this), this);
        getManager().registerEvents(new EntityMount(this), this);
        getManager().registerEvents(new EntityTarget(this), this);
        getManager().registerEvents(new PlayerBucketEmpty(this), this);
        getManager().registerEvents(new PlayerBucketEntity(this), this);
        getManager().registerEvents(new PlayerBucketFill(this), this);
        getManager().registerEvents(new PlayerCommandPreprocess(this), this);
        getManager().registerEvents(new PlayerInteract(this), this);
        getManager().registerEvents(new PlayerInteractAtEntity(this), this);
        getManager().registerEvents(new PlayerJoin(this), this);
        getManager().registerEvents(new PlayerLeashEntity(this), this);
        getManager().registerEvents(new PlayerLogin(this), this);
        getManager().registerEvents(new PlayerMove(this), this);
        getManager().registerEvents(new PlayerQuit(this), this);
        getManager().registerEvents(new PlayerShearEntity(this), this);
        getManager().registerEvents(new SignChange(this), this);
    }
    public void reload() {
        File file = new File(getDataFolder(), "config.yml");
        if (file.exists()) {
            try {
                getConfig().load(file);
            } catch (IOException | InvalidConfigurationException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        } else {
            getConfig().options().copyDefaults(true);
            try {
                getConfig().save(file);
            } catch (IOException e) {
                getMessage().sendLog(Level.WARNING, e.getMessage());
            }
        }
        getUserdata().reload(getServer().getOfflinePlayers());
        getChunkdata().reload();
    }
    private boolean isEconomyInstalled() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
    public boolean isAllowed(Chunk chunk) {
        return getConfig().getStringList("worlds").contains(chunk.getWorld().getName());
    }
    public boolean isEditor(Player player) {
        return getChunkEditors().contains(player);
    }
    private PluginManager getManager() {
        return getServer().getPluginManager();
    }
    public List<Player> getChunkEditors() {
        return chunkEditors;
    }
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
    public Economy getEconomy() {
        return economy;
    }
    public Userdata getUserdata() {
        return userdata;
    }
    public Chunkdata getChunkdata() {
        return chunkdata;
    }
    public Message getMessage() {
        return message;
    }
    public static Chunks getInstance() {
        return instance;
    }
}