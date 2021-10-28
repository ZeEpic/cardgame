package me.zeepic.cardgame.commands;

import lombok.Getter;
import me.zeepic.cardgame.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CustomCommand implements CommandExecutor {

    @Getter private final Main plugin;
    @Getter protected final int requiredArgs;

    public CustomCommand(Main plugin, int requiredArgs) {
        this.plugin = plugin;
        this.requiredArgs = requiredArgs;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player))
            return true;

        if (args.length != getRequiredArgs())
            return false;

        return executeCommand((Player) sender, args);

    }

    protected abstract boolean executeCommand(Player sender, String[] args);

}
