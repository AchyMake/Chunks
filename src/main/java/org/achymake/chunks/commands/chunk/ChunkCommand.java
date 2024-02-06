package org.achymake.chunks.commands.chunk;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunk.sub.*;
import org.achymake.chunks.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChunkCommand implements CommandExecutor, TabCompleter {
    private final Chunks plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    private final ArrayList<ChunkSubCommand> chunkSubCommands = new ArrayList<>();
    public ChunkCommand(Chunks plugin) {
        this.plugin = plugin;
        chunkSubCommands.add(new BanCommand(plugin));
        chunkSubCommands.add(new BannedCommand(plugin));
        chunkSubCommands.add(new ClaimCommand(plugin));
        chunkSubCommands.add(new HelpCommand(plugin));
        chunkSubCommands.add(new MembersCommand(plugin));
        chunkSubCommands.add(new TNTCommand(plugin));
        chunkSubCommands.add(new UnBanCommand(plugin));
        chunkSubCommands.add(new UnClaimCommand(plugin));
        chunkSubCommands.add(new ViewCommand(plugin));
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                for (ChunkSubCommand commands : getSubCommands()) {
                    if (args[0].equals(commands.getName())) {
                        commands.perform(player, args);
                        return true;
                    }
                }
                return true;
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
                if (player.hasPermission("chunks.command.chunk.view")) {
                    commands.add("view");
                }
            }
            if (args.length == 2) {
                if (player.hasPermission("chunks.command.chunk.view.others")) {
                    if (args[0].equalsIgnoreCase("view")) {
                        for (OfflinePlayer offlinePlayer : player.getServer().getOfflinePlayers()) {
                            if (getUserdata().exist(offlinePlayer)) {
                                commands.add(offlinePlayer.getName());
                            }
                        }
                    }
                }
                if (player.hasPermission("chunks.command.chunk.members")) {
                    if (args[0].equalsIgnoreCase("members")) {
                        commands.add("add");
                        commands.add("remove");
                    }
                }
                if (player.hasPermission("chunks.command.chunk.ban")) {
                    if (args[0].equalsIgnoreCase("ban")) {
                        for (OfflinePlayer players : getServer().getOfflinePlayers()) {
                            commands.add(players.getName());
                        }
                    }
                }
                if (player.hasPermission("chunks.command.chunk.unban")) {
                    if (args[0].equalsIgnoreCase("unban")) {
                        for (OfflinePlayer players : getServer().getOfflinePlayers()) {
                            commands.add(players.getName());
                        }
                    }
                }
            }
            if (args.length == 3) {
                if (player.hasPermission("chunks.command.chunk.members")) {
                    if (args[0].equalsIgnoreCase("members")) {
                        if (args[1].equalsIgnoreCase("add")) {
                            for (OfflinePlayer players : getServer().getOfflinePlayers()) {
                                commands.add(players.getName());
                            }
                        }
                        if (args[1].equalsIgnoreCase("remove")) {
                            for (String uuidString : getUserdata().getConfig(player).getStringList("members")) {
                                commands.add(getServer().getOfflinePlayer(UUID.fromString(uuidString)).getName());
                            }
                        }
                    }
                }
            }
        }
        return commands;
    }
}
