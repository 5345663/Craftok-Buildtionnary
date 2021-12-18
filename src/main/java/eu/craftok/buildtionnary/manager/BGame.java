package eu.craftok.buildtionnary.manager;

import eu.craftok.buildtionnary.BMain;
import eu.craftok.buildtionnary.cmd.CommandRegister;
import eu.craftok.buildtionnary.listener.EventRegister;
import eu.craftok.buildtionnary.manager.cuboid.Cuboid;
import eu.craftok.buildtionnary.manager.player.BPlayer;
import eu.craftok.buildtionnary.manager.player.BPlayerManager;
import eu.craftok.buildtionnary.manager.task.GameTask;
import eu.craftok.buildtionnary.utils.TopUtils;
import eu.craftok.buildtionnary.utils.ValueComparator;
import eu.craftok.utils.CConfig;
import eu.craftok.utils.PlayerUtils;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.*;

import static eu.craftok.buildtionnary.utils.TopUtils.getTop;

/**
 * Project buildtionnary Created by Sithey
 */

public class BGame {

    private CConfig configuration;
    private Location spawn;
    private STATE state;
    private List<String> wordcache = new ArrayList<>();
    private List<Block> blocks = new ArrayList<>();
    private String word = null;
    private int top, buildfound, wordfoundtop1, wordfoundtop2, wordfoundtop3, wordfoundtop4, wordfoundtop5, wordfoundtop6, wordfoundtop7, wordfoundtop8;
    public void setupGame(){
        EventRegister.registerEvent();
        CommandRegister.registerCommand();
        for (World world : Bukkit.getWorlds()) {
            world.setTime(0);
            world.setDifficulty(Difficulty.EASY);
            world.setGameRuleValue("doFireTick", "false");
            world.setGameRuleValue("doDaylightCycle", "false");
        }
        this.configuration = new CConfig("configuration.yml", BMain.getInstance());
        this.configuration.addValue("spawn", "world:-1.5:118:13.5:180:3");
        this.configuration.addValue("corner1", "world:15:109:32:0:0");
        this.configuration.addValue("corner2", "world:-18:125:-1:0:0");
        this.configuration.addValue("words", Arrays.asList( "Lit", "Maison", "Piscine", "Homme"));
        this.configuration.addValue("coins.buildfound", 10);
        this.configuration.addValue("coins.wordfound.top1", 50);
        this.configuration.addValue("coins.wordfound.top2", 50);
        this.configuration.addValue("coins.wordfound.top3", 50);
        this.configuration.addValue("coins.wordfound.top4", 50);
        this.configuration.addValue("coins.wordfound.top5", 50);
        this.configuration.addValue("coins.wordfound.top6", 50);
        this.configuration.addValue("coins.wordfound.top7", 50);
        this.configuration.addValue("coins.wordfound.top8", 50);
        buildfound = ((int) this.configuration.getValue("coins.buildfound"));
        wordfoundtop1 = ((int) this.configuration.getValue("coins.wordfound.top1"));
        wordfoundtop2 = ((int) this.configuration.getValue("coins.wordfound.top2"));
        wordfoundtop3 = ((int) this.configuration.getValue("coins.wordfound.top3"));
        wordfoundtop4 = ((int) this.configuration.getValue("coins.wordfound.top4"));
        wordfoundtop5 = ((int) this.configuration.getValue("coins.wordfound.top5"));
        wordfoundtop6 = ((int) this.configuration.getValue("coins.wordfound.top6"));
        wordfoundtop7 = ((int) this.configuration.getValue("coins.wordfound.top7"));
        wordfoundtop8 = ((int) this.configuration.getValue("coins.wordfound.top8"));
        this.top = 0;
        this.wordcache.addAll((this.configuration.getConfig().getStringList("words")));
        this.spawn = CConfig.getLocationString(this.configuration.getValue("spawn").toString());

        this.state = STATE.LOBBY;
    }

    public void startGame(){
        BPlayerManager.playersbuildcache.addAll(BPlayerManager.getPlayingBPlayers());
        getBlocks().addAll(new Cuboid(CConfig.getLocationString(this.configuration.getValue("corner1").toString()), CConfig.getLocationString(this.configuration.getValue("corner2").toString())).getBlocks());
        setState(STATE.GAME);
        teleportAll();
        GameTask.selectBuilder();
    }

    public void teleportAll(){
        BPlayerManager.getAllPlayers().forEach(p -> {
            p.setGameMode(GameMode.SPECTATOR);
            if (BPlayerManager.getDPlayerByPlayer(p).isPlaying())
                BPlayerManager.getDPlayerByPlayer(p).getStats().addPlaying();
            p.teleport(spawn);
        });
    }

    public BPlayer chooseBuilder(){

        for (Block block : BMain.getInstance().getGame().getBlocks()){
            block.setType(Material.AIR);
        }
        word = wordcache.remove(new Random().nextInt(wordcache.size()));
        return BPlayerManager.playersbuildcache.remove(0);
    }

    public List<String> getTopPoints(){
        List<String> values = new ArrayList<>();
        values.add("=====================");
        values.add("");
        values.add(ChatColor.YELLOW + "§nTop " + BPlayerManager.getPlayingBPlayers().size());
        values.add("");
        int i = 1;
        List<TopUtils.TopUtilsObject> tops = new ArrayList<>();
        BPlayerManager.getPlayingBPlayers().forEach(b -> {
            tops.add(new TopUtils.TopUtilsObject(b, b.getPoints()));
        });
        for (TopUtils.TopUtilsObject s : getTop(tops)) {
            values.add("\u00a7c" + i + ") " + ((BPlayer) s.getObject()).getOfflinePlayer().getName() + " \u00a77- " + s.getValue());
            ++i;
        }
        values.add("");
        return values;
    }

    public void setWin() {
        if (state == STATE.GAME) {
            BPlayerManager.getAllPlayers().forEach(p -> p.setGameMode(GameMode.SPECTATOR));
            getTopPoints().forEach(s -> {
                new PlayerUtils(BPlayerManager.getAllPlayers()).sendMessage(s);
            });

            List<TopUtils.TopUtilsObject> tops = new ArrayList<>();

            for (BPlayer b : BPlayerManager.getPlayingBPlayers()){
                tops.add(new TopUtils.TopUtilsObject(b, b.getPoints()));
            }

            tops = new ArrayList<>(getTop(tops));

            int pointmax = tops.get(0).getValue();

            tops.forEach(t -> {
               if (t.getValue() != pointmax){
                   ((BPlayer) t.getObject()).setPlaying(false);
               }
            });
            setState(STATE.FINISH);
            GameTask.timer = 15;
            final String[] winner = new String[1];
            BPlayerManager.getPlayingBPlayers().forEach(w -> {
                GameTask.fireworkwin.add(w.getPlayer().getLocation());
                winner[0] = w.getPlayer().getName();
                w.getPlayer().setGameMode(GameMode.CREATIVE);
                PlayerUtils utils = new PlayerUtils(w.getPlayer());
                utils.sendTitle(20, 20 * 3, 20, "§6§lVICTOIRE", "§7Vous avez gagné la partie !");
            });
            BPlayerManager.getSpectatingPlayers().forEach(bPlayer -> {
                new PlayerUtils(bPlayer.getPlayer()).sendTitle(20, 20 * 3, 20, "§c§lFIN DE PARTIE !", "§7" + winner[0] + " gagne la partie !");
            });
        }
    }


    public CConfig getConfiguration() {
        return configuration;
    }

    public Location getSpawn() {
        return spawn;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public List<String> getWordcache() {
        return wordcache;
    }

    public String[] getWord() {
        return word.split(":");
    }

    public int getBuildfound() {
        return buildfound;
    }

    public int getWordFoundByTop(){
        this.top++;
        if (top == 1)
            return wordfoundtop1;
        if (top == 2)
            return wordfoundtop2;
        if (top == 3)
            return wordfoundtop3;
        if (top == 4)
            return wordfoundtop4;
        if (top == 5)
            return wordfoundtop5;
        if (top == 6)
            return wordfoundtop6;
        if (top == 7)
            return wordfoundtop7;
        if (top == 8)
            return wordfoundtop8;
        return 0;
    }

    public void resetTop(){
        this.top = 0;
        BPlayerManager.playerswordcache.clear();
    }

    public enum STATE {LOBBY, GAME, FINISH}
}
