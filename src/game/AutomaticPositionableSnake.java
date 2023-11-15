package game;

import environment.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


// TODO - REMOVE FROM FINAL DELIVERY
public class AutomaticPositionableSnake extends Snake {
    private Board board;
    private boolean isTrapped = false;
    private BoardPosition lastMove = null;
    private Goal goal;
    private int initialX;
    private int initialY;

    public AutomaticPositionableSnake(int id, TestBoard board, Goal goal, int x, int y) {
        super(id, board);
        this.board = board;
        this.goal = goal;
        initialX = x;
        initialY = y;
    }

    @Override
    protected void doInitialPositioning() {
        BoardPosition at = new BoardPosition(initialX, initialY);

        try {
            board.getCell(at).request(this);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        cells.add(board.getCell(at));
        System.err.println("Snake " + getIdentification() + " starting at:" + getCells().getLast());
    }

    @Override
    public void run() {
        doInitialPositioning();
        while (true) {
            try {
                tryMove();
                if (isTrapped) {
                    System.out.println("Snake got stuck.");
                    break;
                }
                sleep(TestBoard.PLAYER_PLAY_INTERVAL);
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
