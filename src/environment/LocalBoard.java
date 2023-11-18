package environment;

import game.AutomaticSnake;
import game.Obstacle;
import game.ObstacleMover;
import game.Snake;
import util.ThreadPool;

/**
 * Class representing the state of a game running locally
 *
 * @author luismota
 */
public class LocalBoard extends Board {

    private static final int NUM_SNAKES = 2;
    private static final int NUM_OBSTACLES = 25;
    private static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 3;

    private ThreadPool threadPool = new ThreadPool(NUM_SIMULTANEOUS_MOVING_OBSTACLES);

    public LocalBoard() {
        addGoal();
        for (int i = 0; i < NUM_SNAKES; i++) {
            AutomaticSnake snake = new AutomaticSnake(i, this);
            snakes.add(snake);
        }
        addObstacles(NUM_OBSTACLES);
    }

    public void init() {
        for (Snake s : snakes)
            s.start();

        // launch other threads
        for (Obstacle obstacle : getObstacles()) {
            try {
                ObstacleMover mover = new ObstacleMover(obstacle, this);
                threadPool.submit(mover);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        countObstacles();

        setChanged();
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
