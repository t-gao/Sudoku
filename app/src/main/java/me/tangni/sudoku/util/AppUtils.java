package me.tangni.sudoku.util;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by gaojian on 2017/12/7.
 */

public class AppUtils {

    /**
     * Get the process name of the given pid
     *
     * @param pid the process pid
     * @return process name
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isMainProcess(Context context) {
        boolean isMainProcess;

        try {
            int pid = android.os.Process.myPid();
            String packageName = context.getApplicationContext().getPackageName();
            String processName = getProcessName(pid);
            isMainProcess = processName == null || processName.equalsIgnoreCase(packageName);

            TLog.d("AppUtils", "APP PID: " + pid + ", processName: " + processName + ", isMainProcess: " + isMainProcess);
        } catch (Exception e) {
            isMainProcess = true;//to make sure some initializing work can be done
        }

        return isMainProcess;
    }
}
