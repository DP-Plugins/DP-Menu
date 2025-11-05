package com.darksoldier1404.dpm;

import com.darksoldier1404.dpm.commands.DPMCommand;
import com.darksoldier1404.dpm.events.DPMEvent;
import com.darksoldier1404.dppc.annotation.DPPCoreVersion;
import com.darksoldier1404.dppc.data.DPlugin;
import com.darksoldier1404.dppc.data.DataContainer;
import com.darksoldier1404.dppc.data.DataType;
import com.darksoldier1404.dppc.utils.PluginUtil;
import org.bukkit.configuration.file.YamlConfiguration;

@DPPCoreVersion(since = "5.3.0")
public class Menu extends DPlugin {
    public static Menu plugin;
    public static DataContainer<String, YamlConfiguration> menus;

    public Menu() {
        super(true);
        plugin = this;
        init();
    }

    public static Menu getInstance() {
        return plugin;
    }

    @Override
    public void onLoad() {
        PluginUtil.addPlugin(plugin, 26570);
        menus = loadDataContainer(new DataContainer<>(this, DataType.YAML, "menus"), null);
    }

    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(new DPMEvent(), plugin);
        getCommand("dpm").setExecutor(new DPMCommand().getExecuter());
    }

    @Override
    public void onDisable() {
        saveAllData();
    }
}
