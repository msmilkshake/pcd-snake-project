package game;

import environment.Board;
import environment.Cell;

public class Obstacle extends GameElement {
	

    public static final int OBSTACLE_MOVE_INTERVAL = 1000;
    
    private static final int NUM_MOVES = 3;
    private int remainingMoves = NUM_MOVES;
    private Board board;
    private Cell occupyingCell;

    public Obstacle(Board board) {
        super();
        this.board = board;
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
