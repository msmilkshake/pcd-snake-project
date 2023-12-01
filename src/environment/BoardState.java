package environment;

import game.Snake;

import java.io.Serializable;
import java.util.LinkedList;

public class BoardState implements Serializable {
    private Cell[][] cells;
    private LinkedList<Snake> snakes;
    
    public BoardState(Cell[][] cells, LinkedList<Snake> snakes) {
        this.cells = cells;
        this.snakes = snakes;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public LinkedList<Snake> getSnakes() {
        return snakes;
    }
}
