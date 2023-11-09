package environment;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.Snake;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Main class for game representation.
 *
 * @author luismota
 */
public class Cell {
    private BoardPosition position;
    private Snake ocuppyingSnake = null;
    private GameElement gameElement = null;
    private Lock lock = new ReentrantLock();
    private Condition positionAvailable = lock.newCondition();

    public GameElement getGameElement() {
        return gameElement;
    }


    public Cell(BoardPosition position) {
        super();
        this.position = position;
    }

    public BoardPosition getPosition() {
        return position;
    }

    public synchronized void request(Snake snake)
            throws InterruptedException {
        //TODO coordination and mutual exclusion
        while(gameElement!=null || ocuppyingSnake != null){
            wait();
        }
        ocuppyingSnake = snake;
    }

    public synchronized void release() {
        ocuppyingSnake = null;
        gameElement = null;
    }

    public boolean isOccupiedBySnake() {
        return ocuppyingSnake != null;
    }


    public void setGameElement(GameElement element) {
        // TODO coordination and mutual exclusion
        gameElement = element;

    }

    public boolean isOccupied() {
        return isOccupiedBySnake() || (gameElement != null && gameElement instanceof Obstacle);
    }


    public Snake getOcuppyingSnake() {
        return ocuppyingSnake;
    }


    public Goal removeGoal() {
        // TODO
        return null;
    }

    public void removeObstacle() {
        //TODO
    }


    public Goal getGoal() {
        return (Goal) gameElement;
    }


    public boolean isOccupiedByGoal() {
        return (gameElement != null && gameElement instanceof Goal);
    }


}
