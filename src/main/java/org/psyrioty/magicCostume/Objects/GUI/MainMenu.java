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
import org.psyrioty.magicCostume.Objects.ActiveCostumeEntity;
import org.psyrioty.magicCostume.Objects.Slot;
import org.psyrioty.magicCostume.utils.ConfigLanguage;

import java.util.List;

import static org.psyrioty.magicCostume.utils.ConfigLanguage.getHideAllCostumesButtonName;
import static org.psyrioty.magicCostume.utils.ConfigLanguage.getMainMenuInfo;

public class MainMenu implements InventoryHolder {
    Inventory inventory;
    List<Slot> slotList;
    ActiveCostumeEntity activeCostumeEntity;

    public MainMenu(Player player){
        activeCostumeEntity = MagicCostume.getPlugin().findActiveCostumeEntityForEntity(player);
        if(activeCostumeEntity == null){
            return;
        }
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
            if(i == 49){
                continue;
            }
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

        //реализация кнопки для скрытия всех костюмов других игроков
        ItemStack hideAllCostumesButton = new ItemStack(Material.STRUCTURE_VOID);
        ItemMeta hideAllCostumesButtonMeta = hideAllCostumesButton.getItemMeta();
        if(activeCostumeEntity.isHideOtherCostumes()){
            hideAllCostumesButtonMeta.setEnchantmentGlintOverride(activeCostumeEntity.isHideOtherCostumes());
        }

        hideAllCostumesButtonMeta.setDisplayName(getHideAllCostumesButtonName());
        hideAllCostumesButton.setItemMeta(hideAllCostumesButtonMeta);
        inventory.setItem(49, hideAllCostumesButton);
    }

    public void click(Player player, int slot){
        switch (slot){
            case 49:
                if(activeCostumeEntity == null){
                    return;
                }
                boolean hideOtherCostumes = !activeCostumeEntity.isHideOtherCostumes();
                activeCostumeEntity.setHideOtherCostumes(hideOtherCostumes);

                ItemStack item = inventory.getItem(slot);
                ItemMeta meta = item.getItemMeta();
                meta.setEnchantmentGlintOverride(hideOtherCostumes);
                item.setItemMeta(meta);
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
