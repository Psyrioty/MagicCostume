package org.psyrioty.magicCostume.Objects;

import org.bukkit.entity.Entity;
import org.psyrioty.magicCostume.MagicCostume;

import java.util.ArrayList;
import java.util.List;

public class ActiveCostumeEntity {
    Entity entity;
    List<ActiveSlot> activeSlotList = new ArrayList<>();

    public ActiveCostumeEntity(Entity entity){
        this.entity = entity;

        for(Slot slot: MagicCostume.getPlugin().getSlots()){
            ActiveSlot activeSlot = new ActiveSlot(
                    slot
            );
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
}
