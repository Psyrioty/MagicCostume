package org.psyrioty.magicCostume;

import com.google.gson.JsonObject;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.psyrioty.magicCostume.Commands.MainPluginCommands;
import org.psyrioty.magicCostume.Listeners.TargetEvents;
import org.psyrioty.magicCostume.Objects.ActiveCostume;
import org.psyrioty.magicCostume.Objects.Animations.AnimationState;
import org.psyrioty.magicCostume.Objects.Costume;
import org.psyrioty.magicCostume.Objects.Player.ActiveEntity;
import org.psyrioty.magicCostume.utils.Converter;
import org.psyrioty.magicCostume.utils.Tasker;

import java.util.*;

public final class MagicCostume extends JavaPlugin {

    static MagicCostume plugin;
    Set<Costume> costumes = new HashSet<>();
    PluginManager pm;
    List<AnimationState> animationStateList = new ArrayList<>();
    Set<ActiveCostume> activeCostumes = new HashSet<>();
    Set<ActiveEntity> activeEntities = new HashSet<>();

    static List<JsonObject> caseList = new ArrayList<>(); //для ресурспака

    Tasker tasker; //выполнитель задач, например функция update

    @Override
    public void onEnable() {
        plugin = this;
        pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new TargetEvents(), this);

        this.getCommand("magiccostume").setExecutor(new MainPluginCommands());

        //createDefaultAnimationStates();
        Converter.ConvertBBModelsToResourcePackAndModels();

        tasker = new Tasker();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private static List<JsonObject> getCaseList() {
        return caseList;
    }

    private void createDefaultAnimationStates(){
        AnimationState idle = new AnimationState(
                "idle"
        );
        animationStateList.add(idle);

        AnimationState walk = new AnimationState(
                "walk"
        );
        animationStateList.add(walk);

        AnimationState jump = new AnimationState(
                "jump"
        );
        animationStateList.add(jump);

        AnimationState fly = new AnimationState(
                "fly"
        );
        animationStateList.add(fly);

        AnimationState endJump = new AnimationState(
                "end_jump"
        );
        animationStateList.add(endJump);

        AnimationState death = new AnimationState(
                "death"
        );
        animationStateList.add(death);
    }

    public Set<ActiveEntity> getActiveEntities() {
        return activeEntities;
    }


    public ActiveEntity findActiveEntity(Entity entity){
        for (ActiveEntity activeEntity: activeEntities){
            if(entity == activeEntity.getTarget()){
                return activeEntity;
            }
        }

        return null;
    }

    public ActiveEntity findActiveEntity(UUID uuid){
        for (ActiveEntity activeEntity: activeEntities){
            if(uuid.equals(activeEntity.getTarget().getUniqueId())){
                return activeEntity;
            }
        }

        return null;
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
