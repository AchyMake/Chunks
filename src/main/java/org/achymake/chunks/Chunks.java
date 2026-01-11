package org.achymake.chunks;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.commands.*;
import org.achymake.chunks.data.*;
import org.achymake.chunks.handlers.*;
import org.achymake.chunks.listeners.*;
import org.achymake.chunks.providers.*;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class Chunks extends JavaPlugin {
    private static Chunks instance;
    private Message message;
    private Userdata userdata;
    private ChunkHandler chunkHandler;
    private DateHandler dateHandler;
    private EntityHandler entityHandler;
    private GameModeHandler gameModeHandler;
    private MaterialHandler materialHandler;
    private ScheduleHandler scheduleHandler;
    private UUIDHandler uuidHandler;
    private WorldHandler worldHandler;
    private UpdateChecker updateChecker;
    private BukkitScheduler bukkitScheduler;
    private PluginManager pluginManager;
    private Economy economy = null;
    private StateFlag CHUNK_CLAIM;
    @Override
    public void onLoad() {
        CHUNK_CLAIM = new StateFlag("chunk-claim", true);
        getWorldGuard().getFlagRegistry().register(getFlag());
    }
    public StateFlag getFlag() {
        return CHUNK_CLAIM;
    }
    @Override
    public void onEnable() {
        instance = this;
        message = new Message();
        userdata = new Userdata();
        chunkHandler = new ChunkHandler();
        dateHandler = new DateHandler();
        entityHandler = new EntityHandler();
        gameModeHandler = new GameModeHandler();
        materialHandler = new MaterialHandler();
        scheduleHandler = new ScheduleHandler();
        uuidHandler = new UUIDHandler();
        worldHandler = new WorldHandler();
        updateChecker = new UpdateChecker();
        bukkitScheduler = getServer().getScheduler();
        pluginManager = getServer().getPluginManager();
        var rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            sendWarning("Economy not found");
            getPluginManager().disablePlugin(this);
        } else economy = rsp.getProvider();
        commands();
        events();
        reload();
        new PlaceholderProvider().register();
        sendInfo("Economy hook to " + getEconomy().getName());
        sendInfo("Enabled for " + getMinecraftProvider() + " " + getMinecraftVersion());
        getUpdateChecker().getUpdate();
    }
    @Override
    public void onDisable() {
        getUserdata().disable();
        new PlaceholderProvider().unregister();
        getScheduleHandler().disable();
        sendInfo("Disabled for " + getMinecraftProvider() + " " + getMinecraftVersion());
    }
    private void commands() {
        new ChunkCommand();
        new ChunksCommand();
    }
    private void events() {
        new BlockBreak();
        new BlockDispense();
        new BlockDispenseArmor();
        new BlockFertilize();
        new BlockFromTo();
        new BlockIgnite();
        new BlockPistonExtend();
        new BlockPistonRetract();
        new BlockPlace();
        new BlockRedstone();
        new BlockSpread();
        new CauldronLevelChange();
        new ChunkLoad();
        new CrafterCraft();
        new EntityChangeBlock();
        new EntityDamageByEntity();
        new EntityEnterLoveMode();
        new EntityExplode();
        new EntityMount();
        new EntityPlace();
        new EntityTarget();
        new EntityTargetLivingEntity();
        new HangingBreakByEntity();
        new HangingPlace();
        new NotePlay();
        new PlayerArmorStandManipulate();
        new PlayerBedEnter();
        new PlayerBucketEmpty();
        new PlayerBucketEntity();
        new PlayerBucketFill();
        new PlayerChangedChunk();
        new PlayerCommandPreprocess();
        new PlayerFish();
        new PlayerHarvestBlock();
        new PlayerInteract();
        new PlayerInteractEntity();
        new PlayerJoin();
        new PlayerLeashEntity();
        new PlayerLogin();
        new PlayerMove();
        new PlayerQuit();
        new PlayerShearEntity();
        new PlayerTakeLecternBook();
        new PlayerToggleFlight();
        new PlayerUnleashEntity();
        new SignChange();
        new VehicleDamage();
        if (getPluginManager().isPluginEnabled("Replant")) {
            new PlayerReplant();
        }
        if (getPluginManager().isPluginEnabled("Capture")) {
            new PlayerPickEntity();
        }
    }
    public void reload() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        } else reloadConfig();
        getMessage().reload();
    }
    public Collection<? extends Player> getOnlinePlayers() {
        return getServer().getOnlinePlayers();
    }
    public List<OfflinePlayer> getOfflinePlayers() {
        return getUserdata().getOfflinePlayers();
    }
    public Economy getEconomy() {
        return economy;
    }
    public PluginManager getPluginManager() {
        return pluginManager;
    }
    public BukkitScheduler getBukkitScheduler() {
        return bukkitScheduler;
    }
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
    public WorldHandler getWorldHandler() {
        return worldHandler;
    }
    public UUIDHandler getUUIDHandler() {
        return uuidHandler;
    }
    public ScheduleHandler getScheduleHandler() {
        return scheduleHandler;
    }
    public MaterialHandler getMaterialHandler() {
        return materialHandler;
    }
    public GameModeHandler getGameModeHandler() {
        return gameModeHandler;
    }
    public EntityHandler getEntityHandler() {
        return entityHandler;
    }
    public DateHandler getDateHandler() {
        return dateHandler;
    }
    public ChunkHandler getChunkHandler() {
        return chunkHandler;
    }
    public Userdata getUserdata() {
        return userdata;
    }
    public Message getMessage() {
        return message;
    }
    public static Chunks getInstance() {
        return instance;
    }
    public void sendInfo(String message) {
        getLogger().info(message);
    }
    public void sendWarning(String message) {
        getLogger().warning(message);
    }
    public String name() {
        return getDescription().getName();
    }
    public String version() {
        return getDescription().getVersion();
    }
    public String getMinecraftVersion() {
        return getServer().getBukkitVersion();
    }
    public String getMinecraftProvider() {
        return getServer().getName();
    }
    public Player getPlayer(String string) {
        return getServer().getPlayer(string);
    }
    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        return getServer().getOfflinePlayer(uuid);
    }
    public OfflinePlayer getOfflinePlayer(String string) {
        return getServer().getOfflinePlayer(string);
    }
    public WorldGuard getWorldGuard() {
        return WorldGuard.getInstance();
    }
    public boolean isPvpInsideClaims() {
        return getConfig().getBoolean("pvp-inside-claims");
    }
    public boolean isTNTBlockDamageDisabled() {
        return getConfig().getBoolean("disable-tnt-block-damage");
    }
    public boolean isRedstoneOnlyInClaims() {
        return getConfig().getBoolean("redstone-only-in-claims");
    }
    public boolean isBlockPlaceDisabled() {
        return getConfig().getBoolean("disable-block-place");
    }
    public boolean isBlockFertilizeDisabled() {
        return getConfig().getBoolean("disable-block-fertilize");
    }
    public boolean isBlockBreakDisabled() {
        return getConfig().getBoolean("disable-block-break");
    }
    public boolean isCauldronLevelChangeDisabled() {
        return getConfig().getBoolean("disable-cauldron-level-change");
    }
    public boolean isSignChangeDisabled() {
        return getConfig().getBoolean("disable-sign-change");
    }
    public boolean isFluidFromOutsideDisabled() {
        return getConfig().getBoolean("disable-fluid-from-outside");
    }
    public boolean isPistonFromOutsideDisabled() {
        return getConfig().getBoolean("disable-piston-from-outside");
    }
    public boolean isHarvestBlockDisabled(Material blockType) {
        return getConfig().getBoolean("disable-harvest-blocks." + blockType);
    }
    public boolean isPhysicalBlockDisabled(Material blockType) {
        return getConfig().getBoolean("disable-physical-blocks." + blockType);
    }
    public boolean isInteractBlockDisabled(Material blockType) {
        return getConfig().getBoolean("disable-interact-blocks." + blockType);
    }
    public boolean isChangeBlockDisabled(Material blockType) {
        return getConfig().getBoolean("disable-change-blocks." + blockType);
    }
    public boolean isBucketDisabled(Material material) {
        return getConfig().getBoolean("disable-buckets." + material);
    }
    public boolean isBedDisabled(Material blockType) {
        return getConfig().getBoolean("disable-beds." + blockType);
    }
    public boolean manipulateFly() {
        return getConfig().getBoolean("claim.manipulate-fly");
    }
}