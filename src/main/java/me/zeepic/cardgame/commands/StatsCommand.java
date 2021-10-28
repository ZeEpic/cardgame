package me.zeepic.cardgame.commands;

import me.zeepic.cardgame.Main;
import me.zeepic.cardgame.game.PlayerDocument;
import me.zeepic.cardgame.util.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class StatsCommand extends CustomCommand implements InventoryHolder {

    private Player viewing;

    public StatsCommand(Main plugin) {
        super(plugin, 1);
    }

    @Override
    protected boolean executeCommand(Player sender, String[] args) {
        viewing = Bukkit.getPlayer(args[0]);
        sender.openInventory(getInventory());
        return false;
    }

    @Override
    public Inventory getInventory() {
        PlayerDocument document = getPlugin().fetchPlayerDocument(viewing);
        Inventory inventory = Bukkit.createInventory(this, 9, viewing.getName());
        Map<Material, String[]> display = new HashMap<>();
        display.put(Material.IRON_CHESTPLATE, new String[]{"Rank", document.getRank().toString()});
        display.put(Material.GRASS, new String[]{"Wins/Lose Ratio", String.valueOf(document.getWins()/(document.getLoses()+1))});
        display.put(Material.PAPER, new String[]{"Cards Played", String.valueOf(document.getCardsPlayed())});
        display.put(Material.BOOKSHELF, new String[]{"Collection Size", String.valueOf(document.getCollection().size())});
        display.put(Material.BOOK, new String[]{"Deck Size", String.valueOf(document.getDeck().size())});

        int i = 0;
        for (Material material : display.keySet()) {
            String name = display.get(material)[0];
            String value = display.get(material)[1];
            inventory.setItem(i, new Item(material, ChatColor.GRAY + name + ChatColor.WHITE + " | " + ChatColor.GOLD + value));
            i++;
        }
        return inventory;

    }

}
