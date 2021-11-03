package me.zeepic.cardgame.elements;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import me.zeepic.cardgame.events.MonsterActionEvent;

public class SimpleEvents {

    static {
        Skript.registerEvent("Monster Action", SimpleEvent.class, MonsterActionEvent.class, "monster (begin turn|move|attack|damaged|play|death|end turn) action")
                .description("Called when one of a monster's actions are triggered. These are when your turn begins/ends, when it attacks, moves, dies, is damaged, or played.")
                .examples("on monster attack:", "\tmonster's name is \"Reciprocate\"", "\tset {_d} to monster's damage", "\tdeal {_d} damage to monster");
//        Skript.registerEvent("Spell Play", SimpleEvent.class, SpellPlayEvent.class, "spell play")
//                .description("Called whenever a spell is played.")
//                .examples("on spell play:", "\t spell's name is \"Fireball\"", "\t set {_d} to spell's owner's casting power", "\t deal {_d} damage to spell's owner's opponent");

    }

}
