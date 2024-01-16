package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.files.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class MembersCommand extends ChunkSubCommand {
    private Database getDatabase() {
        return Chunks.getDatabase();
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
                if (getDatabase().getConfig(player).getStringList("members").isEmpty()){
                    Chunks.send(player, "&cYou don't have any members");
                } else {
                    Chunks.send(player, "&6Chunk Members:");
                    for (String uuidListed : getDatabase().getConfig(player).getStringList("members")) {
                        Chunks.send(player, "- " + player.getServer().getOfflinePlayer(UUID.fromString(uuidListed)).getName());
                    }
                }
            }
            if (args.length == 3) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
                if (args[1].equalsIgnoreCase("add")) {
                    if (getDatabase().getConfig(player).getStringList("members").contains(target.getUniqueId().toString())) {
                        Chunks.send(player, "&cYou already have&f " + target.getName() + "&c as member");
                    } else {
                        List<String> members = getDatabase().getConfig(player).getStringList("members");
                        members.add(target.getUniqueId().toString());
                        getDatabase().setStringList(player, "members", members);
                        Chunks.send(player, "&6You added&f " + target.getName() + "&6 to members");
                    }
                }
                if (args[1].equalsIgnoreCase("remove")) {
                    if (getDatabase().getConfig(player).getStringList("members").contains(target.getUniqueId().toString())) {
                        List<String> members = getDatabase().getConfig(player).getStringList("members");
                        members.remove(target.getUniqueId().toString());
                        getDatabase().setStringList(player, "members", members);
                        Chunks.send(player, "&6You removed&f " + target.getName() + "&6 from members");
                    } else {
                        Chunks.send(player, "&6You don't have&f " + target.getName() + "&6 as member");
                    }
                }
            }
        }
    }
}
