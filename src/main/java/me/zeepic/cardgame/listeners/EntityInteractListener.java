package me.zeepic.cardgame.listeners;

import me.zeepic.cardgame.Main;
import me.zeepic.cardgame.cards.Monster;
import me.zeepic.cardgame.enums.State;
import me.zeepic.cardgame.game.CardGamePlayer;
import me.zeepic.cardgame.util.Config;
import me.zeepic.cardgame.util.ListenerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class EntityInteractListener extends ListenerUtil implements Listener {

    public static final Location spawnLocation = new Location(Bukkit.getWorld("world"), 1, 66, -12.5, -162, 4   );

    private final Main plugin;

    public EntityInteractListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onArmorStandInteract(PlayerArmorStandManipulateEvent event) {

        Player player = event.getPlayer();
        if (canPerformAction(player))
            return;

        openCardViewer(event.getRightClicked(), event.getPlayer());

        event.setCancelled(true);

    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!event.getEntityType().equals(EntityType.PLAYER))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getPlayer().getLocation().getY() < 50)
            event.getPlayer().teleport(spawnLocation);
    }

    @EventHandler
    public void onPlayerHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
        ((Player) event.getEntity()).setFoodLevel(20);
    }

    @EventHandler
    public void onArmorStandHit(EntityDamageByEntityEvent event) {

        Entity damager = event.getDamager();
        if (!event.getEntityType().equals(EntityType.ARMOR_STAND))
            return;
        if (!damager.getType().equals(EntityType.PLAYER))
            return;

        Player player = (Player) event.getDamager();
        openCardViewer((ArmorStand) event.getEntity(), player);
        event.setCancelled(true);

    }

    private void openCardViewer(ArmorStand damaged, Player damager) {

        int slot = damager.getInventory().getHeldItemSlot();

        List<MetadataValue> values = damaged.getMetadata("item");
        if (values.size() == 0)
            return;

        Monster card = (Monster) values.get(0).value();

        CardGamePlayer player = plugin.getPlayer(damager);
        if (player == null)
            return;

        ArmorStandInteractions interactions = new ArmorStandInteractions();
        if (slot == Config.getInt("items.hotbar.move_cards")) {
            // move armor stand
            if (!player.getState().equals(State.MOVE_STEP)
                    && !player.getState().equals(State.MOVING)) {
                player.getPlayer().sendMessage("You must be in the move step during your turn to move a card.");
                return;
            }
            interactions.beginMoveCard(player, card);
        } else if (slot == Config.getInt("items.hotbar.attack_cards")) {
            // attack!
            if (card.isAttackedThisTurn()) {
                player.getPlayer().sendMessage("This card is exhausted, because it already attacked this turn.");
                return;
            }
            if (player.getState().equals(State.ATTACK_STEP)) {
                interactions.beginAttack(player, card);
            } else if (player.getState().equals(State.ATTACKING)) {
                if (player.getUsing() == null)
                    interactions.beginAttack(player, card);
                else
                    interactions.attackArmorStand(player, card);
            } else {
                player.getPlayer().sendMessage("You must be in the attack step during your turn to move a card.");
            }

        } else {
            damager.openInventory(card.getInventory());
        }
    }

}
