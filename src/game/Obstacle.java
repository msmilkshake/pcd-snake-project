package game;

import environment.Board;

public class Obstacle extends GameElement {


<<<<<<< Updated upstream
=======
    public static final int OBSTACLE_MOVE_INTERVAL = 1000;

>>>>>>> Stashed changes
    private static final int NUM_MOVES = 3;
    private static final int OBSTACLE_MOVE_INTERVAL = 400;
    private int remainingMoves = NUM_MOVES;
    private Board board;

    public Obstacle(Board board) {
        super();
        this.board = board;
    }

    public int getRemainingMoves() {
        return remainingMoves;
    }

}
