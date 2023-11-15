package game;

import environment.Board;

public class Goal extends GameElement {
<<<<<<< Updated upstream
=======

    public static final int MAX_VALUE = 9;

>>>>>>> Stashed changes
    private int value = 1;
    private Board board;
    public static final int MAX_VALUE = 10;

    public Goal(Board board2) {
        this.board = board2;
    }

    public int getValue() {
        return value;
    }

    public void incrementValue() throws InterruptedException {
        //TODO
    }

    public int captureGoal() {
<<<<<<< Updated upstream
        //		TODO
        return -1;
=======
        if (value == MAX_VALUE) {
            board.gameFinished();
        } else {
            incrementValue();
            board.addGameElement(this);
        }
        return value;
>>>>>>> Stashed changes
    }
}
