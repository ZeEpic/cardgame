package me.zeepic.cardgame.listeners;

import me.zeepic.cardgame.util.ListenerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldListener extends ListenerUtil implements Listener {

    @EventHandler
    public void onWeatherStart(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {

        Player player = event.getPlayer();
        if (canPerformAction(player))
            return;
        event.setCancelled(true);

    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

}
