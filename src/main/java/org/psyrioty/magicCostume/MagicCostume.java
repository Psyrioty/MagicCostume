package org.psyrioty.magicCostume;

import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.psyrioty.magicCostume.Commands.MainPluginCommands;
import org.psyrioty.magicCostume.Listeners.EntityEvents;
import org.psyrioty.magicCostume.Listeners.GUIEvents;
import org.psyrioty.magicCostume.Objects.*;
import org.psyrioty.magicCostume.Objects.GUI.CostumeMenu;
import org.psyrioty.magicCostume.Objects.GUI.MainMenu;
import org.psyrioty.magicCostume.Objects.GUI.SlotMenu;
import org.psyrioty.magicModels.MagicModels;
import org.psyrioty.magicModels.Objects.ActiveModel;
import org.psyrioty.magicModels.Objects.Model;

import java.io.File;
import java.util.*;

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

    @Override
    public void onEnable() {
        plugin = this;
        pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new GUIEvents(), this);
        pm.registerEvents(new EntityEvents(), this);

        this.getCommand("costume").setExecutor(new MainPluginCommands());

        getCostumeFiles();

        createAllOnlinePlayersActiveCostumeEntity();
    }

    private void createAllOnlinePlayersActiveCostumeEntity(){
        for(Player player: Bukkit.getOnlinePlayers()){
            ActiveCostumeEntity activeCostumeEntity = createActiveCostumeEntity(player);
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
        // Plugin shutdown logic
    }

    public static MagicCostume getPlugin() {
        return plugin;
    }

    public List<Costume> getCostumes() {
        return costumes;
    }

    private void getCostumeFiles(){
        File costumesFolder = new File(plugin.getDataFolder(), "Costumes");

        if (!costumesFolder.exists()) {
            costumesFolder.mkdirs();
        }

        List<File> files = findYamlFiles(costumesFolder);

        for(File file: files){
            try {
                String id = file.getName().replace(".yml", "");

                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                double offsetX = yamlConfiguration.getDouble("offset.X");
                double offsetY = yamlConfiguration.getDouble("offset.Y");
                double offsetZ = yamlConfiguration.getDouble("offset.Z");
                String slotName = yamlConfiguration.getString("slotName");
                String modelName = yamlConfiguration.getString("model");
                String name = yamlConfiguration.getString("name");

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

                Costume costume = new Costume(
                        id,
                        offsetX, offsetY, offsetZ,
                        modelCostume,
                        slotCostume,
                        name
                );

                slotCostume.addCostume(costume);

                costumes.add(costume);
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

    public ActiveCostume spawnActiveCostume(Entity entity, Costume costume){
        ActiveModel activeModel = MagicModels.getPlugin().spawnModel(entity, costume.getModel().getName());
        ActiveCostume activeCostume = new ActiveCostume(
                costume,
                activeModel
        );

        ActiveCostumeEntity activeCostumeEntity = getActiveCostumeEntity(entity);
        for(ActiveSlot activeSlot: activeCostumeEntity.getActiveSlotList()) {
            if(activeSlot.getSlot() == costume.getSlot()) {
                activeSlot.setActiveCostume(activeCostume);
                return activeCostume;
            }
        }

        return null;
    }
}
