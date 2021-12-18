package eu.craftok.buildtionnary.manager.task;

import eu.craftok.buildtionnary.BMain;
import eu.craftok.buildtionnary.manager.BGame;
import eu.craftok.buildtionnary.manager.player.BPlayer;
import eu.craftok.buildtionnary.manager.player.BPlayerManager;
import eu.craftok.utils.PlayerUtils;
import eu.craftok.utils.firework.FireworkBuilder;
import eu.craftok.utils.firework.FireworkUtils;
import org.bukkit.*;
import java.util.List;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

import static eu.craftok.buildtionnary.manager.BGame.STATE.LOBBY;

/**
 * Project buildtionnary Created by Sithey
 */

public class GameTask extends BukkitRunnable {

    public static int timer = 60;
    public static int minplayer = 4;
    public static BPlayer builder;
    public static List<Location> fireworkwin = new ArrayList<>();
    @Override
    public void run() {

        Bukkit.getOnlinePlayers().forEach(b -> {
           if (b.getLocation().distance(BMain.getInstance().getGame().getSpawn()) > 35)
               b.teleport(BMain.getInstance().getGame().getSpawn());
        });

        BGame.STATE state = BMain.getInstance().getGame().getState();
        if (state != LOBBY) {
            if (BPlayerManager.getPlayingBPlayers().size() == 0) {
                Bukkit.getServer().shutdown();
            }
        }
        PlayerUtils util = new PlayerUtils(BPlayerManager.getAllPlayers());
        if (state == LOBBY){
            if (timer == 30 || timer == 15 || timer == 10 || timer == 5 || timer == 4 || timer == 3 || timer == 2) {
                if (timer == 5){
                    util.sendTitle(5, 20, 5, "§a❺", "");
                }
                if (timer == 4){
                    util.sendTitle(5, 20, 5, "§e➍", "");
                }
                if (timer == 3){
                    util.sendTitle(5, 20, 5, "§e➌", "");
                }
                if (timer == 2){
                    util.sendTitle(5, 20, 5, "§6➋", "");
                }
                util.sendMessage("§c§lCRAFTOK §8§l» §7La partie va commencer dans §c" + timer + "§7 secondes");
                util.sendSound(Sound.NOTE_PIANO, 2f);
            }
            if (timer == 1) {
                util.sendMessage("§c§lCRAFTOK §8§l» §7La partie va commencer dans §c" + timer + "§7 seconde");
                util.sendTitle(5, 20, 5, "§c➊", "");
                util.sendSound(Sound.NOTE_PIANO, 2f);
            }
            if (timer == 0) {
                util.sendMessage("§7§m-------------------------------");
                util.sendMessage("§fBienvenue en §cBuildtionnary §f!");
                util.sendMessage("§fLe but du jeu est simple : Trouver en premier le build");
                util.sendMessage("§fpour gagner des points");
                util.sendMessage("§fVous devez avoir le plus de points pour gagner");
                util.sendMessage("§7§m-------------------------------");
                util.sendSound(Sound.ORB_PICKUP, 2f);
                BMain.getInstance().getGame().startGame();
                return;
            }
            if (BPlayerManager.getPlayingBPlayers().size() < minplayer) {
                cancel();
                timer = 60;
                return;
            }
        }else if (state == BGame.STATE.GAME){
            if (timer == 0){
                BPlayerManager.playerswordcache.forEach(b -> b.getStats().addWordnotFound());
                BMain.getInstance().getGame().getTopPoints().forEach(s -> new PlayerUtils(BPlayerManager.getAllPlayers()).sendMessage(s));
                if (BMain.getInstance().getGame().getWord() != null)
                    new PlayerUtils(BPlayerManager.getAllPlayers()).sendMessage("§c§lCRAFTOK §8§l» §r§7Le bon mot était §c§l" + BMain.getInstance().getGame().getWord()[0]);
                selectBuilder();
                return;
            }
        }else{
            fireworkwin.clear();

            BPlayerManager.getPlayingBPlayers().forEach(b -> {
               fireworkwin.add(b.getPlayer().getLocation());
            });

            fireworkwin.forEach(f -> FireworkBuilder.summonInstantFirework(FireworkUtils.getRandomFireworkEffect(), f));

            if (timer == 0) {
                BPlayerManager.getAllPlayers().forEach(jp -> {
                    jp.kickPlayer(null);
                });
                Bukkit.getServer().shutdown();
            }
        }
        timer--;
    }

    public static void selectBuilder(){
        if (BPlayerManager.playersbuildcache.isEmpty()){
            BMain.getInstance().getGame().setWin();
            return;
        }
        if (builder != null && builder.getPlayer() != null)
            builder.getPlayer().setGameMode(GameMode.SPECTATOR);

        builder = BMain.getInstance().getGame().chooseBuilder();
        BMain.getInstance().getGame().resetTop();

        BPlayerManager.players.forEach(player -> {
           if (player == builder) {
               new PlayerUtils(player.getPlayer()).sendTitle(5, 90, 5, "§fVous devez constuire :", "§c" + BMain.getInstance().getGame().getWord()[0]);
           }else{
               BPlayerManager.playerswordcache.add(player);
               new PlayerUtils(player.getPlayer()).sendTitle(5, 90, 5, "§fVous devez §cécrire §fdans le chat", "le §cmot §fà §ctrouver");
           }
        });
        builder.getPlayer().setGameMode(GameMode.CREATIVE);
        builder.getPlayer().teleport(BMain.getInstance().getGame().getSpawn());
        timer = 120;
    }

}
