package com.lairon.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;

public class ItemStackUtils {

    public static ItemStack setDisplayName(ItemStack itemStack, String name){
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack setLore(ItemStack itemStack, ArrayList<String> lore){
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack setLore(ItemStack itemStack, String... lore){
        ArrayList<String> c = new ArrayList<>();
        Collections.addAll(c, lore);
        return setLore(itemStack, c);
    }

    public static ItemStack addEnchantment(ItemStack itemStack, Enchantment enchantment, int lvl){
        ItemMeta meta = itemStack.getItemMeta();
        meta.addEnchant(enchantment, lvl, true);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    public static ItemStack setUnbreakable(ItemStack itemStack){
        return  setUnbreakable(itemStack, true);
    }
    public static ItemStack setUnbreakable (ItemStack itemStack, boolean visible){
        ItemMeta meta = itemStack.getItemMeta();
        meta.setUnbreakable(true);
        if(!visible) meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(meta);
        return  itemStack;
    }

    public static ItemStack setCustomModelData(ItemStack itemStack, int data){
        ItemMeta meta = itemStack.getItemMeta();
        meta.setCustomModelData(data);
        itemStack.setItemMeta(meta);
        return  itemStack;
    }

    public static boolean equalsItemsByName(ItemStack item1, ItemStack item2){
        if(item1 == null) return false;
        if(item2 == null) return false;

        if(item1.getItemMeta() == null) return false;
        if(item2.getItemMeta() == null) return false;

        if(item1.getItemMeta().getDisplayName() == null) return false;
        if(item2.getItemMeta().getDisplayName() == null) return false;

        return item1.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName());
    }

    public static void giveItem(Player player, ItemStack item) {
        for (ItemStack slotItem : player.getInventory().getStorageContents()) {
            if (slotItem == null) {
                player.getInventory().addItem(item);
                return;
            }
        }
        player.getWorld().dropItem(player.getLocation(), item);
    }

}
