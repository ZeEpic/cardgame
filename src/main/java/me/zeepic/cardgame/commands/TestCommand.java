package me.zeepic.cardgame.commands;

import me.zeepic.cardgame.Main;
import me.zeepic.cardgame.cards.TestCard;
import me.zeepic.cardgame.game.CardGamePlayer;
import org.bukkit.entity.Player;

public final class TestCommand extends CustomCommand {

    public TestCommand(Main plugin) {
        super(plugin, 0);
    }

    @Override
    public boolean executeCommand(Player player, String[] args) {

        CardGamePlayer gamePlayer = getPlugin().getPlayer(player);
        if (gamePlayer == null) {
            player.sendMessage("You aren't in a game right now!");
            return true;
        }
        gamePlayer.addCard(TestCard.class); // give them a test card
        player.sendMessage("You got a test card! Yay!");

        return true;

    }

}
