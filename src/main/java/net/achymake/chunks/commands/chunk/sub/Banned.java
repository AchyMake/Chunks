package net.achymake.chunks.commands.chunk.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunk.ChunkSubCommand;
import net.achymake.chunks.files.Database;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Banned extends ChunkSubCommand {
    private Database getDatabase() {
        return Chunks.getDatabase();
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
                    Chunks.send(player, "&cYou don't have any banned players");
                } else {
                    Chunks.send(player, "&6Banned:");
                    for (String uuidListed : getDatabase().getConfig(player).getStringList("banned")) {
                        Chunks.send(player, "- " + player.getServer().getOfflinePlayer(UUID.fromString(uuidListed)).getName());
                    }
                }
            }
        }
    }
}