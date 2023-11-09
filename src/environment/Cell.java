package environment;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.Snake;

/**
 * Main class for game representation.
 *
 * @author luismota
 */
public class Cell {
    private BoardPosition position;
    private Snake ocuppyingSnake = null;
    private GameElement gameElement = null;

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

    public void request(Snake snake)
            throws InterruptedException {
        //TODO coordination and mutual exclusion
        ocuppyingSnake = snake;
    }

    public void release() {
        ocuppyingSnake = null;
        gameElement = null;
    }

    public boolean isOcupiedBySnake() {
        return ocuppyingSnake != null;
    }


    public void setGameElement(GameElement element) {
        // TODO coordination and mutual exclusion
        gameElement = element;

    }

    public boolean isOcupied() {
        return isOcupiedBySnake() || (gameElement != null && gameElement instanceof Obstacle);
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


    public boolean isOcupiedByGoal() {
        return (gameElement != null && gameElement instanceof Goal);
    }


}
