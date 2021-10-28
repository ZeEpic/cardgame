package me.zeepic.cardgame.cards;

import lombok.Getter;
import me.zeepic.cardgame.game.BoardLocation;
import me.zeepic.cardgame.game.CardGamePlayer;
import me.zeepic.cardgame.enums.CardRarity;
import me.zeepic.cardgame.enums.CardSubtype;
import me.zeepic.cardgame.enums.CardType;
import me.zeepic.cardgame.util.Item;
import org.bukkit.Material;

public abstract class Card {

    @Getter private final CardGamePlayer owner;
    @Getter private final CardSubtype    subType;
    @Getter private final CardRarity     rarity;
    @Getter private final Material       icon;
    @Getter private final CardType       type;
    @Getter private final String         name;
    @Getter private final String         lore;
    @Getter private final Item           item;
    @Getter private final int            cost;

    protected Card(int cost, String name, Material icon, CardRarity rarity, CardType type, CardSubtype subType, String lore, CardGamePlayer owner) {
        this.subType = subType;
        this.rarity  = rarity;
        this.owner   = owner;
        this.cost    = cost;
        this.name    = name;
        this.icon    = icon;
        this.type    = type;
        this.lore    = lore;
        this.item    = new Item(icon, name, cost);
    }

    public abstract void play(CardGamePlayer gamePlayer, BoardLocation location);
    protected abstract void updateItem();
    protected abstract void onPlayAction();

}
