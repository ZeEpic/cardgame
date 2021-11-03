package me.zeepic.cardgame.listeners;

import lombok.Getter;
import me.zeepic.cardgame.Main;
import me.zeepic.cardgame.enums.State;
import me.zeepic.cardgame.game.CardGamePlayer;
import me.zeepic.cardgame.util.Config;
import me.zeepic.cardgame.util.ListenerUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class InteractListener extends ListenerUtil implements Listener {

    private final Main plugin;
    @Getter private static final int invSlotOffset = Config.getInt("items.hotbar.cards_offset");

    public InteractListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK)
                && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if (event.getItem() == null
                || canPerformAction(player))
            return;

        event.setCancelled(true);

        CardGamePlayer gamePlayer = plugin.getPlayer(player);
        if (gamePlayer == null) {
            player.sendMessage("You aren't in a game.");
            return;
        }

        final int itemSlot = player.getInventory().getHeldItemSlot();
        final ArmorStandInteractions interactions = new ArmorStandInteractions();
        Location location = Objects.requireNonNull(event.getClickedBlock()).getLocation();

        for (int cardSlot : CardGamePlayer.getCardSlots()) {
            if (itemSlot == cardSlot) {
                if (gamePlayer.getState().equals(State.PLAY_STEP))
                    interactions.playCard(gamePlayer, cardSlot, location);
                else
                    player.sendMessage("You must be in the play step during your turn to play a card.");
                return;
            }
        }

        if (itemSlot == Config.getInt("items.hotbar.move_cards")) {
            interactions.moveArmorStand(gamePlayer, location);
        } else if (itemSlot == Config.getInt("items.hotbar.attack_cards")) {
            if (event.getClickedBlock().getType().equals(Material.valueOf(Config.getString("player1.base_block")))) {
                interactions.attackEnchantmentTable(gamePlayer, location.subtract(0, 1, 0));
            }
        } else if (itemSlot == Config.getInt("items.hotbar.next_step")) {
            interactions.forceNextPhase(gamePlayer);
        }

    }

}
