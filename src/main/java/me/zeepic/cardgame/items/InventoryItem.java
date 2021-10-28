package me.zeepic.cardgame.items;

import me.zeepic.cardgame.util.Item;
import org.bukkit.Material;

public abstract class InventoryItem extends Item {

    public InventoryItem(Material material, String name, String lore) {
        super(material, name, lore);
    }
    public InventoryItem(Material material, String name, String lore, int amount) {
        super(material, name, lore, amount);
    }

}
