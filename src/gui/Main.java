package gui;

import environment.LocalBoard;
import environment.StressTestBoard;
import environment.TestBoard;

public class Main {
    public static SnakeGui game;
    public static void main(String[] args) {
        // LocalBoard board = new LocalBoard();
        StressTestBoard board = new StressTestBoard();
        game = new SnakeGui(board, 500, 0);
        game.init();
        // Launch server
        // TODO

    }
}
