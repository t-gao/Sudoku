package me.tangni.sudoku;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by gaojian on 2017/11/4.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFlags(0x04000000 /*WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS*/,
//                0x04000000 /*WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS*/);


    }
}
