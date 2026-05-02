package org.psyrioty.magicCostume.Objects.Animations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Animation {
    String name; //название анимации
    UUID uuid;
    List<AnimationLine> animationLines = new ArrayList<>();
    boolean loop; //true - зациклен, false - запуск один раз

    public Animation(
            String name,
            UUID uuid,
            boolean loop
    ){
        this.name = name;
        this.uuid = uuid;
        this.loop = loop;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void addAnimationLine(AnimationLine animationLine){
        animationLines.add(animationLine);
    }
}
