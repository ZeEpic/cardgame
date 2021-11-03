package me.zeepic.cardgame.items;

import me.zeepic.cardgame.util.Config;
import org.bukkit.Material;

public final class NextStep extends InventoryItem {


    public NextStep() {
        super(Material.TALL_GRASS, "Next", Config.getString("items.lore.next_step"));
    }
}
