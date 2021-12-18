package eu.craftok.buildtionnary.listener.player;

import eu.craftok.buildtionnary.manager.player.BPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Project buildtionnary Created by Sithey
 */

public class PlayerJoinEvent implements Listener {

    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        (new BPlayer(event.getPlayer().getUniqueId())).login(event);
    }

//    @EventHandler
//    public void onLogin(AsyncPlayerPreLoginEvent event) {
//        if (JPlayerManager.getPlayingPlayers().size() >= 10) {
//            Group group = JLMain.getAPI().getGroupManager().getUserGroup(event.getUniqueId());
//            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_FULL);
//            event.setKickMessage("Serveur complet");
//        }
//    }
}
