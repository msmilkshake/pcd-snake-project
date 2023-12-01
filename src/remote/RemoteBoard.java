package remote;

import environment.Board;
import environment.BoardState;
import environment.Cell;
import game.Snake;

import java.awt.event.KeyEvent;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * Remote representation of the game, no local threads involved.
 * Game state will be changed when updated info is received from Srver.
 * Only for part II of the project.
 *
 * @author luismota
 */
public class RemoteBoard extends Board {
    
    private PrintWriter writer;

    @Override
    public void handleKeyPress(int keyCode) {
        if (keyCode == KeyEvent.VK_UP) {
            System.err.println("UP");
            writer.write("UP");
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            System.err.println("RIGHT");
            writer.write("RIGHT");
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            System.err.println("DOWN");
            writer.write("DOWN");

        }
        if (keyCode == KeyEvent.VK_LEFT) {
            System.err.println("LEFT");
            writer.write("LEFT");
        }
        writer.write("\n");
        writer.flush();
    }

    @Override
    public void handleKeyRelease() {
        System.err.println("RELEASE KEY");
        writer.write("STOP\n");
        writer.flush();
    }

    @Override
    public void init() {
        // TODO
    }
    
    public void registerClientOutput(PrintWriter writer) {
        this.writer = writer;
        System.out.println("Writer registered");
    }
    
    public void updateGame(BoardState state) {
        super.cells = state.getCells();
        super.snakes = state.getSnakes();
        setChanged();
    }


}
