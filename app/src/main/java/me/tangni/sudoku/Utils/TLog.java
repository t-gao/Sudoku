package me.tangni.sudoku.Utils;

import android.util.Log;

import me.tangni.sudoku.BuildConfig;

/**
 * Created by gaojian on 2017/11/2.
 */

public class TLog {
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG)
            Log.d(tag, msg);
    }
}
