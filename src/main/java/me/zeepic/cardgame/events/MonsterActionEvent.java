package me.zeepic.cardgame.events;

import lombok.Getter;
import lombok.Setter;
import me.zeepic.cardgame.cards.Monster;
import me.zeepic.cardgame.enums.Action;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MonsterActionEvent extends Event implements Cancellable {

    @Getter private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Setter @Getter private boolean isCancelled;

    @Getter private final Monster monster;
    @Getter private final Action actionType;

    public MonsterActionEvent(Monster monster, Action actionType) {
        this.monster = monster;
        this.actionType = actionType;
        this.isCancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

}
