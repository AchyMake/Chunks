package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.files.Database;
import org.achymake.chunks.files.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class BanCommand extends ChunkSubCommand {
    private Chunks getPlugin() {
        return Chunks.getInstance();
    }
    private Database getDatabase() {
        return getPlugin().getDatabase();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
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
                if (target == null) {
                    getMessage().send(player, "&cError:&f " + args[1] + "&7 does not exist.");
                } else {
                    if (getDatabase().getBanned(player).contains(target.getUniqueId().toString())) {
                        getMessage().send(player, "&cError:&7 You already banned&f " + target.getName());
                    } else {
                        if (getDatabase().getMembers(player).contains(target.getUniqueId().toString())) {
                            List<String> members = getDatabase().getMembers(player);
                            members.remove(target.getUniqueId().toString());
                            getDatabase().setStringList(player, "members", members);
                        }
                        List<String> banned = getDatabase().getBanned(player);
                        banned.add(target.getUniqueId().toString());
                        getDatabase().setStringList(player, "banned", banned);
                        getMessage().send(player, "&6You banned&f " + target.getName());
                    }
                }
            }
        }
    }
}
