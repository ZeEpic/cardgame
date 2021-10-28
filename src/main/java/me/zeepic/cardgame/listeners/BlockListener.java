package me.zeepic.cardgame.listeners;

import me.zeepic.cardgame.util.ListenerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener extends ListenerUtil implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (canPerformAction(player))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (canPerformAction(player))
            return;
        event.setCancelled(true);
    }

}
