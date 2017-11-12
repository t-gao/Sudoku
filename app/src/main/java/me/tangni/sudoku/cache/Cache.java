package me.tangni.sudoku.cache;

import android.content.Context;
import android.content.SharedPreferences;

import me.tangni.sudoku.SudokuApp;

/**
 * Created by gaojian on 2017/11/12.
 */

public class Cache {

    private static final String KEY_GAME = "c_k_game";

    private SharedPreferences sprefs;

    private Cache() {
        sprefs = SudokuApp.app.getSharedPreferences("TANGNI_SUDOKU_CACHE", Context.MODE_PRIVATE);
    }

    public static Cache getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final Cache instance = new Cache();
    }

    public void putGameSerialized(String game) {
        sprefs.edit().putString(KEY_GAME, game).apply();
    }

    public String getGameSerialized() {
        return sprefs.getString(KEY_GAME, null);
    }
}
