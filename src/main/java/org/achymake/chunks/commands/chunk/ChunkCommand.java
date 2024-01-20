package org.achymake.chunks.commands.chunk;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.sub.*;
import org.achymake.chunks.files.Database;
import org.achymake.chunks.files.Message;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChunkCommand implements CommandExecutor, TabCompleter {
    private Chunks getPlugin() {
        return Chunks.getInstance();
    }
    private Database getDatabase() {
        return getPlugin().getDatabase();
    }
    private Message getMessage() {
        return getPlugin().getMessage();
    }
    private final ArrayList<ChunkSubCommand> chunkSubCommands = new ArrayList<>();

    public ChunkCommand() {
        chunkSubCommands.add(new BanCommand());
        chunkSubCommands.add(new BannedCommand());
        chunkSubCommands.add(new ClaimCommand());
        chunkSubCommands.add(new HelpCommand());
        chunkSubCommands.add(new MembersCommand());
        chunkSubCommands.add(new TNTCommand());
        chunkSubCommands.add(new UnBanCommand());
        chunkSubCommands.add(new UnClaimCommand());
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                getMessage().send(player, "&cUsage: &f/chunk help");
                return true;
            } else {
                for (ChunkSubCommand commands : getSubCommands()) {
                    if (args[0].equals(commands.getName())) {
                        commands.perform(player, args);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public ArrayList<ChunkSubCommand> getSubCommands(){
        return chunkSubCommands;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("chunks.command.chunk.ban")) {
                    commands.add("ban");
                }
                if (player.hasPermission("chunks.command.chunk.banned")) {
                    commands.add("banned");
                }
                if (player.hasPermission("chunks.command.chunk.claim")) {
                    commands.add("claim");
                }
                if (player.hasPermission("chunks.command.chunk.help")) {
                    commands.add("help");
                }
                if (player.hasPermission("chunks.command.chunk.members")) {
                    commands.add("members");
                }
                if (player.hasPermission("chunks.command.chunk.tnt")) {
                    commands.add("tnt");
                }
                if (player.hasPermission("chunks.command.chunk.unban")) {
                    commands.add("unban");
                }
                if (player.hasPermission("chunks.command.chunk.unclaim")) {
                    commands.add("unclaim");
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("chunks.command.chunk.members")) {
                    if (args[0].equalsIgnoreCase("members")) {
                        commands.add("add");
                        commands.add("remove");
                    }
                }
                if (player.hasPermission("chunks.command.chunk.ban")) {
                    if (args[0].equalsIgnoreCase("ban")) {
                        for (OfflinePlayer players : player.getServer().getOfflinePlayers()) {
                            commands.add(players.getName());
                        }
                    }
                }
                if (player.hasPermission("chunks.command.chunk.unban")) {
                    if (args[0].equalsIgnoreCase("unban")) {
                        for (OfflinePlayer players : player.getServer().getOfflinePlayers()) {
                            commands.add(players.getName());
                        }
                    }
                }
            }
            if (args.length == 3) {
                if (player.hasPermission("chunks.command.chunk.members")) {
                    if (args[0].equalsIgnoreCase("members")) {
                        if (args[1].equalsIgnoreCase("add")) {
                            for (OfflinePlayer players : player.getServer().getOfflinePlayers()) {
                                commands.add(players.getName());
                            }
                        }
                        if (args[1].equalsIgnoreCase("remove")) {
                            for (String uuidString : getDatabase().getConfig(player).getStringList("members")) {
                                commands.add(player.getServer().getOfflinePlayer(UUID.fromString(uuidString)).getName());
                            }
                        }
                    }
                }
            }
        }
        return commands;
    }
}
