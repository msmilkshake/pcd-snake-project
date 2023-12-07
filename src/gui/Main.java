package gui;

import environment.LocalBoard;
import game.Server;

public class Main {
    public static SnakeGui game;
    public static void main(String[] args) throws InterruptedException {
        LocalBoard board = new LocalBoard();
        game = new SnakeGui(board, 500, 0);
        game.init();
        // Launch server
        Server server = new Server(board);
        server.start();
        server.join();
    }
}
