package me.zeepic.cardgame.util;

import me.zeepic.cardgame.enums.Team;
import me.zeepic.cardgame.game.CardGamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;

public class ObjectiveManager {

    private Objective  objective;
    private Scoreboard board;
    private int        scoreValue;

    private final String resultFormat = ChatColor.GRAY + "%s" + ChatColor.WHITE + " | " + ChatColor.GOLD + "%s";

    public void updateScoreboard(CardGamePlayer player) {

        Map<String, String> scoreMap = new HashMap<>();
        scoreMap.put("Casting Power", String.valueOf(player.getCastingPower()));
        scoreMap.put("Turn State", player.getState().toString().toLowerCase().replace("_", " "));
        scoreMap.put("Health", player.getHealth() + " ❤");
        if (player.getTeam().equals(Team.WHITE))
            scoreMap.put("Team", "§fWhite");
        else
            scoreMap.put("Team", "§0Black");
        setScores(scoreMap);
        setBoard(player.getPlayer());

    }

    private void addScore(int number, String name, String value) {
        String result = String.format(resultFormat, name, value);
        if (result.length() > 40)
            result = result.substring(0, 39);
        Score score = objective.getScore(result);
        score.setScore(number);
    }

    public void setBoard(Player player) {
        player.setScoreboard(board);
    }

    public void setScores(Map<String, String> scores) {

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        this.board = manager.getNewScoreboard();

        this.objective = board.registerNewObjective("ACard-1", "dummy");
        this.objective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "A CARD");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.scoreValue = scores.size();
        scores.forEach((name, value) -> {
            scoreValue -= 1;
            addScore(scoreValue, name, value);
        });

    }
}
