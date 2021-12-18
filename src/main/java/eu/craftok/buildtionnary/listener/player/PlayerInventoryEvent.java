package eu.craftok.buildtionnary.listener.player;

import eu.craftok.buildtionnary.BMain;
import eu.craftok.buildtionnary.manager.BGame;
import eu.craftok.buildtionnary.manager.player.BPlayer;
import eu.craftok.buildtionnary.manager.player.BPlayerManager;
import eu.craftok.buildtionnary.manager.task.GameTask;
import eu.craftok.core.common.user.User;
import eu.craftok.utils.PlayerUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Project buildtionnary Created by Sithey
 */

public class PlayerInventoryEvent implements Listener {



    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    private List<Player> cooldown = new ArrayList<>();

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        User user = BMain.getAPI().getUserManager().getUserByUniqueId(event.getPlayer().getUniqueId());
        BPlayer bPlayer = BPlayerManager.getDPlayerByPlayer(event.getPlayer());


        if (BMain.getInstance().getGame().getState() == BGame.STATE.GAME) {

            if (!BPlayerManager.getDPlayerByPlayer(event.getPlayer()).isPlaying()) {
                event.setCancelled(true);
                BPlayerManager.getSpectatingBPlayers().forEach(s -> s.getPlayer().sendMessage(user.getDisplayName() + "§f: " + event.getMessage()));
                return;
            }else{
                if ((GameTask.builder != null && bPlayer == GameTask.builder) || !BPlayerManager.playerswordcache.contains(bPlayer)){
                    event.setCancelled(true);
                    return;
                }else{
                    if (cooldown.contains(event.getPlayer())){
                        event.getPlayer().sendMessage("§4§lPatientez 2 secondes... Vous n'avez pas besoin de spam :D");
                        event.setCancelled(true);
                        return;
                    }
                    boolean found = false;
                    for (String w : BMain.getInstance().getGame().getWord()){
                        if (w.toLowerCase().equalsIgnoreCase(event.getMessage().toLowerCase())){
                            found = true;
                        }
                    }
                    if (found){
                        event.setCancelled(true);
                        if (GameTask.builder != null) {
                            GameTask.builder.addPoints(BMain.getInstance().getGame().getBuildfound());
                            GameTask.builder.getStats().addBuildFound();
                        }
                        event.getPlayer().sendMessage("§c§lVous avez trouvé le bon mot !");
                        BPlayerManager.playerswordcache.remove(bPlayer);
                        bPlayer.addPoints(BMain.getInstance().getGame().getWordFoundByTop());
                        bPlayer.getStats().addWordFound();
                        int dif4 = GameTask.timer / 4;
                        new PlayerUtils(BPlayerManager.getAllPlayers()).sendMessage("§c§lCRAFTOK §8§l» §r§c" + event.getPlayer().getName() + "§7 a trouvé le mot");
                        new PlayerUtils(BPlayerManager.getAllPlayers()).sendSound(Sound.ORB_PICKUP, 2f);
                        if (GameTask.timer - dif4 <= 0 || BPlayerManager.playerswordcache.isEmpty()){
                            BPlayerManager.playerswordcache.forEach(b -> b.getStats().addWordnotFound());
                            BMain.getInstance().getGame().getTopPoints().forEach(s -> {
                                new PlayerUtils(BPlayerManager.getAllPlayers()).sendMessage(s);
                            });
                            new PlayerUtils(BPlayerManager.getAllPlayers()).sendMessage("§c§lCRAFTOK §8§l» §r§7Le mot à trouver était §c" + BMain.getInstance().getGame().getWord()[0]);                            GameTask.selectBuilder();
                        }else {
                            GameTask.timer = GameTask.timer - dif4;
                        }
                        return;
                    }
                    cooldown.add(event.getPlayer());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            cooldown.remove(event.getPlayer());
                        }
                    }.runTaskLater(BMain.getInstance(), 40);
                }
            }
        }

        event.setFormat(user.getDisplayName() + "§f: %2$s");
    }

    @EventHandler
    public void onMotd(ServerListPingEvent event) {
        if (BMain.getInstance().getGame().getState() != BGame.STATE.LOBBY || BPlayerManager.getPlayingBPlayers().size() == 8) {
            event.setMotd("INGAME");
        }
    }



    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if (!BMain.getInstance().getGame().getBlocks().contains(event.getBlock())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        if (event.getBlock().getType().equals(Material.SIGN_POST))
            event.setCancelled(true);
        if (!BMain.getInstance().getGame().getBlocks().contains(event.getBlock())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSign(SignChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent event){
        event.setCancelled(true);
    }
}
