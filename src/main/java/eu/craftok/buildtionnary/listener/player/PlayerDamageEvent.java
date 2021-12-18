package eu.craftok.buildtionnary.listener.player;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Project buildtionnary Created by Sithey
 */

public class PlayerDamageEvent implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        event.setCancelled(true);
    }
}
