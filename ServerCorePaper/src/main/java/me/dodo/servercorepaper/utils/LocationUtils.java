package me.dodo.servercorepaper.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Random;

public class LocationUtils {
    public static boolean IsLocationSafe(Location location) {
        try {
            // Feet control
            Block feet = location.getBlock();
            if (!feet.getType().isAir())
                return false;

            // Head control
            Block head = feet.getRelative(BlockFace.UP);
            if (!head.getType().isAir())
                return false;

            // Ground control
            Block ground = feet.getRelative(BlockFace.DOWN);
            return ground.getType().isSolid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Location FindRandomLocation(Player player, World world, int radius) {
        Random random = new Random();
        int x;
        int z;

        if (player.getWorld() == world) {
            Location location = player.getLocation();
            x = random.nextInt((int) location.getX() - radius, (int) location.getX() + radius);
            z = random.nextInt((int) location.getZ() - radius, (int) location.getZ() + radius);
        } else {
            x = random.nextInt(-radius, radius);
            z = random.nextInt(-radius, radius);
        }

        return world.getHighestBlockAt(x, z).getRelative(BlockFace.UP).getLocation();
    }
}
