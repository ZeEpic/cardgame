package me.zeepic.cardgame.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import me.zeepic.cardgame.enums.Action;
import me.zeepic.cardgame.events.MonsterActionEvent;
import org.bukkit.event.Event;

public class EvtMonsterAction extends SkriptEvent {

    static {
        String pattern = "monster (0¦begin turn|1¦move|2¦attack|3¦damaged|4¦play|5¦death|6¦end turn) [action]";
        Skript.registerEvent("Monster Action", EvtMonsterAction.class, MonsterActionEvent.class, pattern)
                .description("Called when one of a monster's actions are triggered. These are when your turn begins/ends, when it attacks, moves, dies, is damaged, or played.")
                .examples("on monster attack action:", "\tmonster's name is \"Reciprocate\"", "\tset {_d} to monster's damage", "\tdeal {_d} damage to monster");
//        EventValues.registerEventValue(SimpleEvent.class, .class, new Getter<Number, EntityAirChangeEvent>() {
//            @Override
//            public Number get(EntityAirChangeEvent e) {
//                return e.getAmount();
//            }
//        }, 0);

    }

    private Action action = null;

    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        action = switch (parseResult.mark) {
            case 0 -> Action.BEGIN_TURN;
            case 1 -> Action.MOVE;
            case 2 -> Action.ATTACK;
            case 3 -> Action.DAMAGED;
            case 4 -> Action.PLAY;
            case 5 -> Action.DEATH;
            case 6 -> Action.END_TURN;
            default -> throw new IllegalStateException("Unexpected value: " + parseResult.mark);
        };
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (e instanceof MonsterActionEvent actionEvent) {
            return action == actionEvent.getActionType();
        }
        return false;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "Monster event for " + action.toString() + " action.";
    }
}
