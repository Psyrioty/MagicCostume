package org.psyrioty.magicCostume.Objects;

import org.psyrioty.magicModels.Objects.ActiveModel;
import org.psyrioty.magicModels.Objects.Model;

public class Costume {

    double offsetX, offsetY, offsetZ;
    Model model;
    Slot slot;

    String id;
    String name;

    public Costume(
            String id,
            double offsetX, double offsetY, double offsetZ,
            Model model,
            Slot slot,
            String name
    ){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;

        this.model = model;

        this.slot = slot;

        this.name = name;

        this.id = id;
    }

    public Slot getSlot() {
        return slot;
    }

    public Model getModel() {
        return model;
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

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
