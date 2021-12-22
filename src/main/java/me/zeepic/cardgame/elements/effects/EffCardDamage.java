package me.zeepic.cardgame.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;

public class EffCardDamage extends Effect {

    static {
        Skript.registerEffect(EffCardDamage.class, "place monument at %location%");
    }

    private Expression<Location> location;

    @Override
    protected void execute(Event e) {
        Location loc = location.getSingle(e);
        if (loc == null)
            return;
        loc.getBlock().setType(Material.OAK_WOOD);
        loc.getBlock().getRelative(BlockFace.UP).setType(Material.OAK_FENCE);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Places a little monument at the location expression: " + location.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.location = (Expression<Location>) expressions[0];
        return true;
    }

}
