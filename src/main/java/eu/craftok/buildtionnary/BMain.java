package eu.craftok.buildtionnary;

import eu.craftok.buildtionnary.manager.BGame;
import eu.craftok.buildtionnary.manager.sb.Scoreboard;
import eu.craftok.core.common.CoreCommon;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Project buildtionnary Created by Sithey
 */

public class BMain  extends JavaPlugin {

    private static BMain instance;

    private static CoreCommon API;
    private BGame game;
    private Scoreboard scoreboard;
    public static CoreCommon getAPI() {
        return API;
    }

    public static BMain getInstance() {
        return instance;
    }

    public BGame getGame() {
        return game;
    }


    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    public void onEnable() {
        instance = this;
        API = CoreCommon.getCommon();
        (this.game = new BGame()).setupGame();
        (this.scoreboard = new Scoreboard()).uptadeAllTime();
    }

}
