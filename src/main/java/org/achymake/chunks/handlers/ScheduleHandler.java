package org.achymake.chunks.handlers;

import org.achymake.chunks.Chunks;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class ScheduleHandler {
    private Chunks getInstance() {
        return Chunks.getInstance();
    }
    private BukkitScheduler getScheduler() {
        return getInstance().getBukkitScheduler();
    }
    public BukkitTask runLater(Runnable runnable, long timer) {
        return getScheduler().runTaskLater(getInstance(), runnable, timer);
    }
    public void runAsynchronously(Runnable runnable) {
        getScheduler().runTaskAsynchronously(getInstance(), runnable);
    }
    public boolean isQueued(int taskID) {
        return getScheduler().isQueued(taskID);
    }
    public void cancel(int taskID) {
        getScheduler().cancelTask(taskID);
    }
    public void disable() {
        getScheduler().cancelTasks(getInstance());
    }
}