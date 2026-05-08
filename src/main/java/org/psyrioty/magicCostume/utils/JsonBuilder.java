package org.psyrioty.magicCostume.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.psyrioty.magicCostume.Objects.ResourcePack.Element;
import org.psyrioty.magicCostume.Objects.ResourcePack.Group;

import java.util.ArrayList;
import java.util.List;

public class JsonBuilder {
    public static void createBoneJsonResourcePack(
            String name,
            List<String> textureNames,
            List<Element> elements,
            Group group
    ){
        JsonObject root = new JsonObject();

        /* ------------------ format_version / credit ---------------- */
        root.addProperty("format_version", "1.21.11");
        root.addProperty("credit", "Made by Psyrioty");

        /* ------------------ textures ---------------- */
        JsonObject textures = new JsonObject();
        if(textureNames != null) {
            textures.addProperty("particle", "textures/" + textureNames.getFirst());
        }

        int texturesIterator = 0;
        for(String textureName: textureNames){
            textures.addProperty(texturesIterator + "", "textures/" + textureName);
            root.add("textures", textures);
            texturesIterator++;
        }

        /* ---------------- elements ------------------- */
        JsonArray elementsJson = new JsonArray();

        List<JsonObject> jsonElements = new ArrayList<>();

        //для groups-ов
        int elementIterator = 0;
        JsonArray children = new JsonArray();
        //

        for(Element element: elements){
            JsonObject jsonElement = new JsonObject();

            jsonElement.add("from", vec3(element.getFrom()));
            jsonElement.add("to", vec3(element.getTo()));

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

            //для groups-ов
            children.add(elementIterator);

            elementIterator++;

            elementsJson.add(jsonElement);
        }

        root.add("elements", elementsJson);

        /*   ------------------  groups ----------------  */
        JsonArray groups = new JsonArray();

        JsonObject groupsJson = new JsonObject();
        groupsJson.addProperty("name", group.getName());
        groupsJson.add("origin", vec3(group.getOrigin()));
        groupsJson.addProperty("scope", group.getScope());
        groupsJson.addProperty("color", group.getColor());


        groupsJson.add("children", children);
        groups.add(groupsJson);

        root.add("groups", groups);

        /* ---------------- output ------------------ */
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(root);
        Bukkit.getLogger().info(json);
    }

    private static JsonArray vec3(List<Float> values) {
        JsonArray arr = new JsonArray();

        for(float value: values){
            arr.add(value);
        }

        return arr;
    }

    private static JsonObject face(List<Float> values, String textureName){
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
}

/***
 * {
 * 	"format_version": "1.21.11",
 * 	"credit": "Made by Psyrioty",
 * 	"textures": {
 * 		"0": "block/texture",
 * 		"particle": "block/texture"
 * 	    },
 * 	"elements": [
 *        {
 * 			"from": [7, 0, 5],
 * 			"to": [9, 2, 7],
 * 			"rotation": {"angle": 0, "axis": "y", "origin": [7, 0, 5]},
 * 			"faces": {
 * 				"north": {"uv": [2, 4, 4, 6], "texture": "#0"},
 * 				"east": {"uv": [4, 2, 6, 4], "texture": "#0"},
 * 				"south": {"uv": [4, 4, 6, 6], "texture": "#0"},
 * 				"west": {"uv": [0, 6, 2, 8], "texture": "#0"},
 * 				"up": {"uv": [8, 2, 6, 0], "texture": "#0"},
 * 				"down": {"uv": [4, 6, 2, 8], "texture": "#0"}
 *            }
 *        },
 *        {
 * 			"from": [3, 3, 5],
 * 			"to": [5, 5, 7],
 * 			"rotation": {"angle": 0, "axis": "y", "origin": [3, 3, 5]},
 * 			"faces": {
 * 				"north": {"uv": [0, 0, 2, 2], "texture": "#0"},
 * 				"east": {"uv": [0, 2, 2, 4], "texture": "#0"},
 * 				"south": {"uv": [2, 0, 4, 2], "texture": "#0"},
 * 				"west": {"uv": [2, 2, 4, 4], "texture": "#0"},
 * 				"up": {"uv": [2, 6, 0, 4], "texture": "#0"},
 * 				"down": {"uv": [6, 0, 4, 2], "texture": "#0"}
 *            }
 *        }
 * 	],
 * 	"groups": [
 *        {
 * 			"name": "group",
 * 			"origin": [8, 8, 8],
 * 			"scope": 0,
 * 			"color": 0,
 * 			"children": [0, 1]
 *        }
 * 	]
 * }
 */
