package org.psyrioty.magicCostume.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.Animations.Animation;
import org.psyrioty.magicCostume.Objects.Animations.AnimationController;
import org.psyrioty.magicCostume.Objects.Player.ActiveEntity;

import java.util.ArrayList;
import java.util.List;

public class ActiveCostume {
    Entity target;
    List<Bone> headBones = new ArrayList<>();
    AnimationController animationController;
    ActiveEntity activeEntity;

    public ActiveCostume(
            Entity target,
            Costume costume,
            ActiveEntity activeEntity
    ){
        this.target = target;
        getHeadBones(costume.getHeadBones(), null);
        this.animationController = costume.getAnimationController();


        this.activeEntity = activeEntity;

        Spawn();

        MagicCostume.getPlugin().getActiveCostumes().add(this);
    }

    public AnimationController getAnimationController() {
        return animationController;
    }

    private void getHeadBones(List<Bone> bones, Bone headBone){
        try {
            for(Bone bone: bones){
                Bone newHeadBone = bone.clone();
                if(headBone == null){
                    headBones.add(newHeadBone);
                }else{
                    headBone.addChildBone(newHeadBone);
                    newHeadBone.setHeadBone(headBone);
                }
                List<Bone> boneList = bone.getChildBones();
                if(boneList.isEmpty()){
                    continue;
                }
                Bukkit.getLogger().info(newHeadBone.getName() + " " + boneList);
                getHeadBones(boneList, newHeadBone);
            }
        }catch (Exception exception){
            Bukkit.getLogger().severe("MagicCostume error ActiveCostume.java getHeadBones() " + exception.getMessage());
        }
    }

    private void Spawn(){
        spawnBone(null, headBones);
    }

    private void spawnBone(Bone headBone, List<Bone> bones){
        try {
            for(Bone bone: bones){
                if(headBone == null){
                    bone.createBoneEntity(target);
                }else{
                    bone.createBoneEntity(headBone.getBoneEntity());
                }
                List<Bone> boneList = bone.getChildBones();
                if(boneList.isEmpty()){
                    continue;
                }
                spawnBone(bone, boneList);
            }
        }catch (Exception exception){
            Bukkit.getLogger().severe("MagicCostume error ActiveCostume.java spawnBone() " + exception.getMessage());
        }
    }

    public void animationTick(){
        walk(activeEntity, target);
        animationController.animationTick(headBones, target);

        clearNewOriginBones(headBones);
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



    private void clearNewOriginBones(List<Bone> bones){
        if(bones == null){
            return;
        }

        for(Bone bone: bones){
            bone.setNewOrigin(null);
            clearNewOriginBones(bone.getChildBones());
        }

    }
}