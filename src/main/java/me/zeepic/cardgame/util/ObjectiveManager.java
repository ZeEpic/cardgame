package me.zeepic.cardgame.util;

import me.zeepic.cardgame.enums.State;
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
        scoreMap.put("Health", ChatColor.RED + "" + player.getHealth() + " ❤");
        if (player.getTeam().equals(Team.WHITE))
            scoreMap.put("Team", "§fWhite");
        else
            scoreMap.put("Team", "§0Black");
        if (player.getState().equals(State.WAITING))
            scoreMap.put("Turn", player.getGame().getOtherPlayer(player).getPlayer().getName());
        else
            scoreMap.put("Turn", player.getPlayer().getName());
        setScores(scoreMap);

        Score beginScore = objective.getScore(ChatColor.DARK_GRAY + "┏ " + ChatColor.GRAY + "Begin Turn");
        beginScore.setScore(8);
        Score moveScore;
        if (player.getState().equals(State.MOVE_STEP) || player.getState().equals(State.MOVING))
            moveScore = objective.getScore(ChatColor.DARK_GRAY + "┣ " + ChatColor.GOLD + "Move Step");
        else
            moveScore = objective.getScore(ChatColor.DARK_GRAY + "┃ " + ChatColor.GRAY + "Move Step");
        moveScore.setScore(7);
        Score attackScore;
        if (player.getState().equals(State.ATTACK_STEP) || player.getState().equals(State.ATTACKING))
            attackScore = objective.getScore(ChatColor.DARK_GRAY + "┣ " + ChatColor.GOLD + "Attack Step");
        else
            attackScore = objective.getScore(ChatColor.DARK_GRAY + "┃ " + ChatColor.GRAY + "Attack Step");
        attackScore.setScore(6);
        Score playScore;
        if (player.getState().equals(State.PLAY_STEP))
            playScore = objective.getScore(ChatColor.DARK_GRAY + "┣ " + ChatColor.GOLD + "Play Step");
        else
            playScore = objective.getScore(ChatColor.DARK_GRAY + "┃ " + ChatColor.GRAY + "Play Step");
        playScore.setScore(5);
        Score endScore = objective.getScore(ChatColor.DARK_GRAY + "┗ " + ChatColor.GRAY + "End Turn");
        endScore.setScore(4);

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

        this.objective = board.registerNewObjective("ACard-1", "dummy",
                ChatColor.YELLOW + "" + ChatColor.BOLD + "A CARD");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.scoreValue = scores.size();
        scores.forEach((name, value) -> {
            scoreValue -= 1;
            addScore(scoreValue, name, value);
        });

    }
}
