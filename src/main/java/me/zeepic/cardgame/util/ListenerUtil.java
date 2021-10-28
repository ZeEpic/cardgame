package me.zeepic.cardgame.util;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class ListenerUtil {

    public boolean canPerformAction(Player player) {
        return player.isOp() && player.getGameMode().equals(GameMode.CREATIVE);
    }

}
