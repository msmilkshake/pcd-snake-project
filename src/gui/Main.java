package gui;

import environment.LocalBoard;
import environment.TestBoard;

public class Main {
    private static SnakeGui game;
    public static void main(String[] args) {
        LocalBoard board = new LocalBoard();
        // TestBoard board = new TestBoard();
        game = new SnakeGui(board, 500, 0);
        game.init();
        // Launch server
        // TODO

    }

    public static SnakeGui getSnakeGui() {
        return game;
    }
}
