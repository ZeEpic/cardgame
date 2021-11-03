package me.zeepic.cardgame.items;

import me.zeepic.cardgame.util.Config;
import org.bukkit.Material;

public final class AttackCard extends InventoryItem {


    public AttackCard() {
        super(Material.WOODEN_SWORD, "Attack", Config.getString("items.lore.attack_cards"));
    }
}
