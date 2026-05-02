package org.psyrioty.magicCostume.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.Animations.Animation;
import org.psyrioty.magicCostume.Objects.Animations.AnimationController;
import org.psyrioty.magicCostume.Objects.Animations.AnimationKey;
import org.psyrioty.magicCostume.Objects.Animations.AnimationLine;
import org.psyrioty.magicCostume.Objects.BBModel.Outliner;
import org.psyrioty.magicCostume.Objects.Bone;
import org.psyrioty.magicCostume.Objects.Costume;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getLogger;

public class Converter {
    public static void ConvertBBModelsToResourcePackAndModels(){
        List<File> modelFiles = getAllBbModels();
        for(File modelFile: modelFiles){
            try {
                //############## создание модели #####################
                List<Bone> bones = getBones(modelFile); // создание костей
                Costume costume = getCostume(modelFile, bones); //создание модели
                MagicCostume.getPlugin().getCostumes().add(costume);
                AnimationController animationController = createAnimationController(modelFile, bones); //создание анимаций
                //####################################################
            }catch (Exception exception){
                Bukkit.getLogger().severe("MagicCostume error Converter.java ConvertBBModelsToResourcePack() " + exception.getMessage());
            }
        }
    }

    private static AnimationController createAnimationController(File modelFile, List<Bone> bones){
        try {
            List<Animation> animations = getAnimations(modelFile, bones);
        }catch (Exception exception){
            Bukkit.getLogger().severe("MagicCostume error Converter.java createAnimationController() " + exception.getMessage());
        }
        return null;
    }

    private static List<Animation> getAnimations(File modelFile, List<Bone> bones){
        try {
            List<Animation> animationList = new ArrayList<>();
            String jsonAnimations = getKeyValue(modelFile, "animations");
            if(jsonAnimations == null){
                return null;
            }
            List<JsonObject> jsonAnimationList = getObjects(jsonAnimations);
            for(JsonObject jsonAnimation: jsonAnimationList){
                String name = getKeyValue(String.valueOf(jsonAnimation), "name");
                String uuidString = getKeyValue(String.valueOf(jsonAnimation), "uuid");
                uuidString = uuidString.replace("\"", "");
                String loopString = getKeyValue(String.valueOf(jsonAnimation), "loop");
                boolean loop = false;
                if(loopString.equals("loop")){
                    loop = true;
                }

                List<AnimationLine> animationLines = getAnimationLines(jsonAnimation);
            }

            return animationList;
        }catch (Exception e){
            Bukkit.getLogger().severe("MagicCostume error Converter.java getAnimations() " + e.getMessage());
        }
        return null;
    }

    private static List<AnimationLine> getAnimationLines(JsonObject jsonObject){
        try {
            List<AnimationLine> animationLines = new ArrayList<>();
            String jsonAnimators = getKeyValue(jsonObject.toString(), "animators");
            Map<String, JsonObject> jsonAnimatorMap = getObjectsWithKeys(jsonAnimators);
            for(String uuidString: jsonAnimatorMap.keySet()){
                if(uuidString.equals("effects")){

                    continue;
                }

                JsonObject jsonObjectAnimator =  jsonAnimatorMap.get(uuidString);

                List<AnimationKey> animationKeys = getAnimationKeys(jsonObjectAnimator);
            }
            return animationLines;
        }catch (Exception e){
            Bukkit.getLogger().severe("MagicCostume error Converter.java getAnimationLines() " + e.getMessage());
        }
        return null;
    }

    private static List<AnimationKey> getAnimationKeys(JsonObject jsonObject){
        try {
            List<AnimationKey> animationKeys = new ArrayList<>();

            String keyframes = getKeyValue(String.valueOf(jsonObject), "keyframes");
            if(keyframes == null){
                return null;
            }
            List<JsonObject> keyframeList = getObjects(keyframes);
            for(JsonObject jsonKeyframe: keyframeList) {
                Bukkit.getLogger().info(jsonKeyframe.toString());
            }

            return animationKeys;
        }catch (Exception exception){
            Bukkit.getLogger().info("MagicCostume error Converter.java getAnimationKeys() " + exception.getMessage());
        }
        return null;
    }

    private static List<Bone> getBones(File modelFile){
        try {
            List<Bone> bones = new ArrayList<>();

            String jsonGroups = getKeyValue(modelFile, "groups");
            List<JsonObject> jsonGroupsList = getObjects(jsonGroups);
            for(JsonObject jsonGroup: jsonGroupsList){
                String name = getKeyValue(String.valueOf(jsonGroup), "name");
                List<Float> origins = jsonToFloat(getKeyValue(String.valueOf(jsonGroup), "origin"));
                List<Float> rotations = jsonToFloat(getKeyValue(String.valueOf(jsonGroup), "rotation"));
                String uuidString = getKeyValue(String.valueOf(jsonGroup), "uuid");
                uuidString = uuidString.replace("\"", "");
                UUID uuid = UUID.fromString(uuidString);


                Bone bone = new Bone(
                        origins.get(0) / 16,
                        origins.get(1) / 16,
                        origins.get(2) / 16,

                        rotations.get(0),
                        rotations.get(1),
                        rotations.get(2),

                        name,

                        uuid
                );

                bones.add(bone);
            }
            return bones;

        }catch (Exception exception){
            Bukkit.getLogger().severe("MagicCostume error: Converter.java getBones() " + exception.getMessage());
        }
        return null;
    }

    private static Costume getCostume(File modelFile, List<Bone> bones){
        try {
            String jsonOutlines = getKeyValue(modelFile, "outliner");

            List<Outliner> allOutliners = getOutliners(jsonOutlines, new ArrayList<>());
            List<Bone> headBones = new ArrayList<>();
            setChildBones(bones, allOutliners, headBones);

            Costume costume = new Costume(
                    headBones,
                    modelFile.getName().replace(".bbmodel", "")
            );

            return costume;

        }catch (Exception exception){
            Bukkit.getLogger().severe("MagicCostume Converter.java error getCostume() " + exception.getMessage());
        }
        return null;
    }

    private static void setChildBones(List<Bone> bones, List<Outliner> outliners, List<Bone> headBones){
        try {
            for(Outliner outliner: outliners){
                Bone headBone = getBoneForUUID(outliner.getUuid(), bones);

                if(headBones != null){
                    headBones.add(headBone);
                }

                List<Outliner> childOutliners = outliner.getChildOutliners();
                if(childOutliners.isEmpty()){
                    return;
                }
                for(Outliner outlinerChild: childOutliners){
                    Bone childBone = getBoneForUUID(outlinerChild.getUuid(), bones);
                    headBone.addChildBone(childBone);
                }

                setChildBones(bones, childOutliners, null);
            }
        }catch (Exception exception){
            Bukkit.getLogger().severe("MagicCostume error Converter.java setChildBones() " + exception.getMessage());
        }
    }

    private static Bone getBoneForUUID(UUID uuid, List<Bone> bones){
        for(Bone bone: bones){
            if(bone.getUuid().equals(uuid)) {
                return bone;
            }
        }
        return null;
    }

    private static List<Outliner> getOutliners(String jsonOutlines, List<Outliner> outliners){
        try {
            List<JsonObject> jsonOutlinersList = getObjects(jsonOutlines);
            for(JsonObject jsonOutliner: jsonOutlinersList){
                String uuidString = getKeyValue(jsonOutliner.toString(),"uuid");
                uuidString = uuidString.replace("\"","");
                UUID uuid = UUID.fromString(uuidString);

                Outliner outliner = new Outliner(uuid);
                outliners.add(outliner);

                String newJsonOutlines = getKeyValue(jsonOutliner.toString(), "children");
                if(newJsonOutlines == null){
                    break;
                }
                List<Outliner> childOutlinersNew = new ArrayList<>();
                List<Outliner> childOutliners = getOutliners(newJsonOutlines, childOutlinersNew);

                for(Outliner outlinerChild: childOutliners){
                    outliner.addChildOutliner(outlinerChild);
                }
            }
        }catch (Exception exception){
            Bukkit.getLogger().severe("MagicCostume error Converter.java getOutliners() " + exception.getMessage());
        }
        return outliners;
    }

    private static List<File> getAllBbModels() {
        File modelsDir = new File(MagicCostume.getPlugin().getDataFolder(), "models");

        if (!modelsDir.exists()) {
            modelsDir.mkdirs();
            return List.of();
        }

        try {
            return Files.walk(modelsDir.toPath())
                    .filter(Files::isRegularFile)
                    .map(path -> path.toFile())
                    .filter(file -> file.getName().endsWith(".bbmodel"))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            getLogger().severe("Failed to scan bbmodel files: " + e.getMessage());
            return List.of();
        }
    }

    //###################################################################################
    //###################################################################################
    //####################################РАБОТА JSON####################################
    //###################################################################################
    //###################################################################################
    private static List<Float> jsonToFloat(String json){
        if(json == null){
            return null;
        }

        String[] jsonSplit = json.split(",");
        List<Float> floats = new ArrayList<>();
        for(String floatString: jsonSplit){
            try {
                floatString = floatString.
                        replace("[", "").
                        replace("]", "").
                        replace("\"", "");
                floats.add(Float.valueOf(floatString));
            }catch (Exception exception){
                Bukkit.getLogger().severe("MagicCostume error Converter.java jsonToFloat() " + exception.getMessage());
            }
        }

        return floats;
    }

    private static String getKeyValue(File bbmodelFile, String key) {
        try (FileReader reader = new FileReader(bbmodelFile)) {
            return getKeyValue(reader, key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getKeyValue(String jsonString, String key) {
        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            return getFromJson(json, key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getKeyValue(Reader reader, String key) {
        try {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            return getFromJson(json, key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getFromJson(JsonObject json, String key) {
        if (json.has(key)) {
            JsonElement element = json.get(key);
            return element.isJsonNull() ? null : element.toString();
        }
        return null;
    }

    private static List<JsonObject> getObjects(String jsonString) {
        List<JsonObject> list = new ArrayList<>();

        try {
            JsonElement root = JsonParser.parseString(jsonString);

            if (root.isJsonArray()) {
                for (JsonElement element : root.getAsJsonArray()) {
                    if (element.isJsonObject()) {
                        list.add(element.getAsJsonObject());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private static Map<String, JsonObject> getObjectsWithKeys(String jsonString) {
        Map<String, JsonObject> map = new LinkedHashMap<>();

        try {
            JsonElement root = JsonParser.parseString(jsonString);

            if (root.isJsonObject()) {
                JsonObject rootObject = root.getAsJsonObject();

                for (Map.Entry<String, JsonElement> entry : rootObject.entrySet()) {
                    if (entry.getValue().isJsonObject()) {
                        map.put(entry.getKey(), entry.getValue().getAsJsonObject());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
    //###################################################################################
    //###################################################################################
    //####################################РАБОТА JSON КОНЕЦ##############################
    //###################################################################################
    //###################################################################################
}
