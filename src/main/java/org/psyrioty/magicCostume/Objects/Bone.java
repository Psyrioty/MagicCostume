package org.psyrioty.magicCostume.Objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bone {
    float originX, originY, originZ; //начальная точка привязки
    float rotationX, rotationY, rotationZ; //поворот кости
    List<Bone> childBones = new ArrayList<>(); //дочерние кости
    String name; //имя кости
    UUID uuid; //идентификатор кости
    ItemDisplay boneEntity;

    public Bone(
            float originX,
            float originY,
            float originZ,

            float rotationX,
            float rotationY,
            float rotationZ,

            String name,

            UUID uuid
    ){
        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;

        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;

        this.name = name;

        this.uuid = uuid;
    }

    public void addChildBone(Bone bone){
        childBones.add(bone);
    }

    public List<Bone> getChildBones() {
        return childBones;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Bone clone() {
        Bone copy = new Bone(
                originX,
                originY,
                originZ,
                rotationX,
                rotationY,
                rotationZ,
                name,
                uuid
        );

        return copy;
    }

    public ItemDisplay getBoneEntity() {
        return boneEntity;
    }

    public void createBoneEntity(Entity target){
        World world = target.getWorld();
        Location location = target.getLocation();
        location.setPitch(0);

        ItemStack itemStack = new ItemStack(Material.DIAMOND);

        ItemDisplay display = world.spawn(location, ItemDisplay.class, entity -> {
            entity.setItemStack(itemStack);
        });

        Quaternionf rotation = new Quaternionf()
                .rotateXYZ(
                        (float) Math.toRadians(rotationX),
                        (float) Math.toRadians(rotationY),
                        (float) Math.toRadians(rotationZ)
                );

        display.setTransformation(new Transformation(
                new Vector3f(originX, originY, originZ),        // смещение
                new Quaternionf(),                              // левый поворот
                new Vector3f(1, 1, 1),                 // масштаб
                rotation                                        // правый поворот
        ));

        target.addPassenger(display);

        boneEntity = display;
    }
}
