package eu.craftok.buildtionnary.cmd;


import eu.craftok.buildtionnary.BMain;

/**
 * Project buildtionnary Created by Sithey
 */

public class CommandRegister {
    public static void registerCommand() {

        BMain main = BMain.getInstance();
        main.getCommand("start").setExecutor(new StartCommand());
        main.getCommand("stats").setExecutor(new StatsCommand());
    }

}
