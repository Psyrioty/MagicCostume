package org.psyrioty.magicCostume.Objects.GUI;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class SlotMenu implements InventoryHolder {
    Inventory inventory;

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
