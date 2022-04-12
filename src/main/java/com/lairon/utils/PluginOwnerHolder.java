package com.lairon.utils;

import org.bukkit.plugin.Plugin;

public class PluginOwnerHolder {

    private static Plugin owner;

    public static Plugin getOwner() {
        return owner;
    }

    public static void setOwner(Plugin owner) {
        PluginOwnerHolder.owner = owner;
    }
}
