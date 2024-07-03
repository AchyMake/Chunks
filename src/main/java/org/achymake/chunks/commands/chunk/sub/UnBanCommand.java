package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class UnBanCommand extends ChunkSubCommand {
    private final Chunks plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public UnBanCommand(Chunks plugin) {
        this.plugin = plugin;
    }
    @Override
    public String getName() {
        return "unban";
    }
    @Override
    public String getDescription() {
        return "unbans certain player";
    }
    @Override
    public String getSyntax() {
        return "/chunk unban target";
    }
    @Override
    public void perform(Player player, String[] args) {
        if (player.hasPermission("chunks.command.chunk.unban")) {
            if (args.length == 2) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if (getUserdata().getBanned(player).contains(target.getUniqueId().toString())) {
                    List<String> banned = getUserdata().getBannedUUIDString(player);
                    banned.remove(target.getUniqueId().toString());
                    getUserdata().setStringList(player, "banned", banned);
                    getMessage().send(player, "&6You banned&f " + target.getName() + "&6 from you're chunks");
                } else {
                    getMessage().send(player, target.getName() + "&c is already banned");
                }
            }
        }
    }
}
