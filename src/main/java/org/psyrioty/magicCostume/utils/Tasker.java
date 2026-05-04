package org.psyrioty.magicCostume.utils;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.ActiveCostume;
import org.psyrioty.magicCostume.Objects.Costume;

import java.util.List;
import java.util.Set;

public class Tasker {
    BukkitTask updateTask;
    Set<ActiveCostume> costumes;

    public Tasker(){
        update();
    }

    private void update(){
        updateTask = Bukkit.getScheduler().runTaskTimerAsynchronously(MagicCostume.getPlugin(), () -> {
            for(ActiveCostume costume: MagicCostume.getPlugin().getActiveCostumes()){
                costume.animationTick();
            }
        },1L,1L);
    }

    public void reload(){
        updateTask.cancel();

        update();
    }
}
