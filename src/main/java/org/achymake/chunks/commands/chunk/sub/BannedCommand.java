package org.achymake.chunks.commands.chunk.sub;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.ChunkSubCommand;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BannedCommand extends ChunkSubCommand {
    private final Userdata userdata;
    private final Message message;
    public BannedCommand(Chunks plugin) {
        userdata = plugin.getUserdata();
        message = plugin.getMessage();
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
                if (userdata.getConfig(player).getStringList("banned").isEmpty()){
                    message.send(player, "&c&lHey!&7 Sorry, but you don't have any banned players");
                } else {
                    message.send(player, "&6Banned:");
                    for (String uuidListed : userdata.getConfig(player).getStringList("banned")) {
                        message.send(player, "- " + player.getServer().getOfflinePlayer(UUID.fromString(uuidListed)).getName());
                    }
                }
            }
        }
    }
}
