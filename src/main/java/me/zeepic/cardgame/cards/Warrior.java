package me.zeepic.cardgame.cards;

import me.zeepic.cardgame.game.CardGamePlayer;
import me.zeepic.cardgame.enums.CardRarity;
import me.zeepic.cardgame.enums.CardSubtype;
import org.bukkit.Material;

public abstract class Warrior extends Monster {

    public Warrior(int health, int damage, int speed, int cost, String name, Material icon, CardRarity rarity, String lore, CardGamePlayer owner, String actionText) {
        super(health, damage, speed, cost, name, icon, rarity, CardSubtype.WARRIOR, lore, owner, actionText);
    }

}
