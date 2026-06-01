package org.psyrioty.magicCostume.Objects.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.psyrioty.magicCostume.Database.DatabaseManager;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.*;
import org.psyrioty.magicCostume.utils.ConfigLanguage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.psyrioty.magicCostume.Database.Requests.deleteCostumePartByEntityUUIDAndSlotName;
import static org.psyrioty.magicCostume.utils.ConfigLanguage.*;

public class SlotMenu implements InventoryHolder {
    Inventory inventory;
    Slot slot;

    List<Costume> trueCostumes = new ArrayList<>();

    public SlotMenu(
            Player player,
            Slot slot
    ){
        this.slot = slot;

        createInventory(player);
        open(player);

        MagicCostume.getPlugin().getActiveSlotMenus().add(this);
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

    private void createInventory(Player player){
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
        metaInfoSlot.setDisplayName(getSlotMenuInfo(slot));
        metaInfoSlot.setLore(getSlotMenuInfoLore(slot));
        infoSlot.setItemMeta(metaInfoSlot);

        inventory.setItem(4, infoSlot);

        int slotIterator = 9;
        for(Costume costume: slot.getCostumes()){
            if(!player.hasPermission(costume.getPermission())){
                continue;
            }
            ItemStack slotItem = new ItemStack(Material.PAPER);
            ItemMeta slotMeta = slotItem.getItemMeta();
            slotMeta.setDisplayName(costume.getName());
            slotItem.setItemMeta(slotMeta);

            inventory.setItem(slotIterator, slotItem);

            trueCostumes.add(costume);

            slotIterator++;
        }

        //----------УДАЛЕНИЕ КОСТЮМА--------------
        ItemStack deleteButton = new ItemStack(Material.STRUCTURE_VOID);
        ItemMeta deleteMeta = deleteButton.getItemMeta();
        deleteMeta.setDisplayName(getDeleteCostumeButtonName());
        deleteButton.setItemMeta(deleteMeta);
        inventory.setItem(49, deleteButton);
        //========================================
    }

    public void click(Player player, int slot) throws SQLException {
        List<Costume> costumes = this.slot.getCostumes();
        if(
                8 < slot &&
                slot <= 8 + costumes.size()
        ){
            CostumeMenu costumeMenu = new CostumeMenu(player, trueCostumes.get(slot - 9));
        }else{
            switch (slot){
                case 49:
                    ActiveCostumeEntity activeCostumeEntity = MagicCostume.getPlugin().findActiveCostumeEntityForEntity(player);
                    if(activeCostumeEntity == null){
                        return;
                    }

                    for(ActiveSlot activeSlot: activeCostumeEntity.getActiveSlotList()){
                        if(activeSlot.getSlot().equals(this.slot)){
                            ActiveCostume activeCostume = activeSlot.getActiveCostume();
                            if(activeCostume == null){
                                return;
                            }
                            activeCostume.remove();
                            deleteCostumePartByEntityUUIDAndSlotName(
                                    DatabaseManager.getConnection(),
                                    player.getUniqueId().toString(),
                                    this.slot.getName()
                                    );
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
