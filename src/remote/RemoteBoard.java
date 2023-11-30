package remote;

import environment.Board;
import environment.Cell;

/**
 * Remote representation of the game, no local threads involved.
 * Game state will be changed when updated info is received from Srver.
 * Only for part II of the project.
 *
 * @author luismota
 */
public class RemoteBoard extends Board {

    @Override
    public void handleKeyPress(int keyCode) {
        //TODO
    }

    @Override
    public void handleKeyRelease() {
        // TODO
    }

    @Override
    public void init() {
        // TODO
    }
    
    public void updateGame(Cell[][] cells) {
        super.cells = cells;
        setChanged();
    }


}
