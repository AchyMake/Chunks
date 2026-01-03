package org.achymake.chunks.commands;

import net.milkbowl.vault.economy.Economy;
import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.ScheduleHandler;
import org.achymake.chunks.handlers.WorldHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChunkCommand implements CommandExecutor, TabCompleter {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private ScheduleHandler getScheduleHandler() {
        return getInstance().getScheduleHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private Economy getEconomy() {
        return getInstance().getEconomy();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public ChunkCommand() {
        getInstance().getCommand("chunk").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("claim")) {
                    if (player.hasPermission("chunks.command.chunk.claim")) {
                        var chunk = player.getLocation().getChunk();
                        if (getWorldHandler().isAllowedClaim(chunk)) {
                            if (!getChunkHandler().isClaimed(chunk)) {
                                if (!getUserdata().hasReachedChunkLimit(player)) {
                                    var cost = getConfig().getDouble("economy.cost");
                                    var claimed = getUserdata().getClaimCount(player);
                                    if (claimed > 0) {
                                        var multiply = getConfig().getInt("economy.multiply");
                                        var calculator = multiply * cost / 100 * claimed;
                                        var result = cost + calculator;
                                        var eco = getEconomy().currencyNamePlural() + getEconomy().format(result);
                                        if (getEconomy().has(player, result)) {
                                            if (getChunkHandler().setOwner(chunk, player)) {
                                                getEconomy().withdrawPlayer(player, result);
                                                getUserdata().playEffect(player, player);
                                                player.sendMessage(getMessage().get("commands.chunk.claim.success", eco));
                                            }
                                        } else player.sendMessage(getMessage().get("commands.chunk.claim.insufficient-funds", eco));
                                    } else {
                                        var eco = getEconomy().currencyNamePlural() + getEconomy().format(cost);
                                        if (getEconomy().has(player, cost)) {
                                            if (getChunkHandler().setOwner(chunk, player)) {
                                                getEconomy().withdrawPlayer(player, cost);
                                                getUserdata().claimEffect(player, chunk);
                                                getUserdata().claimSound(player);
                                                player.sendMessage(getMessage().get("commands.chunk.claim.success", eco));
                                            }
                                        } else player.sendMessage(getMessage().get("commands.chunk.claim.insufficient-funds", eco));
                                    }
                                } else player.sendMessage(getMessage().get("commands.chunk.claim.reached-limit", String.valueOf(getUserdata().getClaimCount(player)), String.valueOf(getUserdata().getMaxClaims(player))));
                            } else player.sendMessage(getMessage().get("commands.chunk.claim.claimed", getChunkHandler().getName(chunk)));
                        } else player.sendMessage(getMessage().get("commands.chunk.claim.disabled-area"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("banned")) {
                    if (player.hasPermission("chunks.command.chunk.banned")) {
                        if (!getUserdata().getBannedStringList(player).isEmpty()) {
                            player.sendMessage(getMessage().get("commands.chunk.banned.title"));
                            for (var offlinePlayer : getUserdata().getBanned(player)) {
                                player.sendMessage(getMessage().get("commands.chunk.banned.listed", offlinePlayer.getName()));
                            }
                        } else player.sendMessage(getMessage().get("commands.chunk.banned.empty"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("unclaim")) {
                    if (player.hasPermission("chunks.command.chunk.unclaim")) {
                        var chunk = player.getLocation().getChunk();
                        if (getChunkHandler().isClaimed(chunk)) {
                            if (getChunkHandler().isOwner(chunk, player)) {
                                if (getChunkHandler().removeOwner(chunk)) {
                                    getUserdata().unclaimEffect(player, chunk);
                                    getUserdata().unclaimSound(player);
                                    player.sendMessage(getMessage().get("commands.chunk.unclaim.success", getEconomy().currencyNamePlural() + getEconomy().format(getConfig().getDouble("economy.refund"))));
                                }
                            } else player.sendMessage(getMessage().get("commands.chunk.unclaim.claimed", getChunkHandler().getName(chunk)));
                        } else player.sendMessage(getMessage().get("commands.chunk.unclaim.unclaimed"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("view")) {
                    if (player.hasPermission("chunks.command.chunk.view")) {
                        var config = getUserdata().getConfig(player);
                        if (config.isInt("tasks.effect")) {
                            var taskID = config.getInt("tasks.effect");
                            if (getScheduleHandler().isQueued(taskID)) {
                                getScheduleHandler().cancel(taskID);
                            }
                            getUserdata().setObject(player, "tasks.effect", null);
                        } else getChunkHandler().scheduleEffect(player);
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("tnt")) {
                    if (player.hasPermission("chunks.command.chunk.tnt")) {
                        var chunk = player.getLocation().getChunk();
                        if (getChunkHandler().isClaimed(chunk)) {
                            if (getChunkHandler().isOwner(chunk, player)) {
                                getChunkHandler().toggleTNT(chunk);
                                if (getChunkHandler().isTNTAllowed(chunk)) {
                                    player.sendMessage(getMessage().get("commands.chunk.tnt.enable"));
                                } else player.sendMessage(getMessage().get("commands.chunk.tnt.disable"));
                            } else if (getUserdata().isEditor(player)) {
                                getChunkHandler().toggleTNT(chunk);
                                if (getChunkHandler().isTNTAllowed(chunk)) {
                                    player.sendMessage(getMessage().get("commands.chunk.tnt.enable"));
                                } else player.sendMessage(getMessage().get("commands.chunk.tnt.disable"));
                            } else player.sendMessage(getMessage().get("commands.chunk.tnt.claimed", getChunkHandler().getName(chunk)));
                        } else player.sendMessage(getMessage().get("commands.chunk.tnt.unclaimed"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("members")) {
                    if (player.hasPermission("chunks.command.chunk.members")) {
                        if (!getUserdata().getMembersStringList(player).isEmpty()) {
                            player.sendMessage(getMessage().get("commands.chunk.members.title"));
                            for (var offlinePlayer : getUserdata().getMembers(player)) {
                                player.sendMessage(getMessage().get("commands.chunk.members.listed", offlinePlayer.getName()));
                            }
                        } else player.sendMessage(getMessage().get("commands.chunk.members.empty"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    if (player.hasPermission("chunks.command.chunk.help")) {
                        player.sendMessage(getMessage().get("commands.chunk.help.title"));
                        if (player.hasPermission("chunks.command.chunk.ban")) {
                            player.sendMessage(getMessage().get("commands.chunk.help.ban"));
                        }
                        if (player.hasPermission("chunks.command.chunk.banned")) {
                            player.sendMessage(getMessage().get("commands.chunk.help.banned"));
                        }
                        if (player.hasPermission("chunks.command.chunk.claim")) {
                            player.sendMessage(getMessage().get("commands.chunk.help.claim"));
                        }
                        player.sendMessage(getMessage().get("commands.chunk.help.help"));
                        if (player.hasPermission("chunks.command.chunk.members")) {
                            player.sendMessage(getMessage().get("commands.chunk.help.members"));
                        }
                        if (player.hasPermission("chunks.command.chunk.tnt")) {
                            player.sendMessage(getMessage().get("commands.chunk.help.tnt"));
                        }
                        if (player.hasPermission("chunks.command.chunk.unban")) {
                            player.sendMessage(getMessage().get("commands.chunk.help.unban"));
                        }
                        if (player.hasPermission("chunks.command.chunk.unclaim")) {
                            player.sendMessage(getMessage().get("commands.chunk.help.unclaim"));
                        }
                        if (player.hasPermission("chunks.command.chunk.view")) {
                            player.sendMessage(getMessage().get("commands.chunk.help.view"));
                        }
                        if (player.hasPermission("chunks.command.chunk.view.others")) {
                            player.sendMessage(getMessage().get("commands.chunk.help.view-others"));
                        }
                        return true;
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("ban")) {
                    if (player.hasPermission("chunks.command.chunk.ban")) {
                        var target = getInstance().getOfflinePlayer(args[1]);
                        if (target != player) {
                            if (!getUserdata().getBanned(player).contains(target)) {
                                if (getUserdata().getMembers(player).contains(target)) {
                                    var members = getUserdata().getMembersStringList(player);
                                    members.remove(target.getUniqueId().toString());
                                    getUserdata().setObject(player, "members", members);
                                }
                                var banned = getUserdata().getBannedStringList(player);
                                banned.add(target.getUniqueId().toString());
                                getUserdata().setObject(player, "banned", banned);
                                player.sendMessage(getMessage().get("commands.chunk.ban.success", target.getName()));
                            } else player.sendMessage(getMessage().get("commands.chunk.ban.already-banned", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.chunk.ban.self"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("unclaim")) {
                    if (player.hasPermission("chunks.command.chunk.unclaim")) {
                        if (args[1].equalsIgnoreCase("all")) {
                            if (getUserdata().removeAll(player)) {
                                player.sendMessage(getMessage().get("commands.chunk.unclaim.all.success", getEconomy().currencyNamePlural() + getEconomy().format(getConfig().getDouble("economy.refund"))));
                                return true;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("view")) {
                    if (player.hasPermission("chunks.command.chunk.view")) {
                        if (player.hasPermission("chunks.command.chunk.view.others")) {
                            var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                            var userdataOffline = getUserdata();
                            if (userdataOffline.exists(offlinePlayer)) {
                                getUserdata().playEffect(player, offlinePlayer);
                                return true;
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("unban")) {
                    if (player.hasPermission("chunks.command.chunk.unban")) {
                        var target = getInstance().getOfflinePlayer(args[1]);
                        if (getUserdata().getBanned(player).contains(target)) {
                            var banned = getUserdata().getBannedStringList(player);
                            banned.remove(target.getUniqueId().toString());
                            getUserdata().setObject(player, "banned", banned);
                            player.sendMessage(getMessage().get("commands.chunk.unban.success", target.getName()));
                        } else player.sendMessage(getMessage().get("commands.chunk.unban.invalid", target.getName()));
                        return true;
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("members")) {
                    if (player.hasPermission("chunks.command.chunk.members")) {
                        var target = getInstance().getOfflinePlayer(args[2]);
                        var members = getUserdata().getMembersStringList(player);
                        if (args[1].equalsIgnoreCase("add")) {
                            if (target != player) {
                                if (!getUserdata().getMembers(player).contains(target)) {
                                    members.add(target.getUniqueId().toString());
                                    getUserdata().setObject(player, "members", members);
                                    player.sendMessage(getMessage().get("commands.chunk.members.add.success", target.getName()));
                                } else player.sendMessage(getMessage().get("commands.chunk.members.add.already-member", target.getName()));
                            } else player.sendMessage(getMessage().get("commands.chunk.members.add.self"));
                            return true;
                        } else if (args[1].equalsIgnoreCase("remove")) {
                            if (getUserdata().getMembers(player).contains(target)) {
                                members.remove(target.getUniqueId().toString());
                                getUserdata().setObject(player, "members", members);
                                player.sendMessage(getMessage().get("commands.chunk.members.remove.success", target.getName()));
                            } else player.sendMessage(getMessage().get("commands.chunk.members.remove.invalid", target.getName()));
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
        var commands = new ArrayList<String>();
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
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("unclaim")) {
                    if (player.hasPermission("chunks.command.chunk.unclaim")) {
                        commands.add("all");
                    }
                } else if (args[0].equalsIgnoreCase("view")) {
                    if (player.hasPermission("chunks.command.chunk.view.others")) {
                        for (var target : getInstance().getOnlinePlayers()) {
                            if (!target.isInvulnerable()) {
                                if (target.getName().startsWith(args[1])) {
                                    commands.add(target.getName());
                                }
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("members")) {
                    if (player.hasPermission("chunks.command.chunk.members")) {
                        commands.add("add");
                        commands.add("remove");
                    }
                } else if (args[0].equalsIgnoreCase("ban")) {
                    if (player.hasPermission("chunks.command.chunk.ban")) {
                        for (var target : getInstance().getOnlinePlayers()) {
                            if (!target.isInvulnerable()) {
                                if (target.getName().startsWith(args[1])) {
                                    commands.add(target.getName());
                                }
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("unban")) {
                    if (player.hasPermission("chunks.command.chunk.unban")) {
                        if (!getUserdata().getBanned(player).isEmpty()) {
                            for (var target : getUserdata().getBanned(player)) {
                                if (target.getName().startsWith(args[1])) {
                                    commands.add(target.getName());
                                }
                            }
                        }
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("members")) {
                    if (player.hasPermission("chunks.command.chunk.members")) {
                        if (args[1].equalsIgnoreCase("add")) {
                            for (var target : getInstance().getOnlinePlayers()) {
                                if (!target.isInvulnerable()) {
                                    if (target.getName().startsWith(args[2])) {
                                        commands.add(target.getName());
                                    }
                                }
                            }
                        } else if (args[1].equalsIgnoreCase("remove")) {
                            if (!getUserdata().getMembers(player).isEmpty()) {
                                for (var offlinePlayer : getUserdata().getMembers(player)) {
                                    if (offlinePlayer.getName().startsWith(args[2])) {
                                        commands.add(offlinePlayer.getName());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return commands;
    }
}