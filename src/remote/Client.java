package remote;


import environment.Cell;
import environment.LocalBoard;
import game.Server;
import gui.SnakeGui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Remore client, only for part II
 *
 * @author luismota
 */

public class Client {

    public static InetAddress address;

    static {
        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static SnakeGui game;

    private Socket connection;

    private PrintWriter out;
    private ObjectInputStream in;

    private InetAddress serverName;
    private int port;

    private RemoteBoard board;

    public Client(InetAddress serverName, int port, RemoteBoard board) {
        this.serverName = serverName;
        this.port = port;
        this.board = board;
    }

    public void runClient() {
        try {
            // 1. Connect to server
            connection = new Socket(serverName, port);

            // 2. Get i/o streams
            getStreams();

            // 3. Process connection
            processConnection();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 4. Close connection
            closeConnection();
        }
    }

    private void processConnection() {
        Thread gameStateReceiver = new Thread(new Runnable() {
            @Override
            public void run() {
                Cell[][] cells;
                while (true) {
                    try {
                        cells = (Cell[][])in.readObject();
                        board.updateGame(cells);
                        System.out.println();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        gameStateReceiver.start();
        while(true){

        }
    }

    private void getStreams() throws IOException {
        out = new PrintWriter(connection.getOutputStream(), true);

        // Input - Read
        in = new ObjectInputStream(connection.getInputStream());
    }

    private void closeConnection() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        RemoteBoard board = new RemoteBoard();
        game = new SnakeGui(board, 500, 0);
        game.init();
        Client client = new Client(Client.address, Server.port, board);
        client.runClient();
    }

}
