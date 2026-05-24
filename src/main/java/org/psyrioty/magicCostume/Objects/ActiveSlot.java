package org.psyrioty.magicCostume.Objects;

public class ActiveSlot {
    Slot slot;
    ActiveCostume activeCostume;

    public ActiveSlot(Slot slot){
        this.slot = slot;
    }

    public void setActiveCostume(ActiveCostume activeCostume) {
        this.activeCostume = activeCostume;
    }

    public ActiveCostume getActiveCostume() {
        return activeCostume;
    }

    public Slot getSlot() {
        return slot;
    }
}
