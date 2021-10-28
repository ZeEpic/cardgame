package me.zeepic.cardgame.subscribers;

import me.zeepic.cardgame.Main;
import me.zeepic.cardgame.cards.Card;
import me.zeepic.cardgame.enums.Rank;
import me.zeepic.cardgame.game.PlayerDocument;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDocumentSubscriber implements Subscriber<Document> {

    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onNext(Document document) {

        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(document.getString("uuid")));

        List<Class<? extends Card>> cardCollection = new ArrayList<>();
        List<Class<? extends Card>> cardDeck = new ArrayList<>();

        document.getList("collection", String.class)
                .forEach(
                        str -> cardCollection.add(Main.getCardMap().get(str))
                );
        document.getList("deck", String.class)
                .forEach(
                        str -> cardDeck.add(Main.getCardMap().get(str))
                );

        Main.getPlayerDocuments().put(player, new PlayerDocument(player,
                Rank.valueOf(document.getString("rank")),
                document.getInteger("wins"),
                document.getInteger("loses"),
                document.getInteger("cards_played"),
                cardCollection,
                cardDeck
        ));
        
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }

}
