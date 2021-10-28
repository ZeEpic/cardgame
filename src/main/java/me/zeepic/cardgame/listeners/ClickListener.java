package me.zeepic.cardgame.listeners;

import me.zeepic.cardgame.cards.Monster;
import me.zeepic.cardgame.commands.StatsCommand;
import me.zeepic.cardgame.util.ListenerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class ClickListener extends ListenerUtil implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        InventoryHolder holder = event.getInventory().getHolder();

        if (holder == null)
            return;
        if (event.getCurrentItem() == null)
            return;

        Player player = (Player) event.getWhoClicked();

        if (holder.equals(player)) {
            if (canPerformAction(player))
                return;
            event.setCancelled(true);
        } else if (holder instanceof Monster) { // when you click on a physical card
            event.setCancelled(true);
        } else if (holder instanceof StatsCommand) {
            event.setCancelled(true);
        }

    }

}
