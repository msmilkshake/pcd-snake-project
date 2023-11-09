package environment;

import game.AutomaticSnake;
import game.Goal;
import game.Snake;

/**
 * Class representing the state of a game running locally
 *
 * @author luismota
 */
public class LocalBoard extends Board {

    private static final int NUM_SNAKES = 2;
    private static final int NUM_OBSTACLES = 25;
    private static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 3;

    public LocalBoard() {
        
        Goal goal = addGoal();
        
        for (int i = 0; i < NUM_SNAKES; i++) {
            AutomaticSnake snake = new AutomaticSnake(i, this, goal);
            snakes.add(snake);
        }

        addObstacles(NUM_OBSTACLES);

        
        
        //		System.err.println("All elements placed");
    }

    public void init() {
        for (Snake s : snakes)
            s.start();
        // TODO: launch other threads
        setChanged();
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
