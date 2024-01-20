package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.files.Database;
import org.achymake.chunks.files.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class UnBanCommand extends ChunkSubCommand {
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
                if (getDatabase().getBanned(player).contains(target.getUniqueId().toString())) {
                    List<String> banned = getDatabase().getBanned(player);
                    banned.remove(target.getUniqueId().toString());
                    getDatabase().setStringList(player, "banned", banned);
                    getMessage().send(player, "&6You unbanned&f " + target.getName());
                } else {
                    getMessage().send(player, "&cError:&7 You already banned&f " + target.getName());
                }
            }
        }
    }
}
