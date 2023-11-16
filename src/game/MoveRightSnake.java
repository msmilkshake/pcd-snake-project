package game;

import environment.*;

public class MoveRightSnake extends Snake {
    private Board board;
    private boolean isTrapped = false;
    private int posY;

    public MoveRightSnake(int id, StressTestBoard board, int posY) {
        super(id, board);
        this.board = board;
        this.posY = posY;
    }

    @Override
    public void run() {
        doInitialPositioning();

        // TODO: automatic movement

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
        Cell target = board.getCell(cells.getFirst().getPosition().getCellRight());
        move(target);
    }

    @Override
    protected void doInitialPositioning() {
        int posX = 0;
        try {
            BoardPosition at = new BoardPosition(posX, posY);
            if (!board.getCell(at).isOccupied()) {
                board.getCell(at).request(this);
                cells.add(board.getCell(at));
            }
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
