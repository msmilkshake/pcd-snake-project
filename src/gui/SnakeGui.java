package gui;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;
import game.Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Class to create and configure GUI.
 * Only the listener to the button should be edited, see TODO below.
 *
 * @author luismota
 */
public class SnakeGui implements Observer {
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;
    public static final int NUM_COLUMNS = 40;
    public static final int NUM_ROWS = 30;
    private JFrame frame;
    private BoardComponent boardGui;
    private Board board;

    public SnakeGui(Board board, int x, int y) {
        super();
        this.board = board;
        frame = new JFrame("The Snake Game: " + (board instanceof LocalBoard ? "Local" : "Remote"));
        frame.setLocation(x, y);
        buildGui();
    }

    private void buildGui() {
        frame.setLayout(new BorderLayout());

        boardGui = new BoardComponent(board);
        boardGui.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        frame.add(boardGui, BorderLayout.CENTER);

        JButton resetObstaclesButton = new JButton("Reset snakes' directions");
        resetObstaclesButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                board.interruptSnakes();
            }

        });
        
        // For debugging purposes
        JButton obstacleCountButton = new JButton("Obstacle Count");
        obstacleCountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.countObstacles();
            }
        });

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(resetObstaclesButton);
        buttonsPanel.add(obstacleCountButton);
        
        if (board instanceof LocalBoard) {
            frame.add(buttonsPanel, BorderLayout.SOUTH);
        }

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void init() {
        frame.setVisible(true);
        board.addObserver(this);
        board.init();
    }

    @Override
    public void update(Observable o, Object arg) {
        boardGui.repaint();
    }

    public void endGamePopup(int winnerSnakeID) {

        JOptionPane.showMessageDialog(frame, 
                "Snake " + winnerSnakeID + " won the game!",
                "Game finished", JOptionPane.INFORMATION_MESSAGE);
        // System.exit(0);
    }
}
