package com.lairon.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class LocationUtils {

    public static ArrayList<Block> blockSphere(Location loc, int radius) {
        ArrayList<Block> blocks = new ArrayList<>();
        Block center = loc.getBlock();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block b = center.getRelative(x, y, z);
                    if (center.getLocation().distance(b.getLocation()) <= radius) {
                        blocks.add(b);
                    }
                }
            }
        }
        return blocks;
    }

    public static ArrayList<Location> pointsEmptySphere(Location loc, double radius, int space) {
        ArrayList<Location> locations = new ArrayList<>();
        for (int yaw = -180; yaw < 180; yaw += space) {
            loc.setYaw(yaw);
            for (int pitch = -90; pitch < 90; pitch += space) {
                loc.setPitch(pitch);
                locations.add(loc.clone().add(loc.getDirection().normalize().multiply(radius)));
            }
        }
        return locations;
    }

    public static ArrayList<Location> pointsEmptyVerticalCircle(Location loc, double radius, int space) {
        ArrayList<Location> locations = new ArrayList<>();
        for (int pitch = -90; pitch < 90; pitch += space) {
            loc.setPitch(pitch);
            locations.add(loc.clone().add(loc.getDirection().normalize().multiply(radius)));
        }
        return locations;
    }

    public static ArrayList<Location> pointsEmptyHorizontalCircle(Location loc, double radius, int space) {
        ArrayList<Location> locations = new ArrayList<>();
        for (int yaw = -180; yaw < 180; yaw += space) {
            loc.setYaw(yaw);
            locations.add(loc.clone().add(loc.getDirection().normalize().multiply(radius)));
        }
        return locations;
    }

    public static ArrayList<Block> blockCube(Location point1, Location point2) {
        ArrayList<Block> blocks = new ArrayList<>();
        for (int x = point1.getBlockX(); x < point2.getBlockX(); x++)
            for (int y = point1.getBlockY(); y < point2.getBlockX(); y++)
                for (int z = point1.getBlockZ(); z < point2.getBlockZ(); z++)
                    blocks.add(new Location(point1.getWorld(), x, y, z).getBlock());
        return blocks;
    }


}
