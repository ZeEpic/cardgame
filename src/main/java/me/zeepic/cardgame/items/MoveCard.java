package me.zeepic.cardgame.items;

import me.zeepic.cardgame.util.Config;
import org.bukkit.Material;

public final class MoveCard extends InventoryItem {

    public MoveCard() {
        super(Material.ARROW, "Move", Config.getString("items.lore.move_cards"));
    }

}
