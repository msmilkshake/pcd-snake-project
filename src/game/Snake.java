package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Base class for representing Snakes.
 * Will be extended by HumanSnake and AutomaticSnake.
 * Common methods will be defined here.
 *
 * @author luismota
 */
public abstract class Snake extends Thread implements Serializable {

    protected LinkedList<Cell> cells = new LinkedList<>();
    protected int size = 5;
    protected boolean isStuck = false;
    private int id;
    private Board board;


    public Snake(int id, Board board) {
        this.id = id;
        this.board = board;
    }

    public int getSize() {
        return size;
    }

    public int getIdentification() {
        return id;
    }

    public int getLength() {
        return cells.size();
    }

    public LinkedList<Cell> getCells() {
        return cells;
    }

    protected void move(Cell cell) throws InterruptedException {
        cell.request(this);
        int goalSize = 0;
        if (cell.isOccupiedByGoal()) {
            Goal goal = cell.removeGoal();
            goalSize = goal.captureGoal();
            System.err.println(goalSize);
            size += goalSize;
        }
        cells.addFirst(cell);
        if (cells.size() > size) {
            BoardPosition lastAt = cells.removeLast().getPosition();
            board.getCell(lastAt).release();
        }
        board.setChanged();
        if (goalSize == Goal.MAX_VALUE) {
            board.gameFinished();
        }
    }

    public LinkedList<BoardPosition> getPath() {
        LinkedList<BoardPosition> coordinates = new LinkedList<>();
        for (Cell cell : cells) {
            coordinates.add(cell.getPosition());
        }

        return coordinates;
    }

    protected void doInitialPositioning() {
        // Random position on the first column. 
        // At startup, snake occupies a single cell
        int posX = 0;
        boolean placed = false;
        try {
            while (!placed) {
                int posY = (int) (Math.random() * Board.NUM_ROWS);
                BoardPosition at = new BoardPosition(posX, posY);
                if (!board.getCell(at).isOccupied()) {
                    board.getCell(at).request(this);
                    cells.add(board.getCell(at));
                    placed = true;
                }
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        System.err.println("Snake " + getIdentification() + " starting at:" + getCells().getLast());
    }

    public Board getBoard() {
        return board;
    }

    public void setStuck() {
        isStuck = true;
    }
}
