package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

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
                    player.sendMessage(getMessage().getString("commands.chunk.members.empty"));
                } else {
                    player.sendMessage(getMessage().getString("commands.chunk.members.title"));
                    for (String uuidListed : getUserdata().getConfig(player).getStringList("members")) {
                        OfflinePlayer offlinePlayer = player.getServer().getOfflinePlayer(UUID.fromString(uuidListed));
                        player.sendMessage(MessageFormat.format(getMessage().getString("commands.chunk.members.list"), offlinePlayer.getName()));
                    }
                }
            }
            if (args.length == 3) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
                if (args[1].equalsIgnoreCase("add")) {
                    if (getUserdata().getConfig(player).getStringList("members").contains(target.getUniqueId().toString())) {
                        player.sendMessage(MessageFormat.format(getMessage().getString("commands.chunk.members.add.already-member"), target.getName()));
                    } else {
                        List<String> members = getUserdata().getConfig(player).getStringList("members");
                        members.add(target.getUniqueId().toString());
                        getUserdata().setStringList(player, "members", members);
                        player.sendMessage(MessageFormat.format(getMessage().getString("commands.chunk.members.add.success"), target.getName()));
                    }
                }
                if (args[1].equalsIgnoreCase("remove")) {
                    if (getUserdata().getConfig(player).getStringList("members").contains(target.getUniqueId().toString())) {
                        List<String> members = getUserdata().getConfig(player).getStringList("members");
                        members.remove(target.getUniqueId().toString());
                        getUserdata().setStringList(player, "members", members);
                        player.sendMessage(MessageFormat.format(getMessage().getString("commands.chunk.members.remove.success"), target.getName()));
                    } else {
                        player.sendMessage(MessageFormat.format(getMessage().getString("commands.chunk.members.remove.non-member"), target.getName()));
                    }
                }
            }
        }
    }
}
