package org.achymake.chunks.data;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.achymake.chunks.Chunks;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Message {
    public Chunks getPlugin() {
        return Chunks.getInstance();
    }
    private final File file = new File(getPlugin().getDataFolder(), "message.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public boolean exist() {
        return file.exists();
    }
    public File getFile() {
        return file;
    }
    public FileConfiguration getConfig() {
        return config;
    }
    public String getString(String path) {
        if (config.isString(path)) {
            String text = config.getString(path);
            if (text.contains("&")) {
                return addColor(text);
            } else {
                return config.getString(path);
            }
        } else {
            return path + " is not string :c";
        }
    }
    public void send(ConsoleCommandSender sender, String message) {
        sender.sendMessage(message);
    }
    public void send(Player player, String message) {
        player.sendMessage(addColor(message));
    }
    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(addColor(message)));
    }
    public void reload() {
        if (exist()) {
            config = YamlConfiguration.loadConfiguration(file);
        } else {
            config.set("commands.chunk.ban.already-banned", "{0}&c is already banned");
            config.set("commands.chunk.ban.success", "&6You banned&f {0}");
            config.set("commands.chunk.banned.empty", "&cYou do not have any banned members");
            config.set("commands.chunk.banned.title", "&6Banned:");
            config.set("commands.chunk.banned.list", "- {0}");
            config.set("commands.chunk.claim.already-owned", "&cYou already own the chunk");
            config.set("commands.chunk.claim.already-claimed", "&cChunk is already claimed by&f {0}");
            config.set("commands.chunk.claim.insufficient-funds", "&cYou do not have&a {0}&c to claim chunk");
            config.set("commands.chunk.claim.insufficient-claims", "&cYou have reach you're limit to&f {0}");
            config.set("commands.chunk.claim.worldguard", "&c&lHey!&7 Sorry, but you are not allowed to claim here");
            config.set("commands.chunk.help.title", "&6Chunk Help:");
            config.set("commands.chunk.help.ban", "&f/chunk ban player&7 - bans target from chunk");
            config.set("commands.chunk.help.banned", "&f/chunk banned&7 - check list of banned");
            config.set("commands.chunk.help.claim", "&f/chunk claim&7 - claim current chunk");
            config.set("commands.chunk.help.help", "&f/chunk help&7 - show this list");
            List<String> members = new ArrayList<>();
            members.add("/chunk members add player&7 - adds chunk member");
            members.add("/chunk members remove player&7 - removes chunk member");
            config.set("commands.chunk.help.members", members);
            config.set("commands.chunk.help.tnt", "/chunk tnt&7 - toggle tnt");
            config.set("commands.chunk.help.unban", "/chunk unban player&7 - unbans target");
            config.set("commands.chunk.help.unclaim", "/chunk unclaim&7 - unclaim current chunk");
            config.set("commands.chunk.members.empty", "&cYou do not have any members");
            config.set("commands.chunk.members.title", "&6Chunk Members:");
            config.set("commands.chunk.members.list", "- {0}");
            config.set("commands.chunk.members.add.already-member", "{0}&c is already a member");
            config.set("commands.chunk.members.add.success", "&6You added&f {0}&6 as member");
            config.set("commands.chunk.members.remove.non-member", "{0}&c is not a member");
            config.set("commands.chunk.members.remove.success", "&6You removed&f {0}&6 as member");
            config.set("commands.chunk.tnt.enable", "&6You enable tnt");
            config.set("commands.chunk.tnt.disable", "&6You disable tnt");
            config.set("commands.chunk.tnt.unclaimed", "&cChunk is unclaimed");
            config.set("commands.chunk.unban.success", "&6You unbanned&f {0}");
            config.set("commands.chunk.unban.already-unbanned", "{0}&c is already unbanned");
            config.set("commands.chunk.unclaim.success", "&6You unclaimed a chunk and refunded for&a {0}");
            config.set("commands.chunk.unclaim.claimed", "&cChunk is owned by&f {0}");
            config.set("commands.chunk.unclaim.unclaimed", "&cChunk is already unclaimed");
            config.set("events.block-break", "&cYou are not allowed to break blocks inside&f {0}&c's chunk");
            config.set("events.block-fertilize", "&cYou are not allowed to fertilize blocks inside&f {0}&c's chunk");
            config.set("events.cauldron-level-change", "&cYou are not allowed to change cauldrons inside&f {0}&c's chunk");
            config.set("events.entity-damage-by-entity", "&cYou are not allowed to damage entities inside&f {0}&c's chunk");
            config.set("events.entity-mount", "&cYou are not allowed to mount inside&f {0}'s chunk");
            config.set("events.player-bucket-empty", "&cYou are not allowed to empty buckets inside&f {0}&c's chunk");
            config.set("events.player-bucket-entity", "&cYou are not allowed to bucket entities from&f {0}&c's chunk");
            config.set("events.player-bucket-fill", "&cYou are not allowed to fill buckets from&f {0}&c's chunk");
            config.set("events.player-command-preprocess", "&cYou are not allowed to sethome inside&f {0}&c's chunk");
            config.set("events.player-interact", "&cYou are not allowed to interact blocks from&f {0}&c's chunk");
            config.set("events.player-interact-at-entity", "&cYou are not allowed to interact entities from&f {0}&c's chunk");
            config.set("events.player-leash-entity", "&cYou are not allowed to leash entities from&f {0}&c's chunk");
            config.set("events.player-visit-chunk-banned", "&cYou are not allowed to visit&f {0}'s chunk");
            config.set("events.player-visit-chunk", "&6Visiting&f {0}&6's chunk");
            config.set("events.player-exit-chunk", "&6Exiting&f {0}&6's chunk");
            config.set("events.player-shear-entity", "&cYou are not allowed to shear entities from&f {0}&c's chunk");
            config.set("events.sign-change", "&cYou are not allowed to change signs from&f {0}&c's chunk");
            config.options().copyDefaults(true);
            try {
                config.save(file);
            } catch (IOException e) {
                sendLog(Level.WARNING, e.getMessage());
            }
        }
    }
    public String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public void sendLog(Level level, String message) {
        getPlugin().getLogger().log(level, message);
    }
}