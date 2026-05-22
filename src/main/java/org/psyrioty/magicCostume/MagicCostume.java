package org.psyrioty.magicCostume;

import com.google.gson.JsonObject;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.psyrioty.magicCostume.Commands.MainPluginCommands;
import org.psyrioty.magicCostume.Listeners.GUIEvents;
import org.psyrioty.magicCostume.Objects.Costume;

import java.util.*;

public final class MagicCostume extends JavaPlugin {

    static MagicCostume plugin;
    PluginManager pm;

    Set<Costume> costumes = new HashSet<>();

    @Override
    public void onEnable() {
        plugin = this;
        pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new GUIEvents(), this);

        this.getCommand("magiccostume").setExecutor(new MainPluginCommands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MagicCostume getPlugin() {
        return plugin;
    }
}
