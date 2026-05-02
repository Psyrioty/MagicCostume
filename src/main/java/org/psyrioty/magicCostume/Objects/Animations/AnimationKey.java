package org.psyrioty.magicCostume.Objects.Animations;

import org.psyrioty.magicCostume.Objects.Bone;

public class AnimationKey {
    int tick; //это типо время будет, на котором метка
    Bone bone; //кость, для которой сделана метка
    float rotationX, rotationY, rotationZ; //ротация
    float scaleX, scaleY, scaleZ; //скэйл
    float translateX, translateY, translateZ; //смещение/позиция

    public AnimationKey(
            int tick,
            Bone bone,
            float rotationX, float rotationY, float rotationZ,
            float scaleX, float scaleY, float scaleZ,
            float translateX, float translateY, float translateZ
    ){
        this.tick = tick;
        this.bone = bone;

        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;

        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;

        this.translateX = translateX;
        this.translateY = translateY;
        this.translateZ = translateZ;
    }
}
