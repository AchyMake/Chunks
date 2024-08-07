package org.achymake.chunks.commands;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChunkCommand implements CommandExecutor, TabCompleter {
    private final Chunks plugin;
    private FileConfiguration getConfig() {
        return plugin.getConfig();
    }
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Economy getEconomy() {
        return plugin.getEconomy();
    }
    private Server getServer() {
        return plugin.getServer();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public ChunkCommand(Chunks plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("claim")) {
                    if (player.hasPermission("chunks.command.chunk.claim")) {
                        Chunk chunk = player.getLocation().getChunk();
                        if (getChunkdata().isAllowedClaim(chunk) ) {
                            if (getChunkdata().isAllowedWorld(chunk.getWorld())) {
                                if (getChunkdata().isClaimed(chunk)) {
                                    if (getChunkdata().isOwner(player ,chunk)) {
                                        getMessage().send(player, "&cYou already own it");
                                    } else {
                                        getMessage().send(player, "&cChunk is already owned by " + getChunkdata().getOwner(chunk).getName());
                                    }
                                } else {
                                    if (getUserdata().getClaimCount(player) >= getConfig().getInt("claim.max-claims")) {
                                        getMessage().send(player, "&cYou have reach you're limit of&f " + getUserdata().getClaimCount(player) + "&c claimed chunks");
                                    } else {
                                        double cost = getConfig().getDouble("economy.cost");
                                        int claimed = getUserdata().getClaimCount(player);
                                        if (claimed > 0) {
                                            int multiply = getConfig().getInt("economy.multiply");
                                            double calculator = multiply * cost / 100 * claimed;
                                            double result = cost + calculator;
                                            if (getEconomy().has(player, result)) {
                                                getChunkdata().setup(player, chunk);
                                                getChunkdata().playEffect(player, chunk, "claim");
                                                getEconomy().withdrawPlayer(player, result);
                                                getMessage().send(player, "&6You claimed a chunk for&a " + getEconomy().currencyNamePlural() + getEconomy().format(result));
                                            } else {
                                                getMessage().send(player, "&cYou do not have&a " +  getEconomy().currencyNamePlural() + result + "&c to claim it");
                                            }
                                        } else {
                                            if (getEconomy().has(player, cost)) {
                                                getChunkdata().setup(player, chunk);
                                                getChunkdata().playEffect(player, chunk, "claim");
                                                getEconomy().withdrawPlayer(player, cost);
                                                getMessage().send(player, "&6You claimed a chunk for&a " + getEconomy().currencyNamePlural() + getEconomy().format(cost));
                                            } else {
                                                getMessage().send(player, "&cYou do not have&a " +  getEconomy().currencyNamePlural() + getEconomy().format(cost) + "&c to claim it");
                                            }
                                        }
                                    }
                                }
                            } else {
                                getMessage().send(player, "&c&lHey!&7 Sorry but you are not allowed to claim here");
                            }
                        } else {
                            getMessage().send(player, "&c&lHey!&7 Sorry but you are not allowed to claim here");
                        }
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("banned")) {
                    if (player.hasPermission("chunks.command.chunk.banned")) {
                        if (getUserdata().getConfig(player).getStringList("banned").isEmpty()) {
                            getMessage().send(player, "&cYou do not have any banned members");
                        } else {
                            getMessage().send(player, "&6Banned:");
                            for (String uuidListed : getUserdata().getConfig(player).getStringList("banned")) {
                                OfflinePlayer offlinePlayer = player.getServer().getOfflinePlayer(UUID.fromString(uuidListed));
                                getMessage().send(player, "- " + offlinePlayer.getName());
                            }
                        }
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("unclaim")) {
                    if (player.hasPermission("chunks.command.chunk.unclaim")) {
                        Chunk chunk = player.getLocation().getChunk();
                        if (getChunkdata().isClaimed(chunk)) {
                            if (getChunkdata().isOwner(player, chunk)) {
                                getMessage().send(player, "&6You unclaimed current chunk and got refunded&a " + getEconomy().currencyNamePlural() + getEconomy().format(getConfig().getDouble("economy.refund")));
                                getChunkdata().remove(player, chunk);
                                getChunkdata().playEffect(player, chunk, "unclaim");
                            } else {
                                getMessage().send(player, "&cChunk is owned by&f " + getChunkdata().getOwner(chunk).getName());
                            }
                        } else {
                            getMessage().send(player, "&cChunk is already unclaimed");
                        }
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("view")) {
                    if (player.hasPermission("chunks.command.chunk.view")) {
                        getUserdata().chunkView(player, player);
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("tnt")) {
                    if (player.hasPermission("chunks.command.chunk.tnt")) {
                        Chunk chunk = player.getLocation().getChunk();
                        if (getChunkdata().isClaimed(chunk)) {
                            if (getChunkdata().isOwner(player, chunk)) {
                                if (getChunkdata().isTNTAllowed(chunk)) {
                                    getChunkdata().toggleTNT(chunk, false);
                                    getMessage().send(player, "&6Disabled&f TNT&6 for current chunk");
                                } else {
                                    getChunkdata().toggleTNT(chunk, true);
                                    getMessage().send(player, "&6Enabled&f TNT&6 for current chunk");
                                }
                            } else {
                                if (player.hasPermission("chunks.command.chunks.edit")) {
                                    if (getChunkdata().isTNTAllowed(chunk)) {
                                        getChunkdata().toggleTNT(chunk, false);
                                        getMessage().send(player, "&6Disabled&f TNT&6 for current chunk");
                                    } else {
                                        getChunkdata().toggleTNT(chunk, true);
                                        getMessage().send(player, "&6Enabled&f TNT&6 for current chunk");
                                    }
                                } else {
                                    getMessage().send(player, "&cChunk is owned by&f " + getChunkdata().getOwner(chunk).getName());
                                }
                            }
                        } else {
                            getMessage().send(player, "&cChunk is unclaimed");
                        }
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("members")) {
                    if (player.hasPermission("chunks.command.chunk.members")) {
                        if (getUserdata().getConfig(player).getStringList("members").isEmpty()) {
                            getMessage().send(player, "&cYou do not have any members");
                        } else {
                            getMessage().send(player, "&6Chunk Members:");
                            for (OfflinePlayer offlinePlayer : getUserdata().getMembers(player)) {
                                getMessage().send(player, "- " + offlinePlayer.getName());
                            }
                        }
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("help")) {
                    if (player.hasPermission("chunks.command.chunk.help")) {
                        getMessage().send(player, "&6Chunk Help:");
                        if (player.hasPermission("chunks.command.chunk.ban")) {
                            getMessage().send(player, "&f/chunk ban player&7 - bans target from chunk");
                        }
                        if (player.hasPermission("chunks.command.chunk.banned")) {
                            getMessage().send(player, "&f/chunk banned&7 - check list of banned");
                        }
                        if (player.hasPermission("chunks.command.chunk.claim")) {
                            getMessage().send(player, "&f/chunk claim&7 - claim current chunk");
                        }
                        getMessage().send(player, "&f/chunk help&7 - show this list");
                        if (player.hasPermission("chunks.command.chunk.members")) {
                            getMessage().send(player, "&f/chunk members add player&7 - adds chunk member");
                            getMessage().send(player, "&f/chunk members remove player&7 - removes chunk member");
                        }
                        if (player.hasPermission("chunks.command.chunk.tnt")) {
                            getMessage().send(player, "&f/chunk tnt&7 - toggle tnt");
                        }
                        if (player.hasPermission("chunks.command.chunk.unban")) {
                            getMessage().send(player, "&f/chunk unban player&7 - unbans target");
                        }
                        if (player.hasPermission("chunks.command.chunk.unclaim")) {
                            getMessage().send(player, "&f/chunk unclaim&7 - unclaim current chunk");
                        }
                        if (player.hasPermission("chunks.command.chunk.view")) {
                            getMessage().send(player, "&f/chunk view&7 - view claimed chunks");
                        }
                        if (player.hasPermission("chunks.command.chunk.view.others")) {
                            getMessage().send(player, "&f/chunk view target&7 - view target claimed chunks");
                        }
                        return true;
                    }
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("ban")) {
                    if (player.hasPermission("chunks.command.chunk.ban")) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                        if (getUserdata().getBanned(player).contains(target)) {
                            getMessage().send(player, target.getName() + "&c is already banned");
                        } else {
                            if (getUserdata().getMembers(player).contains(target)) {
                                List<String> members = getUserdata().getMembersUUIDString(player);
                                List<String> banned = getUserdata().getBannedUUIDString(player);
                                members.remove(target.getUniqueId().toString());
                                banned.add(target.getUniqueId().toString());
                                getUserdata().setStringList(player, "members", members);
                                getUserdata().setStringList(player, "banned", banned);
                                getMessage().send(player, "&6You banned&f " + target.getName());
                            } else {
                                List<String> banned = getUserdata().getBannedUUIDString(player);
                                banned.add(target.getUniqueId().toString());
                                getUserdata().setStringList(player, "banned", banned);
                                getMessage().send(player, "&6You banned&f " + target.getName());
                            }
                        }
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("unclaim")) {
                    if (player.hasPermission("chunks.command.chunk.unclaim")) {
                        if (args[1].equalsIgnoreCase("all")) {
                            getChunkdata().removeAll(player);
                            getMessage().send(player, "&6You unclaimed all chunks and got refunded each for&a " + getEconomy().currencyNamePlural() + getEconomy().format(getConfig().getDouble("chunks.economy.refund")));
                            return true;
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("view")) {
                    if (player.hasPermission("chunks.command.chunk.view")) {
                        if (player.hasPermission("chunks.command.chunk.view.others")) {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                            if (getUserdata().exist(offlinePlayer)) {
                                getUserdata().chunkView(player, offlinePlayer);
                                return true;
                            }
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("unban")) {
                    if (player.hasPermission("chunks.command.chunk.unban")) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                        if (getUserdata().getBanned(player).contains(target.getUniqueId().toString())) {
                            List<String> banned = getUserdata().getBannedUUIDString(player);
                            banned.remove(target.getUniqueId().toString());
                            getUserdata().setStringList(player, "banned", banned);
                            getMessage().send(player, "&6You banned&f " + target.getName() + "&6 from you're chunks");
                        } else {
                            getMessage().send(player, target.getName() + "&c is already banned");
                        }
                        return true;
                    }
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("members")) {
                    if (player.hasPermission("chunks.command.chunk.members")) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
                        if (args[1].equalsIgnoreCase("add")) {
                            if (getUserdata().getMembers(player).contains(target)) {
                                getMessage().send(player, target.getName() + "&c is already a member");
                            } else {
                                List<String> members = getUserdata().getConfig(player).getStringList("members");
                                members.add(target.getUniqueId().toString());
                                getUserdata().setStringList(player, "members", members);
                                getMessage().send(player, target.getName() + "&6 is now a member");
                            }
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("remove")) {
                            if (getUserdata().getConfig(player).getStringList("members").contains(target.getUniqueId().toString())) {
                                List<String> members = getUserdata().getConfig(player).getStringList("members");
                                members.remove(target.getUniqueId().toString());
                                getUserdata().setStringList(player, "members", members);
                                getMessage().send(player, target.getName() + "&6 is now removed from members");
                            } else {
                                getMessage().send(player, target.getName() + "&c is not a member");
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
                if (args[0].equalsIgnoreCase("unclaim")) {
                    if (player.hasPermission("chunks.command.chunk.unclaim")) {
                        commands.add("all");
                    }
                }
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
                            for (OfflinePlayer offlinePlayers : getUserdata().getMembers(player)) {
                                commands.add(offlinePlayers.getName());
                            }
                        }
                    }
                }
            }
        }
        return commands;
    }
}