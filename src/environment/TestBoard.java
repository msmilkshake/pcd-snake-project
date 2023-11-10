package environment;

import game.AutomaticPositionableSnake;
import game.Goal;
import game.Snake;

public class TestBoard extends Board {

    public static final long PLAYER_PLAY_INTERVAL = 750;

    private static final int NUM_SNAKES = 4;
    private static final int NUM_OBSTACLES = 0;
    private static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 3;

    // TODO - REMOVE FROM FINAL DELIVERY
    public TestBoard() {

        Goal goal = new Goal(this);
        placeGoal(goal, new BoardPosition(8, 8));
        int[][] coords = {
                {8, 0},
                {8, 16},
                {0, 8},
                {16, 8},
                
        };
        for (int i = 0; i < NUM_SNAKES; i++) {
            AutomaticPositionableSnake snake = new AutomaticPositionableSnake(i, this, goal, coords[i][0], coords[i][1]);
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

    public void placeGoal(Goal goal, BoardPosition pos) {
        getCell(pos).setGameElement(goal);
        setGoalPosition(pos);
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
