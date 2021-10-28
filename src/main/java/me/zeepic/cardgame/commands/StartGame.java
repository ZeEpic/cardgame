package me.zeepic.cardgame.commands;

import me.zeepic.cardgame.Main;
import org.bukkit.entity.Player;

public final class StartGame extends CustomCommand {

    public StartGame(Main plugin) {
        super(plugin, 1);
    }

    @Override
    protected boolean executeCommand(Player sender, String[] args) {

        Player opponent = getPlugin().getServer().getPlayer(args[0]);

        if (opponent == null)
            return false;

        getPlugin().makeGame(sender, opponent);
        sender.sendMessage("A game has been started with " + opponent.getName() + ".");
        opponent.sendMessage("You are now in a game with " + sender.getName() + ".");

        return true;
    }
}
