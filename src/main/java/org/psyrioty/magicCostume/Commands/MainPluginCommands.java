package org.psyrioty.magicCostume.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.ActiveCostume;
import org.psyrioty.magicCostume.Objects.Costume;

import java.util.Set;

public class MainPluginCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Entity player)){
            return true;
        }
        if(args.length == 0){
            return true;
        }
        Set<Costume> costumeList = MagicCostume.getPlugin().getCostumes();
        for(Costume costume: costumeList){
            if(costume.getName().equals(args[0])){
                ActiveCostume activeCostume = new ActiveCostume(
                        player,
                        costume
                );
                return true;
            }
        }

        return true;
    }
}
