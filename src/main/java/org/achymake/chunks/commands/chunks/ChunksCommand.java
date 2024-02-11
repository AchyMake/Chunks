package org.achymake.chunks.commands.chunks;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.commands.chunks.sub.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ChunksCommand implements CommandExecutor, TabCompleter {
    private final ArrayList<ChunksSubCommand> chunksSubCommands = new ArrayList<>();
    public ChunksCommand(Chunks plugin) {
        chunksSubCommands.add(new EditCommand(plugin));
        chunksSubCommands.add(new EffectCommand(plugin));
        chunksSubCommands.add(new HelpCommand(plugin));
        chunksSubCommands.add(new InfoCommand(plugin));
        chunksSubCommands.add(new ReloadCommand(plugin));
        chunksSubCommands.add(new SetOwnerCommand(plugin));
        chunksSubCommands.add(new UnClaimCommand(plugin));
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            for (ChunksSubCommand commands : chunksSubCommands) {
                if (args[0].equals(commands.getName())) {
                    commands.perform(sender, args);
                }
            }
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("chunks.command.chunks.unclaim")) {
                commands.add("unclaim");
            }
            if (sender.hasPermission("chunks.command.chunks.edit")) {
                commands.add("edit");
            }
            if (sender.hasPermission("chunks.command.chunks.effect")) {
                commands.add("effect");
            }
            if (sender.hasPermission("chunks.command.chunks.help")) {
                commands.add("help");
            }
            if (sender.hasPermission("chunks.command.chunks.info")) {
                commands.add("info");
            }
            if (sender.hasPermission("chunks.command.chunks.reload")) {
                commands.add("reload");
            }
            if (sender.hasPermission("chunks.command.chunks.setowner")) {
                commands.add("setowner");
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("unclaim")) {
                if (sender.hasPermission("chunks.command.chunk.unclaim")) {
                    for (OfflinePlayer offlinePlayers : sender.getServer().getOfflinePlayers()) {
                        commands.add(offlinePlayers.getName());
                    }
                }
            }
            if (args[0].equalsIgnoreCase("effect")) {
                if (sender.hasPermission("chunks.command.chunks.effect")) {
                    commands.add("claim");
                    commands.add("unclaim");
                }
            }
            if (args[0].equalsIgnoreCase("info")) {
                if (sender.hasPermission("chunks.command.chunks.info")) {
                    for (OfflinePlayer offlinePlayers : sender.getServer().getOfflinePlayers()) {
                        commands.add(offlinePlayers.getName());
                    }
                }
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("chunks.command.chunks.reload")) {
                    commands.add("players");
                }
            }
            if (args[0].equalsIgnoreCase("setowner")) {
                if (sender.hasPermission("chunks.command.chunks.setowner")) {
                    for (OfflinePlayer offlinePlayers : sender.getServer().getOfflinePlayers()) {
                        commands.add(offlinePlayers.getName());
                    }
                }
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("unclaim")) {
                if (sender.hasPermission("chunks.command.chunk.unclaim")) {
                    commands.add("all");
                }
            }
        }
        return commands;
    }
}
