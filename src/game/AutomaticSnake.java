package game;

import environment.LocalBoard;

public class AutomaticSnake extends Snake {

    public AutomaticSnake(int id, LocalBoard board) {
        super(id, board);

    }

    @Override
    public void run() {
        doInitialPositioning();
<<<<<<< Updated upstream
        System.err.println("initial size:" + cells.size());
        try {
            cells.getLast().request(this);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //TODO: automatic movement

=======
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
>>>>>>> Stashed changes
    }

    private void tryMove() {

    }


}
