package me.zeepic.cardgame.listeners;

import me.zeepic.cardgame.Main;
import me.zeepic.cardgame.cards.Monster;
import me.zeepic.cardgame.enums.State;
import me.zeepic.cardgame.game.CardGamePlayer;
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
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class EntityInteractListener extends ListenerUtil implements Listener {

    public static final Location spawnLocation = new Location(Bukkit.getWorld("world"), 0, 67, -2.5);

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

        if (!event.getEntityType()
                .equals(EntityType.PLAYER))
            return;

        event.setCancelled(true);
        if (!event.getCause()
                .equals(EntityDamageEvent.DamageCause.VOID))
            return;

        Player player = (Player) event.getEntity();
        player.teleport(spawnLocation);
        
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

        if (slot == 0) {
            // move armor stand
            if (!player.getState().equals(State.MOVE_STEP)
                    && !player.getState().equals(State.MOVING)) {
                player.getPlayer().sendMessage("You must be in the move step during your turn to move a card.");
                return;
            }
            if (card.getSpeed() <= 0
                    || !player.getPlayer().equals(card.getOwner().getPlayer()))
                return;
            player.setState(State.MOVING);
            player.setUsing(card);
            damager.sendMessage("Click an adjacent block to move this card.");
        } else if (slot == 1) {
            // attack!
            ArmorStandInteractions interactions = new ArmorStandInteractions();
            if (player.getState().equals(State.ATTACK_STEP)) {
                interactions.beginAttack(player, card);
            } else if (player.getState().equals(State.ATTACKING)) {
                if (player.getUsing() == null)
                    interactions.beginAttack(player, card);
                else
                    interactions.attackArmorStand(player, card);
            } else
                player.getPlayer().sendMessage("You must be in the attack step during your turn to move a card.");

        } else {
            damager.openInventory(card.getInventory());
        }
    }

}
