package eu.craftok.buildtionnary.manager.sb;

import eu.craftok.buildtionnary.BMain;
import eu.craftok.buildtionnary.manager.BGame;
import eu.craftok.buildtionnary.manager.player.BPlayer;
import eu.craftok.buildtionnary.manager.player.BPlayerManager;
import eu.craftok.buildtionnary.manager.task.GameTask;
import eu.craftok.utils.PlayerUtils;
import org.bukkit.Bukkit;

/**
 * Project buildtionnary Created by Sithey
 */

public class Scoreboard extends SidebarManager {

    @Override
    public void build(BPlayer player, SidebarEditor e) {
        BGame.STATE state = BMain.getInstance().getGame().getState();
        e.clear();
        PlayerUtils util = new PlayerUtils(player.getPlayer());
        e.setTitle("§c§lBUILDTIONNARY");

        if (state == BGame.STATE.LOBBY) {
            e.add("");
            e.add("§c§LSERVEUR");
            e.add(" §fServeur §3» §b" + Bukkit.getServerName());
            e.add(" §fEn jeu §3» §b" + BPlayerManager.getPlayingPlayers().size() + "/8");

            if (GameTask.timer != 60) {
                util.sendActionBar("§7Début dans §c" + GameTask.timer + "§7s");
            } else {
                util.sendActionBar("§cManque de joueurs pour commencer...");
            }
            e.add("");
            e.add("§c§lJEU");
            e.add(" §fMode §3» §bSolo");
            e.add("");
            e.add("§b[§fcraftok.fr§c]");
        } else if (state == BGame.STATE.GAME) {

            if (GameTask.builder != null && GameTask.builder == player) {
                e.add("");
                e.add("§c§lPARTIE");
                e.add(" §fPoints §3» §b" + player.getPoints());
                e.add("");
                e.add(" §fA toi de dessiner §b" + GameTask.builder.getPlayer().getName());
                e.add(" §fTu dois dessiner §3» §b" + BMain.getInstance().getGame().getWord()[0]);
                e.add("");
                e.add("§b[§fcraftok.fr§c]");
            }else{
                e.setByScore(" ", 1000);
                BPlayerManager.getPlayingBPlayers().forEach(p -> {
                    e.setByScore(" §f" + p.getPlayer().getName() + " §3» §b" + p.getPoints(), p.getPoints());
                });
                e.setByScore("", -1);
                e.setByScore("§b[§fcraftok.fr§c]", -2);
            }
            util.sendActionBar(" §fTemps restant §3» §b" + GameTask.timer + "§3s");
        } else {
            e.add("");
            e.add(" §fPoints §3» §b" + player.getPoints());  e.add("");
            e.add("§b[§fcraftok.fr§c]");
        }
    }


    public void uptadeAllTime() {
        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(BMain.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (BPlayerManager.getAllPlayers().size() != 0) {
                    BMain.getInstance().getScoreboard().update();
                }
            }
        }, 0L, 5L);
    }


}
