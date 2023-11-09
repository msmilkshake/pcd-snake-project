package game;

import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AutomaticSnake extends Snake {

    public AutomaticSnake(int id, LocalBoard board) {
        super(id, board);

    }

    @Override
    public void run() {
        doInitialPositioning();
        System.err.println("initial size:" + cells.size());
        try {
            cells.getLast().request(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //TODO: automatic movement
        try {
            while (true) {
                tryMove();
                sleep(LocalBoard.PLAYER_PLAY_INTERVAL);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void tryMove() throws InterruptedException {
        List<BoardPosition> positions = getBoard().getNeighboringPositions(cells.getFirst());
        Cell target = getBoard().getCell(positions.get(ThreadLocalRandom.current().nextInt(positions.size())));
        move(target);

    }


}
