package org.psyrioty.magicCostume.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.GUI.CostumeMenu;
import org.psyrioty.magicCostume.Objects.GUI.MainMenu;
import org.psyrioty.magicCostume.Objects.GUI.SlotMenu;

import java.util.List;

public class GUIEvents implements Listener {
    @EventHandler
    private void onClick(InventoryClickEvent event){
        Inventory inventory = event.getInventory();
        if(isGUI(inventory)){
            event.setCancelled(true);
        }

        MainMenu mainMenu = findActiveMainMenu(inventory);

        /*if(mainMenu == null){
            Bukkit.getLogger().severe("MagicCostumeError GUIEvents.java onClick() mainMenu is null");
            return;
        }*/

        if(mainMenu != null) {
            mainMenu.click((Player) event.getWhoClicked(), event.getSlot());
            return;
        }

        SlotMenu slotMenu = findActiveSlotMenu(inventory);

        if(slotMenu != null){
            slotMenu.click((Player) event.getWhoClicked(), event.getSlot());
            return;
        }

        CostumeMenu costumeMenu = findActiveCostumeMenu(inventory);

        if(costumeMenu != null){
            costumeMenu.click((Player) event.getWhoClicked(), event.getSlot());
        }
    }

    private boolean isGUI(Inventory inventory){
        if(
                inventory.getHolder() instanceof CostumeMenu ||
                inventory.getHolder() instanceof MainMenu ||
                inventory.getHolder() instanceof SlotMenu
        ){
            return true;
        }
        return false;
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event){
        Inventory inventory = event.getInventory();
        if(!isGUI(inventory)){
            return;
        }

        MainMenu mainMenu = findActiveMainMenu(inventory);

        /*if(mainMenu == null){
            Bukkit.getLogger().severe("MagicCostumeError GUIEvents.java onClose() mainMenu is null");
            return;
        }*/

        if(mainMenu != null) {
            MagicCostume.getPlugin().getActiveMainMenus().remove(mainMenu);
            return;
        }

        SlotMenu slotMenu = findActiveSlotMenu(inventory);
        if(slotMenu != null){
            MagicCostume.getPlugin().getActiveSlotMenus().remove(slotMenu);
            return;
        }

        CostumeMenu costumeMenu = findActiveCostumeMenu(inventory);
        if(costumeMenu != null){
            MagicCostume.getPlugin().getActiveCostumeMenus().remove(costumeMenu);
            return;
        }
    }

    private SlotMenu findActiveSlotMenu(Inventory inventory){
        if(!(inventory.getHolder() instanceof SlotMenu)){
            return null;
        }

        List<SlotMenu> activeSlotMenus = MagicCostume.getPlugin().getActiveSlotMenus();

        for(SlotMenu menu: activeSlotMenus){
            if(menu.getInventory().equals(inventory)){
                return menu;
            }
        }

        return null;
    }

    private CostumeMenu findActiveCostumeMenu(Inventory inventory){
        if(!(inventory.getHolder() instanceof CostumeMenu)){
            return null;
        }

        List<CostumeMenu> activeCostumeMenus = MagicCostume.getPlugin().getActiveCostumeMenus();

        for(CostumeMenu menu: activeCostumeMenus){
            if(menu.getInventory().equals(inventory)){
                return menu;
            }
        }

        return null;
    }

    private MainMenu findActiveMainMenu(Inventory inventory){
        if(!(inventory.getHolder() instanceof MainMenu)){
            return null;
        }

        List<MainMenu> activeMainMenuList = MagicCostume.getPlugin().getActiveMainMenus();

        for(MainMenu mainMenu: activeMainMenuList){
            if(mainMenu.getInventory().equals(inventory)){
                return mainMenu;
            }
        }

        return null;
    }
}
