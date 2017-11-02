package me.tangni.sudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.tangni.sudoku.view.SudokuBoard;

public class MainActivity extends AppCompatActivity {

    SudokuBoard sudokuBoard;
    SudokuGame sudokuGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sudokuBoard = (SudokuBoard) findViewById(R.id.sudodu_board);
        sudokuGame = new SudokuGame();
        sudokuBoard.attachGame(sudokuGame);
        sudokuGame.generatePuzzle();
    }

    public void onStartClick(View view) {
        sudokuGame.startGame();
    }
}
