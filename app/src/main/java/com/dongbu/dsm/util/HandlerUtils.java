package com.dongbu.dsm.util;

/**
 * Created by landonjung on 2017. 5. 23..
 */

import android.os.Handler;
import android.os.Looper;

public final class HandlerUtils {
    private static final Handler handler = new Handler(Looper.getMainLooper());

    private HandlerUtils() {
    }

    public static boolean post(Runnable r) {
        return handler.post(r);
    }

    public static boolean postDelayed(Runnable r, long delayMillis) {
        return handler.postDelayed(r, delayMillis);
    }
}
