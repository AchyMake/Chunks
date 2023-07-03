package net.achymake.chunks.commands.chunk.sub;

import net.achymake.chunks.Chunks;
import net.achymake.chunks.commands.chunk.ChunkSubCommand;
import net.achymake.chunks.files.ChunkStorage;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class TNT extends ChunkSubCommand {
    private ChunkStorage getChunkStorage() {
        return Chunks.getChunkStorage();
    }
    @Override
    public String getName() {
        return "tnt";
    }
    @Override
    public String getDescription() {
        return "toggle tnt";
    }
    @Override
    public String getSyntax() {
        return "/chunk tnt";
    }
    @Override
    public void perform(Player player, String[] args) {
        if (player.hasPermission("chunks.command.chunk.tnt")) {
            if (args.length == 1) {
                Chunk chunk = player.getLocation().getChunk();
                if (getChunkStorage().isProtected(chunk)) {
                    Chunks.send(player, "&cChunk already owned by&f Server");
                } else if (getChunkStorage().isClaimed(chunk)) {
                    if (getChunkStorage().isOwner(player, chunk)) {
                        if (getChunkStorage().TNTAllowed(chunk)) {
                            getChunkStorage().getData(chunk).remove(NamespacedKey.minecraft("tnt"));
                            Chunks.send(player, "&6You disabled tnt for this chunk");
                        } else {
                            getChunkStorage().getData(chunk).set(NamespacedKey.minecraft("tnt"), PersistentDataType.STRING, "true");
                            Chunks.send(player, "&6You enabled tnt for this chunk");
                        }
                    } else {
                        if (player.hasPermission("chunks.command.chunks.edit")) {
                            if (getChunkStorage().TNTAllowed(chunk)) {
                                getChunkStorage().getData(chunk).remove(NamespacedKey.minecraft("tnt"));
                                Chunks.send(player, "&6You disabled tnt for this chunk");
                            } else {
                                getChunkStorage().getData(chunk).set(NamespacedKey.minecraft("tnt"), PersistentDataType.STRING, "true");
                                Chunks.send(player, "&6You enabled tnt for this chunk");
                            }
                        } else {
                            Chunks.send(player, "&cChunk already owned by&f " + getChunkStorage().getOwner(chunk).getName());
                        }
                    }
                } else {
                    Chunks.send(player, "&cChunk is already unclaimed");
                }
            }
        }
    }
}