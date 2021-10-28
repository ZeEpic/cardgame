package me.zeepic.cardgame.game;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;

public final class GameBoard {

    @Getter private static final int boardSize = 8;
    @Getter private final BoardLocation[][] board = new BoardLocation[getBoardSize()][getBoardSize()];

    public GameBoard(World world) {
        for (int z = 0; z < getBoardSize(); z++) {
            for (int x = 0; x < getBoardSize(); x++) {
                getBoard()[x][z] = new BoardLocation(world, x, z);
            }
        }
    }

    public BoardLocation getWithLocation(Location location) {
        Location relative = location.subtract(BoardLocation.getBoardOffset());
        if (relative.getBlockX() >= boardSize
                || relative.getBlockZ() >= boardSize
                || relative.getBlockX() < 0
                || relative.getBlockZ() < 0) // can't find the location on the board
            return null;
        return getBoard()[relative.getBlockX()][relative.getBlockZ()];
    }

}
