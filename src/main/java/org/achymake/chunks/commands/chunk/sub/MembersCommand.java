package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class MembersCommand extends ChunkSubCommand {
    private final Chunks plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public MembersCommand(Chunks plugin) {
        this.plugin = plugin;
    }
    @Override
    public String getName() {
        return "members";
    }
    @Override
    public String getDescription() {
        return "add or removes members to the chunk";
    }
    @Override
    public String getSyntax() {
        return "/chunk members add/remove target";
    }
    @Override
    public void perform(Player player, String[] args) {
        if (player.hasPermission("chunks.command.chunk.members")) {
            if (args.length == 1) {
                if (getUserdata().getConfig(player).getStringList("members").isEmpty()) {
                    getMessage().send(player, "&cYou do not have any members");
                } else {
                    getMessage().send(player, "&6Chunk Members:");
                    for (OfflinePlayer offlinePlayer : getUserdata().getMembers(player)) {
                        getMessage().send(player, "- " + offlinePlayer.getName());
                    }
                }
            }
            if (args.length == 3) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
                if (args[1].equalsIgnoreCase("add")) {
                    if (getUserdata().getMembers(player).contains(target)) {
                        getMessage().send(player, target.getName() + "&c is already a member");
                    } else {
                        List<String> members = getUserdata().getConfig(player).getStringList("members");
                        members.add(target.getUniqueId().toString());
                        getUserdata().setStringList(player, "members", members);
                        getMessage().send(player, target.getName() + "&6 is now a member");
                    }
                }
                if (args[1].equalsIgnoreCase("remove")) {
                    if (getUserdata().getConfig(player).getStringList("members").contains(target.getUniqueId().toString())) {
                        List<String> members = getUserdata().getConfig(player).getStringList("members");
                        members.remove(target.getUniqueId().toString());
                        getUserdata().setStringList(player, "members", members);
                        getMessage().send(player, target.getName() + "&6 is now removed from members");
                    } else {
                        getMessage().send(player, target.getName() + "&c is not a member");
                    }
                }
            }
        }
    }
}
