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
                System.out.println("[" + getName() + "] waiting...");
                sleep(Obstacle.OBSTACLE_MOVE_INTERVAL);
                System.out.println("[" + getName() + "] will move.");
                System.out.println("[" + getName() + "] Start remaining moves: " + obstacle.getRemainingMoves());
                move();
                System.out.println("[" + getName() + "] moved.");
                obstacle.decrementRemainingMoves();
                System.out.println("[" + getName() + "] decremented.");
                System.out.println("[" + getName() + "] End remaining moves: " + obstacle.getRemainingMoves());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void move() {
        obstacle.getOccupyingCell().removeObstacle();
        obstacle.setOccupyingCell(board.addGameElement(obstacle));
        board.setChanged();
    }
}
