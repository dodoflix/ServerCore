package me.dodo.servercorepaper;

import me.dodo.servercorepaper.commands.CommandRandomTeleport;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerCorePaper extends JavaPlugin {
    private static ServerCorePaper instance;

    public static ServerCorePaper getInstance() {
        return instance;
    }



    @Override
    public void onEnable() {
        // Instance init
        ServerCorePaper.instance = this;

        getCommand("rtp").setExecutor(new CommandRandomTeleport());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
