package org.achymake.chunks.handlers;

import org.achymake.chunks.Chunks;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;

public class EntityHandler {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    public EntityType getEntityType(String entityTypeString) {
        return EntityType.valueOf(entityTypeString.toUpperCase());
    }
    public boolean isFriendly(EntityType entityType) {
        return getConfig().getBoolean("friendly." + entityType);
    }
    public boolean isTNT(EntityType entityType) {
        return entityType.equals(getEntityType("tnt")) || entityType.equals(getEntityType("tnt_minecart"));
    }
    public boolean isOwner(Entity entity, Player player) {
        if (entity instanceof Tameable tameable) {
            if (tameable.isTamed()) {
                return tameable.getOwner() == player;
            }
        }
        return false;
    }
    public boolean isLeashHolder(Entity entity, Player player) {
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.isLeashed()) {
                return livingEntity.getLeashHolder() == player;
            } else return false;
        } else return false;
    }
}