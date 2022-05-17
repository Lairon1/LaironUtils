package com.lairon.utils;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static java.lang.Math.*;

public class EffectUtils {

    public static void startFlyingLinesEffect(Location location, int ticks, ParticleBuilder builder, Plugin plugin) {
        new BukkitRunnable() {
            float step = 0;
            int count = 0;

            @Override
            public void run() {
                if (count >= ticks) {
                    this.cancel();
                    return;
                }
                Vector vector = new Vector();
                for (int i = 0; i < 10; i++) {
                    step++;
                    float t = (float) PI / 150 * step;
                    float r = (float) sin(t) * 1.0F;
                    float s = (float) (PI * 2) * t;
                    vector.setX(1.0F * r * cos(s) + 0.0F);
                    vector.setZ(1.0F * r * sin(s) + 0.0F);
                    vector.setY(2.0F * 1.0F * cos(t) + 0.8F + 1);
                    location.add(vector);
                    builder.location(location).spawn();
                    location.subtract(vector);
                }
                count++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public static void startFlyingLinesEffect(Entity entity, int ticks, ParticleBuilder builder, Plugin plugin) {
        new BukkitRunnable() {
            float step = 0;
            int count = 0;

            @Override
            public void run() {
                if (count >= ticks) {
                    this.cancel();
                    return;
                }
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }
                Location location = entity.getLocation();
                Vector vector = new Vector();
                for (int i = 0; i < 10; i++) {
                    step++;
                    float t = (float) PI / 150 * step;
                    float r = (float) sin(t) * 1.0F;
                    float s = (float) (PI * 2) * t;
                    vector.setX(1.0F * r * cos(s) + 0.0F);
                    vector.setZ(1.0F * r * sin(s) + 0.0F);
                    vector.setY(2.0F * 1.0F * cos(t) + 0.8F + 1);
                    location.add(vector);
                    builder.location(location)
                            .spawn();
                    location.subtract(vector);
                }
                count++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public static void startGroundLinesEffect(Entity entity, int ticks, ParticleBuilder builder, Plugin plugin) {
        new BukkitRunnable() {
            float step = 0;
            int count = 0;

            @Override
            public void run() {
                if (count >= ticks) {
                    this.cancel();
                    return;
                }
                if (entity.isDead()) {
                    this.cancel();
                    return;
                }

                Location location = entity.getLocation();
                Vector vector = new Vector();
                for (int i = 0; i < 10; i++) {
                    step++;
                    float t = (float) PI / 150 * step;
                    float r = (float) sin(t) * 1.0F;
                    float s = (float) (PI * 2) * t;
                    vector.setX(1.0F * r * cos(s) + 0.0F);
                    vector.setZ(1.0F * r * sin(s) + 0.0F);
                    location.add(vector);
                    builder.location(location)
                            .spawn();
                    location.subtract(vector);
                }
                count++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public static void startGroundLinesEffect(Location location, int ticks, ParticleBuilder builder, Plugin plugin) {
        new BukkitRunnable() {
            float step = 0;
            int count = 0;

            @Override
            public void run() {
                if (count >= ticks) {
                    this.cancel();
                    return;
                }
                Vector vector = new Vector();
                for (int i = 0; i < 10; i++) {
                    step++;
                    float t = (float) PI / 150 * step;
                    float r = (float) sin(t) * 1.0F;
                    float s = (float) (PI * 2) * t;
                    vector.setX(1.0F * r * cos(s) + 0.0F);
                    vector.setZ(1.0F * r * sin(s) + 0.0F);
                    location.add(vector);
                    builder.location(location)
                            .spawn();
                    location.subtract(vector);
                }
                count++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public static void drawParticleLine(Location point1, Location point2, double space, ParticleBuilder builder) {
        World world = point1.getWorld();
        double distance = point1.distance(point2);
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
        double length = 0;
        for (; length < distance; p1.add(vector)) {
            builder.location(new Location(world, p1.getX(), p1.getY(), p1.getZ())).spawn();
            length += space;
        }
    }

    public void drawHeart(Location loc, double maxRadius, ParticleBuilder builder) {
        double r, x, y;
        for (int angle = 0; angle < 50; angle++) {
            r = 1 - sin(angle);
            x = cos(angle) * r * maxRadius + loc.getX();
            y = sin(angle) * r * maxRadius + loc.getY();
            var add = loc.clone().set(x, y, 0);
            builder.location(add).spawn();
        }
    }
}
    


