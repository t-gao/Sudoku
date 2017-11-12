package me.tangni.sudoku.view;

/**
 * Created by gaojian on 2017/11/2.
 */

public interface ISudokuBoardView {
    void startGame();

    void pauseGame();

    void resumeGame();

    void setCurCellValue(int value);

    String serialize();

    int[][] deserialize(String serialized);
}
