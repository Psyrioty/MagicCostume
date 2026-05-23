package org.psyrioty.magicCostume.utils;

import org.psyrioty.magicCostume.Objects.Costume;
import org.psyrioty.magicCostume.Objects.Slot;

import java.util.ArrayList;
import java.util.List;

public class ConfigLanguage {
    public static String getMainName(){
        return "Костюмы";
    }

    public static String getCommandError(){
        return "Эта команда только для игроков";
    }

    public static String getMainMenuInfo(){
        return "В этом окне расположены все слоты для костюмов";
    }

    public static String getSlotMenuInfo(Slot slot){
        return "{slot_name}".replace("{slot_name}", slot.getName());
    }

    public static List<String> getSlotMenuInfoLore(Slot slot){
        List<String> lore = new ArrayList<>();
        lore.add("Здесь находятся все костюмы, которые Вы имеете для слота {slot_name}".replace("{slot_name}", slot.getName()));
        return lore;
    }

    public static String getCostumeMenuInfo(Costume costume){
        return "{costume_name}".replace("{costume_name}", costume.getName());
    }

    public static List<String> getCostumeMenuInfoLore(Costume costume){
        List<String> lore = new ArrayList<>();
        lore.add("Вы находитесь в настройках костюма {costume_name}".replace("{costume_name}", costume.getName()));
        return lore;
    }

    public static String getCostumeMenuSummonButtonName(){
        return "Экипировать/Обновить костюм";
    }
}
