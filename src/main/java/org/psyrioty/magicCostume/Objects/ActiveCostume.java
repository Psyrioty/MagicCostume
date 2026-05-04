package org.psyrioty.magicCostume.Objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.Animations.Animation;
import org.psyrioty.magicCostume.Objects.Animations.AnimationController;

import java.util.ArrayList;
import java.util.List;

public class ActiveCostume {
    Entity target;
    List<Bone> headBones = new ArrayList<>();
    AnimationController animationController;

    public ActiveCostume(
            Entity target,
            Costume costume
    ){
        this.target = target;
        getHeadBones(costume.getHeadBones(), null);
        this.animationController = costume.getAnimationController();

        Spawn();

        MagicCostume.getPlugin().getActiveCostumes().add(this);
    }

    private void getHeadBones(List<Bone> bones, Bone headBone){
        try {
            for(Bone bone: bones){
                Bone newHeadBone = bone.clone();
                if(headBone == null){
                    headBones.add(newHeadBone);
                }else{
                    headBone.addChildBone(newHeadBone);
                }
                List<Bone> boneList = bone.getChildBones();
                if(boneList.isEmpty()){
                    continue;
                }
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
        animationController.animationTick(headBones);
    }
}
