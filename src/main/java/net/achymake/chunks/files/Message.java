package net.achymake.chunks.files;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Message {
    private final Logger logger;
    public Message(Logger logger) {
        this.logger = logger;
    }
    public void send(CommandSender sender, String message) {
        sender.sendMessage(addColor(message));
    }
    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(addColor(message)));
    }
    public String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public void sendLog(Level level, String message) {
        logger.log(level, message);
    }
}