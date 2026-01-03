package org.achymake.chunks.handlers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.achymake.chunks.Chunks;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

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