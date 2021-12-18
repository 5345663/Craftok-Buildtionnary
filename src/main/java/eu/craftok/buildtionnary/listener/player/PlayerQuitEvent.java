package eu.craftok.buildtionnary.listener.player;

import eu.craftok.buildtionnary.manager.player.BPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Project buildtionnary Created by Sithey
 */

public class PlayerQuitEvent implements Listener {

    @EventHandler
    public void onQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        BPlayerManager.getDPlayerByPlayer(event.getPlayer()).logout(event);
    }
}
