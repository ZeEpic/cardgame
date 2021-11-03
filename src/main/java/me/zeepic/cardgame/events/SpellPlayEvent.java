package me.zeepic.cardgame.events;

import lombok.Getter;
import lombok.Setter;
import me.zeepic.cardgame.cards.Spell;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpellPlayEvent extends Event implements Cancellable {

    @Getter private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Setter @Getter private boolean isCancelled;

    @Getter private final Spell spell;

    public SpellPlayEvent(Spell spell) {
        this.spell = spell;
        this.isCancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

}
