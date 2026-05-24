package org.psyrioty.magicCostume.Objects;

import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicModels.Objects.ActiveModel;
import org.psyrioty.magicModels.Objects.Target.ActiveEntity;

public class ActiveCostume {
    Costume costume;
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
            ActiveModel activeModel
    ){
        this.costume = costume;
        this.activeModel = activeModel;

        //не забыть дописать
    }
}
