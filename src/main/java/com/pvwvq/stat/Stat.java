package com.pvwvq.stat;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Stat extends JavaPlugin {

    StatRegister register;

    @Override
    public void onEnable() {

        register.registerEvents();
        register.registerCommands();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
