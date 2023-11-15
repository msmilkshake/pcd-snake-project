package game;

import environment.LocalBoard;

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //TODO: automatic movement

    }

    private void tryMove() {

    }


}
