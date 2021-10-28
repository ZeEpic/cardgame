package me.zeepic.cardgame;

import com.mongodb.*;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import lombok.Getter;
import me.zeepic.cardgame.cards.Card;
import me.zeepic.cardgame.cards.TestCard;
import me.zeepic.cardgame.commands.*;
import me.zeepic.cardgame.enums.Rank;
import me.zeepic.cardgame.game.CardGamePlayer;
import me.zeepic.cardgame.game.GameManager;
import me.zeepic.cardgame.game.PlayerDocument;
import me.zeepic.cardgame.listeners.*;
import me.zeepic.cardgame.subscribers.PlayerDocumentSubscriber;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Main extends JavaPlugin {

    @Getter private final List<GameManager> games = new ArrayList<>();

    @Getter private FileConfiguration myConfig;
    @Getter private static Main instance;
    @Getter private static final Map<OfflinePlayer, PlayerDocument> playerDocuments = new HashMap<>();

    @Getter private static final Map<String, Class<? extends Card>> cardMap = new HashMap<>();

    public void makeGame(Player player1, Player player2) {
        GameManager game = new GameManager(this, new Player[] {player1, player2});
        games.add(game);
    }

    public CardGamePlayer getPlayer(Player player) {

        for (GameManager game : getGames()) {
            CardGamePlayer possiblePlayer = game.hasPlayer(player);
            if (possiblePlayer != null)
                return possiblePlayer;
        }
        return null;

    }

    private void addCommand(String name, CommandExecutor executor) {

        PluginCommand command = getCommand(name);
        if (command == null)
            return;
        command.setExecutor(executor);
        Bukkit.getLogger().info("Command " + name + " added.");

    }

    private void enableListeners() {

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ClickListener(), this);
        pluginManager.registerEvents(new WorldListener(), this);
        pluginManager.registerEvents(new EntityInteractListener(this), this);
        pluginManager.registerEvents(new InteractListener(this), this);
        pluginManager.registerEvents(new JoinListener(this), this);
        pluginManager.registerEvents(new BlockListener(), this);

    }

    @Override
    public void onEnable() {

        saveDefaultConfig();
        myConfig = getConfig();
        instance = this;

        enableListeners();
        addCommand("test", new TestCommand(this));
        addCommand("killcards", new KillCards(this));
        addCommand("start", new StartGame(this));
        addCommand("temp", new TempCommand(this));
        addCommand("stats", new StatsCommand(this));

        for (World world : getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType().equals(EntityType.ARMOR_STAND)
                        && entity.getMetadata("item").size() != 0)
                    entity.remove();
            }
        }

        enableDB();

        cardMap.put("TestCard", TestCard.class);

    }

    private void enableDB() {
        ConnectionString connectionString = new ConnectionString("mongodb+srv://zeepic:it5fyvXZZAd2ZiLh@mhacard.trcd7.mongodb.net/ACard?retryWrites=true&w=majority");
        Bukkit.broadcastMessage(String.valueOf(connectionString.isDirectConnection()));
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("ACard");
        MongoCollection<Document> playerCollection = database.getCollection("players");
        playerCollection.find().subscribe(new PlayerDocumentSubscriber());
        // this will look at each player document in the DB and put it into memory as a PlayerDocument object, which can be fetched

    }

    public static PlayerDocument fetchPlayerDocument(OfflinePlayer player) {
        return playerDocuments.getOrDefault(player, null);
    }

    @Override
    public void onDisable() {

        getServer().broadcastMessage("Reloading main plugin...");

    }
}
