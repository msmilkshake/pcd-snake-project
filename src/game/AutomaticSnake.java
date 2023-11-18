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

        // automatic movement
        while (!board.isFinished()) {
            try {
                tryMove();
                if (isTrapped) {
                    System.out.println("Snake got stuck.");
                    break;
                }
                sleep(LocalBoard.PLAYER_PLAY_INTERVAL);
            } catch (InterruptedException e) {
            }
        }
        System.out.println("Snake [" + getName() + "] terminated.");
    }

    private void tryMove() throws InterruptedException {

        // Build a list with available positions that are not the snake itself
        List<BoardPosition> positions =
                getBoard().getNeighboringPositions(cells.getFirst());
        List<BoardPosition> availablePos = new ArrayList<>();
        for (BoardPosition p : positions) {
            if (!board.getCell(p).isOccupiedBySnake() ||
                    !board.getCell(p).getOcuppyingSnake().equals(this)) {
                availablePos.add(p);
            }
        }

        // If there's no available cell to move, end the snake's thread.
        if (availablePos.isEmpty()) {
            isTrapped = true;
        } else {

            // Sorts the available positions in distance to goal ascending order
            availablePos.sort((o1, o2) ->
                    Double.compare(
                            o1.distanceTo(board.getGoalPosition()),
                            o2.distanceTo(board.getGoalPosition())));
            int index = 0;
            Cell target = board.getCell(availablePos.get(index++));

            // If the snake got stuck in the previous movement
            // try to choose a different cell
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
            move(target);
        }

    }


}
