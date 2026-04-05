package org.achymake.chunks.handlers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.achymake.chunks.Chunks;
import org.bukkit.Chunk;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class WorldHandler {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    public World get(String worldName) {
        return getInstance().getServer().getWorld(worldName);
    }
    public Chunk getChunk(World world, long chunkKey) {
        return world.getChunkAt((int) chunkKey, (int) (chunkKey >> 32));
    }
    public Chunk getChunk(World world, String longString) {
        return getChunk(world, Long.parseLong(longString));
    }
    public void spawnParticle(Player player, String particleType, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        player.spawnParticle(Particle.valueOf(particleType), x, y, z, count, offsetX, offsetY, offsetZ, 0.0);
    }
    public void playSound(Player player, String soundType, double volume, double pitch) {
        player.playSound(player, Sound.valueOf(soundType), (float) volume, (float) pitch);
    }
    private boolean isAllowed(ApplicableRegionSet applicableRegionSet) {
        for (var regionIn : applicableRegionSet) {
            if (regionIn != null) {
                var flag = regionIn.getFlag(getInstance().getFlag());
                if (flag == StateFlag.State.ALLOW) {
                    return true;
                } else if (flag == StateFlag.State.DENY) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean isAllowedClaim(Chunk chunk) {
        var world = chunk.getWorld();
        if (getConfig().getStringList("worlds").contains(world.getName())) {
            var regionManager = getWorldGuard().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
            if (regionManager != null) {
                var x = chunk.getX() << 4;
                var z = chunk.getZ() << 4;
                var protectedCuboidRegion = new ProtectedCuboidRegion("_", BlockVector3.at(x, -64, z), BlockVector3.at(x + 15, 320, z + 15));
                return isAllowed(regionManager.getApplicableRegions(protectedCuboidRegion));
            } else return true;
        } else return false;
    }
    private WorldGuard getWorldGuard() {
        return getInstance().getWorldGuard();
    }
}