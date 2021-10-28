package me.zeepic.cardgame.game;

import lombok.Getter;
import me.zeepic.cardgame.cards.Card;
import me.zeepic.cardgame.enums.Rank;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class PlayerDocument {

    @Getter private final OfflinePlayer player;
    @Getter private final Rank rank;
    @Getter private final int wins;
    @Getter private final int loses;
    @Getter private final int cardsPlayed;
    @Getter private final List<Class<? extends Card>> collection;
    @Getter private final List<Class<? extends Card>> deck;

    public PlayerDocument(OfflinePlayer player, Rank rank, int wins, int loses, int cardsPlayed, List<Class<? extends Card>> collection, List<Class<? extends Card>> deck) {
        this.player = player;
        this.rank = rank;
        this.wins = wins;
        this.loses = loses;
        this.cardsPlayed = cardsPlayed;
        this.collection = collection;
        this.deck = deck;
    }
}
