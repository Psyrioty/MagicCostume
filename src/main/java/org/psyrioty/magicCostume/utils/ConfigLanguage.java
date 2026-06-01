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

    public static String getCostumeMenuAddScaleButtonName(float scale){
        String string = String.format("%.1f", scale);
        return "Размер +0.1: {scale}".replace("{scale}", string);
    }

    public static String getCostumeMenuMinusScaleButtonName(float scale){
        String string = String.format("%.1f", scale);
        return "Размер -0.1: {scale}".replace("{scale}", string);
    }

    public static String getCostumeMenuAddBrightnessButton(int brightness){
        return "Яркость +1: {brightness}".replace("{brightness}", brightness + "");
    }

    public static String getCostumeMenuMinusBrightnessButton(int brightness){
        return "Яркость -1: {brightness}".replace("{brightness}", brightness + "");
    }

    public static String getCostumeMenuOffsetXAddButton(float x){
        String string = String.format("%.1f", x);
        return "Смещение по X +0.1: {x}".replace("{x}", string);
    }

    public static String getCostumeMenuOffsetXMinusButton(float x){
        String string = String.format("%.1f", x);
        return "Смещение по X -0.1: {x}".replace("{x}", string);
    }

    public static String getCostumeMenuOffsetYAddButton(float y){
        String string = String.format("%.1f", y);
        return "Смещение по Y +0.1: {y}".replace("{y}", string);
    }

    public static String getCostumeMenuOffsetYMinusButton(float y){
        String string = String.format("%.1f", y);
        return "Смещение по Y -0.1: {y}".replace("{y}", string);
    }

    public static String getCostumeMenuOffsetZAddButton(float z){
        String string = String.format("%.1f", z);
        return "Смещение по Z +0.1: {z}".replace("{z}", string);
    }

    public static String getCostumeMenuOffsetZMinusButton(float z){
        String string = String.format("%.1f", z);
        return "Смещение по Z -0.1: {z}".replace("{z}", string);
    }

    public static List<String> getBrightnessSettingsLore(){
        List<String> lore = new ArrayList<>();
        lore.add("-1 - Стандартная яркость");
        lore.add("Максимальная: 15");
        lore.add("");
        lore.add("Shift + ЛКМ - Сбросить настройки");

        return lore;
    }

    public static List<String> getScaleSettingsLore(float min, float max){
        List<String> lore = new ArrayList<>();
        lore.add("Минимальный размер: {min}".replace("{min}", String.valueOf(min)));
        lore.add("Максимальный размер: {max}".replace("{max}", String.valueOf(max)));
        lore.add("");
        lore.add("Shift + ЛКМ - Сбросить настройки");

        return lore;
    }

    public static List<String> getOffsetXSettingsLore(float min, float max){
        List<String> lore = new ArrayList<>();
        lore.add("Минимальное смещение: {min}".replace("{min}", String.valueOf(min)));
        lore.add("Максимальное смещение: {max}".replace("{max}", String.valueOf(max)));
        lore.add("");
        lore.add("Shift + ЛКМ - Сбросить настройки");

        return lore;
    }

    public static List<String> getOffsetYSettingsLore(float min, float max){
        List<String> lore = new ArrayList<>();
        lore.add("Минимальное смещение: {min}".replace("{min}", String.valueOf(min)));
        lore.add("Максимальное смещение: {max}".replace("{max}", String.valueOf(max)));
        lore.add("");
        lore.add("Shift + ЛКМ - Сбросить настройки");

        return lore;
    }

    public static List<String> getOffsetZSettingsLore(float min, float max){
        List<String> lore = new ArrayList<>();
        lore.add("Минимальное смещение: {min}".replace("{min}", String.valueOf(min)));
        lore.add("Максимальное смещение: {max}".replace("{max}", String.valueOf(max)));
        lore.add("");
        lore.add("Shift + ЛКМ - Сбросить настройки");

        return lore;
    }

    public static String getHideAllCostumesButtonName(){
        return "Скрыть все костюмы";
    }

    public static String getDeleteCostumeButtonName(){
        return "Убрать костюм";
    }
}
