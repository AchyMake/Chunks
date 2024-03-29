package org.achymake.chunks.data;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.achymake.chunks.Chunks;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public record Message(Chunks plugin) {
    public File getFile() {
        return new File(plugin.getDataFolder(), "message.yml");
    }
    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(getFile());
    }
    public boolean exist() {
        return getFile().exists();
    }
    public String getString(String path) {
        String text = (String) getConfig().get(path);
        if (text.contains("&")) {
            return addColor(text);
        } else {
            return text;
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
        File file = getFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (exist()) {
            try {
                config.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                sendLog(Level.WARNING, e.getMessage());
            }
        } else {
            config.set("commands.chunk.ban.already-banned", "{0}&c is already banned");
            config.set("commands.chunk.ban.success", "&6You banned&f {0}");
            config.set("commands.chunk.banned.empty", "&cYou do not have any banned members");
            config.set("commands.chunk.banned.title", "&6Banned:");
            config.set("commands.chunk.banned.list", "- {0}");
            config.set("commands.chunk.claim.success", "&6You claimed a chunk for&a {0}");
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
            config.set("commands.chunk.unclaim.all", "&6You unclaimed all chunks and refunded for each&a {0}");
            config.set("commands.chunk.unclaim.claimed", "&cChunk is owned by&f {0}");
            config.set("commands.chunk.unclaim.unclaimed", "&cChunk is already unclaimed");
            config.set("events.block-break", "&c&lHey!&7 Sorry, chunk is owned by&f {0}");
            config.set("events.block-fertilize", "&c&lHey!&7 Sorry, chunk is owned by&f {0}");
            config.set("events.block-place", "&c&lHey!&7 Sorry, chunk is owned by&f {0}");
            config.set("events.cauldron-level-change", "&c&lHey!&7 Sorry, chunk is owned by&f {0}");
            config.set("events.entity-damage-by-entity", "&c&lHey!&7 Sorry, chunk is owned by&f {0}");
            config.set("events.entity-mount", "&c&lHey!&7 Sorry, chunk is owned by&f {0}");
            config.set("events.player-bucket-empty", "&c&lHey!&7 Sorry, chunk is owned by&f {0}");
            config.set("events.player-bucket-entity", "&c&lHey!&7 Sorry, chunk is owned by&f {0}");
            config.set("events.player-bucket-fill", "&c&lHey!&7 Sorry, chunk is owned by&f {0}");
            config.set("events.player-command-preprocess", "&c&lHey!&7 Sorry, chunk is owned by&f {0}");
            config.set("events.player-interact", "&c&lHey!&7 Sorry, chunk is owned by&f {0}");
            config.set("events.player-interact-at-entity", "&c&lHey!&7 Sorry, chunk is owned by&f {0}");
            config.set("events.player-leash-entity", "&c&lHey!&7 Sorry, chunk is owned by&f {0}");
            config.set("events.player-visit-chunk-banned", "&cYou are not allowed to visit&f {0}&c's chunk");
            config.set("events.player-visit-chunk", "&6Visiting&f {0}&6's chunk");
            config.set("events.player-exit-chunk", "&6Exiting&f {0}&6's chunk");
            config.set("events.player-shear-entity", "&c&lHey!&7 Sorry, chunk is owned by&f {0}");
            config.set("events.sign-change", "&c&lHey!&7 Sorry, chunk is owned by&f {0}");
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
        plugin.getLogger().log(level, message);
    }
}