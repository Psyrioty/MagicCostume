package org.psyrioty.magicCostume.Objects.Animations;

//Это типо линия анимации для отдельной кости
//пупупууу

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.psyrioty.magicCostume.Objects.Bone;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AnimationLine {
    List<AnimationKey> rotationKeys = new ArrayList<>();
    List<AnimationKey> scaleKeys = new ArrayList<>();
    List<AnimationKey> translateKeys = new ArrayList<>();
    UUID uuidBone;

    public AnimationLine(
            UUID uuidBone,
            List<AnimationKey> animationKeys
    ){
        this.uuidBone = uuidBone;
        //this.animationKeys = animationKeys;

        for(AnimationKey animationKey: animationKeys){
            switch (animationKey.getTypeKey()){
                case "rotation":
                    rotationKeys.add(animationKey);
                    break;
                case "position":
                    translateKeys.add(animationKey);
                    break;
                case "scale":
                    scaleKeys.add(animationKey);
                    break;
            }
        }
    }

    public void animationTick(int tick, List<Bone> bones){
        Bone bone = getNeedBone(bones);

        if(bone == null){
            return;
        }

        rotationTick(bone, tick);
    }

    private Bone getNeedBone(List<Bone> bones){

        try{
            for(Bone bone: bones){
                if(bone.getUuid().equals(uuidBone)){
                    return bone;
                }
                List<Bone> childBones = bone.getChildBones();
                if(childBones == null){
                    continue;
                }
                if(childBones.isEmpty()){
                    continue;
                }

                Bone boneNeed = getNeedBone(childBones);
                if(boneNeed != null){
                    return boneNeed;
                }
            }
        }catch (Exception exception){
            Bukkit.getLogger().severe("MagicCostume error AnimationLine.java getNeedBone() " + exception.getMessage());
        }

        return null;
    }

    private void rotationTick(Bone bone, int tick){
        List<AnimationKey> animationKeys = getCurrentGapBetweenKeys(rotationKeys, tick);
        if(animationKeys == null){
            return;
        }
        if(animationKeys.isEmpty()){
            return;
        }

        float x = mathValue(
                animationKeys.getFirst().getX(),
                animationKeys.getLast().getX(),
                tick,
                animationKeys.getFirst().getTick(),
                animationKeys.getLast().getTick()
        );

        float y = mathValue(
                animationKeys.getFirst().getY(),
                animationKeys.getLast().getY(),
                tick,
                animationKeys.getFirst().getTick(),
                animationKeys.getLast().getTick()
        );

        float z = mathValue(
                animationKeys.getFirst().getZ(),
                animationKeys.getLast().getZ(),
                tick,
                animationKeys.getFirst().getTick(),
                animationKeys.getLast().getTick()
        );

        rotate(bone, x, y, z);
    }

    private List<AnimationKey> getCurrentGapBetweenKeys(List<AnimationKey> animationKeys, int tick){
        AnimationKey oldKey = null;
        AnimationKey nextKey = null;
        for(AnimationKey animationKey: animationKeys){
            if(animationKey.getTick() <= tick) {
                if (oldKey == null) {
                    oldKey = animationKey;
                }else if (oldKey.getTick() < animationKey.getTick()){
                    oldKey = animationKey;
                }
            }

            if(animationKey.getTick() >= tick){
                if(nextKey == null) {
                    nextKey = animationKey;
                }else if (nextKey.getTick() > animationKey.getTick()){
                    nextKey = animationKey;
                }
            }
        }

        if(oldKey == null || nextKey == null){
            return null;
        }

        List<AnimationKey> animationKeyList = new ArrayList<>();

        animationKeyList.add(oldKey);
        animationKeyList.add(nextKey);

        return animationKeyList;
    }

    private float mathValue(
            float oldKeyValue, float nextKeyValue,
            int tick,
            int oldKeyTick, int nextKeyTick
    ){
        int differenceTick = nextKeyTick - oldKeyTick;
        float differenceValue = nextKeyValue - oldKeyValue;

        float stepValueInTick = 0;
        if(differenceTick != 0) {
            stepValueInTick = differenceValue / differenceTick;
        }

        int actualityTick = tick - oldKeyTick;

        return oldKeyValue + (actualityTick * stepValueInTick);
    }

    private void rotate(Bone bone, float x, float y, float z){
        double rx = Math.toRadians(((x % 360) + 360) % 360);
        double ry = Math.toRadians(((y % 360) + 360) % 360);
        double rz = Math.toRadians(((z % 360) + 360) % 360);

        Quaternionf left = new Quaternionf().rotationXYZ((float) rx, (float) ry, (float) rz);

        ItemDisplay boneEntity = bone.getBoneEntity();
        Transformation transformation = boneEntity.getTransformation();
        Vector3f translation = transformation.getTranslation();
        Vector3f scale = transformation.getScale();
        Quaternionf rightRotation = transformation.getRightRotation();

        //Bukkit.getLogger().info(bone.getName() + " X: " + x + " Y: " + y + " Z: " + z);

        Transformation t = new Transformation(
                translation,
                left,
                scale,
                rightRotation
        );
        bone.getBoneEntity().setTransformation(t);
    }
}
