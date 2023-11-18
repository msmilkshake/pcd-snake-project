package environment;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.Snake;
import gui.Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public abstract class Board extends Observable {
    protected Cell[][] cells;
    private BoardPosition goalPosition;
    public static final long PLAYER_PLAY_INTERVAL = 100;
    public static final long REMOTE_REFRESH_INTERVAL = 200;
    public static final int NUM_COLUMNS = 30;
    public static final int NUM_ROWS = 30;
    protected LinkedList<Snake> snakes = new LinkedList<>();
    private LinkedList<Obstacle> obstacles = new LinkedList<>();
    protected boolean isFinished;

    public Board() {
        cells = new Cell[NUM_COLUMNS][NUM_ROWS];
        for (int x = 0; x < NUM_COLUMNS; x++) {
            for (int y = 0; y < NUM_ROWS; y++) {
                cells[x][y] = new Cell(new BoardPosition(x, y));
            }
        }
    }

    public Cell getCell(BoardPosition cellCoord) {
        return cells[cellCoord.x][cellCoord.y];
    }

    public Cell[][] getCells() {
        return cells;
    }

    protected BoardPosition getRandomPosition() {
        return new BoardPosition((int) (Math.random() * NUM_ROWS), (int) (Math.random() * NUM_ROWS));
    }

    public BoardPosition getGoalPosition() {
        return goalPosition;
    }

    public void setGoalPosition(BoardPosition goalPosition) {
        this.goalPosition = goalPosition;
    }

    public Cell addGameElement(GameElement gameElement) {
        boolean placed = false;
        BoardPosition pos = getRandomPosition();
        while (!placed) {
            if (!getCell(pos).isOccupied() && !getCell(pos).isOccupiedByGoal()) {
                getCell(pos).setGameElement(gameElement);
                if (gameElement instanceof Goal) {
                    setGoalPosition(pos);
                }
                placed = true;
            } else {
                pos = getRandomPosition();
            }
        }
        return getCell(pos);
    }

    public List<BoardPosition> getNeighboringPositions(Cell cell) {
        ArrayList<BoardPosition> possibleCells = new ArrayList<>();
        BoardPosition pos = cell.getPosition();
        if (pos.x > 0)
            possibleCells.add(pos.getCellLeft());
        if (pos.x < NUM_COLUMNS - 1)
            possibleCells.add(pos.getCellRight());
        if (pos.y > 0)
            possibleCells.add(pos.getCellAbove());
        if (pos.y < NUM_ROWS - 1)
            possibleCells.add(pos.getCellBelow());
        return possibleCells;
    }

    protected Goal addGoal() {
        Goal goal = new Goal(this);
        addGameElement(goal);
        return goal;
    }

    protected void addObstacles(int numberObstacles) {
        // clear obstacle list , necessary when resetting obstacles.
        getObstacles().clear();
        while (numberObstacles > 0) {
            Obstacle obs = new Obstacle();
            obs.setOccupyingCell(addGameElement(obs));
            getObstacles().add(obs);
            numberObstacles--;
        }
    }

    public LinkedList<Snake> getSnakes() {
        return snakes;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        notifyObservers();
    }

    public LinkedList<Obstacle> getObstacles() {
        return obstacles;
    }

    public abstract void init();

    public abstract void handleKeyPress(int keyCode);

    public abstract void handleKeyRelease();

    public void interruptSnakes() {
        for (Snake s : snakes) {
            s.interrupt();
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void gameFinished() {
        isFinished = true;
        interruptSnakes();

        int winnerSnakeID = 0, winnerSnakeSize = 0;
        for (Snake s : snakes) {
            if (s.getSize() > winnerSnakeSize) {
                winnerSnakeID = s.getIdentification();
                winnerSnakeSize = s.getSize();
            }
        }
        final int value = winnerSnakeID;
        new Thread(() -> Main.game.endGamePopup(value)).start();
    }

    public void countObstacles() {
        int obstacleCount = 0;
        for (Cell[] rows : cells) {
            for (Cell cell : rows) {
                if (cell.getGameElement() instanceof Obstacle) {
                    ++obstacleCount;
                }
            }
        }
        System.out.println("Obstacle count: " + obstacleCount);
    }
}