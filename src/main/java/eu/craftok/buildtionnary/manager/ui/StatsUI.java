package eu.craftok.buildtionnary.manager.ui;

import eu.craftok.buildtionnary.manager.player.stats.BStats;
import eu.craftok.utils.ItemCreator;
import eu.craftok.utils.inventory.CustomInventory;
import eu.craftok.utils.inventory.item.StaticItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Project buildtionnary Created by Sithey
 */

public class StatsUI extends CustomInventory {
    private BStats stats;
    public StatsUI(Player p, BStats target) {
        super(p, "§7Statistiques » §b" + target.getPlayer().getOfflinePlayer().getName(), 1, 1);
        stats = target;
    }

    @Override
    public void setupMenu() {
        addItem(new StaticItem(0, new ItemCreator(Material.DIAMOND_SWORD).setName("§7Partie jouées » §c" + stats.getPlaying()).getItemstack()));
        addItem(new StaticItem(1, new ItemCreator(Material.REDSTONE).setName("§7Mots trouvés » §c" + stats.getWordfound()).getItemstack()));
        addItem(new StaticItem(2, new ItemCreator(Material.EMERALD).setName("§7Mots non trouvés » §c" + stats.getWordnotfound()).getItemstack()));
        addItem(new StaticItem(3, new ItemCreator(Material.DIAMOND).setName("§7Constructions découverte » §c" + stats.getBuildfound()).getItemstack()));

        addItem(new StaticItem(8, new ItemCreator(Material.SKULL_ITEM).addLore("").addLore("§7Temps de jeu » §c" + stats.getPlayingtime()).setDurability((short) 3).setOwner(stats.getPlayer().getOfflinePlayer().getName()).setName(" §7Statistiques de » §c" + stats.getPlayer().getOfflinePlayer().getName()).getItemstack()));
    }
}
