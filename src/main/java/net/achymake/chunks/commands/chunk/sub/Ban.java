package net.achymake.chunks.commands.chunk.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunk.ChunkSubCommand;
import net.achymake.chunks.files.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class Ban extends ChunkSubCommand {
    private Database getDatabase() {
        return Chunks.getDatabase();
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
                if (getDatabase().getBanned(player).contains(target.getUniqueId().toString())) {
                    Chunks.send(player, "&cError:&7 You already banned&f " + target.getName());
                } else {
                    if (getDatabase().getMembers(player).contains(target.getUniqueId().toString())) {
                        List<String> members = getDatabase().getMembers(player);
                        members.remove(target.getUniqueId().toString());
                        getDatabase().setStringList(player, "members", members);
                    }
                    List<String> banned = getDatabase().getBanned(player);
                    banned.add(target.getUniqueId().toString());
                    getDatabase().setStringList(player, "banned", banned);
                    Chunks.send(player, "&6You banned&f " + target.getName());
                }
            }
        }
    }
}