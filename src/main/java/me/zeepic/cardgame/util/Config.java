package me.zeepic.cardgame.util;
import me.zeepic.cardgame.Main;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class Config {

    public static String getString(String path) {
        return Main.getInstance().getMyConfig().getString(path).replace("\\n", "\n");
    }
    public static int getInt(String path) {
        return Main.getInstance().getMyConfig().getInt(path);
    }

    public static Vector getVector(String path) {
        String[] splitVector = Main.getInstance().getMyConfig().getString(path).split(";");
        return new Vector(
                Double.parseDouble(splitVector[0]),
                Double.parseDouble(splitVector[1]),
                Double.parseDouble(splitVector[2])
        );
    }

    public static Material getMaterial(String path) {
        return Material.valueOf(Main.getInstance().getMyConfig().getString(path));
    }
}
