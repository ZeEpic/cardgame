package me.zeepic.cardgame.game;

import lombok.Getter;
import me.zeepic.cardgame.enums.Team;
import me.zeepic.cardgame.util.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public final class BoardLocation extends Location {

    @Getter private static final Vector boardOffset = Config.getVector("board.offset");
    @Getter private static final Material whiteBlock = Config.getMaterial("white_team.block");
    @Getter private static final Material blackBlock = Config.getMaterial("black_team.block");

    @Getter private final Map<Team, Boolean> cardPlaceableTeamMap = new HashMap<>();

    public BoardLocation(World boardWorld, int x, int z) {
        super(boardWorld,
                x + getBoardOffset().getX(),
                getBoardOffset().getY(),
                z + getBoardOffset().getZ());
        cardPlaceableTeamMap.put(Team.WHITE, x == 0);
        cardPlaceableTeamMap.put(Team.BLACK, x == ( GameBoard.getBoardSize() - 1 ));
    }

    public boolean isCardPlaceableBy(CardGamePlayer player) {
        return cardPlaceableTeamMap.get(player.getTeam());
    }
}
