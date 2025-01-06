package org.achymake.chunks.handlers;

import org.achymake.chunks.Chunks;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

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
}