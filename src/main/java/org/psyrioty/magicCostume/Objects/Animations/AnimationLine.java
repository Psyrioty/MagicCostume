package org.psyrioty.magicCostume.Objects.Animations;

//Это типо линия анимации для отдельной кости
//пупупууу

import org.psyrioty.magicCostume.Objects.Bone;

import java.util.ArrayList;
import java.util.List;

public class AnimationLine {
    List<AnimationKey> animationKeys = new ArrayList<>();
    Bone bone;

    public AnimationLine(Bone bone){
        this.bone = bone;
    }

    public void addAnimationKey(AnimationKey animationKey){
        animationKeys.add(animationKey);
    }
}
