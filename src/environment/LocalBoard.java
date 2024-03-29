package environment;

import game.*;
import util.ThreadPool;

import java.io.Serializable;

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
        
        // For debugging purposes
        // countObstacles();
    }

    public void init() {
        for (Snake s : snakes)
            s.start();
        
        // new Thread(() -> {
        //     try {
        //         Thread.sleep(5_000);
        //         Snake s = new HumanSnake(777, this);
        //         snakes.add(s);
        //         s.start();
        //         setChanged();
        //     } catch (InterruptedException e) {
        //         throw new RuntimeException(e);
        //     }
        // }).start();
        // new Thread(() -> {
        //     try {
        //         Thread.sleep(15_000);
        //         Snake s = new HumanSnake(777, this);
        //         snakes.add(s);
        //         s.start();
        //         setChanged();
        //     } catch (InterruptedException e) {
        //         throw new RuntimeException(e);
        //     }
        // }).start();
        
        // launch other threads
        for (Obstacle obstacle : getObstacles()) {
            try {
                ObstacleMover mover = new ObstacleMover(obstacle, this);
                threadPool.submit(mover);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        setChanged();
    }

    @Override
    public void gameFinished() {
        threadPool.shutdownNow();
        super.gameFinished();
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
