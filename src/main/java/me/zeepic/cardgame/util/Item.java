package me.zeepic.cardgame.util;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Item extends ItemStack {

    private String name;
    private List<String> lore;

    public Item(Material material) {

        setType(material);
        setAmount(1);

    }

    public Item(Material material, String name) {

        setType(material);
        setName(ChatColor.WHITE + name);
        setAmount(1);

    }

    public Item(Material material, String name, int amount) {

        setType(material);
        setAmount(amount);
        setName(ChatColor.WHITE + name);

    }

    public Item(Material material, String name, String lore) {

        setType(material);
        setName(ChatColor.WHITE + name);
        setLore(loreFromString(lore));
        setAmount(1);

    }

    public Item(Material material, String name, String lore, int amount) {

        setType(material);
        setName(ChatColor.WHITE + name);
        setLore(loreFromString(lore));
        setAmount(amount);

    }

    public static ItemStack withName(ItemStack itemStack, String name) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static String getName(ItemStack currentItem) {

        ItemMeta meta = currentItem.getItemMeta();
        assert meta != null;
        return meta.getDisplayName();

    }

    public static List<String> formattedLore(List<String> lore, String[] replacement) {

        String formatted = stringFromLore(lore);
        formatted = String.format(formatted, (Object[]) replacement);
        return loreFromString(formatted);

    }

    public static String getLoreAsString(Item item) {

        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        return stringFromLore(Objects.requireNonNull(meta.getLore()));

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        ItemMeta meta = getItemMeta();
        assert meta != null;
        meta.setDisplayName(this.name);
        setItemMeta(meta);
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        if (lore == null || lore.isEmpty())
            return;
        List<String> newLore = new ArrayList<>();
        for (String line : lore) {
            newLore.add(ChatColor.GRAY + line);
        }
        this.lore = newLore;

        ItemMeta meta = getItemMeta();
        assert meta != null;
        meta.setLore(newLore);
        setItemMeta(meta);
    }

    public static List<String> loreFromString(String stringLore) {
        return new ArrayList<>(
                Arrays.asList(stringLore.split("\n"))
        );
    }

    public static String stringFromLore(List<String> lore) {

        if (lore.size() == 0)
            return " ";

        StringBuilder combinedValue = new StringBuilder();
        lore.forEach(line -> combinedValue.append(line).append("\n"));
        return combinedValue.toString();

    }

    public ItemStack withLore(List<String> lore) {
        ItemStack newItem = new ItemStack(getType(), getAmount());
        ItemMeta meta = newItem.getItemMeta();
        assert meta != null;
        meta.setLore(lore);
        meta.setDisplayName(getName());
        newItem.setItemMeta(meta);
        return newItem;
    }

    public void wrapLore(int maxLineLength) {

        String[] lore = stringFromLore(getLore()).split(" ");
        int lineCount = 0;
        StringBuilder newLine = new StringBuilder();
        List<String> newLore = new ArrayList<>();

        for (String line : lore) {
            lineCount += line.length() + 1; // +1 for the space char added
            newLine.append(line).append(" ");
            if (lineCount >= maxLineLength) {
                lineCount = 0;
                newLine = new StringBuilder();
                newLore.add(newLine.toString());
            }
        }

        setLore(newLore);

    }

    public Item colored(Color color) {

        LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
        assert meta != null;
        meta.setColor(color);
        setItemMeta(meta);

        return this;

    }

}
