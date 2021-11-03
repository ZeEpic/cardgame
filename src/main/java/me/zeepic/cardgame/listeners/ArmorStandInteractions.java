package me.zeepic.cardgame.listeners;

import me.zeepic.cardgame.cards.Card;
import me.zeepic.cardgame.cards.Monster;
import me.zeepic.cardgame.enums.Action;
import me.zeepic.cardgame.enums.State;
import me.zeepic.cardgame.events.MonsterActionEvent;
import me.zeepic.cardgame.game.BoardLocation;
import me.zeepic.cardgame.game.CardGamePlayer;
import me.zeepic.cardgame.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.List;

public class ArmorStandInteractions {

    public void beginMoveCard(CardGamePlayer player, Monster card) {

        if (card.getSpeed() <= 0) {
            player.getPlayer().sendMessage("This card is exhausted. Wait until next turn for it's speed to recharge.");
            return;
        }
        if (!player.getPlayer().equals(card.getOwner().getPlayer())) {
            player.getPlayer().sendMessage("You can only move your own cards.");
            return;
        }

        player.setState(State.MOVING);
        player.setUsing(card);
        player.getPlayer().sendMessage("Click an adjacent block to move this card.");

    }

    public void moveArmorStand(CardGamePlayer player, Location clickLocation) {

        Monster using = player.getUsing();
        final boolean canMoveArmorStand = player.getState().equals(State.MOVING)
                && using != null
                && using.getLocation().distance(clickLocation) <= 1.0
                && !using.getLocation().equals(clickLocation)
                && player.getGame().getOverlappingMonsters(clickLocation).isEmpty();
        if (!canMoveArmorStand)
            return;

        // to move the armor stand after you've selected where it should go
        using.setLocation(player.getGame().getBoardLocation(clickLocation));
        Location location = using.getLocation();
        using.setSpeed(using.getSpeed() - 1);
        using.updateArmorStand();
        player.getPlayer().sendMessage("You've moved the armor stand to "
                + ChatColor.GOLD + location.getBlockX() + ", "
                + location.getBlockZ() + ChatColor.WHITE + ".");
        player.setUsing(null);

        nextPhase(player);

    }

    public void forceNextPhase(CardGamePlayer player) {

        if (player.getState().equals(State.WAITING))
            return;
        nextPhase(player);

    }

    private void endTurn(CardGamePlayer player) {
        player.getGame().endTurn(player);
        Util.sendSimpleTitle(player.getPlayer(), "Opponent's Turn");
    }

    public void playCard(CardGamePlayer player, int cardSlot, Location clickLocation) {

        BoardLocation boardLocation = player.getGame().getBoardLocation(clickLocation);
        if (boardLocation == null) {
            player.getPlayer().sendMessage("You can't place a card here.");
            return;
        }
        if (!boardLocation.isCardPlaceableBy(player)) {
            player.getPlayer().sendMessage("You can't place a card here.");
            return;
        }
        List<Monster> overlappingMonsters = player.getGame().getOverlappingMonsters(boardLocation);
        if (overlappingMonsters.size() > 0) {
            player.getPlayer().sendMessage("You can't place a card here.");
            return;
        }

        final int realSlot = cardSlot - InteractListener.getInvSlotOffset();
        final Card card = player.getCards()[realSlot];
        if (card == null)
            return;
        if (card.getCost() > player.getCastingPower()) {
            player.getPlayer().sendMessage("You don't have enough casting power.");
            return;
        }
        player.removeCard(realSlot);
        player.addCastingPower(-card.getCost());
        card.play(player, boardLocation);

        nextPhase(player);

    }

    public void beginAttack(CardGamePlayer player, Monster card) {
        if (!card.getOwner().getPlayer().equals(player.getPlayer()))
            return; // you can only attack using your own cards
        player.setState(State.ATTACKING);
        player.setUsing(card);
        player.getPlayer().sendMessage("Click an adjacent enemy armor stand to attack!");
    }

    public void attackArmorStand(CardGamePlayer player, Monster card) {

        if (card.getOwner().getPlayer().equals(player.getPlayer())) {
            player.getPlayer().sendMessage("You can't attack your own monsters!");
            return;
        }

        Monster using = player.getUsing();
        if (using == null)
            return;
        final boolean canAttackArmorStand = !using.isAttackedThisTurn()
                && using.getLocation()
                .distance(card.getLocation())
                <= 1.0;
        if (!canAttackArmorStand)
            return;

        card.setHealth(card.getHealth() - using.getDamage());
        using.onAttack(); // TODO: remove this method
        Bukkit.getPluginManager().callEvent(new MonsterActionEvent(using, Action.ATTACK));
        if (card.getHealth() <= 0) {
            card.kill();
        } else {
            card.onDamage(); // TODO: remove this method
            Bukkit.getPluginManager().callEvent(new MonsterActionEvent(card, Action.DAMAGED));
            using.updateArmorStand();
        }
        Location location = card.getLocation();
        player.getPlayer().sendMessage("You attacked the armor stand at "
                + ChatColor.GOLD + location.getBlockX() + ", "
                + location.getBlockZ() + ChatColor.WHITE + " for " + using.getDamage() + " damage.");
        using.setAttackedThisTurn(true);
        player.setUsing(null);

        nextPhase(player);

    }

    public void attackEnchantmentTable(CardGamePlayer player, Location clickLocation) {

        if (clickLocation.equals(player.getEnchantingTable())) {
            player.getPlayer().sendMessage("You can't attack your own enchantment table!");
            return;
        }

        Monster using = player.getUsing();
        if (using == null)
            return;
        final boolean canAttackArmorStand = !using.isAttackedThisTurn()
                && using.getLocation()
                .distance(clickLocation)
                <= 1.0;
        if (!canAttackArmorStand)
            return;
        player.getPlayer().sendMessage("You attacked your opponent's enchantment table for " + using.getDamage() + " damage!");
        player.getGame().getOtherPlayer(player).subtractHealth(using.getDamage());
        using.setAttackedThisTurn(true);
        player.setUsing(null);

        nextPhase(player);

    }

    public void nextPhase(CardGamePlayer player) {
        if (player.hasMovableCards() && player.getState().equals(State.MOVING)) {
            player.setState(State.MOVE_STEP);
            Util.sendSimpleTitle(player.getPlayer(), "Move Step");
        } else if (player.hasBloodthirstyCards() && !player.getState().equals(State.PLAY_STEP)) {
            player.setState(State.ATTACK_STEP);
            Util.sendSimpleTitle(player.getPlayer(), "Attack Step");
        } else if (player.hasPlayableCards()) {
            player.setState(State.PLAY_STEP);
            Util.sendSimpleTitle(player.getPlayer(), "Play Step");
        } else {
            endTurn(player);
        }
    }

}
