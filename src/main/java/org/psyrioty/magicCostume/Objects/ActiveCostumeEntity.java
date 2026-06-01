package org.psyrioty.magicCostume.Objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicModels.Objects.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ActiveCostumeEntity {
    Entity entity;
    List<ActiveSlot> activeSlotList = new ArrayList<>();

    HashMap<Costume, HashMap<UUID, Integer>> brightnessCostumes = new HashMap<>();

    boolean hideOtherCostumes = false;

    public ActiveCostumeEntity(Entity entity){
        this.entity = entity;

        for(Slot slot: MagicCostume.getPlugin().getSlots()){
            ActiveSlot activeSlot = new ActiveSlot(
                    slot
            );
            activeSlotList.add(activeSlot);
        }

        MagicCostume.getPlugin().getActiveCostumeEntities().add(this);
    }

    public Entity getEntity() {
        return entity;
    }

    public List<ActiveSlot> getActiveSlotList() {
        return activeSlotList;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setHideOtherCostumes(boolean hideOtherCostumes) {
        this.hideOtherCostumes = hideOtherCostumes;

        if(!(entity instanceof Player player)){
            return;
        }

        for(ActiveCostumeEntity activeCostumeEntity: MagicCostume.getPlugin().getActiveCostumeEntities()){
            if(activeCostumeEntity == this){
                continue;
            }

            Bukkit.getLogger().info(activeCostumeEntity.getActiveSlotList() + "");

            for (ActiveSlot activeSlot : activeCostumeEntity.getActiveSlotList()) {
                if(activeSlot.getActiveCostume() == null){
                    continue;
                }
                if(hideOtherCostumes) {
                    MagicCostume.getPlugin().hideAllBones(activeSlot.getActiveCostume().getActiveModel().getHeadBones(), player);
                }else {
                    MagicCostume.getPlugin().showAllBones(activeSlot.getActiveCostume().getActiveModel().getHeadBones(), player);
                }
            }
        }
    }

    public boolean isHideOtherCostumes() {
        return hideOtherCostumes;
    }
}
