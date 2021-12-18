package eu.craftok.buildtionnary.cmd;

import eu.craftok.buildtionnary.BMain;
import eu.craftok.buildtionnary.manager.BGame;
import eu.craftok.buildtionnary.manager.player.BPlayerManager;
import eu.craftok.buildtionnary.manager.task.GameTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Project buildtionnary Created by Sithey
 */

public class StartCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()){
                if (BMain.getInstance().getGame().getState() == BGame.STATE.LOBBY) {
                    if (BPlayerManager.getPlayingPlayers().size() > 1) {
                        player.sendMessage("§aVous venez de lancer la partie §e" + Bukkit.getServerName() + " §a!");
                        GameTask.minplayer = 2;
                        if (GameTask.timer == 60) {
                            new GameTask().runTaskTimer(BMain.getInstance(), 0, 20);
                        }
                        GameTask.timer = 3;
                    } else {
                        player.sendMessage("§cVous ne pouvez pas lancer la partie car il faut 2 joueurs minimum !");
                    }
                }
            }else{
                player.sendMessage("§cVous n'avez pas l'autorisation de faire ceci !");
            }
        }
        return false;
    }
}
