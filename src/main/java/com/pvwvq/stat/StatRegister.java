package com.pvwvq.stat;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StatRegister {

    Stat mainPlugin;

    public StatRegister(Stat plugin) {

        mainPlugin = plugin;

    }

    private List<Listener> listeners = Arrays.asList(

    );

    private HashMap<String, CommandExecutor> executors = new HashMap<>() {{

    }};

    public void registerEvents() {

        for (Listener listener : listeners)
        mainPlugin.getServer().getPluginManager().registerEvents(listener, mainPlugin);

    }

    public void registerCommands() {

        for (int i = 0; i < executors.size(); i++) {

            mainPlugin.getCommand((String) executors.keySet().toArray()[i]).setExecutor((CommandExecutor) executors.values().toArray()[i]);
        }
    }
}
