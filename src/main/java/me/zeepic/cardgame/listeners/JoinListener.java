package me.zeepic.cardgame.listeners;

import me.zeepic.cardgame.Main;
import me.zeepic.cardgame.game.GameManager;
import me.zeepic.cardgame.util.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.function.Predicate;

public class JoinListener implements Listener {

    private final Main plugin;

    public JoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        player.sendMessage(Config.getString("welcome_message"));

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Predicate<GameManager> predicate = game -> game.hasPlayer(event.getPlayer()) != null;
        plugin.getGames()
                .stream()
                .filter(predicate)
                .forEach(GameManager::stopGame);
        plugin.getGames().removeIf(predicate);
    }

}
