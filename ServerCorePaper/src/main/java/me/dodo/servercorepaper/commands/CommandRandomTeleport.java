package me.dodo.servercorepaper.commands;

import me.dodo.servercorepaper.ServerCorePaper;
import me.dodo.servercorepaper.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EventListener;

public class CommandRandomTeleport implements @Nullable CommandExecutor, EventListener {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            // Variable inits
            Player senderPlayer = (Player) sender;
            int radius = 100;
            Player targetPlayer = (Player) sender;
            World world = targetPlayer.getWorld();

            BukkitScheduler scheduler = Bukkit.getScheduler();

            try {
                switch (args.length) {
                    case 1:
                        targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                        break;
                    case 2:
                        targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                        radius = Integer.parseInt(args[1]);
                        break;
                    case 3:
                        targetPlayer = Bukkit.getServer().getPlayer(args[0]);
                        radius = Integer.parseInt(args[1]);
                        world = Bukkit.getWorld(args[2]);
                        break;
                    default:
                        // Ignore
                        break;
                }

                if (radius < 10) {
                    senderPlayer.sendMessage("Teleportation radius cannot be lower than 10.");
                    return false;
                }

                if (targetPlayer == null || !targetPlayer.isOnline()) {
                    senderPlayer.sendMessage("Target player must be online.");
                    return false;
                }

                if (world == null) {
                    senderPlayer.sendMessage("Target world couldn't found.");
                    return false;
                }

                targetPlayer.sendMessage("Finding safe place for teleport.. Please wait!");

                // Final variable inits for scheduler
                Player finalTargetPlayer = targetPlayer;
                World finalWorld = world;
                int finalRadius = radius;
                scheduler.runTaskTimerAsynchronously(ServerCorePaper.getInstance(), FindLocation -> {
                    if(!finalTargetPlayer.isOnline())
                        FindLocation.cancel();


                    // Find location
                    Location location = LocationUtils.FindRandomLocation(finalTargetPlayer, finalWorld, finalRadius);

                    if(!location.isChunkLoaded())
                        location.getChunk().load();

                    // If the location safe teleport player to location
                    if(LocationUtils.IsLocationSafe(location)){
                        scheduler.runTaskAsynchronously(ServerCorePaper.getInstance(), TeleportPlayerToLocation -> {
                            if(!finalTargetPlayer.isOnline())
                                FindLocation.cancel();

                            finalTargetPlayer.teleportAsync(location);
                            TeleportPlayerToLocation.cancel();
                        });
                        FindLocation.cancel();
                    }
                }, 0L, 20L * 5L);
            } catch (NumberFormatException e) {
                senderPlayer.sendMessage(e.getMessage());
                return false;
            }
        } else {

        }
        return true;
    }
}
