package org.achymake.chunks.providers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import org.bukkit.Chunk;

public class WorldGuardProvider {
    private StateFlag CHUNK_CLAIM;
    private boolean isAllowed(ApplicableRegionSet applicableRegionSet) {
        for (var regionIn : applicableRegionSet) {
            var flag = regionIn.getFlag(getFlag());
            if (flag == StateFlag.State.ALLOW) {
                return true;
            } else if (flag == StateFlag.State.DENY) {
                return false;
            }
        }
        return false;
    }
    public boolean isAllowedClaim(Chunk chunk) {
        var regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(chunk.getWorld()));
        if (regionManager != null) {
            var x = chunk.getX() << 4;
            var z = chunk.getZ() << 4;
            var protectedCuboidRegion = new ProtectedCuboidRegion("_", BlockVector3.at(x, -64, z), BlockVector3.at(x + 15, 320, z + 15));
            if (regionManager.getApplicableRegions(protectedCuboidRegion).getRegions().isEmpty()) {
                return true;
            } else return isAllowed(regionManager.getApplicableRegions(protectedCuboidRegion));
        } else return true;
    }
    public void register() {
        var registry = WorldGuard.getInstance().getFlagRegistry();
        CHUNK_CLAIM = new StateFlag("chunk-claim", true);
        registry.register(CHUNK_CLAIM);
    }
    public StateFlag getFlag() {
        return CHUNK_CLAIM;
    }
}