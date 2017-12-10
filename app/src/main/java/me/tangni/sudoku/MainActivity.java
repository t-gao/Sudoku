package me.tangni.sudoku;

import android.content.DialogInterface;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import me.tangni.sudoku.cache.Cache;
import me.tangni.sudoku.game.SudokuGame;
import me.tangni.sudoku.game.SudokuGameListener;
import me.tangni.sudoku.util.TLog;
import me.tangni.sudoku.util.UiUtils;
import me.tangni.sudoku.view.SudokuBoard;

public class MainActivity extends BaseActivity implements SudokuGameListener {
    private static final int MSG_UPDATE_TIME = 100;

    SudokuBoard sudokuBoard;
    SudokuGame sudokuGame;

    TextView levelTv, pauseTv;

    private String[] levelNames;
    private TimerTask timerTask;
    private Timer timer;

    private Handler handler;

    private ImageView successImgView;
    private View successCover;

    private TextView btnPencil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        levelTv = (TextView) findViewById(R.id.tv_level);
        pauseTv = (TextView) findViewById(R.id.tv_pause);

        btnPencil = (TextView) findViewById(R.id.btn_pencil);

        successImgView = (ImageView) findViewById(R.id.iv_success);
        successCover = findViewById(R.id.success_cover);

        levelNames = getResources().getStringArray(R.array.difficulty_levels);

        sudokuBoard = (SudokuBoard) findViewById(R.id.sudodu_board);
        sudokuGame = new SudokuGame();
        sudokuBoard.attachGame(sudokuGame);
        sudokuGame.setListener(this);

        handler = new UiHandler(this);

        String cachedGame = Cache.getInstance().getGameSerialized();
        if (!TextUtils.isEmpty(cachedGame)) {
            resumeGameFromSerialized(cachedGame);
        } else {
            restartGame(0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sudokuGame.pauseGame();
        cancelTimer();
        TLog.d("SUDOKU", "serialized: " + sudokuGame.serialize());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sudokuGame.isFinished()) {
            Cache.getInstance().putGameSerialized("");
        } else {
            String serialized = sudokuGame.serialize();
            Cache.getInstance().putGameSerialized(serialized);
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
        cancelTimer();
        showSuccess();
    }

    private void showSuccess() {
        successCover.setVisibility(View.VISIBLE);
        successImgView.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimatedVectorDrawable animatedVectorDrawable =  (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.ic_animated_success);
            successImgView.setImageDrawable(animatedVectorDrawable);
            if(animatedVectorDrawable != null) {
                animatedVectorDrawable.start();
            }
        } else {
            successImgView.setImageResource(R.drawable.ic_success);
        }
    }

    @Override
    public void onGamePaused() {
        pauseTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_start, 0);
        cancelTimer();
    }

    @Override
    public void onGameResumed() {
        pauseTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pause, 0);
        startTimer();
    }

    public void onBtnPencilClick(View view) {
        sudokuGame.togglePencilMode();
        view.setSelected(sudokuGame.isPencilMode());
    }

    private void showLevelSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dlg_title_select_difficulty)
                .setItems(levelNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnPencil.setSelected(false);
                        sudokuGame.setPencilMode(false);
                        restartGame(which);
                    }
                });
        builder.create().show();
    }

    private void resumeGameFromSerialized(String cachedGame) {
        successCover.setVisibility(View.GONE);
        successImgView.setVisibility(View.GONE);
        sudokuGame.resumeGameFromSerialized(cachedGame);

        int level = sudokuGame.getLevel();
        if (levelNames != null && levelNames.length > level) {
            levelTv.setText("Lv. " + (level + 1) + ": " + levelNames[level]);
        }

        boolean pencilMode = sudokuGame.isPencilMode();
        btnPencil.setSelected(pencilMode);

        pauseTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pause, 0);

        startTimer();
    }

    private void restartGame(int level) {
        successCover.setVisibility(View.GONE);
        successImgView.setVisibility(View.GONE);
        sudokuGame.setLevel(level);
        sudokuGame.generatePuzzle();
        sudokuGame.restartGame();
        if (levelNames != null && levelNames.length > level) {
            levelTv.setText("Lv. " + (level + 1) + ": " + levelNames[level]);
        }

        pauseTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pause, 0);
        btnPencil.setSelected(sudokuGame.isPencilMode());
        startTimer();
    }

    private void startTimer() {

        cancelTimer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    private void getAndShowElapsedTime() {
        long elapsed = sudokuGame.getElapsedTime();
        String timeStr = UiUtils.convertMillisToString(elapsed);
        pauseTv.setText(timeStr);
    }

    public void onNewGameClick(View view) {
        showLevelSelectionDialog();
    }

    private void cancelTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void onPauseClick(View view) {
        if (sudokuGame.isPaused()) {
            pauseTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_pause, 0);
            sudokuGame.resumeGame();
            startTimer();
        } else if (sudokuGame.isStarted()) {
            pauseTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_start, 0);
            sudokuGame.pauseGame();
            cancelTimer();
        }
    }

    private static class UiHandler extends Handler {
        WeakReference<MainActivity> ref;
        UiHandler(MainActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_TIME:
                    if (ref != null && ref.get() != null)
                        ref.get().getAndShowElapsedTime();
                    break;
            }
        }
    }
}
