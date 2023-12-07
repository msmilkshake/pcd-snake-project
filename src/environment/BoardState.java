package environment;

import game.Snake;

import java.io.Serializable;
import java.util.LinkedList;

public class BoardState implements Serializable {
    private Cell[][] cells;
    private LinkedList<Snake> snakes;
    
    public BoardState(LocalBoard board) {
        createClone(board);
    }

    private void createClone(LocalBoard board) {
        this.cells = board.getCells().clone();
        this.snakes = (LinkedList<Snake>) board.getSnakes().clone();
    }

    public Cell[][] getCells() {
        return cells;
    }

    public LinkedList<Snake> getSnakes() {
        return snakes;
    }
}
