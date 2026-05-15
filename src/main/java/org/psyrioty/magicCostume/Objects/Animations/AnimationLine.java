package org.psyrioty.magicCostume.Objects.Animations;

//Это типо линия анимации для отдельной кости
//пупупууу

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Matrix3d;
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

        translateTick(bone, tick);
        rotationTick(bone, tick);

        //doTransformation(bone);
    }

    private void doTransformation(Bone bone){
        ItemDisplay boneEntity = bone.getBoneEntity();
        Transformation transformation = boneEntity.getTransformation();
        Quaternionf rightRotation = new Quaternionf(transformation.getRightRotation());
        Vector3f translation = new Vector3f(bone.getAnimPositionX(), bone.getAnimPositionY(), bone.getAnimPositionZ());
        Quaternionf rotation = new Quaternionf().rotationXYZ(bone.getAnimRotationX(), bone.getAnimRotationY(), bone.getAnimRotationZ());
        Vector3f scale = new Vector3f(bone.getAnimScaleX(), bone.getAnimScaleY(), bone.getAnimScaleZ());

        boneEntity.setTransformation(new Transformation(
                translation,
                rotation,
                scale,
                rightRotation
        ));
    }

    //type = rotation, scale, position
    private void mathChildBonesAnim(float x, float y, float z, String type, List<Bone> bonesChild){
        if(bonesChild == null){
            return;
        }
        if(bonesChild.isEmpty()){
            return;
        }

        for(Bone boneChild: bonesChild){
            boneChild.mathAddAnimation(x, y, z, type);

            mathChildBonesAnim(x, y, z, type, boneChild.getChildBones());
        }
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
        float x = bone.getAddRotateX();
        float y = bone.getAddRotateY();
        float z = bone.getAddRotateZ();

        if(animationKeys != null && !animationKeys.isEmpty()) {
            x += mathValue(
                    animationKeys.getFirst().getX(),
                    animationKeys.getLast().getX(),
                    tick,
                    animationKeys.getFirst().getTick(),
                    animationKeys.getLast().getTick()
            );

            y += mathValue(
                    animationKeys.getFirst().getY(),
                    animationKeys.getLast().getY(),
                    tick,
                    animationKeys.getFirst().getTick(),
                    animationKeys.getLast().getTick()
            );

            z += mathValue(
                    animationKeys.getFirst().getZ(),
                    animationKeys.getLast().getZ(),
                    tick,
                    animationKeys.getFirst().getTick(),
                    animationKeys.getLast().getTick()
            );
        }

        rotate(
                bone,
                x,
                y,
                z
        );
        bone.clearAddRotate();
        mathChildBonesAnim(x, y, z, "rotation", bone.getChildBones());
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

    private void rotate(Bone bone, float x, float y, float z) {
        float rx = (float) Math.toRadians(x);
        float ry = (float) Math.toRadians(y);
        float rz = (float) Math.toRadians(z);

        float addRx = (float) Math.toRadians(bone.getAddRotateX());
        float addRy = (float) Math.toRadians(bone.getAddRotateY());
        float addRz = (float) Math.toRadians(bone.getAddRotateZ());

        Matrix3d matrixBad = new Matrix3d();
        matrixBad.scale(
                bone.getAnimScaleX(),
                bone.getAnimScaleY(),
                bone.getAnimScaleZ()
        );

        matrixBad.transform(
                new Vector3f(
                        bone.getAnimPositionX(),
                        bone.getAnimPositionY(),
                        bone.getAnimPositionZ()
                )
        );

        matrixBad.rotate(
                new Quaternionf().rotationXYZ(
                        rx,
                        ry,
                        rz
                )
        );

        Quaternionf rotation = new Quaternionf().
                rotateLocalX(rx).
                rotateLocalY(ry).
                rotateLocalZ(rz).
                mul(
                        new Quaternionf().
                                rotateLocalX(addRx).
                                rotateLocalY(addRy).
                                rotateLocalZ(addRz)
                        );
        Vector3f origin = new Vector3f(
                bone.getOriginX(),
                bone.getOriginY(),
                bone.getOriginZ()
        );

        Vector3f addChild = new Vector3f(
                bone.getAddTranslateX(),
                bone.getAddTranslateY(),
                bone.getAddTranslateZ()
        );


        Vector3f translate = new Vector3f(
                bone.getAnimPositionX(),
                bone.getAnimPositionY(),
                bone.getAnimPositionZ()
        );

        translate.add(origin).add(addChild);

        bone.getBoneEntity().setTransformation(
                new Transformation(
                        translate,
                        rotation,
                        new Vector3f(bone.getAnimScaleX(), bone.getAnimScaleY(), bone.getAnimScaleZ()),
                        bone.getBoneEntity().getTransformation().getRightRotation()
                )
        );

        /*Quaternionf rotation = new Quaternionf().rotationXYZ(rx, ry, rz);

        ItemDisplay boneEntity = bone.getBoneEntity();
        Transformation transformation = boneEntity.getTransformation();

        Vector3f pivot = new Vector3f(
                bone.getOriginX(),
                bone.getOriginY(),
                bone.getOriginZ()
        );

        Vector3f rotatedPivot = new Vector3f(pivot);
        rotation.transform(rotatedPivot);

        Vector3f translation = new Vector3f(transformation.getTranslation())
                .add(pivot)
                .sub(rotatedPivot);

        Vector3f scale = new Vector3f(transformation.getScale());
        Quaternionf rightRotation = new Quaternionf(transformation.getRightRotation());*/


    }

    private void translate(Bone bone, float x, float y, float z){
        ItemDisplay boneEntity = bone.getBoneEntity();
        Transformation transformation = boneEntity.getTransformation();

        Vector3f translation = new Vector3f(
                bone.getOriginX() + x,
                bone.getOriginY() + y,
                bone.getOriginZ() + z
        );

        Quaternionf leftRotation = new Quaternionf(transformation.getLeftRotation());
        Vector3f scale = new Vector3f(transformation.getScale());
        Quaternionf rightRotation = new Quaternionf(transformation.getRightRotation());

        bone.setAnimPosition(
                x,
                y,
                z
                );
    }

    private void translateTick(Bone bone, int tick) {
        List<AnimationKey> animationKeys = getCurrentGapBetweenKeys(translateKeys, tick);

        float x = bone.getAddTranslateX();
        float y = bone.getAddTranslateY();
        float z = bone.getAddTranslateZ();

        if (animationKeys != null && !animationKeys.isEmpty()) {
            x += mathValue(
                    animationKeys.getFirst().getX(),
                    animationKeys.getLast().getX(),
                    tick,
                    animationKeys.getFirst().getTick(),
                    animationKeys.getLast().getTick()
            );

            y += mathValue(
                    animationKeys.getFirst().getY(),
                    animationKeys.getLast().getY(),
                    tick,
                    animationKeys.getFirst().getTick(),
                    animationKeys.getLast().getTick()
            );

            z += mathValue(
                    animationKeys.getFirst().getZ(),
                    animationKeys.getLast().getZ(),
                    tick,
                    animationKeys.getFirst().getTick(),
                    animationKeys.getLast().getTick()
            );
        }

        translate(
                bone,
                x,
                y,
                z
        );


        Bukkit.getLogger().info(
                bone.getName() + " " +
                        "X: " + x + " add " + bone.getAddTranslateX() + " origin " + bone.getOriginX() + " " +
                        "Y: " + y + " add " + bone.getAddTranslateY() + " origin " + bone.getOriginY() + " " +
                        "Z: " + z + " add " + bone.getAddTranslateZ() + " origin " + bone.getOriginZ()
        );

        bone.clearAddTranslate();

        mathChildBonesAnim(x, y, z, "position", bone.getChildBones());
    }
}