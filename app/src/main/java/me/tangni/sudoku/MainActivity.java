package me.tangni.sudoku;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import me.tangni.sudoku.game.SudokuGame;
import me.tangni.sudoku.game.SudokuGameListener;
import me.tangni.sudoku.view.SudokuBoard;

public class MainActivity extends AppCompatActivity implements SudokuGameListener {

    SudokuBoard sudokuBoard;
    SudokuGame sudokuGame;

    String[] levels = new String[]{"不屑一顾", "快拿开", "太简单", "很简单", "还凑合"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sudokuBoard = (SudokuBoard) findViewById(R.id.sudodu_board);
        sudokuGame = new SudokuGame();
        sudokuBoard.attachGame(sudokuGame);
        sudokuBoard.setListener(this);

    }

    public void onStartClick(View view) {
        if (!sudokuGame.inGame()) {
            showLevelSelectionDialog();
//            sudokuGame.generatePuzzle();
//            sudokuGame.startGame();
        }
    }


    public void onBtn1Click(View view) {
        sudokuGame.setCellValue(1);
    }
    public void onBtn2Click(View view) {
        sudokuGame.setCellValue(2);
    }
    public void onBtn3Click(View view) {
        sudokuGame.setCellValue(3);
    }
    public void onBtn4Click(View view) {
        sudokuGame.setCellValue(4);
    }
    public void onBtn5Click(View view) {
        sudokuGame.setCellValue(5);
    }
    public void onBtn6Click(View view) {
        sudokuGame.setCellValue(6);
    }
    public void onBtn7Click(View view) {
        sudokuGame.setCellValue(7);
    }
    public void onBtn8Click(View view) {
        sudokuGame.setCellValue(8);
    }
    public void onBtn9Click(View view) {
        sudokuGame.setCellValue(9);
    }

    public void onBtnDelClick(View view) {
        sudokuGame.setCellValue(0);
    }

    @Override
    public void onGameSolved() {
        Toast.makeText(this, "SOLVED!", Toast.LENGTH_LONG).show();
    }

    public void onBtnPencilClick(View view) {
        sudokuGame.togglePencilMode();
        view.setSelected(sudokuGame.isPencilMode());
    }

    public void onReStartClick(View view) {
        showLevelSelectionDialog();
//        sudokuGame.generatePuzzle();
//        sudokuGame.restartGame();
    }

    private void showLevelSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择难度")
                .setItems(levels, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sudokuGame.setLevel(which);
                        sudokuGame.generatePuzzle();
                        sudokuGame.restartGame();
                    }
                })
//                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        sudokuGame.setLevel(position);
//                        sudokuGame.generatePuzzle();
//                        sudokuGame.restartGame();
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                })
        ;
        builder.create().show();
    }
}
