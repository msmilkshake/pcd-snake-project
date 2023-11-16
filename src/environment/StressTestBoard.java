package environment;

import game.*;
import util.ThreadPool;

import java.util.Collections;

/**
 * Class representing the state of a game running locally
 *
 * @author luismota
 */
public class StressTestBoard extends Board {

    private static final int NUM_SNAKES = 30;
    private static final int NUM_OBSTACLES = 30;
    private static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 5;

    private ThreadPool threadPool = new ThreadPool(NUM_SIMULTANEOUS_MOVING_OBSTACLES);

    public StressTestBoard() {
        // addGoal();
        for (int i = 0; i < NUM_SNAKES; i++) {
            MoveRightSnake snake = new MoveRightSnake(i, this, i);
            snakes.add(snake);
        }
        addObstacles(NUM_OBSTACLES);
    }

    public void init() {
        for (Snake s : snakes)
            s.start();

        // TODO: launch other threads
        for (Obstacle obstacle : getObstacles()) {
            try {
                ObstacleLeftMover mover = new ObstacleLeftMover(obstacle, this);
                threadPool.submit(mover);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        setChanged();
    }

    @Override
    protected void addObstacles(int numberObstacles) {
        // clear obstacle list , necessary when resetting obstacles.
        getObstacles().clear();
        for (int i = 0; i < numberObstacles; ++i) {
            BoardPosition pos = new BoardPosition(15, i);
            Obstacle obs = new Obstacle(this);
            obs.setOccupyingCell(getCell(pos));
            getCell(pos).setGameElement(obs);
            getObstacles().add(obs);
        }
        Collections.shuffle(getObstacles());
    }
    
    @Override
    public void gameFinished() {
        super.gameFinished();
        threadPool.shutdownNow();
    }


    @Override
    public void handleKeyPress(int keyCode) {
        // do nothing... No keys relevant in local game
    }

    @Override
    public void handleKeyRelease() {
        // do nothing... No keys relevant in local game
    }


}
