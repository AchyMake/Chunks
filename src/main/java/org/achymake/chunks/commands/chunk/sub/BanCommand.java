package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.Userdata;
import org.achymake.chunks.data.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class BanCommand extends ChunkSubCommand {
    private final Userdata userdata;
    private final Message message;
    public BanCommand(Chunks plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
    }
    @Override
    public String getName() {
        return "ban";
    }
    @Override
    public String getDescription() {
        return "bans certain player";
    }
    @Override
    public String getSyntax() {
        return "/chunk ban target";
    }
    @Override
    public void perform(Player player, String[] args) {
        if (player.hasPermission("chunks.command.chunk.ban")) {
            if (args.length == 2) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if (userdata.getBanned(player).contains(target.getUniqueId().toString())) {
                    message.send(player, "&c&lHey!&7 Sorry, but you already banned&f " + target.getName());
                } else {
                    if (userdata.getMembers(player).contains(target.getUniqueId().toString())) {
                        List<String> members = userdata.getMembers(player);
                        members.remove(target.getUniqueId().toString());
                        userdata.setStringList(player, "members", members);
                    }
                    List<String> banned = userdata.getBanned(player);
                    banned.add(target.getUniqueId().toString());
                    userdata.setStringList(player, "banned", banned);
                    message.send(player, "&6You banned&f " + target.getName());
                }
            }
        }
    }
}
