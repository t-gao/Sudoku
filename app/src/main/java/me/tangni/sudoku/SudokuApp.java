package me.tangni.sudoku;

import android.app.Application;

/**
 * Created by gaojian on 2017/11/12.
 */

public class SudokuApp extends Application {
    public static SudokuApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
