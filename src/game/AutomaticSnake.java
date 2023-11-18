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
    private boolean isTrapped = false;

    public AutomaticSnake(int id, LocalBoard board) {
        super(id, board);
        this.board = board;
    }

    @Override
    public void run() {
        doInitialPositioning();
        
        //automatic movement
        while (true) {
            try {
                if (board.isFinished()) {
                    break;
                }
                tryMove();
                if (isTrapped) {
                    System.out.println("Snake got stuck.");
                    break;
                }
                sleep(LocalBoard.PLAYER_PLAY_INTERVAL);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Snake terminated.");
    }

    private void tryMove() throws InterruptedException {
        List<BoardPosition> positions =
                getBoard().getNeighboringPositions(cells.getFirst());
        List<BoardPosition> availablePos = new ArrayList<>();
        for (BoardPosition p : positions) {
            if (!board.getCell(p).isOccupiedBySnake() ||
                    !board.getCell(p).getOcuppyingSnake().equals(this)) {
                availablePos.add(p);
            }
        }
        if (availablePos.isEmpty()) {
            isTrapped = true;
        } else {
            availablePos.sort((o1, o2) ->
                    Double.compare(
                            o1.distanceTo(board.getGoalPosition()),
                            o2.distanceTo(board.getGoalPosition())));
            int index = 0;
            Cell target = board.getCell(availablePos.get(index++));
            if (isStuck) {
                while (target.isOccupied() && index < availablePos.size()) {
                    target = board.getCell(availablePos.get(index++));
                    if (index >= availablePos.size()) {
                        List<BoardPosition> posSamples =
                                board.getNeighboringPositions(board
                                        .getCell(cells.getFirst().getPosition()));
                        target = board.getCell(posSamples.get(ThreadLocalRandom.current()
                                .nextInt(posSamples.size())));
                    } else {
                        isStuck = false;
                    }
                }
            }
            // System.out.println("[" + Thread.currentThread().getName() + "] Target:" + target.getPosition());
            move(target);
        }

    }


}
