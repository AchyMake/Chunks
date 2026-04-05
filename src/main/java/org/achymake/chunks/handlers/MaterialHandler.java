package org.achymake.chunks.handlers;

import org.bukkit.Material;

public class MaterialHandler {
    public Material get(String materialName) {
        return Material.getMaterial(materialName.toUpperCase());
    }
}