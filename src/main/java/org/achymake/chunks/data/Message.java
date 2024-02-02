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
import java.util.logging.Level;

public class Message {
    private final Chunks plugin;
    private final File dataFolder;
    public Message(Chunks plugin) {
        dataFolder = plugin.getDataFolder();
        this.plugin = plugin;
    }
    public File getFile() {
        return new File(dataFolder, "message.yml");
    }
    public boolean exist() {
        return getFile().exists();
    }
    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(getFile());
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
    public String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public void sendLog(Level level, String message) {
        plugin.getLogger().log(level, message);
    }
}
