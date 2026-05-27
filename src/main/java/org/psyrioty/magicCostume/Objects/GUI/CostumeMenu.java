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
import org.psyrioty.magicModels.Objects.Bone;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.psyrioty.magicCostume.utils.ConfigLanguage.*;

public class CostumeMenu implements InventoryHolder {
    Inventory inventory;
    Costume costume;

    //=============данные для настройки костюмов================
    //смещения
    float offsetX = 0;
    float defaultOffsetX = 0;
    float minOffsetX = -1;
    float maxOffsetX = 1;

    float offsetY = 0;
    float defaultOffsetY = 0;
    float minOffsetY = -1;
    float maxOffsetY = 1;

    float offsetZ = 0;
    float defaultOffsetZ = 0;
    float minOffsetZ = -1;
    float maxOffsetZ = 1;

    //размер
    float scale = 1;
    float minScale = 0.5f;
    float maxScale = 2;
    float defaultScale = 1;

    //яркость
    int brightness = -1;
    int defaultBrightness = -1;
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
            if(
                    i == 4
            ){
                continue;
            }
            inventory.setItem(i, emptySlot);
        }

        for(int i = 45; i < 54; i++){
            inventory.setItem(i, emptySlot);
        }

        //---------------------ИНФОРМАЦИЯ---------------------------
        ItemStack infoSlot = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta metaInfoSlot = infoSlot.getItemMeta();
        metaInfoSlot.setDisplayName(getCostumeMenuInfo(costume));
        metaInfoSlot.setLore(getCostumeMenuInfoLore(costume));
        infoSlot.setItemMeta(metaInfoSlot);

        inventory.setItem(4, infoSlot);
        //==========================================================


        //---------------------СПАВН КОСТЮМА---------------------------
        ItemStack summonCostume = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta summonCostumeMeta = summonCostume.getItemMeta();

        summonCostumeMeta.setDisplayName(getCostumeMenuSummonButtonName());
        summonCostume.setItemMeta(summonCostumeMeta);

        inventory.setItem(22, summonCostume);
        //==========================================================

        //---------------------ДОБАВЛЕНИЕ РАЗМЕРА---------------------------
        ItemStack scaleAddCostume = new ItemStack(Material.FEATHER);
        ItemMeta scaleAddMeta = scaleAddCostume.getItemMeta();

        scaleAddMeta.setDisplayName(getCostumeMenuAddScaleButtonName(scale));
        scaleAddMeta.setLore(getScaleSettingsLore(minScale, maxScale));
        scaleAddCostume.setItemMeta(scaleAddMeta);

        inventory.setItem(11, scaleAddCostume);
        //==========================================================

        //-----------------------УБАВЛЕНИЕ РАЗМЕРА-------------------------
        ItemStack scaleMinusCostume = new ItemStack(Material.FEATHER);
        ItemMeta scaleMinusMeta = scaleMinusCostume.getItemMeta();

        scaleMinusMeta.setDisplayName(getCostumeMenuMinusScaleButtonName(scale));
        scaleMinusMeta.setLore(getScaleSettingsLore(minScale, maxScale));
        scaleMinusCostume.setItemMeta(scaleMinusMeta);

        inventory.setItem(10, scaleMinusCostume);
        //==========================================================

        //-----------------------ПРИБАВЛЕНИЕ ЯРКОСТИ-------------------------
        ItemStack brightnessAdd = new ItemStack(Material.LIGHT);
        ItemMeta brightnessAddMeta = brightnessAdd.getItemMeta();

        brightnessAddMeta.setDisplayName(getCostumeMenuAddBrightnessButton(brightness));
        brightnessAddMeta.setLore(getBrightnessSettingsLore());
        brightnessAdd.setItemMeta(brightnessAddMeta);

        inventory.setItem(20, brightnessAdd);
        //==========================================================

        //-----------------------УБАВЛЕНИЕ ЯРКОСТИ-------------------------
        ItemStack brightnessMinus = new ItemStack(Material.LIGHT);
        ItemMeta brightnessMinusMeta = brightnessMinus.getItemMeta();

        brightnessMinusMeta.setDisplayName(getCostumeMenuMinusBrightnessButton(brightness));
        brightnessMinusMeta.setLore(getBrightnessSettingsLore());
        brightnessMinus.setItemMeta(brightnessMinusMeta);

        inventory.setItem(19, brightnessMinus);
        //==========================================================

        //-----------------------СМЕЩЕНИЕ ПО +X-------------------------
        ItemStack offsetXAddButton = new ItemStack(Material.RED_DYE);
        ItemMeta offsetXAddMeta = offsetXAddButton.getItemMeta();

        offsetXAddMeta.setDisplayName(getCostumeMenuOffsetXAddButton(offsetX));
        offsetXAddMeta.setLore(getOffsetXSettingsLore(minOffsetX, maxOffsetX));
        offsetXAddButton.setItemMeta(offsetXAddMeta);

        inventory.setItem(16, offsetXAddButton);
        //==========================================================

        //-----------------------СМЕЩЕНИЕ ПО -X-------------------------
        ItemStack offsetXMinusButton = new ItemStack(Material.RED_DYE);
        ItemMeta offsetXMinusMeta = offsetXMinusButton.getItemMeta();

        offsetXMinusMeta.setDisplayName(getCostumeMenuOffsetXMinusButton(offsetX));
        offsetXMinusMeta.setLore(getOffsetXSettingsLore(minOffsetX, maxOffsetX));
        offsetXMinusButton.setItemMeta(offsetXMinusMeta);

        inventory.setItem(15, offsetXMinusButton);
        //==========================================================

        //-----------------------СМЕЩЕНИЕ ПО +Y-------------------------
        ItemStack offsetYAddButton = new ItemStack(Material.LIME_DYE);
        ItemMeta offsetYAddMeta = offsetYAddButton.getItemMeta();

        offsetYAddMeta.setDisplayName(getCostumeMenuOffsetYAddButton(offsetY));
        offsetYAddMeta.setLore(getOffsetYSettingsLore(minOffsetY, maxOffsetY));
        offsetYAddButton.setItemMeta(offsetYAddMeta);

        inventory.setItem(25, offsetYAddButton);
        //==========================================================

        //-----------------------СМЕЩЕНИЕ ПО -Y-------------------------
        ItemStack offsetYMinusButton = new ItemStack(Material.LIME_DYE);
        ItemMeta offsetYMinusMeta = offsetYMinusButton.getItemMeta();

        offsetYMinusMeta.setDisplayName(getCostumeMenuOffsetYMinusButton(offsetY));
        offsetYMinusMeta.setLore(getOffsetYSettingsLore(minOffsetY, maxOffsetY));
        offsetYMinusButton.setItemMeta(offsetYMinusMeta);

        inventory.setItem(24, offsetYMinusButton);
        //==========================================================

        //-----------------------СМЕЩЕНИЕ ПО +Z-------------------------
        ItemStack offsetZAddButton = new ItemStack(Material.BLUE_DYE);
        ItemMeta offsetZAddMeta = offsetZAddButton.getItemMeta();

        offsetZAddMeta.setDisplayName(getCostumeMenuOffsetZAddButton(offsetZ));
        offsetZAddMeta.setLore(getOffsetZSettingsLore(minOffsetZ, maxOffsetZ));
        offsetZAddButton.setItemMeta(offsetZAddMeta);

        inventory.setItem(34, offsetZAddButton);
        //==========================================================

        //-----------------------СМЕЩЕНИЕ ПО -Z-------------------------
        ItemStack offsetZMinusButton = new ItemStack(Material.BLUE_DYE);
        ItemMeta offsetZMinusMeta = offsetZMinusButton.getItemMeta();

        offsetZMinusMeta.setDisplayName(getCostumeMenuOffsetZMinusButton(offsetZ));
        offsetZMinusMeta.setLore(getOffsetZSettingsLore(minOffsetZ, maxOffsetZ));
        offsetZMinusButton.setItemMeta(offsetZMinusMeta);

        inventory.setItem(33, offsetZMinusButton);
        //==========================================================
    }

    private HashMap<UUID, Integer> setAllBoneBrightness(List<Bone> bones, HashMap<UUID, Integer> boneBrightness){
        if(boneBrightness == null) {
            boneBrightness = new HashMap<>();
        }

        if(bones == null){
            return boneBrightness;
        }

        for(Bone bone: bones){
            boneBrightness.put(bone.getUuid(), brightness);

            setAllBoneBrightness(bone.getChildBones(), boneBrightness);
        }

        return boneBrightness;
    }

    public void click(Player player, int slot){
        switch (slot){
            //спавн костюма
            case 22:

                HashMap<UUID, Integer> boneBrightness = setAllBoneBrightness(
                        costume.getModel().getHeadBones(),
                        null);

                MagicCostume.getPlugin().spawnActiveCostume(
                        player,
                        costume,
                        boneBrightness,
                        scale,
                        offsetX,
                        offsetY,
                        offsetZ
                );
                break;
            //+размер
            case 11:
                if(scale < maxScale){
                    scale+=0.1f;
                    if(scale > maxScale){
                        scale = maxScale;
                    }
                    renameScale();
                }
                break;
            //-размер
            case 10:
                if(scale > minScale){
                    scale-=0.1f;
                    if(scale < minScale){
                        scale = minScale;
                    }
                    renameScale();
                }
                break;
            //-яркость
            case 19:
                if(brightness > -1){
                    brightness-=1;
                    if(brightness < -1){
                        brightness = -1;
                    }
                    renameBrightness();
                }
                break;
            //+яркость
            case 20:
                if(brightness < 15){
                    brightness+=1;
                    if(brightness > 15){
                        brightness = 15;
                    }
                    renameBrightness();
                }
                break;
            //+X
            case 16:
                if(offsetX < maxOffsetX){
                    offsetX+=0.1f;
                    if(offsetX > maxOffsetX){
                        offsetX = maxOffsetX;
                    }
                    renameOffsetX();
                }
                break;
            //-X
            case 15:
                if(offsetX > minOffsetX){
                    offsetX-=0.1f;
                    if(offsetX < minOffsetX){
                        offsetX = minOffsetX;
                    }
                    renameOffsetX();
                }
                break;
            //+Y
            case 25:
                if(offsetY < maxOffsetY){
                    offsetY+=0.1f;
                    if(offsetY > maxOffsetY){
                        offsetY = maxOffsetY;
                    }
                    renameOffsetY();
                }
                break;
            //-Y
            case 24:
                if(offsetY > minOffsetY){
                    offsetY-=0.1f;
                    if(offsetY < minOffsetY){
                        offsetY = minOffsetY;
                    }
                    renameOffsetY();
                }
                break;
            //+Z
            case 34:
                if(offsetZ < maxOffsetZ){
                    offsetZ+=0.1f;
                    if(offsetZ > maxOffsetZ){
                        offsetZ = maxOffsetZ;
                    }
                    renameOffsetZ();
                }
                break;
            //-Z
            case 33:
                if(offsetZ > minOffsetZ){
                    offsetZ-=0.1f;
                    if(offsetZ < minOffsetZ){
                        offsetZ = minOffsetZ;
                    }
                    renameOffsetZ();
                }
                break;
        }
    }

    private void renameScale(){
        ItemStack itemStack = inventory.getItem(11);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(getCostumeMenuAddScaleButtonName(scale));
        itemStack.setItemMeta(meta);

        ItemStack itemStack1 = inventory.getItem(10);
        ItemMeta meta1 = itemStack1.getItemMeta();
        meta1.setDisplayName(getCostumeMenuMinusScaleButtonName(scale));
        itemStack1.setItemMeta(meta);
    }

    private void renameBrightness(){
        ItemStack itemStack = inventory.getItem(20);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(getCostumeMenuAddBrightnessButton(brightness));
        itemStack.setItemMeta(meta);

        ItemStack itemStack1 = inventory.getItem(19);
        ItemMeta meta1 = itemStack1.getItemMeta();
        meta1.setDisplayName(getCostumeMenuMinusBrightnessButton(brightness));
        itemStack1.setItemMeta(meta);
    }

    private void renameOffsetX(){
        ItemStack itemStack = inventory.getItem(16);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(getCostumeMenuOffsetXAddButton(offsetX));
        itemStack.setItemMeta(meta);

        ItemStack itemStack1 = inventory.getItem(15);
        ItemMeta meta1 = itemStack1.getItemMeta();
        meta1.setDisplayName(getCostumeMenuOffsetXMinusButton(offsetX));
        itemStack1.setItemMeta(meta1);
    }

    private void renameOffsetY(){
        ItemStack itemStack = inventory.getItem(25);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(getCostumeMenuOffsetYAddButton(offsetY));
        itemStack.setItemMeta(meta);

        ItemStack itemStack1 = inventory.getItem(24);
        ItemMeta meta1 = itemStack1.getItemMeta();
        meta1.setDisplayName(getCostumeMenuOffsetYMinusButton(offsetY));
        itemStack1.setItemMeta(meta1);
    }

    private void renameOffsetZ(){
        ItemStack itemStack = inventory.getItem(34);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(getCostumeMenuOffsetZAddButton(offsetZ));
        itemStack.setItemMeta(meta);

        ItemStack itemStack1 = inventory.getItem(33);
        ItemMeta meta1 = itemStack1.getItemMeta();
        meta1.setDisplayName(getCostumeMenuOffsetZMinusButton(offsetZ));
        itemStack1.setItemMeta(meta1);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
