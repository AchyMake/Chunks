package org.achymake.chunks;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.api.*;
import org.achymake.chunks.commands.*;
import org.achymake.chunks.data.*;
import org.achymake.chunks.listeners.*;
import org.achymake.chunks.net.UpdateChecker;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

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
    public static StateFlag FLAG_CHUNKS_CLAIM;
    @Override
    public void onLoad() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flagClaim = new StateFlag("chunks-claim", false);
            FLAG_CHUNKS_CLAIM = flagClaim;
            registry.register(flagClaim);
        } catch (FlagConflictException ignored) {
            Flag<?> existingClaim = registry.get("chunks-claim");
            if (existingClaim instanceof StateFlag) {
                FLAG_CHUNKS_CLAIM = (StateFlag) existingClaim;
            }
        } catch (Exception e) {
            getMessage().sendLog(Level.WARNING, e.getMessage());
        }
    }
    @Override
    public void onEnable() {
        instance = this;
        message = new Message(this);
        chunkdata = new Chunkdata(this);
        userdata = new Userdata(this);
        updateChecker = new UpdateChecker(this);
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getMessage().sendLog(Level.WARNING, "'Vault' does not have any 'Economy' installed");
            getManager().disablePlugin(this);
        }
        if (rsp.getProvider() == null) {
            getMessage().sendLog(Level.WARNING, "'Vault' does not have any 'Economy' installed");
            getManager().disablePlugin(this);
        } else {
            economy = rsp.getProvider();
        }
        new PlaceholderProvider().register();
        commands();
        events();
        reload();
        getMessage().sendLog(Level.INFO, "Enabled " + name() + " " + version());
        getUpdateChecker().getUpdate();
    }
    @Override
    public void onDisable() {
        new PlaceholderProvider().unregister();
        if (!getChunkEditors().isEmpty()) {
            getChunkEditors().clear();
        }
        getMessage().sendLog(Level.INFO, "Disabled " + name() + " " + version());
    }
    private void commands() {
        getCommand("chunk").setExecutor(new ChunkCommand(this));
        getCommand("chunks").setExecutor(new ChunksCommand(this));
    }
    private void events() {
        if (getManager().isPluginEnabled("Harvester")) {
            getManager().registerEvents(new Harvest(this), this);
        }
        if (getManager().isPluginEnabled("Carry")) {
            getManager().registerEvents(new Carry(this), this);
            getManager().registerEvents(new Eject(this), this);
        }
        getManager().registerEvents(new BlockBreak(this), this);
        getManager().registerEvents(new BlockFertilize(this), this);
        getManager().registerEvents(new BlockIgnite(this), this);
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
        getManager().registerEvents(new PlayerHarvestBlock(this), this);
        getManager().registerEvents(new PlayerInteract(this), this);
        getManager().registerEvents(new PlayerInteractAtEntity(this), this);
        getManager().registerEvents(new PlayerJoin(this), this);
        getManager().registerEvents(new PlayerLeashEntity(this), this);
        getManager().registerEvents(new PlayerLogin(this), this);
        getManager().registerEvents(new PlayerMove(this), this);
        getManager().registerEvents(new PlayerQuit(this), this);
        getManager().registerEvents(new PlayerShearEntity(this), this);
        getManager().registerEvents(new PlayerSignOpen(this), this);
        getManager().registerEvents(new SignChange(this), this);
    }
    public StateFlag getFlagChunksClaim() {
        return FLAG_CHUNKS_CLAIM;
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
        getChunkdata().reload();
        getUserdata().reload();
    }
    public boolean isEditor(Player player) {
        return getChunkEditors().contains(player);
    }
    public PluginManager getManager() {
        return getServer().getPluginManager();
    }
    public BukkitScheduler getScheduler() {
        return getServer().getScheduler();
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
    public String name() {
        return getDescription().getName();
    }
    public String version() {
        return getDescription().getVersion();
    }
}