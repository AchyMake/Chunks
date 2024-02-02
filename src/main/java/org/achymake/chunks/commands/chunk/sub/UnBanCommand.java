package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.Userdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class UnBanCommand extends ChunkSubCommand {
    private final Userdata userdata;
    private final Message message;
    public UnBanCommand(Chunks plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
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
                if (userdata.getBanned(player).contains(target.getUniqueId().toString())) {
                    List<String> banned = userdata.getBanned(player);
                    banned.remove(target.getUniqueId().toString());
                    userdata.setStringList(player, "banned", banned);
                    message.send(player, "&6You unbanned&f " + target.getName());
                } else {
                    message.send(player, "&c&lHey!&7 Sorry, but you already banned&f " + target.getName());
                }
            }
        }
    }
}
