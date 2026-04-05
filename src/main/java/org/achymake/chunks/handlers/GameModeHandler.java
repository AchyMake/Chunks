package org.achymake.chunks.handlers;

import org.bukkit.GameMode;

public class GameModeHandler {
    public GameMode get(String gameMode) {
        if (gameMode.equalsIgnoreCase("adventure")) {
            return GameMode.ADVENTURE;
        } else if (gameMode.equalsIgnoreCase("creative")) {
            return GameMode.CREATIVE;
        } else if (gameMode.equalsIgnoreCase("spectator")) {
            return GameMode.SPECTATOR;
        } else if (gameMode.equalsIgnoreCase("survival")) {
            return GameMode.SURVIVAL;
        } else return GameMode.SURVIVAL;
    }
}