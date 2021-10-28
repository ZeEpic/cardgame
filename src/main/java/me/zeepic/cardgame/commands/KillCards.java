package me.zeepic.cardgame.commands;

import me.zeepic.cardgame.Main;
import me.zeepic.cardgame.cards.Monster;
import me.zeepic.cardgame.game.CardGamePlayer;
import org.bukkit.entity.Player;

public final class KillCards extends CustomCommand {


    public KillCards(Main plugin) {
        super(plugin, 1);
    }

    @Override
    public boolean executeCommand(Player player, String[] args) {

        if (!player.isOp()) {
            player.sendMessage("Only ops can use this command.");
            return true;
        }
        CardGamePlayer gamePlayer = getPlugin().getPlayer(player);
        if (gamePlayer == null) {
            player.sendMessage("You aren't in a game right now!");
            return true;
        }
        if (args[0].equals("all")) {
            for (Monster card : gamePlayer.getPlayedCards()) {
                card.getArmorStand().remove();
            }
        } else if (args[0].equals("nearest")) {
            Monster nearest = null;
            for (Monster card : gamePlayer.getPlayedCards()) {
                if (nearest == null)
                    nearest = card;
                if (nearest.getLocation().distance(player.getLocation())
                        > card.getLocation().distance(player.getLocation()))
                    nearest = card; // when the nearest is farther than the new card
            }
            if (nearest != null)
                nearest.getArmorStand().remove();
        } else
            return false;

        return true;

    }

}
