package me.zeepic.cardgame.game;

import lombok.Getter;
import lombok.Setter;
import me.zeepic.cardgame.cards.Card;
import me.zeepic.cardgame.cards.Monster;
import me.zeepic.cardgame.enums.State;
import me.zeepic.cardgame.enums.Team;
import me.zeepic.cardgame.items.AttackCard;
import me.zeepic.cardgame.items.MoveCard;
import me.zeepic.cardgame.items.NextStep;
import me.zeepic.cardgame.util.CountdownTimer;
import me.zeepic.cardgame.util.ObjectiveManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class CardGamePlayer {

    @Getter private static final int[] cardSlots = new int[] {
            4, 5, 6, 7, 8
    };
    @Getter @Setter private Monster       using;
    @Getter @Setter private Card[]        cards = new Card[5];
    @Getter @Setter private Team          team;
    @Getter @Setter private int           health = 20;
    @Getter @Setter private int           turnTaskId;
    @Getter private final   List<Monster> playedCards = new ArrayList<>();
    @Getter private final   GameManager   game;
    @Getter private final   boolean       player1;
    @Getter private final   Player        player;
    @Getter private final   Location      enchantingTable;
    @Getter private         State         state = State.WAITING;
    @Getter private         int           castingPower;

    @Getter @Setter private CountdownTimer timer;
    @Getter private final ObjectiveManager scoreboard = new ObjectiveManager();

    public CardGamePlayer(Player player, boolean isPlayer1, GameManager game, Location enchantingTable) {

        this.player = player;
        this.player1 = isPlayer1;
        this.game = game;
        this.enchantingTable = enchantingTable;
        PlayerInventory inv = player.getInventory();

        inv.addItem(new MoveCard());
        inv.addItem(new AttackCard());
        inv.addItem(new NextStep());

    }

    public void addCard(Class<? extends Card> clazz) {

        Card cardInstance;
        try {
            cardInstance = clazz.getDeclaredConstructor(this.getClass()).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }

        for (int i = 0; i < getCards().length; i++) {
            if (getCards()[i] == null) {
                getCards()[i] = cardInstance;
                updateCards();
                return;
            }
        }

    }

    public void removeCard(int index) { // after you play it
        getCards()[index] = null;
        updateCards();
    }

    private void updateCards() {

        Inventory inv = getPlayer().getInventory();
        for (int i = 0; i < getCards().length; i++) {
            Card card = getCards()[i];
            if (card != null)
                inv.setItem(getCardSlots()[i], card.getItem());
            else
                inv.setItem(getCardSlots()[i], new ItemStack(Material.AIR));
        }

    }

    public void addCastingPower(int amount) {
        castingPower += amount;
        scoreboard.updateScoreboard(this);
    }

    public void subtractHealth(int damage) {
        health -= damage;
        scoreboard.updateScoreboard(this);
        if (health <= 0) {
            getGame().stopGame();
            getPlayer().sendMessage("You lost!");
            return;
        }
        getPlayer().sendMessage("You lost " + damage + " health!");
    }

    public boolean hasMovableCards() {
        return getPlayedCards().stream().anyMatch(monster -> monster.getSpeed() > 0);
    }

    public boolean hasBloodthirstyCards() {
        List<Monster> monsters = getGame().getOtherPlayer(this).getPlayedCards();
        return monsters.stream().anyMatch(this::isMonsterAdjacent);
    }

    private boolean isMonsterAdjacent(Monster monster) {
        final Location monsterLoc = monster.getLocation();
        final Location enchantingLoc = monster.getOwner().getEnchantingTable();
        return getPlayedCards().stream().anyMatch(myMonster ->
                (myMonster.getLocation().distance(monsterLoc) <= 1.0
                    || myMonster.getLocation().distance(enchantingLoc) <= 1.0
                ) && !myMonster.isAttackedThisTurn()
        );
    }

    public boolean hasPlayableCards() {
        Arrays.stream(getCards()).filter(Objects::nonNull).forEach(card -> Bukkit.broadcastMessage(String.valueOf(card.getCost())));
        return Arrays.stream(getCards()).filter(Objects::nonNull).anyMatch(card -> card.getCost() <= getCastingPower());
    }

    public void setState(State state) {
        this.state = state;
        scoreboard.updateScoreboard(this);
    }

}
