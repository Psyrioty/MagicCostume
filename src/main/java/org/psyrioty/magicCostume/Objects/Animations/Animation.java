package org.psyrioty.magicCostume.Objects.Animations;

import org.bukkit.entity.Entity;
import org.psyrioty.magicCostume.Objects.Bone;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Animation {
    String name; //название анимации
    UUID uuid;
    List<AnimationLine> animationLines;
    boolean loop; //true - зациклен, false - запуск один раз
    int tick = 0; //тик хранит в себе данный тик анимации
    int length; //длина анимации

    public Animation(
            String name,
            UUID uuid,
            boolean loop,
            List<AnimationLine> animationLines,
            int length
    ){
        this.name = name;
        this.uuid = uuid;
        this.loop = loop;
        this.animationLines = animationLines;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void animationTick(List<Bone> bones, Entity target){
        for(AnimationLine animationLine: animationLines){
            animationLine.animationTick(tick, bones, target);
        }
        tick++;
        if(tick > length){
            tick = 0;
        }
    }
}