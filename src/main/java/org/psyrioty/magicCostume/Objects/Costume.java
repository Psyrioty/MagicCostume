package org.psyrioty.magicCostume.Objects;

import org.psyrioty.magicCostume.Objects.Animations.AnimationController;

import java.util.List;

public class Costume {
    float scale;
    List<Bone> headBones;
    String name;
    AnimationController animationController;

    public Costume(
            List<Bone> headBones,
            String name
    ){
        this.headBones = headBones;
        this.name = name;
    }

    public List<Bone> getHeadBones() {
        return headBones;
    }

    public String getName() {
        return name;
    }
}
