package org.psyrioty.magicCostume.Objects;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

//слот костюма
public class Slot {
    String name;
    List<Costume> costumes;

    public Slot(
            String name
    ){
        this.name = name;
    }

    public void addCostume(Costume costume){
        if(costumes == null){
            costumes = new ArrayList<>();
        }

        costumes.add(costume);
    }

    public List<Costume> getCostumes() {
        return costumes;
    }

    public String getName() {
        return name;
    }
}
