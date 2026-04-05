package org.achymake.chunks.data;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.achymake.chunks.Chunks;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

public class Message {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private final File file = new File(getInstance().getDataFolder(), "message.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public String get(String path) {
        if (config.isString(path)) {
            return addColor(config.getString(path));
        } else return path + ": is missing a value";
    }
    public String get(String path, String... format) {
        if (config.isString(path)) {
            return addColor(MessageFormat.format(config.getString(path), format));
        } else return path + ": is missing a value";
    }
    private void setup() {
        config.set("commands.chunk.ban.success", "&6You banned&f {0}&6 from your chunks");
        config.set("commands.chunk.ban.already-banned", "{0}&c is already banned from chunks");
        config.set("commands.chunk.ban.self", "&cYou are not allowed to ban your self from your own chunks");
        config.set("commands.chunk.banned.title", "&6Banned:");
        config.set("commands.chunk.banned.listed", "- {0}");
        config.set("commands.chunk.banned.empty", "&cBanned members is currently empty");
        config.set("commands.chunk.claim.success", "&6You claimed a chunk for&a {0}");
        config.set("commands.chunk.claim.insufficient-funds", "&cYou do not have&a {0}&c to claim a chunk");
        config.set("commands.chunk.claim.reached-limit", "&cYou have reached&f {0}&c out of&f {1}&c claims");
        config.set("commands.chunk.claim.claimed", "&cChunk is already claimed by&f {0}");
        config.set("commands.chunk.claim.disabled-area", "&cYou are not allowed to claim chunks around here");
        config.set("commands.chunk.help.title", "&6Chunk Help:");
        config.set("commands.chunk.help.ban", "&f/chunk ban player&7 - bans target from chunk");
        config.set("commands.chunk.help.banned", "&f/chunk banned&7 - check banned players");
        config.set("commands.chunk.help.claim", "&f/chunk claim&7 - claims current chunk");
        config.set("commands.chunk.help.help", "&f/chunk help&7 - shows this list");
        config.set("commands.chunk.help.members", "&f/chunk members add/remove player&7 - adds or removes chunk member");
        config.set("commands.chunk.help.tnt", "&f/chunk tnt&7 - toggle tnt for current chunk");
        config.set("commands.chunk.help.unban", "&f/chunk unban target&7 - unbans player from banned");
        config.set("commands.chunk.help.unclaim", "&f/chunk unclaim&7 - unclaim current chunk");
        config.set("commands.chunk.help.view", "&f/chunk view&7 - view claimed chunks");
        config.set("commands.chunk.help.view-others", "&f/chunk view target&7 - view target chunks");
        config.set("commands.chunk.members.title", "&6Members:");
        config.set("commands.chunk.members.listed", "- {0}");
        config.set("commands.chunk.members.empty", "&cMembers is currently empty");
        config.set("commands.chunk.members.add.success", "&6You added&f {0}&6 as member");
        config.set("commands.chunk.members.add.already-member", "{0}&c is already a member");
        config.set("commands.chunk.members.add.self", "&cYou can not add your self to chunk members");
        config.set("commands.chunk.members.remove.success", "&6You added&f {0}&6 as member");
        config.set("commands.chunk.members.remove.invalid", "{0}&c is not a member");
        config.set("commands.chunk.tnt.enable", "&6Current chunk allows tnt explosion");
        config.set("commands.chunk.tnt.disable", "&6Current chunk no longer allows tnt explosion");
        config.set("commands.chunk.tnt.claimed", "&cChunk is already claimed by&f {0}");
        config.set("commands.chunk.tnt.unclaimed", "&cChunk is unclaimed");
        config.set("commands.chunk.unban.success", "&6You unbanned&f {0}&6 from chunks");
        config.set("commands.chunk.unban.invalid", "{0}&c is not banned");
        config.set("commands.chunk.unclaim.success", "&6You unclaimed a chunk and got refunded&a {0}");
        config.set("commands.chunk.unclaim.claimed", "&cChunk is claimed by&f {0}");
        config.set("commands.chunk.unclaim.unclaimed", "&cChunk is already unclaimed");
        config.set("commands.chunk.unclaim.all.success", "&6You unclaimed all chunks and refunded for each&a {0}");
        config.set("events.cancelled.claimed", "&cChunk is owned by&f {0}");
        config.set("events.cancelled.pvp", "&cPVP is disabled inside claimed chunks");
        config.set("events.cancelled.command", "&cYou are not allowed to use&f {0}&c inside&f {1}&c chunks");
        config.set("events.fly.claimed", "&cYou are not allowed to fly inside&f {0}&c chunk!");
        config.set("events.fly.unclaimed", "&cYou are not allowed to fly outside your chunks!");
        config.set("events.move.banned", "&cYou are banned from&f {0}&c chunk");
        config.set("events.move.visit", "&6You entered&f {0}&6 chunk");
        config.set("events.move.exit", "&6You exited&f {0}&6 chunk");
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void reload() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        } else setup();
    }
    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(addColor(message)));
    }
    public String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public String toTitleCase(String string) {
        if (string.contains(" ")) {
            var builder = getBuilder();
            for (var strings : string.split(" ")) {
                builder.append(strings.toUpperCase().charAt(0)).append(strings.substring(1).toLowerCase());
                builder.append(" ");
            }
            return builder.toString().strip();
        } else if (string.contains("_")) {
            var builder = getBuilder();
            for (var strings : string.split("_")) {
                builder.append(strings.toUpperCase().charAt(0)).append(strings.substring(1).toLowerCase());
                builder.append(" ");
            }
            return builder.toString().strip();
        } else return string.toUpperCase().charAt(0) + string.substring(1).toLowerCase();
    }
    public StringBuilder getBuilder() {
        return new StringBuilder();
    }
}