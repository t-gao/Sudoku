package me.tangni.sudoku.util;

import android.text.TextUtils;

/**
 * Created by gaojian on 2017/11/4.
 */

public class UiUtils {
    private static final long HOUR = 1000 * 60 * 60;
    private static final long MIN = 1000 * 60;

    public static String convertMillisToString(long elapsed) {
        String str = "";
        int hour = (int) (elapsed / HOUR);
        int min = (int) ((elapsed % HOUR) / MIN);
        int sec = (int) (((elapsed % HOUR) % MIN) / 1000);
        long secRemainder = ((elapsed % HOUR) % MIN) % 1000;
        if (secRemainder > 500) {
            sec += 1;
        }

        String hourStr = hour > 0 ? String.valueOf(hour) : "";
        String minStr = String.valueOf(min);
        if (minStr.length() == 1) {
            minStr = "0" + minStr;
        }
        String secStr = String.valueOf(sec);
        if (secStr.length() == 1) {
            secStr = "0" + secStr;
        }

        if (!TextUtils.isEmpty(hourStr)) {
            str += (hourStr + " : ");
        }
        str += (minStr + " : " + secStr);
        return str;
    }
}
