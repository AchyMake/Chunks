package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.UUID;

public class BannedCommand extends ChunkSubCommand {
    private final Chunks plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public BannedCommand(Chunks plugin) {
        this.plugin = plugin;
    }
    @Override
    public String getName() {
        return "banned";
    }
    @Override
    public String getDescription() {
        return "check list of banned players";
    }
    @Override
    public String getSyntax() {
        return "/chunk banned";
    }
    @Override
    public void perform(Player player, String[] args) {
        if (player.hasPermission("chunks.command.chunk.banned")) {
            if (args.length == 1) {
                if (getUserdata().getConfig(player).getStringList("banned").isEmpty()) {
                    player.sendMessage(getMessage().getString("commands.chunk.banned.empty"));
                } else {
                    getMessage().send(player, getMessage().getString("commands.chunk.banned.title"));
                    for (String uuidListed : getUserdata().getConfig(player).getStringList("banned")) {
                        OfflinePlayer offlinePlayer = player.getServer().getOfflinePlayer(UUID.fromString(uuidListed));
                        player.sendMessage(MessageFormat.format(getMessage().getString("commands.chunk.banned.list"), offlinePlayer.getName()));
                    }
                }
            }
        }
    }
}
