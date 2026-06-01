package org.psyrioty.magicCostume.utils;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.ActiveCostumeEntity;

public class Tasker {
    static BukkitTask task;

    public static void Update(){
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(MagicCostume.getPlugin(), () -> {
        }, 20L, 20L);
    }
}
