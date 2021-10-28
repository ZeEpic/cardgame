package me.zeepic.cardgame.commands;

import me.zeepic.cardgame.Main;
import me.zeepic.cardgame.enums.State;
import me.zeepic.cardgame.game.CardGamePlayer;
import org.bukkit.entity.Player;

public class TempCommand extends CustomCommand {

    public TempCommand(Main plugin) {
        super(plugin, 1);
    }

    @Override
    protected boolean executeCommand(Player sender, String[] args) {

        CardGamePlayer player = getPlugin().getPlayer(sender);
        if (player == null)
            return false;
        sender.sendMessage(player.getState().toString());
        player.setState(State.valueOf(args[0]));

        return true;
    }

}
