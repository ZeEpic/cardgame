package me.zeepic.cardgame.cards;

import me.zeepic.cardgame.game.CardGamePlayer;
import me.zeepic.cardgame.enums.CardRarity;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public final class TestCard extends Warrior {

    public TestCard(CardGamePlayer owner) {
        super(
                2,
                1,
                3,
                2,
                "Test Card",
                Material.PAPER,
                CardRarity.COMMON,
                "Card lore goes here.",
                owner,
                """
                             Damage ⨠ Says a message in chat.
                             Move ⨠ Says a message in chat.
                             Play ⨠ Says a message in chat.
                             Death ⨠ Says a message in chat.
                             Attack ⨠ Says a message in chat.
                             End Turn ⨠ Says a message in chat.
                             Begin Turn ⨠ Says a message in chat.
                        """
        );
        updateItem();
    }

    @Override
    public void onPlayAction() {
        Bukkit.broadcastMessage(getOwner().getPlayer().getName() + "'s Test Card Play Action");
    }

    @Override
    public void onMove() {
        Bukkit.broadcastMessage(getOwner().getPlayer().getName() + "'s Test Card Move Action");
    }

    @Override
    public void onDeath() {
        Bukkit.broadcastMessage(getOwner().getPlayer().getName() + "'s Test Card Death Action");
    }

    @Override
    public void onAttack() {
        Bukkit.broadcastMessage(getOwner().getPlayer().getName() + "'s Test Card Attack Action");
    }

    @Override
    public void onDamage() {
        Bukkit.broadcastMessage(getOwner().getPlayer().getName() + "'s Test Card Damage Action");
    }

    @Override
    public void onEndTurn() {
        Bukkit.broadcastMessage(getOwner().getPlayer().getName() + "'s Test Card End Turn Action");
    }

    @Override
    public void onBeginTurn() {
        Bukkit.broadcastMessage(getOwner().getPlayer().getName() + "'s Test Card Begin Turn Action");
    }

}
