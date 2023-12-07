package game;

import environment.Cell;

import java.io.Serializable;

public class Obstacle extends GameElement implements Serializable{


    public static final int OBSTACLE_MOVE_INTERVAL = 1000;

    private static final int NUM_MOVES = 3;
    private int remainingMoves = NUM_MOVES;
    private Cell occupyingCell;

    public Obstacle() {
        super();
    }

    public int getRemainingMoves() {
        return remainingMoves;
    }

    public void decrementRemainingMoves() {
        --remainingMoves;
    }

    public void setOccupyingCell(Cell cell) {
        occupyingCell = cell;
    }

    public Cell getOccupyingCell() {
        return occupyingCell;
    }
}
