package game;

import environment.Board;
import environment.BoardState;
import environment.LocalBoard;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Server extends Thread {

    public static int port = 54321;


    private ServerSocket server;
    private List<ConnectionHandler> newConnections = new ArrayList<>();
    private List<ConnectionHandler> connections = new ArrayList<>();
    private List<ConnectionHandler> deadConnections = new ArrayList<>();

    private LocalBoard board;
    private Thread broadcaster;

    public Server(LocalBoard board) {
        this.board = board;
    }

    @Override
    public void run() {
        runServer();
    }

    public void runServer() {
        try {
            // 1. Create server socket
            server = new ServerSocket(port, 1);

            broadcaster = new Thread(new Runnable() {
                @Override
                public void run() {
                    do {
                        try {
                            BoardState state = new BoardState(board.getCells(), board.getSnakes());
                            connections.addAll(newConnections);
                            newConnections.clear();
                            for (ConnectionHandler connection : connections) {
                                if (!connection.connection.isClosed()) {
                                    
                                    connection.sendGameState(state);
                                    System.out.println("Sending state to client: " +
                                            connection.connection.getPort());
                                }
                            }
                            connections.removeAll(deadConnections);
                            deadConnections.clear();

                            Thread.sleep(Board.REMOTE_REFRESH_INTERVAL);

                        } catch (InterruptedException e) {
                            System.out.println("Broadcaster interrupted.");;
                        }
                    } while (!board.isFinished());
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

        newConnections.add(handler);
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
            // Input - Read
            in = new Scanner(connection.getInputStream());

            // Output - Write
            out = new ObjectOutputStream(connection.getOutputStream());
        }

        private void processConnection() {
            try {
                while (!connection.isClosed()) {
                    String action = in.nextLine();
                    System.out.println(action);
                }
            } catch (NoSuchElementException e) {
                System.out.println("Client has closed the connection");
            }
        }

        private void closeConnection() {
            try {
                deadConnections.add(ConnectionHandler.this);
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
                System.out.println(" IOException in closeConnection");
                e.printStackTrace();
            }
        }

        public void sendGameState(BoardState state) {
            try {
                out.writeObject(state);
                out.reset();
            } catch (IOException e) {
                System.out.println("The out channel was closed");
            }
        }
    }
}
