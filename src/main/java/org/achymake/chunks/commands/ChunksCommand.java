package org.achymake.chunks.commands;

import org.achymake.chunks.Chunks;
import org.achymake.chunks.data.Message;
import org.achymake.chunks.data.Userdata;
import org.achymake.chunks.handlers.ChunkHandler;
import org.achymake.chunks.handlers.WorldHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChunksCommand implements CommandExecutor, TabCompleter {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private ChunkHandler getChunkHandler() {
        return getInstance().getChunkHandler();
    }
    private WorldHandler getWorldHandler() {
        return getInstance().getWorldHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public ChunksCommand() {
        getInstance().getCommand("chunks").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (player.hasPermission("chunks.command.chunks.reload")) {
                        getInstance().reload();
                        player.sendMessage(getMessage().addColor("&6Chunks&f: reloaded"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("unclaim")) {
                    if (player.hasPermission("chunks.command.chunks.unclaim")) {
                        var chunk = player.getLocation().getChunk();
                        if (getChunkHandler().isClaimed(chunk)) {
                            player.sendMessage(getMessage().addColor("&6You safely unclaimed&f " + getChunkHandler().getName(chunk) + "&6's chunk"));
                            getUserdata().unclaimEffect(player, chunk);
                            getUserdata().unclaimSound(player);
                            getChunkHandler().removeOwner(chunk);
                        } else player.sendMessage(getMessage().addColor("&cCurrent chunk is already unclaimed"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("info")) {
                    if (player.hasPermission("chunks.command.chunks.info")) {
                        var chunk = player.getLocation().getChunk();
                        if (getChunkHandler().isClaimed(chunk)) {
                            var owner = getChunkHandler().getOwner(chunk);
                            player.sendMessage(getMessage().addColor("&6Chunks Info&f: Chunk"));
                            player.sendMessage(getMessage().addColor("&6owner&f: " + owner.getName()));
                            player.sendMessage(getMessage().addColor("&6chunks-claimed&f: " + getUserdata().getClaimCount(owner)));
                            if (!getUserdata().getMembers(owner).isEmpty()) {
                                player.sendMessage(getMessage().addColor(getChunkHandler().getName(chunk) + "&6 members:"));
                                for (var offlinePlayer : getUserdata().getMembers(owner)) {
                                    player.sendMessage(getMessage().addColor("- " + offlinePlayer.getName()));
                                }
                            } else player.sendMessage(getMessage().addColor(owner.getName() + "&6 has no members"));
                            if (!getChunkHandler().getRecentOwners(chunk).isEmpty()) {
                                player.sendMessage(getMessage().addColor("&6recent-owners:"));
                                for (var offlinePlayer : getChunkHandler().getRecentOwners(chunk)) {
                                    player.sendMessage(getMessage().addColor("- " + offlinePlayer.getName()));
                                }
                            }
                            return true;
                        } else if (getChunkHandler().exists(chunk)) {
                            if (!getChunkHandler().getRecentOwners(chunk).isEmpty()) {
                                player.sendMessage(getMessage().addColor("&6Chunks Info&f: Chunk"));
                                player.sendMessage(getMessage().addColor("&6recent-owners:"));
                                for (var offlinePlayer : getChunkHandler().getRecentOwners(chunk)) {
                                    player.sendMessage(getMessage().addColor("- " + offlinePlayer.getName()));
                                }
                            }
                        } else player.sendMessage(getMessage().addColor("&cChunk has never been claimed"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    if (player.hasPermission("chunks.command.chunks.help")) {
                        player.sendMessage(getMessage().addColor("&6Chunks Help:"));
                        if (player.hasPermission("chunks.command.chunks.delete")) {
                            player.sendMessage(getMessage().addColor("&f/chunks delete &7- safely unclaim chunk"));
                        }
                        if (player.hasPermission("chunks.command.chunks.edit")) {
                            player.sendMessage(getMessage().addColor("&f/chunks edit &7- toggle chunk edit"));
                        }
                        if (player.hasPermission("chunks.command.chunks.effect")) {
                            player.sendMessage(getMessage().addColor("&f/chunks effect claim &7- effects of claim"));
                            player.sendMessage(getMessage().addColor("&f/chunks effect unclaim &7- effects of unclaim"));
                        }
                        player.sendMessage(getMessage().addColor("&f/chunks help &7- show this list"));
                        if (player.hasPermission("chunks.command.chunks.info")) {
                            player.sendMessage(getMessage().addColor("&f/chunks info &7- checks info of chunk"));
                        }
                        if (player.hasPermission("chunks.command.chunks.reload")) {
                            player.sendMessage(getMessage().addColor("&f/chunks reload &7- reload chunks plugin"));
                        }
                        if (player.hasPermission("chunks.command.chunks.setowner")) {
                            player.sendMessage(getMessage().addColor("&f/chunks setowner target &7- sets chunk owner"));
                        }
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("edit")) {
                    if (player.hasPermission("chunks.command.chunks.edit")) {
                        if (getUserdata().getEditors().contains(player)) {
                            getUserdata().getEditors().remove(player);
                            getMessage().sendActionBar(player, "&6Chunk Edit:&c Disabled");
                        } else {
                            getUserdata().getEditors().add(player);
                            getMessage().sendActionBar(player, "&6Chunk Edit:&a Enabled");
                        }
                        return true;
                    }
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (player.hasPermission("chunks.command.chunks.reload")) {
                        if (args[1].equalsIgnoreCase("userdata")) {
                            getUserdata().reload();
                            player.sendMessage(getMessage().addColor("&6Chunks&f: reloaded > userdata"));
                            return true;
                        }
                    }
                } else if (args[0].equalsIgnoreCase("effect")) {
                    if (player.hasPermission("chunks.command.chunks.effect")) {
                        var chunk = player.getLocation().getChunk();
                        if (args[1].equalsIgnoreCase("claim")) {
                            getUserdata().claimEffect(player, chunk);
                            getUserdata().claimSound(player);
                            getMessage().sendActionBar(player, "&6Started the effects of claim");
                        }
                        if (args[1].equalsIgnoreCase("unclaim")) {
                            getUserdata().unclaimEffect(player, chunk);
                            getUserdata().unclaimSound(player);
                            getMessage().sendActionBar(player, "&6Started the effects of unclaim");
                        }
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("info")) {
                    if (player.hasPermission("chunks.command.chunks.info")) {
                        var target = getInstance().getOfflinePlayer(args[1]);
                        if (getUserdata().exists(target)) {
                            player.sendMessage(getMessage().addColor("&6chunks info:&f " + target.getName()));
                            player.sendMessage(getMessage().addColor("&6chunks claimed:&f " + getUserdata().getClaimCount(target)));
                            if (!getUserdata().getMembers(target).isEmpty()) {
                                player.sendMessage(getMessage().addColor("&6members:"));
                                for (var offlinePlayer : getUserdata().getMembers(target)) {
                                    player.sendMessage(getMessage().addColor("- " + offlinePlayer.getName()));
                                }
                            } else player.sendMessage(getMessage().addColor(target.getName() + "&6 has no members"));
                        }
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("setowner")) {
                    if (player.hasPermission("chunks.command.chunks.setowner")) {
                        var chunk = player.getLocation().getChunk();
                        var target = getInstance().getOfflinePlayer(args[1]);
                        if (getWorldHandler().isAllowedClaim(chunk)) {
                            if (getUserdata().exists(target)) {
                                getChunkHandler().setOwner(chunk, target);
                                getUserdata().playEffect(player, target);
                                player.sendMessage(getMessage().addColor("&6Chunk is now owned by&f " + target.getName()));
                            } else player.sendMessage(getMessage().addColor(target.getName() + "&c has never joined"));
                        } else player.sendMessage(getMessage().addColor("&cYou are not allowed to claim chunks around here"));
                        return true;
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("unclaim")) {
                    if (player.hasPermission("chunks.command.chunks.unclaim")) {
                        if (args[2].equalsIgnoreCase("all")) {
                            var offlinePlayer = getInstance().getOfflinePlayer(args[1]);
                            if (getUserdata().removeAll(offlinePlayer)) {
                                player.sendMessage(getMessage().addColor("&6You safely unclaimed all&f " + offlinePlayer.getName() + "&6's chunks"));
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
        var commands = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("chunks.command.chunks.edit")) {
                    commands.add("edit");
                }
                if (player.hasPermission("chunks.command.chunks.effect")) {
                    commands.add("effect");
                }
                if (player.hasPermission("chunks.command.chunks.help")) {
                    commands.add("help");
                }
                if (player.hasPermission("chunks.command.chunks.info")) {
                    commands.add("info");
                }
                if (player.hasPermission("chunks.command.chunks.reload")) {
                    commands.add("reload");
                }
                if (player.hasPermission("chunks.command.chunks.setowner")) {
                    commands.add("setowner");
                }
                if (player.hasPermission("chunks.command.chunks.unclaim")) {
                    commands.add("unclaim");
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (player.hasPermission("chunks.command.chunks.reload")) {
                        commands.add("userdata");
                    }
                } else if (args[0].equalsIgnoreCase("effect")) {
                    if (player.hasPermission("chunks.command.chunks.effect")) {
                        commands.add("claim");
                        commands.add("unclaim");
                    }
                } else if (args[0].equalsIgnoreCase("info")) {
                    if (player.hasPermission("chunks.command.chunks.info")) {
                        getInstance().getOnlinePlayers().forEach(target -> {
                            if (!target.isInvulnerable()) {
                                if (target.getName().startsWith(args[1])) {
                                    commands.add(target.getName());
                                }
                            }
                        });
                    }
                } else if (args[0].equalsIgnoreCase("setowner")) {
                    if (player.hasPermission("chunks.command.chunks.setowner")) {
                        getInstance().getOnlinePlayers().forEach(target -> {
                            if (!target.isInvulnerable()) {
                                if (target.getName().startsWith(args[1])) {
                                    commands.add(target.getName());
                                }
                            }
                        });
                    }
                } else if (args[0].equalsIgnoreCase("unclaim")) {
                    if (player.hasPermission("chunks.command.chunks.unclaim")) {
                        getInstance().getOnlinePlayers().forEach(target -> {
                            if (!target.isInvulnerable()) {
                                if (target.getName().startsWith(args[1])) {
                                    commands.add(target.getName());
                                }
                            }
                        });
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("unclaim")) {
                    if (player.hasPermission("chunks.command.chunks.unclaim")) {
                        commands.add("all");
                    }
                }
            }
        }
        return commands;
    }
}