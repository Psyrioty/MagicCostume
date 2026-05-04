package org.psyrioty.magicCostume.Objects.Animations;

import org.bukkit.Bukkit;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.Bone;

import java.util.ArrayList;
import java.util.List;

public class AnimationController {
    List<AnimationState> animationStates;

    List<Animation> animations;

    public AnimationController(List<Animation> animations){
        this.animations = animations;
        this.animationStates = MagicCostume.getPlugin().getAnimationStateList();
    }

    public void animationTick(List<Bone> bones){
        for(Animation animation: animations){
            if(animation.getName().equals("idle")){
                animation.animationTick(bones);
            }
        }
    }
}
