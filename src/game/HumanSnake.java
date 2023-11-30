package game;

import environment.Board;
import environment.BoardPosition;

/**
 * Class for a remote snake, controlled by a human
 *
 * @author luismota
 */
public class HumanSnake extends Snake {
    
    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }
    
    private Direction direction = Direction.RIGHT;

    public HumanSnake(int id, Board board) {
        super(id, board);
    }

    @Override
    public void run() {
        doInitialPositioning();
        
        while (!getBoard().isGameStarted()) {
            try {
                sleep(Board.PLAYER_PLAY_INTERVAL);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        
        while (!getBoard().isFinished()) {
            try {
                BoardPosition nextPos = cells.getFirst()
                        .getPosition()
                        .getCellByDirection(direction);
                
                if (getBoard().isValidPosition(nextPos)) {
                    move(getBoard().getCell(nextPos));
                }
                
                sleep(Board.PLAYER_PLAY_INTERVAL);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
