package com.lairon.utils;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.ScoreboardTrait;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/*
The class is a set of methods for working with NPCs from the Citizens plugin
 */
public class CustomNPC {

    public static final Set<CustomNPC> ALL_CUSTOM_NPC = new HashSet<>();
    private final NPC npc;
    private ArmorStand sitStand;
    private Consumer<PlayerInteractAtEntityEvent> onInteract;
    private Consumer<EntityDeathEvent> onDeath;
    private CustomNPC instance;
    private boolean removeOnShutdown = true;
    /*
    location - Spawn location
     */

    public CustomNPC(Location location, String nickname, EntityType entityType, Plugin plugin) {
        instance = this;
        this.npc = CitizensAPI.getNPCRegistry().createNPC(entityType, nickname);
        npc.spawn(location);
        ALL_CUSTOM_NPC.add(this);

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onInteract(PlayerInteractAtEntityEvent e) {
                onNPCInteract(e);
            }

            @EventHandler
            public void onDeath(EntityDeathEvent e) {
                onNPCDeath(e);
            }

            @EventHandler
            public void onDisable(PluginDisableEvent e) {
                if (removeOnShutdown && e.getPlugin().equals(plugin)) {
                    npc.despawn();
                    ALL_CUSTOM_NPC.remove(instance);
                }
            }

        }, plugin);
    }

    /*
    Make NPCs Watch Visitors
     */
    public CustomNPC lookClose(boolean look) {
        npc.getOrAddTrait(LookClose.class).lookClose(look);
        return this;
    }

    /*
    Set skin for NPC.
    Will only work if EntityType = Player
     */
    public CustomNPC skin(String name) {
        SkinTrait skinTrait = npc.getTrait(SkinTrait.class);
        skinTrait.setSkinName(name);
        npc.addTrait(skinTrait);
        return this;
    }

    /*
    Make NPC Sit
     */
    public CustomNPC sit(boolean sit) {
        if (sit) {
            if (getEntity() == null) return this;
            if (sitStand != null) sitStand.remove();
            sitStand = (ArmorStand) npc.getEntity().getWorld().spawnEntity(npc.getEntity().getLocation().clone().subtract(0, 1, 0), EntityType.ARMOR_STAND);
            sitStand.setGravity(false);
            sitStand.setRemoveWhenFarAway(false);
            sitStand.setVisible(false);
            sitStand.addPassenger(npc.getEntity());
        } else {
            if (sitStand != null) {
                sitStand.remove();
            }
        }
        return this;
    }

    /*
    Make an NPC glow
     */
    public CustomNPC glow(boolean glow) {
        npc.data().setPersistent("glowing", glow);
        return this;
    }

    /*
    Change NPC glow color
     */
    public CustomNPC glowColor(ChatColor color) {
        npc.getOrAddTrait(ScoreboardTrait.class).setColor(color);
        return this;
    }

    public LivingEntity getEntity() {
        return (LivingEntity) npc.getEntity();
    }

    /*
    Get Navigator NPC
    Examples of using:
    CustomNPC.getNavigator().setTarget(Location);
    Make NPCs follow Location
     */
    public Navigator getNavigator() {
        return npc.getNavigator();
    }

    /*
    Set an interaction listener for this NPC
     */
    public CustomNPC onInteractListener(Consumer<PlayerInteractAtEntityEvent> e) {
        onInteract = e;
        return this;
    }

    /*
    Set this NPC death listener
     */
    public CustomNPC onDeathListener(Consumer<EntityDeathEvent> e) {
        onDeath = e;
        return this;
    }

    private static void onNPCInteract(PlayerInteractAtEntityEvent e) {
        if (!CitizensAPI.getNPCRegistry().isNPC(e.getRightClicked())) return;
        for (CustomNPC npc : ALL_CUSTOM_NPC) {
            if (npc.getEntity() == null) continue;
            if (npc.getEntity() == e.getRightClicked()) {
                if (npc.onInteract == null) return;
                npc.onInteract.accept(e);
            }
        }
    }

    private static void onNPCDeath(EntityDeathEvent e) {
        if (!CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) return;
        for (CustomNPC npc : ALL_CUSTOM_NPC) {
            if (npc.getEntity() == null) continue;
            if (npc.getEntity() == e.getEntity()) {
                if (npc.onDeath == null) return;
                npc.onDeath.accept(e);
            }
        }
    }

    public NPC getNpc() {
        return npc;
    }

    /*
    Will the NPC be removed when the plugin is turned off
     */
    public boolean isRemoveOnShutdown() {
        return removeOnShutdown;
    }

    public void setRemoveOnShutdown(boolean removeOnShutdown) {
        this.removeOnShutdown = removeOnShutdown;
    }
}
