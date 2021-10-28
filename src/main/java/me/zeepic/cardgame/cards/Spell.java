package me.zeepic.cardgame.cards;

import lombok.Getter;
import me.zeepic.cardgame.game.BoardLocation;
import me.zeepic.cardgame.game.CardGamePlayer;
import me.zeepic.cardgame.enums.CardRarity;
import me.zeepic.cardgame.enums.CardSubtype;
import me.zeepic.cardgame.enums.CardType;
import me.zeepic.cardgame.util.Config;
import me.zeepic.cardgame.util.Item;
import me.zeepic.cardgame.util.Util;
import org.bukkit.Material;


public abstract class Spell extends Card {

    @Getter private final String actionText;

    public Spell(int cost, String name, Material icon, CardRarity rarity, CardSubtype subType, String lore, CardGamePlayer owner, String actionText) {
        super(cost, name, icon, rarity, CardType.SPELL, subType, lore, owner);
        this.actionText = actionText;
    }


    @Override
    public void play(CardGamePlayer gamePlayer, BoardLocation location) {
        onPlayAction();
    }

    @Override
    public void updateItem() {
        String cardType = Util.titleCase(
                getType().toString().toLowerCase()
                        + " "
                        + getSubType().toString().toLowerCase()
        );
        String itemLore = String.format(Config.getString("cards.item_lore.spell"),
                cardType,
                actionText,
                getRarity().toString(),
                getLore()
        );
        getItem().setLore(Item.loreFromString(itemLore));
    }
}
