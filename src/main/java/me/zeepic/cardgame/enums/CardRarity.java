package me.zeepic.cardgame.enums;

import me.zeepic.cardgame.util.Config;

public enum CardRarity {

    COMMON, UNCOMMON, RARE, FABLED, SPECIAL;

    @Override
    public String toString() {

        if (this == CardRarity.UNCOMMON) {
            return Config.getString("cards.rarity_level.1");
        } else if (this == CardRarity.RARE) {
            return Config.getString("cards.rarity_level.2");
        } else if (this == CardRarity.FABLED) {
            return Config.getString("cards.rarity_level.3");
        } else if (this == CardRarity.SPECIAL) {
            return Config.getString("cards.rarity_level.4");
        }
        return Config.getString("cards.rarity_level.0");

    }

}
