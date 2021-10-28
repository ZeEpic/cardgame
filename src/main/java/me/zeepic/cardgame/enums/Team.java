package me.zeepic.cardgame.enums;

import me.zeepic.cardgame.util.Config;
import org.bukkit.Color;
import org.bukkit.util.Vector;

public enum Team {
    WHITE, BLACK;

    public static Color colorOf(Team team) {

        if (team.equals(Team.WHITE))
            return Color.WHITE;
        // otherwise
        return Color.BLACK;

    }

    public static Vector getVector(Team team) {
        if (team.equals(Team.WHITE)) {
            return Config.getVector("white_team.card_direction");
        }
        return Config.getVector("black_team.card_direction");
    }
}
