package com.lairon.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LoadingGUI implements InventoryHolder {

    private Inventory inventory = Bukkit.createInventory(this, 9, "Загрузка...");
    private double progress = 0;
    private ItemStack panel = new ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE).name("0%").build();

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public  void setProgress(double progress){
        if(progress < 0) throw new IllegalArgumentException("Значение прогресса не может быть меньше 0.");
        if(progress > 1) throw new IllegalArgumentException("Значение прогресса не может быть больше 1.");
        this.progress = progress;
        int plaits = 0;
        if(progress != 1)
            plaits = (int) (9 * progress);
        else
            plaits = 9;

        String percent = "§a" + ((int) (progress * 100d)) + "§7%";
        ItemStackUtils.setDisplayName(panel, percent);
        inventory.clear();
        for (int i = 0; i < plaits; i++) inventory.setItem(i, panel);
    }

}
