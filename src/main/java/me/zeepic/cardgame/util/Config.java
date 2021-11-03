package me.zeepic.cardgame.util;
import me.zeepic.cardgame.Main;
import org.bukkit.Material;
import org.bukkit.util.Vector;

public class Config {

    public static String getString(String path) {
        String target = Main.getInstance().getMyConfig().getString(path);
        assert target != null;
        return target.replace("\\n", "\n");
    }
    public static int getInt(String path) {
        return Main.getInstance().getMyConfig().getInt(path);
    }

    public static Vector getVector(String path) {
        String[] splitVector = getString(path).split(";");
        return new Vector(
                Double.parseDouble(splitVector[0]),
                Double.parseDouble(splitVector[1]),
                Double.parseDouble(splitVector[2])
        );
    }

    public static Material getMaterial(String path) {
        return Material.valueOf(getString(path));
    }
}
