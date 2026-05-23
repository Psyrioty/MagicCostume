package org.psyrioty.magicCostume.Objects;

import org.psyrioty.magicModels.Objects.ActiveModel;
import org.psyrioty.magicModels.Objects.Target.ActiveEntity;

public class ActiveCostume {
    Costume costume;
    Slot slot;
    ActiveSlot activeSlot;
    ActiveCostumeEntity activeCostumeEntity;
    ActiveModel activeModel;

    double offsetX = 0;
    double offsetY = 0;
    double offsetZ = 0;
    double animationSpeed = 1;
    int light = 0;
    int scale = 1;

    public ActiveCostume(
            Costume costume,
            Slot slot,

            ActiveSlot activeSlot,
            ActiveCostumeEntity activeCostumeEntity,
            ActiveModel activeModel
    ){
        this.costume = costume;
        this.slot = slot;

        //не забыть дописать
    }
}
