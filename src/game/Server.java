package game;

import environment.Board;
import environment.Cell;
import environment.LocalBoard;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

    public static int port = 54321;


    private ServerSocket server;
    private List<ConnectionHandler> connections = new ArrayList<>();

    private LocalBoard board;
    private Thread broadcaster;

    public Server(LocalBoard board) {
        this.board = board;
    }


    public void runServer() {
        try {
            // 1. Create server socket
            server = new ServerSocket(port);

            broadcaster = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!board.isFinished()) {
                        try {
                            for (ConnectionHandler connection : connections) {
                                Cell[][] cells = board.getCells();
                                connection.sendGameState(cells);
                                System.out.println("Sending state to client: " + connection.connection.getPort());
                            }
                            System.out.println("Game state sent...");
                            Thread.sleep(Board.REMOTE_REFRESH_INTERVAL);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            broadcaster.start();

            while (true) {
                // 2. Wait for new connection
                waitForConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForConnection() throws IOException {
        System.out.println("[waiting for connection...]");
        Socket connection = server.accept(); // WAITS!

        ConnectionHandler handler = new ConnectionHandler(connection);
        handler.start();

        System.out.println("[new connection]" + connection.getInetAddress().getHostName());
    }

    private class ConnectionHandler extends Thread {

        private Socket connection;

        private ObjectOutputStream out;
        private Scanner in;

        public ConnectionHandler(Socket connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                // 3. Get i/o streams
                getStreams();

                // 4. Process connection
                processConnection();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 5. Close connection
                closeConnection();
            }
        }

        private void getStreams() throws IOException {
            // Output - Write
            out = new ObjectOutputStream(connection.getOutputStream());

            // Input - Read
            in = new Scanner(connection.getInputStream());
        }

        private void processConnection() {

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

        public void sendGameState(Cell[][] cells) {
            try {
                out.writeObject(cells);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
