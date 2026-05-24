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
import org.psyrioty.magicCostume.Objects.*;
import org.psyrioty.magicCostume.utils.ConfigLanguage;

import static org.psyrioty.magicCostume.utils.ConfigLanguage.*;

public class CostumeMenu implements InventoryHolder {
    Inventory inventory;
    Costume costume;

    //=============данные для настройки костюмов================
    //смещения
    double offsetX = 0;
    double offsetY = 0;
    double offsetZ = 0;

    //размер
    double scale = 1;

    //яркость
    int brightness = 0;
    //----------------------------------------------------------

    public CostumeMenu(Player player, Costume costume){
        this.costume = costume;
        createInventory();
        open(player);

        MagicCostume.getPlugin().getActiveCostumeMenus().add(this);
    }

    public void open(Player player){
        if(player == null){
            return;
        }

        if(!player.isOnline()){
            return;
        }

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
        metaInfoSlot.setDisplayName(getCostumeMenuInfo(costume));
        metaInfoSlot.setLore(getCostumeMenuInfoLore(costume));
        infoSlot.setItemMeta(metaInfoSlot);

        inventory.setItem(4, infoSlot);

        ItemStack summonCostume = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta summonCostumeMeta = summonCostume.getItemMeta();

        summonCostumeMeta.setDisplayName(getCostumeMenuSummonButtonName());
        summonCostume.setItemMeta(summonCostumeMeta);

        inventory.setItem(22, summonCostume);
    }

    public void click(Player player, int slot){
        if(slot == 22) {
            MagicCostume.getPlugin().spawnActiveCostume(player, costume);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
