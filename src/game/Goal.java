package game;

import environment.Board;

public class Goal extends GameElement {
    
    public static final int MAX_VALUE = 9;
    
    private int value = 1;
    private Board board;

    public Goal(Board board2) {
        this.board = board2;
    }

    public int getValue() {
        return value;
    }

    public void incrementValue() {
        ++value;
    }

    public int captureGoal() {
        int localVal = value;
        if (value<MAX_VALUE) {
            incrementValue();
            board.addGameElement(this);
        }
        return localVal;
    }
}
