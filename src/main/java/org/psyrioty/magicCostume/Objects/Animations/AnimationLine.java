package org.psyrioty.magicCostume.Objects.Animations;

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
    private final List<AnimationKey> rotationKeys = new ArrayList<>();
    private final List<AnimationKey> scaleKeys = new ArrayList<>();
    private final List<AnimationKey> translateKeys = new ArrayList<>();
    private final UUID uuidBone;

    public AnimationLine(UUID uuidBone, List<AnimationKey> animationKeys) {
        this.uuidBone = uuidBone;

        for (AnimationKey animationKey : animationKeys) {
            switch (animationKey.getTypeKey()) {
                case "rotation" -> rotationKeys.add(animationKey);
                case "position" -> translateKeys.add(animationKey);
                case "scale" -> scaleKeys.add(animationKey);
            }
        }
    }

    public void animationTick(int tick, List<Bone> bones, Entity target) {
        Bone animatedBone = getNeedBone(bones);
        if (animatedBone == null) {
            return;
        }

        rotationTick(animatedBone, tick, target);
        translateTick(animatedBone, tick);

        Bone root = animatedBone;
        while (root.getHeadBone() != null) {
            root = root.getHeadBone();
        }

        applyBoneRecursive(
                root,
                new Vector3f(0, 0, 0),
                new Quaternionf(),
                new Vector3f(0, 0, 0)
        );
    }

    private void applyBoneRecursive(
            Bone bone,
            Vector3f parentWorldPos,
            Quaternionf parentWorldRot,
            Vector3f parentBindOrigin
    ) {
        if (bone == null || bone.getBoneEntity() == null) {
            return;
        }

        Vector3f bindOrigin = new Vector3f(
                bone.getOriginX(),
                bone.getOriginY(),
                bone.getOriginZ()
        );

        Quaternionf bindRotation = new Quaternionf().rotateXYZ(
                (float) Math.toRadians(bone.getRotationX()),
                (float) Math.toRadians(bone.getRotationY()),
                (float) Math.toRadians(bone.getRotationZ())
        );

        Quaternionf animRotation = new Quaternionf().rotateXYZ(
                (float) Math.toRadians(bone.getAnimRotationX()),
                (float) Math.toRadians(bone.getAnimRotationY()),
                (float) Math.toRadians(bone.getAnimRotationZ())
        );

        Quaternionf localRotation = new Quaternionf(bindRotation).mul(animRotation);
        Quaternionf worldRotation = new Quaternionf(parentWorldRot).mul(localRotation);

        Vector3f localOffset = new Vector3f(bindOrigin)
                .sub(parentBindOrigin)
                .add(
                        bone.getAnimPositionX(),
                        bone.getAnimPositionY(),
                        bone.getAnimPositionZ()
                );

        Vector3f worldPos = new Vector3f(localOffset)
                .rotate(parentWorldRot)
                .add(parentWorldPos);

        ItemDisplay display = bone.getBoneEntity();
        Transformation old = display.getTransformation();

        display.setTransformation(new Transformation(
                worldPos,
                worldRotation,
                new Vector3f(
                        bone.getAnimScaleX(),
                        bone.getAnimScaleY(),
                        bone.getAnimScaleZ()
                ),
                old.getRightRotation()
        ));

        for (Bone child : bone.getChildBones()) {
            applyBoneRecursive(child, worldPos, worldRotation, bindOrigin);
        }
    }

    private Bone getNeedBone(List<Bone> bones) {
        try {
            for (Bone bone : bones) {
                if (bone.getUuid().equals(uuidBone)) {
                    return bone;
                }

                List<Bone> childBones = bone.getChildBones();
                if (childBones == null || childBones.isEmpty()) {
                    continue;
                }

                Bone boneNeed = getNeedBone(childBones);
                if (boneNeed != null) {
                    return boneNeed;
                }
            }
        } catch (Exception exception) {
            Bukkit.getLogger().severe("MagicCostume error AnimationLine.java getNeedBone() " + exception.getMessage());
        }

        return null;
    }

    private void rotationTick(Bone bone, int tick, Entity target) {
        List<AnimationKey> animationKeys = getCurrentGapBetweenKeys(rotationKeys, tick);

        float x = 0;
        float y = 0;
        float z = 0;

        if (animationKeys != null && !animationKeys.isEmpty()) {
            x = mathValue(
                    animationKeys.getFirst().getX(),
                    animationKeys.getLast().getX(),
                    tick,
                    animationKeys.getFirst().getTick(),
                    animationKeys.getLast().getTick()
            );

            y = mathValue(
                    animationKeys.getFirst().getY(),
                    animationKeys.getLast().getY(),
                    tick,
                    animationKeys.getFirst().getTick(),
                    animationKeys.getLast().getTick()
            );

            z = mathValue(
                    animationKeys.getFirst().getZ(),
                    animationKeys.getLast().getZ(),
                    tick,
                    animationKeys.getFirst().getTick(),
                    animationKeys.getLast().getTick()
            );
        }


        Bukkit.getLogger().info(target.getYaw() + "");
        if(bone.getHeadBone() == null){
            y -= target.getYaw();
        }
        bone.setAnimRotation(x, y, z);
    }

    private void translateTick(Bone bone, int tick) {
        List<AnimationKey> animationKeys = getCurrentGapBetweenKeys(translateKeys, tick);

        float x = 0;
        float y = 0;
        float z = 0;

        if (animationKeys != null && !animationKeys.isEmpty()) {
            x = mathValue(
                    animationKeys.getFirst().getX(),
                    animationKeys.getLast().getX(),
                    tick,
                    animationKeys.getFirst().getTick(),
                    animationKeys.getLast().getTick()
            );

            y = mathValue(
                    animationKeys.getFirst().getY(),
                    animationKeys.getLast().getY(),
                    tick,
                    animationKeys.getFirst().getTick(),
                    animationKeys.getLast().getTick()
            );

            z = mathValue(
                    animationKeys.getFirst().getZ(),
                    animationKeys.getLast().getZ(),
                    tick,
                    animationKeys.getFirst().getTick(),
                    animationKeys.getLast().getTick()
            );
        }

        bone.setAnimPosition(x, y, z);
    }

    private List<AnimationKey> getCurrentGapBetweenKeys(List<AnimationKey> animationKeys, int tick) {
        AnimationKey oldKey = null;
        AnimationKey nextKey = null;

        for (AnimationKey animationKey : animationKeys) {
            if (animationKey.getTick() <= tick) {
                if (oldKey == null || oldKey.getTick() < animationKey.getTick()) {
                    oldKey = animationKey;
                }
            }

            if (animationKey.getTick() >= tick) {
                if (nextKey == null || nextKey.getTick() > animationKey.getTick()) {
                    nextKey = animationKey;
                }
            }
        }

        if (oldKey == null || nextKey == null) {
            return null;
        }

        List<AnimationKey> animationKeyList = new ArrayList<>(2);
        animationKeyList.add(oldKey);
        animationKeyList.add(nextKey);
        return animationKeyList;
    }

    private float mathValue(float oldKeyValue, float nextKeyValue, int tick, int oldKeyTick, int nextKeyTick) {
        int differenceTick = nextKeyTick - oldKeyTick;
        float differenceValue = nextKeyValue - oldKeyValue;

        float stepValueInTick = 0;
        if (differenceTick != 0) {
            stepValueInTick = differenceValue / differenceTick;
        }

        int actualityTick = tick - oldKeyTick;
        return oldKeyValue + (actualityTick * stepValueInTick);
    }
}