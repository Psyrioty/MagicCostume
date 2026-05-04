package org.psyrioty.magicCostume;

import org.bukkit.event.EventHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.psyrioty.magicCostume.Commands.MainPluginCommands;
import org.psyrioty.magicCostume.Objects.ActiveCostume;
import org.psyrioty.magicCostume.Objects.Animations.AnimationState;
import org.psyrioty.magicCostume.Objects.Costume;
import org.psyrioty.magicCostume.utils.Converter;
import org.psyrioty.magicCostume.utils.Tasker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class MagicCostume extends JavaPlugin {

    static MagicCostume plugin;
    Set<Costume> costumes = new HashSet<>();
    PluginManager pm;
    List<AnimationState> animationStateList = new ArrayList<>();
    Set<ActiveCostume> activeCostumes = new HashSet<>();

    Tasker tasker; //выполнитель задач, например функция update

    @Override
    public void onEnable() {
        plugin = this;
        pm = plugin.getServer().getPluginManager();

        this.getCommand("magiccostume").setExecutor(new MainPluginCommands());

        createDefaultAnimationStates();
        Converter.ConvertBBModelsToResourcePackAndModels();

        tasker = new Tasker();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void createDefaultAnimationStates(){
        AnimationState animationState = new AnimationState(
                "idle",
                null,
                null
        );
        animationStateList.add(animationState);
    }

    public static MagicCostume getPlugin() {
        return plugin;
    }

    public Set<Costume> getCostumes() {
        return costumes;
    }

    public List<AnimationState> getAnimationStateList() {
        return animationStateList;
    }

    public Set<ActiveCostume> getActiveCostumes() {
        return activeCostumes;
    }
}
