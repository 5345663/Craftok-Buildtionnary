package eu.craftok.buildtionnary.manager.player;

import eu.craftok.buildtionnary.BMain;
import eu.craftok.buildtionnary.manager.player.stats.BStats;
import eu.craftok.buildtionnary.manager.task.GameTask;
import eu.craftok.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

import static eu.craftok.buildtionnary.manager.BGame.STATE.LOBBY;
import static eu.craftok.buildtionnary.manager.task.GameTask.selectBuilder;

/**
 * Project buildtionnary Created by Sithey
 */

public class BPlayer {

    private final UUID uuid;
    private boolean playing;
    private int points;
    private BStats stats;

    public BPlayer(UUID uuid){
        this.uuid = uuid;
        this.playing = false;
        this.points = 0;
        this.stats = new BStats(this);
    }


    public UUID getUniqueId() {
        return uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getUniqueId());
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getUniqueId());
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int value){
        this.points = this.points + value;
    }

    public BStats getStats() {
        return stats;
    }

    public void login(PlayerJoinEvent event){
        BPlayerManager.players.add(this);
        BMain.getInstance().getScoreboard().join(this);

        event.setJoinMessage(null);

        Player player = event.getPlayer();
        if (BMain.getInstance().getGame().getState() != LOBBY) {
            player.setGameMode(GameMode.SPECTATOR);
        } else {
            playing = true;
            player.setAllowFlight(false);
            player.setGameMode(GameMode.ADVENTURE);
            if (GameTask.timer == 60) {
                GameTask.timer--;
                new GameTask().runTaskTimer(BMain.getInstance(), 0, 20);
            }
            if (BPlayerManager.getAllPlayers().size() == 8){
                GameTask.timer = 3;
            }
        }
        if (isPlaying())
            Bukkit.broadcastMessage("§c§lCRAFTOK §8§l» §c" + event.getPlayer().getName() + "§7 a rejoint la partie §a(" + BPlayerManager.getPlayingPlayers().size() + "/8)");

        player.setMaxHealth(20.0D);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setSaturation(12.8F);
        player.setMaximumNoDamageTicks(20);
        player.setFireTicks(0);
        player.setFallDistance(0.0F);
        player.setLevel(0);
        player.setExp(0.0F);
        player.setWalkSpeed(0.2F);
        player.getInventory().setHeldItemSlot(0);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.closeInventory();
        player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
        ((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);
        player.getInventory().setHeldItemSlot(0);
        player.updateInventory();
        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(BMain.getInstance().getGame().getSpawn());
            }
        }.runTaskLater(BMain.getInstance(), 2);
    }

    public void logout(PlayerQuitEvent event){
        BMain.getInstance().getScoreboard().leave(this);
        BPlayerManager.players.remove(this);
        BPlayerManager.playersbuildcache.remove(this);
        BPlayerManager.playerswordcache.remove(this);
        if (GameTask.builder == this){
            BMain.getInstance().getGame().getTopPoints().forEach(s -> new PlayerUtils(BPlayerManager.getAllPlayers()).sendMessage(s));
            selectBuilder();
        }
        event.setQuitMessage(null);
        if (isPlaying()){
            Bukkit.broadcastMessage("§c§lCRAFTOK §8§l» §c" + event.getPlayer().getName() + "§7 a quitté la partie §a(" + BPlayerManager.getPlayingPlayers().size() + "/8)");
        } else
            if (BPlayerManager.getPlayingBPlayers().size() == 1)
                BMain.getInstance().getGame().setWin();

    }

}
