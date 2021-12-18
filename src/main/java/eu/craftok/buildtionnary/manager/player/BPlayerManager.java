package eu.craftok.buildtionnary.manager.player;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Project buildtionnary Created by Sithey
 */

public class BPlayerManager {

    public static List<BPlayer> players = new ArrayList<>();
    public static List<BPlayer> playersbuildcache = new ArrayList<>();
    public static List<BPlayer> playerswordcache = new ArrayList<>();
    public static List<BPlayer> getPlayingBPlayers() {
        List<BPlayer> values = new ArrayList<>();
        players.forEach(j -> {
            if (j.isPlaying()) values.add(j);
        });
        return values;
    }

    public static List<Player> getPlayingPlayers() {
        List<Player> values = new ArrayList<>();
        players.forEach(j -> {
            if (j.isPlaying()) values.add(j.getPlayer());
        });
        return values;
    }

    public static List<BPlayer> getSpectatingBPlayers() {
        List<BPlayer> values = new ArrayList<>();
        players.forEach(j -> {
            if (!j.isPlaying()) values.add(j);
        });
        return values;
    }

    public static List<Player> getSpectatingPlayers() {
        List<Player> values = new ArrayList<>();
        players.forEach(j -> {
            if (!j.isPlaying()) values.add(j.getPlayer());
        });
        return values;
    }

    public static List<Player> getAllPlayers() {
        List<Player> values = new ArrayList<>();
        players.forEach(j -> {
            values.add(j.getPlayer());
        });
        return values;
    }

    public static BPlayer getDPlayerByPlayer(Player player) {
        for (BPlayer DPlayers : players) {
            if (DPlayers.getPlayer().getUniqueId().compareTo(player.getUniqueId()) == 0) {
                return DPlayers;
            }
        }

        return null;
    }
}
