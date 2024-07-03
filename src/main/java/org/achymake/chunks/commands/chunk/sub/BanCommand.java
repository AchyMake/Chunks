package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class BanCommand extends ChunkSubCommand {
    private final Chunks plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public BanCommand(Chunks plugin) {
        this.plugin = plugin;
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
                if (getUserdata().getBanned(player).contains(target)) {
                    getMessage().send(player, target.getName() + "&c is already banned");
                } else {
                    if (getUserdata().getMembers(player).contains(target)) {
                        List<String> members = getUserdata().getMembersUUIDString(player);
                        members.remove(target.getUniqueId().toString());
                        getUserdata().setStringList(player, "members", members);
                    } else {
                        List<String> banned = getUserdata().getBannedUUIDString(player);
                        banned.add(target.getUniqueId().toString());
                        getUserdata().setStringList(player, "banned", banned);
                        getMessage().send(player, "&6You banned&f " + target.getName());
                    }
                }
            }
        }
    }
}
