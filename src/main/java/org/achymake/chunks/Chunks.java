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
import org.achymake.chunks.data.*;
import org.achymake.chunks.listeners.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;

public final class Chunks extends JavaPlugin {
    private static Chunks instance;
    private static Userdata userdata;
    private static Message message;
    private static ChunkStorage chunkStorage;
    private static Economy economy = null;
    public static StateFlag FLAG_CHUNKS_CLAIM;
    public static PluginManager manager;
    private final List<Player> chunkEditors = new ArrayList<>();
    @Override
    public void onLoad() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("chunks-claim", false);
            registry.register(flag);
            FLAG_CHUNKS_CLAIM = flag;
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
        message = new Message();
        manager = getServer().getPluginManager();
        userdata = new Userdata(this);
        chunkStorage = new ChunkStorage(this);
        if (isVaultDisable()) {
            getManager().disablePlugin(this);
        }
        if (isPlaceholderAPIDisable()) {
            getManager().disablePlugin(this);
        }
        commands();
        events();
        reload();
        getMessage().sendLog(Level.INFO, "Enabled " + getDescription().getName() + " " + getDescription().getVersion());
        getUpdate();
    }
    @Override
    public void onDisable() {
        if (new PlaceholderProvider().isRegistered()) {
            new PlaceholderProvider().unregister();
        }
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
        getManager().registerEvents(new EntityDamageByEntity(this), this);
        getManager().registerEvents(new EntityChangeBlock(this), this);
        getManager().registerEvents(new EntityEnterLoveMode(this), this);
        getManager().registerEvents(new EntityExplode(this), this);
        getManager().registerEvents(new EntityMount(this), this);
        getManager().registerEvents(new EntityTarget(this), this);
        getManager().registerEvents(new PlayerJoin(this), this);
        getManager().registerEvents(new PlayerBucketEmpty(this), this);
        getManager().registerEvents(new PlayerBucketEntity(this), this);
        getManager().registerEvents(new PlayerBucketFill(this), this);
        getManager().registerEvents(new PlayerCommandPreprocess(this), this);
        getManager().registerEvents(new PlayerInteract(this), this);
        if (getManager().isPluginEnabled("Recovery")) {
            getManager().registerEvents(new PlayerInteractAtEntityRecovery(this), this);
        } else {
            getManager().registerEvents(new PlayerInteractAtEntity(this), this);
        }
        getManager().registerEvents(new PlayerInteractPhysical(this), this);
        getManager().registerEvents(new PlayerLeashEntity(this), this);
        getManager().registerEvents(new PlayerLogin(this), this);
        getManager().registerEvents(new PlayerMove(this), this);
        getManager().registerEvents(new PlayerQuit(this), this);
        getManager().registerEvents(new PlayerShearEntity(this), this);
        getManager().registerEvents(new SignChange(this), this);
    }
    public void getUpdate(Player player) {
        if (notifyUpdate()) {
            getLatest((latest) -> {
                if (!getDescription().getVersion().equals(latest)) {
                    getMessage().send(player,"&6" + getDescription().getName() + " Update:&f " + latest);
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
        getMessage().reload();
        getUserdata().reload(getServer().getOfflinePlayers());
    }
    private boolean isVaultDisable() {
        if (getManager().isPluginEnabled("Vault")) {
            getMessage().sendLog(Level.INFO, "Hooked to 'Vault'");
            if (isEconomyInstalled()) {
                getMessage().sendLog(Level.INFO, "Economy hooked from 'Vault'");
                return false;
            } else {
                getMessage().sendLog(Level.WARNING, "'Vault' does not have any 'Economy' installed");
                return true;
            }
        } else {
            getMessage().sendLog(Level.WARNING, "You have to install 'Vault'");
            return true;
        }
    }
    private boolean isEconomyInstalled() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
    private boolean isPlaceholderAPIDisable() {
        if (getManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderProvider().register();
            getMessage().sendLog(Level.INFO, "Hooked to 'PlaceholderAPI'");
            return false;
        } else {
            getMessage().sendLog(Level.WARNING, "You have to install 'PlaceholderAPI'");
            return true;
        }
    }
    public StateFlag getFlagChunksClaim() {
        return FLAG_CHUNKS_CLAIM;
    }
    public List<Player> getChunkEditors() {
        return chunkEditors;
    }
    public PluginManager getManager() {
        return manager;
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
    public Userdata getUserdata() {
        return userdata;
    }
    public static Chunks getInstance() {
        return instance;
    }
}
