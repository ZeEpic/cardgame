package me.zeepic.cardgame.game;

import lombok.Getter;
import lombok.Setter;
import me.zeepic.cardgame.Main;
import me.zeepic.cardgame.cards.Monster;
import me.zeepic.cardgame.cards.TestCard;
import me.zeepic.cardgame.enums.State;
import me.zeepic.cardgame.enums.Team;
import me.zeepic.cardgame.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

public final class GameManager {

    @Getter @Setter private World world;
    @Getter @Setter private Location start = null;
    @Getter private CardGamePlayer player1;
    @Getter private CardGamePlayer player2;
    @Getter private final Main plugin;
    @Setter private GameBoard board;
    @Getter private static final Vector player1Base = Config.getVector("player1.base");
    @Getter private static final Vector player2Base = Config.getVector("player2.base");

    public GameManager(Main plugin, Player[] players) {
        this.plugin = plugin;
        startGame(players);
    }

    public void startGame(Player[] players) {

        Player p1 = players[0];
        Player p2 = players[1];
        p1.getInventory().clear();
        p2.getInventory().clear();
        setWorld(Bukkit.getWorld("world"));
        player1 = new CardGamePlayer(p1, true, this,
                new Location(getWorld(), 0,0,0).add(player1Base)
        );
        player2 = new CardGamePlayer(p2, false, this,
                new Location(getWorld(), 0,0,0).add(player2Base)
        );
        start = new Location(getWorld(), 0, BoardLocation.getBoardOffset().getY() + 1, 0);
        // TODO: give players starting hand
        p1.teleport(start);
        p2.teleport(start);
        p2.sendMessage("It's your turn!");
        p1.sendMessage("The game has started!");
        getPlayer1().setTeam(Team.BLACK);
        getPlayer2().setTeam(Team.WHITE);
        getPlayer1().setState(State.WAITING);
        getPlayer2().setState(State.PLAY_STEP);
        setBoard(new GameBoard(getWorld()));
        player1.getScoreboard().updateScoreboard(player1);
        player2.getScoreboard().updateScoreboard(player2);

    }

    public BoardLocation getBoardLocation(Location location) {
        return board.getWithLocation(location);
    }

    public void stopGame() {

        // TODO: send players to lobby world
        // TODO: delete old world
        // TODO: Game completion logic
        getPlayer1().getPlayer().getInventory().clear();
        getPlayer2().getPlayer().getInventory().clear();
        // TODO: update stats with a config file

    }

    public CardGamePlayer hasPlayer(Player player) {
        if (getPlayer1().getPlayer().equals(player))
            return getPlayer1();
        if (getPlayer2().getPlayer().equals(player))
            return getPlayer2();
        return null;
    }

    public void endTurn(CardGamePlayer previousTurn) {
        previousTurn.setState(State.WAITING);
        previousTurn.getPlayedCards().forEach(Monster::onEndTurn);
        CardGamePlayer enemyPlayer = getOtherPlayer(previousTurn);
        if (enemyPlayer.getPlayedCards().size() > 0) // pass the turn, now the enemy can move their monsters
            enemyPlayer.setState(State.MOVE_STEP);
        else if (enemyPlayer.getCards().length > 0)
            enemyPlayer.setState(State.PLAY_STEP);
        else {
            endTurn(enemyPlayer);
            return;
        }
        enemyPlayer.addCard(TestCard.class); // TODO: this will be a card from their deck
        enemyPlayer.addCastingPower(1);
        enemyPlayer.getPlayer().sendMessage("It is now your turn.");
        //enemyPlayer.startTurnTimer();
        previousTurn.getPlayedCards().forEach(monster -> {
            monster.setSpeed(monster.getMaxSpeed());
            monster.setHealth(monster.getMaxHealth());
            monster.updateArmorStand();
            monster.setAttackedThisTurn(false);
            monster.onBeginTurn();
        });
        previousTurn.getScoreboard().updateScoreboard(previousTurn);
        enemyPlayer.getScoreboard().updateScoreboard(enemyPlayer);
    }

    public List<Monster> getPlayedCards() {

        List<Monster> playedCards = getPlayer1().getPlayedCards();
        playedCards.addAll(getPlayer2().getPlayedCards());
        return playedCards;

    }

    public CardGamePlayer getOtherPlayer(CardGamePlayer player) {
        return player.isPlayer1() ? getPlayer2() : getPlayer1();
    }

    public List<Monster> getOverlappingMonsters(Location boardLocation) {
        return getPlayedCards()
            .stream()
            .filter(monster ->
                    monster.getLocation()
                            .equals(boardLocation)) // checks if they are in the same location
            .collect(Collectors.toList());
    }
}
