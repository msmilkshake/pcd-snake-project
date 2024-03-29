package environment;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.Snake;

import java.io.Serializable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Main class for game representation.
 *
 * @author luismota
 */
public class Cell implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private BoardPosition position;
    private Snake ocuppyingSnake = null;
    private GameElement gameElement = null;
    private Lock lock = new ReentrantLock();
    private Condition cellNotAvailable = lock.newCondition();


    public GameElement getGameElement() {
        lock.lock();
        try {
            return gameElement;
        } finally {
            lock.unlock();
        }
    }

    public Cell(BoardPosition position) {
        super();
        this.position = position;
    }

    public BoardPosition getPosition() {
        return position;
    }

    public void request(Snake snake) throws InterruptedException {
        // coordination and mutual exclusion
        lock.lock();
        try {
            while (gameElement != null && !(gameElement instanceof Goal) || ocuppyingSnake != null) {
                snake.setStuck();
                cellNotAvailable.await();
            }
            ocuppyingSnake = snake;
        } finally {
            lock.unlock();
        }
    }

    public void release() {
        lock.lock();
        try {
            ocuppyingSnake = null;
            gameElement = null;
            cellNotAvailable.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public boolean isOccupiedBySnake() {
        lock.lock();
        try {
            return ocuppyingSnake != null;
        } finally {
            lock.unlock();
        }
    }

    public boolean setGameElement(GameElement element) {
        // coordination and mutual exclusion
        lock.lock();
        try {
            if (isOccupied()) {
                return false;
            }
            gameElement = element;
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean isOccupied() {
        lock.lock();
        try {
            return isOccupiedBySnake() || (gameElement != null && gameElement instanceof Obstacle);
        } finally {
            lock.unlock();
        }
    }

    public Snake getOcuppyingSnake() {
        lock.lock();
        try {
            return ocuppyingSnake;
        } finally {
            lock.unlock();
        }
    }

    public Goal removeGoal() {
        lock.lock();
        try {
            Goal goal = getGoal();
            gameElement = null;
            return goal;
        } finally {
            lock.unlock();
        }
    }

    public void removeObstacle() {
        lock.lock();
        try {
            if (isOccupied() && gameElement instanceof Obstacle) {
                gameElement = null;
                cellNotAvailable.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }


    public Goal getGoal() {
        lock.lock();
        try {
            return (Goal) gameElement;
        } finally {
            lock.unlock();
        }
    }


    public boolean isOccupiedByGoal() {
        lock.lock();
        try {
            return (gameElement != null && gameElement instanceof Goal);
        } finally {
            lock.unlock();
        }
    }

    public void handleObstacleMovement(Obstacle obstacle, Board board) {
        lock.lock();
        try {
            obstacle.getOccupyingCell().removeObstacle();
            obstacle.setOccupyingCell(board.addGameElement(obstacle));
            board.setChanged();
        } finally {
            lock.unlock();
        }
    }
    
    public void handleGoalPlacement(Goal goal, Board board) {
        lock.lock();
        try {
            board.addGameElement(goal);
            board.setChanged();
        } finally {
            lock.unlock();
        }
    }

}
