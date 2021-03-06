package me.zeepic.cardgame.cards;

import lombok.Getter;
import lombok.Setter;
import me.zeepic.cardgame.enums.*;
import me.zeepic.cardgame.events.MonsterActionEvent;
import me.zeepic.cardgame.game.BoardLocation;
import me.zeepic.cardgame.game.CardGamePlayer;
import me.zeepic.cardgame.util.Config;
import me.zeepic.cardgame.util.Item;
import me.zeepic.cardgame.util.Util;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;


public abstract class Monster extends Card implements InventoryHolder {

    @Getter @Setter private ArmorStand armorStand;
    @Getter @Setter private int health;
    @Getter @Setter private int damage;
    @Getter @Setter private int speed;
    @Getter @Setter private boolean attackedThisTurn;
    @Getter private final int maxHealth;
    @Getter private final int maxDamage;
    @Getter private final int maxSpeed;
    @Getter private BoardLocation location;
    @Getter private final static Vector armorStandOffset = new Vector(0.5, 1, 0.5);
    @Getter private final String actionText;

    protected Monster(int health, int damage, int speed, int cost, String name, Material icon, CardRarity rarity, CardSubtype subType, String lore, CardGamePlayer owner, String actionText) {
        super(cost, name, icon, rarity, CardType.MONSTER, subType, lore, owner);
        this.maxHealth = health;
        this.maxDamage = damage;
        this.maxSpeed = speed;
        this.speed = speed;
        this.damage = damage;
        this.health = health;
        this.actionText = actionText;
    }

    protected void spawnPhysicalCard(BoardLocation boardLocation, Team team) {

        if (boardLocation.getWorld() == null)
            return;

        setLocation(boardLocation);
        boardLocation.setDirection(Team.getVector(team));
        armorStand = (ArmorStand) boardLocation.getWorld().spawnEntity(
                boardLocation.clone().add(getArmorStandOffset()),
                EntityType.ARMOR_STAND);
        armorStand.setArms(true);
        armorStand.setBasePlate(false);
        armorStand.setSmall(true);
        armorStand.setCustomNameVisible(true);
        updateArmorStand();

        EntityEquipment equipment = armorStand.getEquipment();
        if (equipment == null)
            return;
        equipment.setHelmet(new Item(Material.LEATHER_HELMET)
                .colored(Team.colorOf(team)));
        equipment.setChestplate(new Item(Material.LEATHER_CHESTPLATE).colored(Team.colorOf(team)));
        equipment.setLeggings(new Item(Material.LEATHER_LEGGINGS)
                .colored(Team.colorOf(team)));
        equipment.setBoots(new Item(Material.LEATHER_BOOTS)
                .colored(Team.colorOf(team)));

    }

    protected void moveCard(Location location) {
        if (getArmorStand() == null)
            return;
        getArmorStand().teleport(
                location.clone()
                        .add(getArmorStandOffset())
                        .setDirection(Team.getVector(
                                getOwner().getTeam())
                        )
        );
        Bukkit.getPluginManager().callEvent(new MonsterActionEvent(this, Action.MOVE));
        onMove(); // TODO: remove this method
        // TODO make it move slowly
    }

    public void updateArmorStand() {
        updateItem();
        if (getArmorStand() == null)
            return;
        String separator = Config.getString("cards.name.values_separator");
        armorStand.setCustomName(getName() + "  "
                + ChatColor.RED + getHealth() + separator
                + ChatColor.DARK_RED + getDamage() + separator
                + ChatColor.DARK_AQUA + getSpeed());
        armorStand.setMetadata("item", new FixedMetadataValue(getOwner().getGame().getPlugin(), this));

    }

    @Override
    public void play(CardGamePlayer gamePlayer, BoardLocation location) {
        Bukkit.getPluginManager().callEvent(new MonsterActionEvent(this, Action.PLAY));
        spawnPhysicalCard(location, getOwner().getTeam());
        gamePlayer.getPlayedCards().add(this);
        onPlayAction(); // TODO: remove this method
        getOwner().getPlayer().sendMessage(ChatColor.GREEN + "You played a " + getName() + ".");
    }

    public abstract void onMove();
    public abstract void onDeath();
    public abstract void onAttack();
    public abstract void onDamage();
    public abstract void onEndTurn();
    public abstract void onBeginTurn();

    @Override
    protected void updateItem() {

        String cardType = Util.titleCase(
                getType().toString().toLowerCase()
                        + " "
                        + getSubType().toString().toLowerCase()
        );
        String itemLore;
        if (actionText == null) {
            itemLore = String.format(Config.getString("cards.item_lore.basic_monster"),
                    cardType,
                    getHealth(),
                    getDamage(),
                    getSpeed(),
                    getRarity().toString(),
                    getLore()
            );
        } else {
            itemLore = String.format(Config.getString("cards.item_lore.action_monster"),
                    cardType,
                    getHealth(),
                    getDamage(),
                    getSpeed(),
                    actionText,
                    getRarity().toString(),
                    getLore()
            );
        }
        getItem().setLore(Item.loreFromString(itemLore));
    }

    @Override
    public Inventory getInventory() { // when you punch the armor stand

        Inventory inv = Bukkit.createInventory(this, InventoryType.HOPPER, getName());

        Location location = getArmorStand().getLocation();
        inv.setItem(0, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        Vector boardPositionOffset = Config.getVector("board.position_offset");
        inv.setItem(1, new Item(Material.VINE,
                String.format(Config.getString("items.lore.board_position"),
                        location.getBlockX() + boardPositionOffset.getBlockX(),
                        location.getBlockZ() + boardPositionOffset.getBlockX()
                )
        ));
        inv.setItem(2, getItem());

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        Player player = getOwner().getPlayer();
        assert meta != null;
        meta.setDisplayName(ChatColor.WHITE + player.getName());
        meta.setOwningPlayer(player);
        skull.setItemMeta(meta);
        inv.setItem(3, skull);

        inv.setItem(4, new Item(Material.GRAY_STAINED_GLASS_PANE, " "));

        return inv;

    }

    public void setLocation(BoardLocation location) {
        this.location = location;
        moveCard(location);
    }

    public void kill() {

        if (getArmorStand() == null)
            return;
        onDeath(); // TODO: remove this method
        Bukkit.getPluginManager().callEvent(new MonsterActionEvent(this, Action.DEATH));
        getArmorStand().remove();
        getOwner().getPlayedCards().remove(this);

    }

}
