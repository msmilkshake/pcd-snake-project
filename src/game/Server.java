package game;

import environment.Board;
import environment.BoardState;
import environment.Cell;
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
            server = new ServerSocket(port);

            broadcaster = new Thread(new Runnable() {
                @Override
                public void run() {
                    do {
                        try {
                            connections.addAll(newConnections);
                            newConnections.clear();

                            connections.removeAll(deadConnections);
                            deadConnections.clear();

                            sendState();

                            Thread.sleep(Board.REMOTE_REFRESH_INTERVAL);

                        } catch (InterruptedException e) {
                            System.out.println("Broadcaster interrupted.");
                        }
                    } while (!board.isFinished());

                    // Send state one last time to update the clients with the last state
                    sendState();
                }

                private void sendState() {
                    BoardState state = new BoardState(board.getCells(), board.getSnakes());
                    for (ConnectionHandler connection : connections) {
                        if (!connection.connection.isClosed()) {

                            connection.sendGameState(state);
                            System.out.println("Sending state to client: " +
                                    connection.connection.getPort());
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

        newConnections.add(handler);
        System.out.println("[new connection]" + connection.getInetAddress().getHostName());
    }

    private class ConnectionHandler extends Thread {

        private Socket connection;

        private ObjectOutputStream out;
        private Scanner in;

        private HumanSnake snake;

        public ConnectionHandler(Socket connection) {
            this.connection = connection;
            snake = new HumanSnake(connection.getPort(), board);
            board.getSnakes().add(snake);
            board.setChanged();
            snake.start();
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
                    System.out.println("[Client: " + connection.getPort() + "]: Waiting for action...");
                    String action = in.nextLine();
                    System.out.println("[Client: " + connection.getPort() + "]: Got action: " + action);
                    switch (action) {
                        case ("UP"):
                            snake.setDirection(HumanSnake.Direction.UP);
                            snake.setMoving(true);
                            break;
                        case ("RIGHT"):
                            snake.setDirection(HumanSnake.Direction.RIGHT);
                            snake.setMoving(true);
                            break;
                        case ("DOWN"):
                            snake.setDirection(HumanSnake.Direction.DOWN);
                            snake.setMoving(true);
                            break;
                        case ("LEFT"):
                            snake.setDirection(HumanSnake.Direction.LEFT);
                            snake.setMoving(true);
                            break;
                        case ("STOP"):
                            snake.setMoving(false);
                            break;
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.println("Client has closed the connection");
            }
            handleClose();
        }

        private void handleClose() {
            for (Cell cell : snake.getCells()) {
                cell.release();
            }
            board.getSnakes().remove(snake);
            board.setChanged();
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
                if (out != null) {
                    out.reset();
                    out.writeObject(state);
                    out.flush();
                }
            } catch (IOException e) {
                System.out.println("The out channel was closed");
            }
        }
    }
}
