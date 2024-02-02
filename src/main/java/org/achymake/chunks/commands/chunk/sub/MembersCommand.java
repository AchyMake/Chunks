package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class MembersCommand extends ChunkSubCommand {
    private final Userdata userdata;
    private final Message message;
    public MembersCommand(Chunks plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
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
                if (userdata.getConfig(player).getStringList("members").isEmpty()){
                    message.send(player, "&c&lHey!&7 Sorry, but you don't have any members");
                } else {
                    message.send(player, "&6Chunk Members:");
                    for (String uuidListed : userdata.getConfig(player).getStringList("members")) {
                        message.send(player, "- " + player.getServer().getOfflinePlayer(UUID.fromString(uuidListed)).getName());
                    }
                }
            }
            if (args.length == 3) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
                if (args[1].equalsIgnoreCase("add")) {
                    if (userdata.getConfig(player).getStringList("members").contains(target.getUniqueId().toString())) {
                        message.send(player, "&c&lHey!&7 Sorry, but you already have&f " + target.getName() + "&7 as member");
                    } else {
                        List<String> members = userdata.getConfig(player).getStringList("members");
                        members.add(target.getUniqueId().toString());
                        userdata.setStringList(player, "members", members);
                        message.send(player, "&6You added&f " + target.getName() + "&6 to members");
                    }
                }
                if (args[1].equalsIgnoreCase("remove")) {
                    if (userdata.getConfig(player).getStringList("members").contains(target.getUniqueId().toString())) {
                        List<String> members = userdata.getConfig(player).getStringList("members");
                        members.remove(target.getUniqueId().toString());
                        userdata.setStringList(player, "members", members);
                        message.send(player, "&6You removed&f " + target.getName() + "&6 from members");
                    } else {
                        message.send(player, "&c&lHey!&7 Sorry, but you don't have&f " + target.getName() + "&7 as member");
                    }
                }
            }
        }
    }
}
