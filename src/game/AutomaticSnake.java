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
    private BoardPosition lastMove = null;
    private Goal goal;

    public AutomaticSnake(int id, LocalBoard board, Goal goal) {
        super(id, board);
        this.board = board;
        this.goal = goal;
    }

    @Override
    public void run() {
        doInitialPositioning();

        //TODO: automatic movement

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
            lastMove = availablePos.get(0);
            System.out.println("[" + Thread.currentThread().getName() + "] Target:" + target.getPosition());
            move(target);
        }

    }


}
