package game;

import environment.Board;
import environment.LocalBoard;

import java.io.Serializable;

public class ObstacleMover extends Thread {
    private Obstacle obstacle;
    private LocalBoard board;

    public ObstacleMover(Obstacle obstacle, LocalBoard board) {
        super();
        this.obstacle = obstacle;
        this.board = board;
    }

    @Override
    public void run() {

        try {
            if (!board.isGameStarted()) {
                sleep(Board.GAME_START_DELAY);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        while (obstacle.getRemainingMoves() > 0 && !board.isFinished()) {
            try {
                sleep(Obstacle.OBSTACLE_MOVE_INTERVAL);
                move();
                obstacle.decrementRemainingMoves();
            } catch (InterruptedException e) {
            }
        }
    }

    private void move() {
        obstacle.getOccupyingCell().handleObstacleMovement(obstacle, board);
    }
}
