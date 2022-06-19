package com.lairon.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ItemStackBuilder {

    private ItemStack stack;


    public ItemStackBuilder(Material material) {
        stack = new ItemStack(material);
    }

    public ItemStack build() {
        return stack;
    }

    public ItemStackBuilder name(String name) {
        ItemStackUtils.setDisplayName(stack, name);
        return this;
    }


    public ItemStackBuilder lore(List<String> lore) {
        ItemStackUtils.setLore(stack, lore);
        return this;
    }

    public ItemStackBuilder lore(String... lore) {
        ItemStackUtils.setLore(stack, lore);
        return this;
    }

    public ItemStackBuilder enchantment(Enchantment enchantment, int lvl) {
        ItemStackUtils.addEnchantment(stack, enchantment, lvl);
        return this;
    }

    public ItemStackBuilder unbreakable() {
        ItemStackUtils.setUnbreakable(stack, true);
        return this;
    }

    public ItemStackBuilder unbreakable(boolean visible) {
        ItemStackUtils.setUnbreakable(stack, visible);
        return this;
    }

    public ItemStackBuilder data(NamespacedKey key, PersistentDataType type, Object data) {
        ItemStackUtils.addData(stack, key, type, data);
        return this;
    }

    public ItemStackBuilder customModelData(int data){
        ItemStackUtils.setCustomModelData(stack, data);
        return this;
    }

    public ItemStackBuilder itemFlags(ItemFlag... flags){
        ItemStackUtils.addFlag(stack, flags);
        return this;
    }

    public ItemStackBuilder amount(int amount){
        stack.setAmount(amount);
        return this;
    }

}
