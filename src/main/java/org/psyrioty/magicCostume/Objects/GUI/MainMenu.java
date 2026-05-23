package org.psyrioty.magicCostume.Objects.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.Slot;
import org.psyrioty.magicCostume.utils.ConfigLanguage;

import java.util.List;

import static org.psyrioty.magicCostume.utils.ConfigLanguage.getMainMenuInfo;

public class MainMenu implements InventoryHolder {
    Inventory inventory;
    List<Slot> slotList;

    public MainMenu(Player player){
        slotList = MagicCostume.getPlugin().getSlots();
        open(player);

        MagicCostume.getPlugin().getActiveMainMenus().add(this);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void open(Player player){
        if(player == null){
            return;
        }

        if(!player.isOnline()){
            return;
        }

        createInventory();

        player.openInventory(inventory);
    }

    private void createInventory(){
        inventory = Bukkit.getServer().createInventory(this, 54, ConfigLanguage.getMainName());

        ItemStack emptySlot = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta metaEmptySlot = emptySlot.getItemMeta();
        metaEmptySlot.setDisplayName("");
        emptySlot.setItemMeta(metaEmptySlot);

        for(int i = 0; i < 9; i++){
            if(i == 4){
                continue;
            }
            inventory.setItem(i, emptySlot);
        }

        for(int i = 45; i < 54; i++){
            inventory.setItem(i, emptySlot);
        }

        ItemStack infoSlot = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta metaInfoSlot = infoSlot.getItemMeta();
        metaInfoSlot.setDisplayName(getMainMenuInfo());
        infoSlot.setItemMeta(metaInfoSlot);

        inventory.setItem(4, infoSlot);

        int slotIterator = 9;
        for(Slot slot: slotList){
            ItemStack slotItem = new ItemStack(Material.PAPER);
            ItemMeta slotMeta = slotItem.getItemMeta();
            slotMeta.setDisplayName(slot.getName());
            slotItem.setItemMeta(slotMeta);

            inventory.setItem(slotIterator, slotItem);

            slotIterator++;
        }
    }

    public void click(Player player, int slot){
        if(slotList.isEmpty() && slot != 42){
            return;
        }

        if(
                8 < slot &&
                slot <= 8 + slotList.size()
        ){
            SlotMenu slotMenu = new SlotMenu(player, slotList.get(slot - 9));
        }
    }
}
