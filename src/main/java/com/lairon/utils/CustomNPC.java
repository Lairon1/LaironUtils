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
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class CustomNPC {

    public static final Set<CustomNPC> ALL_CUSTOM_NPC = new HashSet<>();

    private final Location spawned;
    private final NPC npc;
    private ArmorStand sitStand;
    private Consumer<PlayerInteractAtEntityEvent> onInteract;
    private Consumer<EntityDeathEvent> onDeath;
    private static boolean isListenersSet = false;

    //nickname - ник который будет отображатся над головой
    //location место спавна
    public CustomNPC(Location location, String nickname, EntityType entityType) {
        this.npc = CitizensAPI.getNPCRegistry().createNPC(entityType, nickname);
        this.spawned = location;

        npc.data().set("player-skin-use-latest-skin", false);

        ALL_CUSTOM_NPC.add(this);
    }

    //Заставить NPC следить за игроком
    public CustomNPC lookClose(boolean look) {
        npc.getOrAddTrait(LookClose.class).lookClose(look);
        return this;
    }
    //Заспавнить NPC в мире
    public CustomNPC spawn() {
        npc.spawn(spawned);
        return this;
    }
    //Установить скин по нику
    //ВАЖНО устанавливаются только те скины что есть на официальных аккаунтах
    //+ если ты играешь с пиратки то у тебя он может не отображатся или отображатся не коректно
    public CustomNPC skin(String name) {
        SkinTrait skinTrait = npc.getTrait(SkinTrait.class);
        skinTrait.setSkinName(name);
        npc.addTrait(skinTrait);
        return this;
    }
    //Заставить NPC сесть на той локации на которой он нахоится
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
                sitStand.getPassengers().remove(0);
                sitStand.remove();
            }
        }
        return this;
    }
    //Заставить NPC светится
    public CustomNPC glow(boolean glow) {
        npc.data().setPersistent("glowing", glow);
        return this;
    }
    //Установить цвет свечения
    public CustomNPC glowColor(ChatColor color) {
        npc.getOrAddTrait(ScoreboardTrait.class).setColor(color);
        return this;
    }
    //Получить Сущьность NPC
    public LivingEntity getEntity() {
        return (LivingEntity) npc.getEntity();
    }
    //Получить Navigator NPC
    /*
    Примеры использования:
    npc.getNavigator().setTarget(Entity, true); - заставить NPC бегать за Entity и атаковать его true false - атаковать или нет
    npc.getNavigator().setTarget(Location); - просто заставить NPC бежать на локацию

     */
    public Navigator getNavigator() {
        return npc.getNavigator();
    }
    //Создать клона NPC
    //Поставить обработчик нажатий на NPC
    /*
    Пример
    npc.onInteractListener((event)->{
    event.getPlayer();
    });
     */
    public CustomNPC onInteractListener(Consumer<PlayerInteractAtEntityEvent> e) {
        onInteract = e;
        return this;
    }
    //Тоже самое что и и onInteractListener только ивент смерти
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
    //Этот статический метод нужно вызвать чтобы работали обработчики у NPC
    public static void setListeners(Plugin plugin) {
        if (isListenersSet) return;
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onInteract(PlayerInteractAtEntityEvent e) {
                onNPCInteract(e);
            }

            @EventHandler
            public void onDeath(EntityDeathEvent e) {
                onNPCDeath(e);
            }

        }, plugin);
    }
    //Этот статический метод нужно вызвать при onDisable чтобы все NPC которых ты создал удалились и не сохранялись в память Citizens


    public NPC getNpc() {
        return npc;
    }

    public static void disable() {
        ALL_CUSTOM_NPC.forEach(n -> n.npc.destroy());
    }


}
