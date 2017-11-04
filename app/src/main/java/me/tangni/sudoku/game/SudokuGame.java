package me.tangni.sudoku.game;

import android.os.SystemClock;

import java.util.Random;

import me.tangni.sudoku.view.ISudokuBoardView;

/**
 * Created by gaojian on 2017/11/1.
 */

public class SudokuGame {
    // TODO: 2017/11/1
    public static final int GAME_STATE_EMPTY = -1;
    public static final int GAME_STATE_INITED = 0;
    public static final int GAME_STATE_STARTED = 1;
    public static final int GAME_STATE_PAUSED = 2;
    public static final int GAME_STATE_FINISHED = 3;

    public static final int GAME_DIFFICULTY_BEGINNER = 0;
//    public static final int GAME_DIFFICULTY_MEDIUM = 2;

    private int state = GAME_STATE_EMPTY;
    private ISudokuBoardView iSudokuBoardViewview;

    private int[][] puzzle;

    private int level = GAME_DIFFICULTY_BEGINNER;

    private boolean pencilMode = false;

    private long startedTime, elapsedTime = 0;


    private SudokuGameListener listener;

    public int gameState() {
        return state;
    }

    public void attach(ISudokuBoardView iSudokuBoardViewview) {
        this.iSudokuBoardViewview = iSudokuBoardViewview;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean inGame() {
        return state >= GAME_STATE_STARTED && state < GAME_STATE_FINISHED;
    }

    public void startGame() {
        if (state < GAME_STATE_STARTED) {
            state = GAME_STATE_STARTED;
            iSudokuBoardViewview.startGame();
            startedTime = System.currentTimeMillis();
            elapsedTime = 0;
        }
    }

    public void restartGame() {
        state = GAME_STATE_STARTED;
        iSudokuBoardViewview.startGame();
        startedTime = System.currentTimeMillis();
        elapsedTime = 0;
    }

    public void pauseGame() {
        if (state == GAME_STATE_STARTED) {
            state = GAME_STATE_PAUSED;
            iSudokuBoardViewview.pauseGame();
            elapsedTime = getElapsedTime();
            if (listener != null) {
                listener.onGamePaused();
            }
        }
    }

    public void resumeGame() {
        if (state == GAME_STATE_PAUSED) {
            state = GAME_STATE_STARTED;
            iSudokuBoardViewview.resumeGame();
            startedTime = System.currentTimeMillis();
            if (listener != null) {
                listener.onGameResumed();
            }
        }
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startedTime + elapsedTime;
    }

    public void togglePencilMode() {
        pencilMode = !pencilMode;
    }

    public boolean isPencilMode() {
        return pencilMode;
    }

    public void generatePuzzle() {

        puzzle = new int[9][9];

        // step 1: making seed
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (i == 0) {
                    puzzle[i][j] = j + 1;
                } else {
                    puzzle[i][j] = 0;
                }
            }
        }

        for(int i=0;i<9;++i){
            int ta = (int)(Math.random()*10)%9;
            int tb = (int)(Math.random()*10)%9;
            int tem = puzzle[0][ta];
            puzzle[0][ta] = puzzle[0][tb];
            puzzle[0][tb] = tem;
        }
        for(int i=0;i<9;++i){
            int ta = (int)(Math.random()*10)%9;
            int tb = (int)(Math.random()*10)%9;
            int tem = puzzle[0][i];
            puzzle[0][i] = puzzle[ta][tb];
            puzzle[ta][tb] = tem;
        }

        // step 2: complete (solve it)
        SudokuSolver.solve(puzzle);

        // step 3: dig some holes
        int holeCount = getHoleCountForLevel();
        for (int i = 0; i < holeCount; ++i) {
            int ta = (int) (Math.random()*10)%9;
            int tb = (int) (Math.random()*10)%9;
            if (puzzle[ta][tb] != 0)
                puzzle[ta][tb] = 0;
            else
                i--;
        }
    }

    private int getHoleCountForLevel() {
        Random random = new Random();
        int min = 5, max = 72;
        switch (level) {
            case 0:
                min = 5;
                max = 16;
                break;
            case 1:
                min = 17;
                max = 31;
                break;
            case 2:
                min = 32;
                max = 45;
                break;
            case 3:
                min = 46;
                max = 59;
                break;
            case 4:
                min = 60;
                max = 72;
                break;
        }
        return random.nextInt(max) % (max - min + 1) + min;
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public boolean isStarted() {
        return state == GAME_STATE_STARTED;
    }

    public boolean isSolved(Cell[][] cells) {
        if (cells != null && cells.length == 9) {
            for (int i = 0; i < 9; i++) {
                if (cells[i] != null && cells[i].length == 9) {
                    for (int j = 0; j < 9; j++) {
                        Cell cell = cells[i][j];
                        if (cell == null || cell.getValue() == 0 || cell.isInvalid()) {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void onGameSolved() {
        state = GAME_STATE_FINISHED;
        if (listener != null) {
            listener.onGameSolved();
        }
    }

    public void setCellValue(int value) {
        iSudokuBoardViewview.setCurCellValue(value);
    }

    public boolean isSafe(Cell[][] cells, int row, int column, int value) {
        return SudokuSolver.isSafe(cells, row, column, value);
    }

    public boolean isPaused() {
        return state == GAME_STATE_PAUSED;
    }

    public void setListener(SudokuGameListener listener) {
        this.listener = listener;
    }

    public boolean isFinished() {
        return state == GAME_STATE_FINISHED;
    }
}
