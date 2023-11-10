package gui;

import environment.LocalBoard;
import environment.TestBoard;

public class Main {
    public static void main(String[] args) {
        LocalBoard board = new LocalBoard();
        // TestBoard board = new TestBoard();
        SnakeGui game = new SnakeGui(board, 500, 0);
        game.init();
        // Launch server
        // TODO

    }
}
