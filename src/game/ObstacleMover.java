package game;

import environment.LocalBoard;

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
