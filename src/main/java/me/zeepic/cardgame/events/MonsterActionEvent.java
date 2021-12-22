package me.zeepic.cardgame.events;

import lombok.Getter;
import me.zeepic.cardgame.cards.Monster;
import me.zeepic.cardgame.enums.Action;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MonsterActionEvent extends Event {

    @Getter private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Getter private final Monster monster;
    @Getter private final Action actionType;

    public MonsterActionEvent(Monster monster, Action actionType) {
        this.monster = monster;
        this.actionType = actionType;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
