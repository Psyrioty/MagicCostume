package org.psyrioty.magicCostume.Objects.Animations;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.ActiveCostume;
import org.psyrioty.magicCostume.Objects.Bone;
import org.psyrioty.magicCostume.Objects.Player.ActiveEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class AnimationController {
    //List<AnimationState> animationStates;

    List<Animation> animations;

    public AnimationController(List<Animation> animations){
        //this.animations = animations;
        //this.animationStates = MagicCostume.getPlugin().getAnimationStateList();


        this.animations = animations.stream()
                .sorted(Comparator.comparingInt(Animation::getWeight))
                .toList();
    }

    public void animationTick(List<Bone> bones, Entity target){
        for(Animation animation: animations){
            if(animation.isEnable()){
                animation.animationTick(bones, target);
                break;
            }
        }
    }

    private void walk(ActiveEntity activeEntity, Entity entity){

        Vector velocity = entity.getVelocity();
        Bukkit.getLogger().info(velocity.toString());

        Location location = entity.getLocation();
        boolean moving = activeEntity.getX() != location.getX() ||
                activeEntity.getZ() != location.getZ();

        activeEntity.setLocation(
                location.getWorld(),
                location.getX(),
                location.getY(),
                location.getZ()
        );

        for (ActiveCostume activeCostume : activeEntity.getActiveCostumes()) {
            for (Animation animation : activeCostume.getAnimationController().getAnimations()) {
                if(animation.getName().equals("walk")){
                    animation.setEnable(moving);
                }
            }
        }
    }

    public List<Animation> getAnimations() {
        return animations;
    }
}
