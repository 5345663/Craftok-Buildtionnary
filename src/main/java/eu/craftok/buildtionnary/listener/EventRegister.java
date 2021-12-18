package eu.craftok.buildtionnary.listener;

import eu.craftok.buildtionnary.BMain;
import eu.craftok.buildtionnary.listener.player.PlayerDamageEvent;
import eu.craftok.buildtionnary.listener.player.PlayerInventoryEvent;
import eu.craftok.buildtionnary.listener.player.PlayerJoinEvent;
import eu.craftok.buildtionnary.listener.player.PlayerQuitEvent;
import eu.craftok.buildtionnary.listener.world.WorldCancelEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

/**
 * Project buildtionnary Created by Sithey
 */

public class EventRegister {

    public static void registerEvent() {
        PluginManager pm = Bukkit.getPluginManager();
        BMain main = BMain.getInstance();
        pm.registerEvents(new WorldCancelEvent(), main);
        pm.registerEvents(new PlayerDamageEvent(), main);
        pm.registerEvents(new PlayerJoinEvent(), main);
        pm.registerEvents(new PlayerQuitEvent(), main);
        pm.registerEvents(new PlayerInventoryEvent(), main);
    }

}
