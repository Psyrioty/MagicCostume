package org.psyrioty.magicCostume.Objects;

import org.psyrioty.magicModels.Objects.ActiveModel;
import org.psyrioty.magicModels.Objects.Model;

public class Costume {

    double offsetX, offsetY, offsetZ;
    Model model;
    Slot slot;

    String id;
    String name;

    boolean headModel;

    String permission;

    public Costume(
            String id,
            double offsetX, double offsetY, double offsetZ,
            Model model,
            Slot slot,
            String name,
            boolean headModel,
            String permission
    ){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;

        this.model = model;

        this.slot = slot;

        this.name = name;

        this.id = id;

        this.headModel = headModel;

        if(permission != null) {
            this.permission = permission;
        }else{
            permission = "group.default";
        }
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

    public boolean isHeadModel() {
        return headModel;
    }

    public String getId() {
        return id;
    }

    public String getPermission() {
        return permission;
    }
}
