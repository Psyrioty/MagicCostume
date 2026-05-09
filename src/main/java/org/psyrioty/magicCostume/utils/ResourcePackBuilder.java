package org.psyrioty.magicCostume.utils;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.ResourcePack.Element;
import org.psyrioty.magicCostume.Objects.ResourcePack.Group;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class ResourcePackBuilder {
    static List<JsonObject> caseList = new ArrayList<>();

    public static void createPackMcmeta() {
        try {
            JsonObject root = new JsonObject();

            JsonObject pack = new JsonObject();
            pack.addProperty("pack_format", 15);
            pack.addProperty("min_format", 15);
            pack.addProperty("max_format", 32767);

            JsonArray supportedFormats = new JsonArray();
            supportedFormats.add(15);
            supportedFormats.add(32767);

            pack.add("supported_formats", supportedFormats);
            pack.addProperty("description", "Created by Psyrioty");

            root.add("pack", pack);

            Path path = MagicCostume.getPlugin()
                    .getDataFolder()
                    .toPath()
                    .resolve("resourcepack")
                    .resolve("pack.mcmeta");

            Files.createDirectories(path.getParent());

            String json = new GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(root);

            Files.writeString(
                    path,
                    json,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            Bukkit.getLogger().info("Created pack.mcmeta");

        } catch (Exception e) {
            Bukkit.getLogger().severe(
                    "createPackMcmeta error: " + e.getMessage()
            );
            e.printStackTrace();
        }
    }

    public static void createBoneJsonResourcePack(
            String name,
            List<String> textureNames,
            List<Element> elements,
            Group group
    ) {
        JsonObject root = new JsonObject();

        JsonObject textures = new JsonObject();
        if (textureNames != null && !textureNames.isEmpty()) {
            textures.addProperty("particle", "#0");
            int texturesIterator = 0;
            for (String textureName : textureNames) {
                textures.addProperty(String.valueOf(texturesIterator), "magiccostume:costumes/" + name + "/" + textureName.replace(".png", ""));
                texturesIterator++;
            }
        }
        root.add("textures", textures);

        JsonArray elementsJson = new JsonArray();
        //JsonArray children = new JsonArray();

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
                    .resolve("assets")
                    .resolve("magiccostume")
                    .resolve("models");

            String folderName = safeFileName(name == null ? "default" : name);
            String fileName = safeFileName(group.getName()) + ".json";

            Path targetDir = baseDir.resolve(folderName);
            Files.createDirectories(targetDir);

            Path targetFile = targetDir.resolve(fileName);
            Files.writeString(targetFile, json);

            createCase(
                    name,
                    group.getName()
            );

            //Bukkit.getLogger().info("Saved model json: " + targetFile);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to save model json: " + e.getMessage());
        }
    }

    public static void createItemFile() {
        JsonObject root = new JsonObject();

        JsonObject model = new JsonObject();
        model.addProperty("type", "minecraft:select");
        model.addProperty("property", "minecraft:custom_model_data");

        JsonArray cases = new JsonArray();
        for (JsonObject caseObject : caseList) {
            cases.add(caseObject);
        }

        model.add("cases", cases);

        JsonObject fallback = new JsonObject();
        fallback.addProperty("type", "minecraft:model");
        fallback.addProperty("model", "minecraft:item/white_wool");
        model.add("fallback", fallback);

        root.add("model", model);

        String json = new GsonBuilder().setPrettyPrinting().create().toJson(root);

        try {
            Path target = MagicCostume.getPlugin().getDataFolder()
                    .toPath()
                    .resolve("resourcepack")
                    .resolve("assets")
                    .resolve("minecraft")
                    .resolve("items")
                    .resolve("white_wool.json");

            Files.createDirectories(target.getParent());
            Files.writeString(target, json);

            Bukkit.getLogger().info("Saved item model: " + target);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to save item model: " + e.getMessage());
        }

        caseList.clear();
    }

    private static void createCase(String modelName, String boneName){
        JsonObject caseObject = new JsonObject();
        caseObject.addProperty("when", modelName + "_" + boneName);

        JsonObject caseModel = new JsonObject();
        caseModel.addProperty("type", "minecraft:model");
        caseModel.addProperty("model", "magiccostume:" + modelName + "/" + boneName);

        caseObject.add("model", caseModel);

        caseList.add(caseObject);
    }

    public static void decodeTextures(File modelFile) {
        try (FileReader reader = new FileReader(modelFile)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            String modelName = root.has("name") && !root.get("name").isJsonNull()
                    ? root.get("name").getAsString()
                    : "unknown_model";

            if (!root.has("textures") || root.get("textures").isJsonNull()) {
                Bukkit.getLogger().warning("No textures in " + modelFile.getName());
                return;
            }

            JsonElement texturesElement = root.get("textures");

            if (texturesElement.isJsonArray()) {
                JsonArray texturesArray = texturesElement.getAsJsonArray();

                for (JsonElement textureElement : texturesArray) {
                    if (!textureElement.isJsonObject()) {
                        continue;
                    }

                    JsonObject textureObj = textureElement.getAsJsonObject();
                    saveTextureObject(textureObj, modelName);
                }

            } else if (texturesElement.isJsonObject()) {
                JsonObject texturesObj = texturesElement.getAsJsonObject();

                for (Map.Entry<String, JsonElement> entry : texturesObj.entrySet()) {
                    if (!entry.getValue().isJsonObject()) {
                        continue;
                    }

                    JsonObject textureObj = entry.getValue().getAsJsonObject();

                    if (!textureObj.has("name")) {
                        textureObj.addProperty("name", entry.getKey());
                    }

                    saveTextureObject(textureObj, modelName);
                }

            } else {
                Bukkit.getLogger().warning("Unsupported textures format in " + modelFile.getName());
            }

        } catch (Exception e) {
            Bukkit.getLogger().severe("decodeTextures error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void saveTextureObject(JsonObject textureObj, String modelName) {
        try {
            if (!textureObj.has("source") || textureObj.get("source").isJsonNull()) {
                return;
            }

            String source = textureObj.get("source").getAsString();

            if (!source.startsWith("data:")) {
                return;
            }

            int commaIndex = source.indexOf(",");
            if (commaIndex == -1) {
                return;
            }

            String base64 = source.substring(commaIndex + 1);
            byte[] imageBytes = Base64.getDecoder().decode(base64);

            String textureName = textureObj.has("name") && !textureObj.get("name").isJsonNull()
                    ? textureObj.get("name").getAsString()
                    : "texture";

            textureName = safeFileName(textureName);
            modelName = safeFileName(modelName);

            if(textureName.endsWith(".png")){
                textureName = textureName.substring(0, textureName.length() - 4);
            }

            Path target = MagicCostume.getPlugin().getDataFolder()
                    .toPath()
                    .resolve("resourcepack")
                    .resolve("assets")
                    .resolve("magiccostume")
                    .resolve("textures")
                    .resolve("item")
                    .resolve("costumes")
                    .resolve(modelName)
                    .resolve(textureName + ".png");

            Files.createDirectories(target.getParent());
            Files.write(target, imageBytes);

            Bukkit.getLogger().info("Saved texture: " + target);

        } catch (Exception e) {
            Bukkit.getLogger().severe("saveTextureObject error: " + e.getMessage());
            e.printStackTrace();
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

    public static void clearResourcePackFolder() {
        Path resourcePackPath = MagicCostume.getPlugin()
                .getDataFolder()
                .toPath()
                .resolve("resourcepack");

        try {

            if (Files.exists(resourcePackPath)) {

                Files.walk(resourcePackPath)
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                                Bukkit.getLogger().severe(
                                        "Failed delete: " + path + " | " + e.getMessage()
                                );
                            }
                        });
            }

            Files.createDirectories(resourcePackPath);

            Bukkit.getLogger().info("Resourcepack folder cleared");

        } catch (Exception e) {
            Bukkit.getLogger().severe(
                    "clearResourcePackFolder error: " + e.getMessage()
            );
            e.printStackTrace();
        }
    }
}