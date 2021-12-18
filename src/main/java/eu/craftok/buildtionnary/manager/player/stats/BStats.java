package eu.craftok.buildtionnary.manager.player.stats;

import eu.craftok.buildtionnary.BMain;
import eu.craftok.buildtionnary.manager.BGame;
import eu.craftok.buildtionnary.manager.player.BPlayer;
import eu.craftok.core.common.user.User;
import eu.craftok.utils.TimeUtils;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Project buildtionnary Created by Sithey
 */

public class BStats {

    private final BPlayer player;

    public BStats(BPlayer player){
        this.player = player;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer().getPlayer() == null) {
                    cancel();
                    return;
                }
                if (BMain.getInstance().getGame().getState() == BGame.STATE.GAME) {
                    if (getPlayer().isPlaying()){
                        User user = BMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
                        user.setStat("buildtionnary.playingtime", Integer.parseInt(user.getStat("buildtionnary.playingtime", 0)) + 1);
                    }
                }

            }
        }.runTaskTimerAsynchronously(BMain.getInstance(), 20, 20);
    }

    public BPlayer getPlayer() {
        return player;
    }


    public int getPlaying() {
        User user = BMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        return Integer.parseInt(user.getStat("buildtionnary.playing", 0));
    }

    public void addPlaying(){
        User user = BMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        user.setStat("buildtionnary.playing", getPlaying() + 1);
    }

    public int getBuildfound() {
        User user = BMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        return Integer.parseInt(user.getStat("buildtionnary.buildfound", 0));
    }

    public void addBuildFound(){
        User user = BMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        user.setStat("buildtionnary.buildfound", getBuildfound() + 1);
    }

    public int getWordfound() {
        User user = BMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        return Integer.parseInt(user.getStat("buildtionnary.wordfound", 0));
    }

    public void addWordFound(){
        User user = BMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        user.setStat("buildtionnary.wordfound", getWordfound() + 1);
    }

    public int getWordnotfound() {
        User user = BMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        return Integer.parseInt(user.getStat("buildtionnary.wordnotfound", 0));
    }

    public void addWordnotFound(){
        User user = BMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        user.setStat("buildtionnary.wordnotfound", getWordnotfound() + 1);
    }

    public String getPlayingtime() {
        User user = BMain.getAPI().getUserManager().getUserByUniqueId(player.getUniqueId());
        return TimeUtils.getDurationBreakdown(Integer.parseInt(user.getStat("buildtionnary.playingtime", 0)) * 1000L);
    }

}
