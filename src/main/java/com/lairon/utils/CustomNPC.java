package com.lairon.utils;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.ScoreboardTrait;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class CustomNPC {

    public static final Set<NPC> ALL_NPC_SET = new HashSet<>();
    public static final Set<CustomNPC> ALL_CUSTOM_NPC = new HashSet<>();

    private final Location spawned;
    private final NPC npc;
    private ArmorStand sitStand;
    private Consumer<PlayerInteractAtEntityEvent> onClick;

    public CustomNPC(Location location, String nickname) {
        this.npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, nickname);
        this.spawned = location;

        npc.data().set("player-skin-use-latest-skin", false);

        ALL_NPC_SET.add(npc);
        ALL_CUSTOM_NPC.add(this);
    }

    public CustomNPC lookClose(boolean look) {
        npc.getOrAddTrait(LookClose.class).lookClose(look);
        return this;
    }

    public CustomNPC spawn() {
        npc.spawn(spawned);
        return this;
    }


    public CustomNPC skin(String name) {
        SkinTrait skinTrait = npc.getTrait(SkinTrait.class);
        skinTrait.setSkinName(name);
        npc.addTrait(skinTrait);
        return this;
    }


    public CustomNPC sit(boolean sit){
        if(sit){
            if(getEntity() == null) return this;
            if(sitStand != null) sitStand.remove();
            sitStand = (ArmorStand) npc.getEntity().getWorld().spawnEntity(npc.getEntity().getLocation().clone().subtract(0,1,0), EntityType.ARMOR_STAND);
            sitStand.setGravity(false);
            sitStand.setPersistent(true);
            sitStand.setRemoveWhenFarAway(false);
            sitStand.setVisible(false);
            sitStand.addPassenger(npc.getEntity());

        }else{
            if(sitStand!= null){
                sitStand.getPassengers().remove(0);
                sitStand.remove();
            }
        }
        return this;
    }

    public CustomNPC glow(boolean glow) {
        npc.data().setPersistent("glowing", glow);
        return this;
    }

    public CustomNPC glowColor(ChatColor color) {
        npc.getOrAddTrait(ScoreboardTrait.class).setColor(color);
        return this;
    }

    public LivingEntity getEntity() {
        return (Player) npc.getEntity();
    }

    public Navigator getNavigator() {
        return npc.getNavigator();
    }

    public static CustomNPC of(Location spawn, String nickname) {
        return new CustomNPC(spawn, nickname);
    }

    public void setOnClickListener(Consumer<PlayerInteractAtEntityEvent> e){
        onClick = e;
    }

    public static void onNPCClick(PlayerInteractAtEntityEvent e){
        if(!CitizensAPI.getNPCRegistry().isNPC(e.getRightClicked())) return;
        for (CustomNPC npc : ALL_CUSTOM_NPC) {
            if(npc.getEntity() == null) continue;
            if(npc.getEntity() == e.getRightClicked()){
                if(npc.onClick == null) return;
                npc.onClick.accept(e);
            }
        }

    }

    public static void ping(){}

    public static void disable() {
        for (NPC npc : ALL_NPC_SET) npc.destroy();
    }
}
