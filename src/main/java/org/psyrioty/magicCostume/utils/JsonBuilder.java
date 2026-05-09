package org.psyrioty.magicCostume.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.joml.Vector3f;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.ResourcePack.Element;
import org.psyrioty.magicCostume.Objects.ResourcePack.Group;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonBuilder {
    public static void createBoneJsonResourcePack(
            String name,
            List<String> textureNames,
            List<Element> elements,
            Group group
    ) {
        JsonObject root = new JsonObject();

        JsonObject textures = new JsonObject();
        if (textureNames != null && !textureNames.isEmpty()) {
            textures.addProperty("particle", "textures/" + textureNames.getFirst());
            int texturesIterator = 0;
            for (String textureName : textureNames) {
                textures.addProperty(String.valueOf(texturesIterator), "textures/" + textureName);
                texturesIterator++;
            }
        }
        root.add("textures", textures);

        JsonArray elementsJson = new JsonArray();
        JsonArray children = new JsonArray();

        //int elementIterator = 0;
        for (Element element : elements) {
            JsonObject jsonElement = new JsonObject();

            List<Float> newVectorFrom = mathVec3(element.getFrom(), group.getOrigin());
            jsonElement.add("from", vec3(newVectorFrom));

            List<Float> newVectorTo = mathVec3(element.getTo(), group.getOrigin());
            jsonElement.add("to", vec3(newVectorTo));

            JsonObject rotationObject = new JsonObject();
            if (element.getRotationX() == 0
                    && element.getRotationY() == 0
                    && element.getRotationZ() == 0) {
                rotationObject.addProperty("angle", 0);
                rotationObject.addProperty("axis", "y");
            } else {
                rotationObject.addProperty("x", element.getRotationX());
                rotationObject.addProperty("y", element.getRotationY());
                rotationObject.addProperty("z", element.getRotationZ());
            }
            rotationObject.add("origin", vec3(element.getRotationOrigin()));
            jsonElement.add("rotation", rotationObject);

            JsonObject faces = new JsonObject();
            faces.add("north", face(element.getNorthFaces(), element.getNorthTextureName()));
            faces.add("east", face(element.getEastFaces(), element.getEastTextureName()));
            faces.add("south", face(element.getSouthFaces(), element.getSouthTextureName()));
            faces.add("west", face(element.getWestFaces(), element.getWestTextureName()));
            faces.add("up", face(element.getUpFaces(), element.getUpTextureName()));
            faces.add("down", face(element.getDownFaces(), element.getDownTextureName()));
            jsonElement.add("faces", faces);

            //children.add(elementIterator++);
            elementsJson.add(jsonElement);
        }

        root.add("elements", elementsJson);

        /*JsonArray groups = new JsonArray();
        JsonObject groupsJson = new JsonObject();
        groupsJson.addProperty("name", group.getName());
        groupsJson.add("origin", vec3(group.getOrigin()));
        groupsJson.addProperty("scope", group.getScope());
        groupsJson.addProperty("color", group.getColor());
        groupsJson.add("children", children);
        groups.add(groupsJson);

        root.add("groups", groups);*/

        String json = new GsonBuilder().setPrettyPrinting().create().toJson(root);

        try {
            Path baseDir = MagicCostume.getPlugin().getDataFolder()
                    .toPath()
                    .resolve("resourcepack")
                    .resolve("magiccostume")
                    .resolve("models");

            String folderName = safeFileName(name == null ? "default" : name);
            String fileName = safeFileName(group.getName()) + ".json";

            Path targetDir = baseDir.resolve(folderName);
            Files.createDirectories(targetDir);

            Path targetFile = targetDir.resolve(fileName);
            Files.writeString(targetFile, json);

            //Bukkit.getLogger().info("Saved model json: " + targetFile);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to save model json: " + e.getMessage());
        }
    }

    private static List<Float> mathVec3(List<Float> cubeVec, List<Float> boneVec){
        List<Float> newVector = new ArrayList<>();
        for(int i = 0; i < cubeVec.size(); i++){
            float value = cubeVec.get(i) - boneVec.get(i);
            newVector.add(value);
        }
        return newVector;
    }

    private static JsonArray vec3(List<Float> values) {
        JsonArray arr = new JsonArray();
        for (float value : values) {
            arr.add(value);
        }
        return arr;
    }

    private static JsonObject face(List<Float> values, String textureName) {
        JsonObject f = new JsonObject();

        JsonArray uv = new JsonArray();
        uv.add(values.get(0));
        uv.add(values.get(1));
        uv.add(values.get(2));
        uv.add(values.get(3));

        f.add("uv", uv);
        f.addProperty("texture", textureName);

        return f;
    }

    private static String safeFileName(String input) {
        return input.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
    }
}