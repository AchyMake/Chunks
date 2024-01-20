package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.files.Database;
import org.achymake.chunks.files.Message;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BannedCommand extends ChunkSubCommand {
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
        return "banned";
    }
    @Override
    public String getDescription() {
        return "check list of banned players";
    }
    @Override
    public String getSyntax() {
        return "/chunk banned";
    }
    @Override
    public void perform(Player player, String[] args) {
        if (player.hasPermission("chunks.command.chunk.banned")) {
            if (args.length == 1) {
                if (getDatabase().getConfig(player).getStringList("banned").isEmpty()){
                    getMessage().send(player, "&cError:&7 You don't have any banned players");
                } else {
                    getMessage().send(player, "&6Banned:");
                    for (String uuidListed : getDatabase().getConfig(player).getStringList("banned")) {
                        getMessage().send(player, "- " + player.getServer().getOfflinePlayer(UUID.fromString(uuidListed)).getName());
                    }
                }
            }
        }
    }
}
