package game;

import environment.BoardPosition;
import environment.Cell;
import environment.StressTestBoard;

public class ObstacleLeftMover extends Thread {
    private Obstacle obstacle;
    private StressTestBoard board;

    public ObstacleLeftMover(Obstacle obstacle, StressTestBoard board) {
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
                throw new RuntimeException(e);
            }
        }
    }

    private void move() {
        Cell temp = obstacle.getOccupyingCell();
        temp.removeObstacle();
        BoardPosition pos = temp.getPosition();
        Cell newCell = board.getCell(pos.getCellLeft());
        if (newCell.isOccupied()) {
            newCell = board.addGameElement(obstacle);
        } else {
            newCell.setGameElement(obstacle);
        }
        obstacle.setOccupyingCell(newCell);
        board.setChanged();
    }
}
