package org.psyrioty.magicCostume;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.psyrioty.magicCostume.Commands.MainPluginCommands;
import org.psyrioty.magicCostume.Objects.Costume;
import org.psyrioty.magicCostume.utils.Converter;

import java.util.HashSet;
import java.util.Set;

public final class MagicCostume extends JavaPlugin {

    static MagicCostume plugin;
    Set<Costume> costumes = new HashSet<>();
    PluginManager pm;

    @Override
    public void onEnable() {
        plugin = this;
        pm = plugin.getServer().getPluginManager();

        this.getCommand("magiccostume").setExecutor(new MainPluginCommands());

        Converter.ConvertBBModelsToResourcePackAndModels();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MagicCostume getPlugin() {
        return plugin;
    }

    public Set<Costume> getCostumes() {
        return costumes;
    }
}
