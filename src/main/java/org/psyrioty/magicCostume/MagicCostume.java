package org.psyrioty.magicCostume;

import com.google.gson.JsonObject;
import net.playavalon.mythicdungeons.api.MythicDungeonsService;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.psyrioty.magicCostume.Commands.MainPluginCommands;
import org.psyrioty.magicCostume.Database.DatabaseManager;
import org.psyrioty.magicCostume.Database.Requests;
import org.psyrioty.magicCostume.Listeners.EntityEvents;
import org.psyrioty.magicCostume.Listeners.GUIEvents;
import org.psyrioty.magicCostume.Listeners.MythicDungeonEvents;
import org.psyrioty.magicCostume.Objects.*;
import org.psyrioty.magicCostume.Objects.GUI.CostumeMenu;
import org.psyrioty.magicCostume.Objects.GUI.MainMenu;
import org.psyrioty.magicCostume.Objects.GUI.SlotMenu;
import org.psyrioty.magicModels.MagicModels;
import org.psyrioty.magicModels.Objects.ActiveModel;
import org.psyrioty.magicModels.Objects.Bone;
import org.psyrioty.magicModels.Objects.Model;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.psyrioty.magicCostume.Database.Requests.*;
import static org.psyrioty.magicCostume.utils.YmlFinder.findYamlFiles;

public final class MagicCostume extends JavaPlugin {

    static MagicCostume plugin;
    PluginManager pm;

    List<Costume> costumes = new ArrayList<>();
    List<Slot> slots = new ArrayList<>();

    List<MainMenu> activeMainMenus = new ArrayList<>();
    List<CostumeMenu> activeCostumeMenus = new ArrayList<>();
    List<SlotMenu> activeSlotMenus = new ArrayList<>();

    Set<ActiveCostumeEntity> activeCostumeEntities = new HashSet<>();

    File dbFile;

    @Override
    public void onEnable() {
        plugin = this;
        pm = plugin.getServer().getPluginManager();
        createDb();

        pm.registerEvents(new GUIEvents(), this);
        pm.registerEvents(new EntityEvents(), this);

        if (pm.getPlugin("MythicDungeons") != null) {
            pm.registerEvents(new MythicDungeonEvents(), this);
        }

        this.getCommand("costume").setExecutor(new MainPluginCommands());

        getCostumeFiles();

        createAllOnlinePlayersActiveCostumeEntity();
    }

    private void createDb(){
        try {
            DatabaseManager.connect(this);
            Requests.createTables(DatabaseManager.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void createAllOnlinePlayersActiveCostumeEntity(){
        for(Player player: Bukkit.getOnlinePlayers()){
            ActiveCostumeEntity activeCostumeEntity = createActiveCostumeEntity(player);
            //activeCostumeEntities.add(activeCostumeEntity);
        }
    }

    public ActiveCostumeEntity createActiveCostumeEntity(Entity entity){
        ActiveCostumeEntity activeCostumeEntity = new ActiveCostumeEntity(
            entity
        );

        return activeCostumeEntity;
    }

    @Override
    public void onDisable() {
        for(CostumeMenu costumeMenu: activeCostumeMenus){
            costumeMenu.getInventory().close();
        }

        for(MainMenu mainMenu: activeMainMenus){
            mainMenu.getInventory().close();
        }

        for(SlotMenu slotMenu: activeSlotMenus){
            slotMenu.getInventory().close();
        }

        Connection connection = DatabaseManager.getConnection();
        for(ActiveCostumeEntity activeCostumeEntity: activeCostumeEntities){

        }

        DatabaseManager.disconnect();
    }

    public static MagicCostume getPlugin() {
        return plugin;
    }

    public List<Costume> getCostumes() {
        return costumes;
    }

    public void getCostumeFiles(){
        File costumesFolder = new File(plugin.getDataFolder(), "Costumes");

        if (!costumesFolder.exists()) {
            costumesFolder.mkdirs();
        }

        List<File> files = findYamlFiles(costumesFolder);

        for(File file: files){
            try {
                String id = file.getName().replace(".yml", "");

                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                double offsetX = yamlConfiguration.getDouble("offset.x");
                double offsetY = yamlConfiguration.getDouble("offset.y");
                double offsetZ = yamlConfiguration.getDouble("offset.z");
                String slotName = yamlConfiguration.getString("slotName");
                String modelName = yamlConfiguration.getString("model");
                String name = yamlConfiguration.getString("name");
                boolean isHeadModel = yamlConfiguration.getBoolean("isHeadModel");
                String permission = yamlConfiguration.getString("permission");
                double scale = yamlConfiguration.getDouble("scale");
                if(scale == 0){
                    scale = 1;
                }

                if(slotName == null || modelName == null){
                    continue;
                }

                Model modelCostume = null;

                for(Model model: MagicModels.getPlugin().getModels()){
                    if(model.getName().equals(modelName)){
                        modelCostume = model;
                    }
                }

                if(modelCostume == null){
                    continue;
                }

                Slot slotCostume = null;
                for(Slot slot: slots){
                    if(slot.getName().equals(slotName)){
                        slotCostume = slot;
                    }
                }

                if(slotCostume == null){
                    slotCostume = new Slot(
                            slotName
                    );
                    slots.add(slotCostume);
                }

                Costume costume = null;

                for(Costume costumeOld: costumes){
                    if(costumeOld.getId().equals(id)){
                        costume = costumeOld;
                        costume.setOffsetX(offsetX);
                        costume.setOffsetY(offsetY);
                        costume.setOffsetZ(offsetZ);
                        costume.setName(name);
                        costume.setSlot(slotCostume);
                        costume.setHeadModel(isHeadModel);
                        costume.setPermission(permission);
                        costume.setScale(scale);

                        break;
                    }
                }

                if(costume == null) {
                    costume = new Costume(
                            id,
                            offsetX, offsetY, offsetZ,
                            modelCostume,
                            slotCostume,
                            name,
                            isHeadModel,
                            permission,
                            scale
                    );

                    slotCostume.addCostume(costume);

                    costumes.add(costume);
                }
            }catch (Exception exception){
                Bukkit.getLogger().severe("MagicCostume error in MagicCostume.java getCostumeFiles() " + file.getName() + " " + exception.getMessage());
            }
        }
    }

    public List<MainMenu> getActiveMainMenus() {
        return activeMainMenus;
    }

    public Set<ActiveCostumeEntity> getActiveCostumeEntities() {
        return activeCostumeEntities;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public List<CostumeMenu> getActiveCostumeMenus() {
        return activeCostumeMenus;
    }

    public List<SlotMenu> getActiveSlotMenus() {
        return activeSlotMenus;
    }

    public ActiveCostumeEntity getActiveCostumeEntity(Entity entity){
        for(ActiveCostumeEntity activeCostumeEntity: activeCostumeEntities){
            if(activeCostumeEntity.getEntity().getUniqueId().equals(entity.getUniqueId())){
                return activeCostumeEntity;
            }
        }

        return null;
    }

    public ActiveCostume spawnActiveCostume(
            Entity entity,
            Costume costume,
            HashMap<UUID, Integer> boneBrightness,
            float scale,
            float offsetX,
            float offsetY,
            float offsetZ,

            boolean headModel
    ){
        ActiveModel activeModel = MagicModels.getPlugin().spawnModel(
                entity,
                costume.getModel(),
                boneBrightness,
                scale * (float) costume.getScale(),
                offsetX + (float) costume.getOffsetX(),
                offsetY + (float) costume.getOffsetY(),
                offsetZ + (float) costume.getOffsetZ()
        );

        if(headModel){
            activeModel.setHeadModel(true);
        }

        for(ActiveCostumeEntity activeCostumeEntity: activeCostumeEntities){
            if(!activeCostumeEntity.isHideOtherCostumes()){
                continue;
            }

            if(!(activeCostumeEntity.getEntity() instanceof Player player)){
                continue;
            }

            if(player == entity){
                continue;
            }

            Bukkit.getScheduler().runTask(MagicCostume.getPlugin(), () -> {
                hideAllBones(activeModel.getHeadBones(), player);
            });
        }

        ActiveCostume activeCostume = new ActiveCostume(
                costume,
                activeModel,

                offsetX,
                offsetY,
                offsetZ,
                boneBrightness,
                scale
        );

        ActiveCostumeEntity activeCostumeEntity = getActiveCostumeEntity(entity);
        for(ActiveSlot activeSlot: activeCostumeEntity.getActiveSlotList()) {
            if(activeSlot.getSlot() == costume.getSlot()) {
                ActiveCostume activeCostumeOld = activeSlot.getActiveCostume();
                if(activeCostumeOld != null) {
                    activeCostumeOld.remove();
                }

                activeSlot.setActiveCostume(activeCostume);
                return activeCostume;
            }
        }

        return null;
    }

    public void hideAllBones(List<Bone> bones, Player target){
        for(Bone bone: bones){
            target.hideEntity(this, bone.getBoneEntity());
            List<Bone> childBones = bone.getChildBones();
            if(childBones == null){
                continue;
            }
            if(childBones.isEmpty()){
                continue;
            }
            hideAllBones(childBones, target);
        }
    }

    public void showAllBones(List<Bone> bones, Player target){
        for(Bone bone: bones){
            target.showEntity(this, bone.getBoneEntity());
            List<Bone> childBones = bone.getChildBones();
            if(childBones == null){
                continue;
            }
            if(childBones.isEmpty()){
                continue;
            }
            showAllBones(childBones, target);
        }
    }

    public ActiveCostumeEntity findActiveCostumeEntityForEntity(Entity entity){
        for(ActiveCostumeEntity activeCostumeEntity: activeCostumeEntities){
            if(entity.getUniqueId().equals(activeCostumeEntity.getEntity().getUniqueId())){
                return activeCostumeEntity;
            }
        }

        return null;
    }

    /*private void createDb(){
        File dbDir = new File(getDataFolder(), "Database");
        dbFile = new File(dbDir, "db.sqlite");


        if (!dbDir.exists() && !dbDir.mkdirs()) {
            getLogger().severe("Failed to create Database folder");
            return;
        }

        if (!dbFile.exists()) {
            try {
                if (dbFile.createNewFile()) {
                    getLogger().info("Created database file: " + dbFile.getPath());
                }
            } catch (IOException e) {
                getLogger().severe("Failed to create db.sqlite");
                e.printStackTrace();
                return;
            }
        }
    }*/

    public HashMap<UUID, Integer> setAllBoneBrightness(List<Bone> bones, HashMap<UUID, Integer> boneBrightness, int brightness){
        if(boneBrightness == null) {
            boneBrightness = new HashMap<>();
        }

        if(bones == null){
            return boneBrightness;
        }

        for(Bone bone: bones){
            boneBrightness.put(bone.getUuid(), brightness);

            setAllBoneBrightness(bone.getChildBones(), boneBrightness, brightness);
        }

        return boneBrightness;
    }
}
