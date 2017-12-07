package me.tangni.sudoku;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

import me.tangni.sudoku.util.AppUtils;
import me.tangni.sudoku.util.TLog;

/**
 * Created by gaojian on 2017/11/12.
 */

public class SudokuApp extends Application {
    public static SudokuApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        initBugly();
    }

    /**
     * Bugly
     */
    private void initBugly() {
        Context context = getApplicationContext();
//        // 获取当前包名
//        String packageName = context.getPackageName();
//        // 获取当前进程名
//        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        strategy.setUploadProcess(AppUtils.isMainProcess(app.getApplicationContext()));
        strategy.setAppPackageName(context.getPackageName());
        strategy.setAppChannel(Config.BUILD_CHANNEL);

        TLog.d("SudokuApp", "initBugly, appChannel: " + Config.BUILD_CHANNEL);

        CrashReport.initCrashReport(getApplicationContext(), Config.BUGLY_APP_ID, Config.DEBUG, strategy);
    }
}
