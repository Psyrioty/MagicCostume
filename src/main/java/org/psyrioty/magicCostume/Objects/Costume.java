package org.psyrioty.magicCostume.Objects;

import org.psyrioty.magicModels.Objects.ActiveModel;
import org.psyrioty.magicModels.Objects.Model;

import java.util.Objects;

public class Costume {

    double offsetX, offsetY, offsetZ;
    Model model;
    Slot slot;

    String id;
    String name;

    boolean headModel;

    String permission;

    double scale;

    public Costume(
            String id,
            double offsetX, double offsetY, double offsetZ,
            Model model,
            Slot slot,
            String name,
            boolean headModel,
            String permission,
            double scale
    ){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;

        this.model = model;

        this.slot = slot;

        this.name = name;

        this.id = id;

        this.headModel = headModel;

        this.permission = Objects.requireNonNullElse(permission, "group.default");

        this.scale = scale;
    }

    public Slot getSlot() {
        return slot;
    }

    public Model getModel() {
        return model;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getScale() {
        return scale;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public double getOffsetZ() {
        return offsetZ;
    }

    public void setOffsetZ(double offsetZ) {
        this.offsetZ = offsetZ;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setHeadModel(boolean headModel) {
        this.headModel = headModel;
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
