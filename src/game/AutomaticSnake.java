package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AutomaticSnake extends Snake {
    private Board board;

    public AutomaticSnake(int id, LocalBoard board) {
        super(id, board);
        this.board = board;
    }

    @Override
    public void run() {
        doInitialPositioning();
        //        System.err.println("initial size:" + size);
        //        try {
        //            cells.getLast().request(this);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }

        //TODO: automatic movement

        while (true) {
            try {
                tryMove();
                sleep(LocalBoard.PLAYER_PLAY_INTERVAL);
            } catch (InterruptedException e) {
            }
        }
    }

    private void tryMove() throws InterruptedException {
        List<BoardPosition> positions = getBoard().getNeighboringPositions(cells.getFirst());
        List<BoardPosition> availablePos = new ArrayList<>();
        for (BoardPosition p : positions) {
            if (!board.getCell(p).isOccupiedBySnake() || !board.getCell(p).getOcuppyingSnake().equals(this)) {
                availablePos.add(p);
            }
        }
        if (availablePos.isEmpty()) {
            Thread.currentThread().interrupt();
        } else {
            Cell target = board.getCell(availablePos.get(
                    ThreadLocalRandom.current().nextInt(availablePos.size())));
            move(target);
        }

    }


}
