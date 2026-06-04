package org.psyrioty.magicCostume.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.psyrioty.magicCostume.MagicCostume;
import org.psyrioty.magicCostume.Objects.GUI.CostumeMenu;
import org.psyrioty.magicCostume.Objects.GUI.MainMenu;
import org.psyrioty.magicModels.utils.Converter;

import java.util.Set;

import static org.psyrioty.magicCostume.utils.ConfigLanguage.getCommandError;

public class MainPluginCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage(getCommandError());
            return true;
        }

        if(args.length == 1) {
            if (args[0].equals("reload")) {
                MagicCostume.getPlugin().getCostumeFiles();
                return true;
            }
        }

        if(MagicCostume.getPlugin().getCostumes().isEmpty()){
            return true;
        }

        MainMenu mainMenu = new MainMenu(player);

        return true;
    }
}
