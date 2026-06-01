package org.psyrioty.magicCostume.Objects;

import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicModels.Objects.ActiveModel;
import org.psyrioty.magicModels.Objects.Target.ActiveEntity;

import java.util.HashMap;
import java.util.UUID;

public class ActiveCostume {
    Costume costume;
    ActiveSlot activeSlot;
    ActiveCostumeEntity activeCostumeEntity;
    ActiveModel activeModel;

    double offsetX = 0;
    double offsetY = 0;
    double offsetZ = 0;
    double animationSpeed = 1;
    HashMap<UUID, Integer> brightness;
    double scale = 1;

    public ActiveCostume(
            Costume costume,
            ActiveModel activeModel,

            double offsetX,
            double offsetY,
            double offsetZ,
            HashMap<UUID, Integer> brightness,
            double scale
    ){
        this.costume = costume;
        this.activeModel = activeModel;

        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.brightness = brightness;
        this.scale = scale;
    }

    public ActiveModel getActiveModel() {
        return activeModel;
    }

    public void remove(){
        activeModel.remove();
    }

    public Costume getCostume() {
        return costume;
    }

    public HashMap<UUID, Integer> getBrightness() {
        return brightness;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getOffsetZ() {
        return offsetZ;
    }

    public double getScale() {
        return scale;
    }
}
