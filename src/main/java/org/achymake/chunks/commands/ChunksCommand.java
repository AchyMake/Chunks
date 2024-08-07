package org.achymake.chunks.commands;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChunksCommand implements CommandExecutor, TabCompleter {
    private final Chunks plugin;
    private Userdata getUserdata() {
        return plugin.getUserdata();
    }
    private Chunkdata getChunkdata() {
        return plugin.getChunkdata();
    }
    private Message getMessage() {
        return plugin.getMessage();
    }
    public ChunksCommand(Chunks plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (player.hasPermission("chunks.command.chunks.reload")) {
                        plugin.reload();
                        getMessage().send(player, "&6Chunks:&f reloaded");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("unclaim")) {
                    if (player.hasPermission("chunks.command.chunks.unclaim")) {
                        Chunk chunk = player.getLocation().getChunk();
                        if (getChunkdata().isClaimed(chunk)) {
                            getMessage().send(player, "&6You safely unclaimed&f " + getChunkdata().getOwner(chunk).getName() + "&6's chunk");
                            getChunkdata().remove(getChunkdata().getOwner(chunk), chunk);
                            getChunkdata().playEffect(player, chunk, "unclaim");
                        } else {
                            getMessage().send(player, "&cCurrent chunk is already unclaimed");
                        }
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("info")) {
                    if (player.hasPermission("chunks.command.chunks.info")) {
                        Chunk chunk = player.getLocation().getChunk();
                        if (getChunkdata().isClaimed(chunk)) {
                            getMessage().send(player, "&6Chunks Info:&f Chunk");
                            getMessage().send(player, "&6Owner:&f " + getChunkdata().getOwner(chunk).getName());
                            getMessage().send(player, "&6Date claimed:&f " + getChunkdata().getDateClaimed(chunk));
                            getMessage().send(player, "&6Chunks claimed:&f " + getChunkdata().getClaimCount(chunk));
                            if (getUserdata().getMembers(getChunkdata().getOwner(chunk)).isEmpty()) {
                                getMessage().send(player, getChunkdata().getOwner(chunk).getName() + "&6 has no members");
                            } else {
                                getMessage().send(player, getChunkdata().getOwner(chunk).getName()+"&6 members:");
                                for (OfflinePlayer offlinePlayer : getUserdata().getMembers(getChunkdata().getOwner(chunk))) {
                                    getMessage().send(player, "- " + offlinePlayer.getName());
                                }
                            }
                        }
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("help")) {
                    if (player.hasPermission("chunks.command.chunks.help")) {
                        getMessage().send(player, "&6Chunks Help:");
                        if (player.hasPermission("chunks.command.chunks.delete")) {
                            getMessage().send(player, "&f/chunks delete &7- safely unclaims chunk");
                        }
                        if (player.hasPermission("chunks.command.chunks.edit")) {
                            getMessage().send(player, "&f/chunks edit &7- toggle chunk edit");
                        }
                        if (player.hasPermission("chunks.command.chunks.effect")) {
                            getMessage().send(player, "&f/chunks effect claim &7- effects of claiming");
                            getMessage().send(player, "&f/chunks effect unclaim &7- effects of unclaiming");
                        }
                        getMessage().send(player, "&f/chunks help &7- show this list");
                        if (player.hasPermission("chunks.command.chunks.info")) {
                            getMessage().send(player, "&f/chunks info &7- checks info of chunk");
                        }
                        if (player.hasPermission("chunks.command.chunks.reload")) {
                            getMessage().send(player, "&f/chunks reload &7- reload chunks plugin");
                        }
                        if (player.hasPermission("chunks.command.chunks.setowner")) {
                            getMessage().send(player, "&f/chunks setowner target &7- sets chunk owner");
                        }
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("edit")) {
                    if (player.hasPermission("chunks.command.chunks.edit")) {
                        if (plugin.getChunkEditors().contains(player)) {
                            plugin.getChunkEditors().remove(player);
                            getMessage().sendActionBar(player, "&6Chunk Edit:&c Disabled");
                        } else {
                            plugin.getChunkEditors().add(player);
                            getMessage().sendActionBar(player, "&6Chunk Edit:&a Enabled");
                        }
                        return true;
                    }
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("effect")) {
                    if (player.hasPermission("chunks.command.chunks.effect")) {
                        if (args[1].equalsIgnoreCase("claim")) {
                            Chunk chunk = player.getLocation().getChunk();
                            getChunkdata().playEffect(player, chunk, args[1]);
                            getMessage().sendActionBar(player, "&6Started the effects of Claiming");
                        }
                        if (args[1].equalsIgnoreCase("unclaim")) {
                            Chunk chunk = player.getLocation().getChunk();
                            getChunkdata().playEffect(player, chunk, args[1]);
                            getMessage().sendActionBar(player, "&6Started the effects of Unclaiming");
                        }
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("info")) {
                    if (player.hasPermission("chunks.command.chunks.info")) {
                        OfflinePlayer target = player.getServer().getOfflinePlayer(args[1]);
                        if (getUserdata().exist(target)) {
                            getMessage().send(player, "&6Chunks Info:&f "+target.getName());
                            getMessage().send(player, "&6Chunks claimed:&f " + getUserdata().getClaimCount(target));
                            if (getUserdata().getMembers(target).isEmpty()) {
                                getMessage().send(player, target.getName() + "&6 has no members");
                            } else {
                                getMessage().send(player, "&6Members:");
                                for (OfflinePlayer offlinePlayer : getUserdata().getMembers(target)) {
                                    getMessage().send(player, "- " + offlinePlayer.getName());
                                }
                            }
                        }
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("setowner")) {
                    if (player.hasPermission("chunks.command.chunks.setowner")) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                        Chunk chunk = player.getLocation().getChunk();
                        if (getChunkdata().isAllowedClaim(chunk)) {
                            if (getUserdata().exist(target)) {
                                getChunkdata().setOwner(player, target, chunk);
                                getChunkdata().playEffect(player, chunk, "claim");
                                getMessage().send(player, "&6Chunk is now owned by&f " + getChunkdata().getOwner(chunk).getName());
                            } else {
                                getMessage().send(player, target.getName() + "&c has never joined");
                            }
                        } else {
                            getMessage().send(player, "&c&lHey!&7 Sorry but you are not allowed to claim here");
                        }
                        return true;
                    }
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("unclaim")) {
                    if (player.hasPermission("chunks.command.chunks.unclaim")) {
                        if (args[2].equalsIgnoreCase("all")) {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                            getChunkdata().removeAll(offlinePlayer);
                            getMessage().send(player, "&6You safely unclaimed all&f " + offlinePlayer.getName() + "&6's chunks");
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
        if (args.length == 1) {
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
            if (sender.hasPermission("chunks.command.chunks.unclaim")) {
                commands.add("unclaim");
            }
        }
        if (args.length == 2) {
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
            if (args[0].equalsIgnoreCase("setowner")) {
                if (sender.hasPermission("chunks.command.chunks.setowner")) {
                    for (OfflinePlayer offlinePlayers : sender.getServer().getOfflinePlayers()) {
                        commands.add(offlinePlayers.getName());
                    }
                }
            }
            if (args[0].equalsIgnoreCase("unclaim")) {
                if (sender.hasPermission("chunks.command.chunks.unclaim")) {
                    for (OfflinePlayer offlinePlayers : sender.getServer().getOfflinePlayers()) {
                        commands.add(offlinePlayers.getName());
                    }
                }
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("unclaim")) {
                if (sender.hasPermission("chunks.command.chunks.unclaim")) {
                    commands.add("all");
                }
            }
        }
        return commands;
    }
}
