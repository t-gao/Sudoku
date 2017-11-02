package me.tangni.sudoku;

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

    private int state = GAME_STATE_EMPTY;
    private ISudokuBoardView iSudokuBoardViewview;

    private int[][] puzzle;

    public int gameState() {
        return state;
    }

    public void attach(ISudokuBoardView iSudokuBoardViewview) {
        this.iSudokuBoardViewview = iSudokuBoardViewview;
    }

    public void startGame() {
        if (state < GAME_STATE_STARTED) {
            state = GAME_STATE_STARTED;
            iSudokuBoardViewview.startGame();
        }
    }

    public void generatePuzzle() {
        // TODO: 2017/11/2
        puzzle = new int[][] {
                {1, 2, 5, 0, 3, 9, 0, 4, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {3, 6, 0, 0, 1, 2, 0, 5, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 5, 0, 8, 0, 0, 4, 0, 7},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {4, 0, 0, 0, 0, 6, 0, 0, 0}
        };
    }

    public int[][] getPuzzle() {
        return puzzle;
    }
}
