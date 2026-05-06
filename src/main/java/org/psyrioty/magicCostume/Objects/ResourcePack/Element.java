package org.psyrioty.magicCostume.Objects.ResourcePack;

import java.util.List;

public class Element {

    List<Float> from,
            to,
            rotationOrigin, // нормальная origin записывается после нахождения группы
            northFaces, eastFaces, southFaces, westFaces, upFaces, downFaces;
    String northTextureName, eastTextureName, southTextureName, westTextureName, upTextureName, downTextureName;
    float rotationX, rotationY, rotationZ;
    String name;

    public Element(
            List<Float> from,
            List<Float> to,

            float rotationX, float rotationY, float rotationZ,
            List<Float> rotationOrigin,

            List<Float> northFaces,
            List<Float> eastFaces,
            List<Float> southFaces,
            List<Float> westFaces,
            List<Float> upFaces,
            List<Float> downFaces,

            String northTextureName,
            String eastTextureName,
            String southTextureName,
            String westTextureName,
            String upTextureName,
            String downTextureName,

            String name
    ){
        this.from = from;
        this.to = to;
        this.rotationOrigin = rotationOrigin;
        this.northFaces = northFaces;
        this.eastFaces = eastFaces;
        this.southFaces = southFaces;
        this.westFaces = westFaces;
        this.upFaces = upFaces;
        this.downFaces = downFaces;

        this.northTextureName = northTextureName;
        this.eastTextureName = eastTextureName;
        this.southTextureName = southTextureName;
        this.westTextureName = westTextureName;
        this.upTextureName = upTextureName;
        this.downTextureName = downTextureName;

        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;

        this.name = name;
    }

    public List<Float> getFrom() {
        return from;
    }

    public List<Float> getTo() {
        return to;
    }

    public List<Float> getRotationOrigin() {
        return rotationOrigin;
    }

    public List<Float> getNorthFaces() {
        return northFaces;
    }

    public List<Float> getEastFaces() {
        return eastFaces;
    }

    public List<Float> getSouthFaces() {
        return southFaces;
    }

    public List<Float> getWestFaces() {
        return westFaces;
    }

    public List<Float> getDownFaces() {
        return downFaces;
    }

    public List<Float> getUpFaces() {
        return upFaces;
    }

    public String getNorthTextureName() {
        return northTextureName;
    }

    public String getEastTextureName() {
        return eastTextureName;
    }

    public String getSouthTextureName() {
        return southTextureName;
    }

    public String getDownTextureName() {
        return downTextureName;
    }

    public String getUpTextureName() {
        return upTextureName;
    }

    public String getWestTextureName() {
        return westTextureName;
    }

    public float getRotationX() {
        return rotationX;
    }

    public float getRotationY() {
        return rotationY;
    }

    public float getRotationZ() {
        return rotationZ;
    }

    public void setRotationOrigin(List<Float> rotationOrigin) {
        this.rotationOrigin = rotationOrigin;
    }
}
