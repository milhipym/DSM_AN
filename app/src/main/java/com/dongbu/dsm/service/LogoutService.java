package com.dongbu.dsm.service;


import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.dongbu.dsm.Config;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.util.Log;


/**
 * Created by junglandon on 15. 12. 28..
 */
public class LogoutService extends Service {
    public static int MAX_TIME = 99999999; //CommonData.LOGOUT_MAX_TIME;
    public static CountDownTimer timer = null;
    public static int countDownvalue = MAX_TIME;
    public static LogoutService instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        countDownvalue = MAX_TIME;
        if(timer == null) {
            timer = new CountDownTimer(99999999 * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    countDownvalue--;

                    if(Config.DISPLAY_LOG && Config.DISPLAY_LOG_SESSION_TIME_CHECK) {
                        Log.d("SessionTimer = " + countDownvalue);
                    }

                    Intent i = new Intent(CommonData.BROADCAST_ACTIVITY_UPDATE_SESSION_TIME);
                    i.putExtra("sessionTime", String.valueOf(countDownvalue));
                    sendBroadcast(i);

                    if(countDownvalue == 0) {
                        cancelTimer();
                        timer.onFinish();
                    }
                }

                public void onFinish() {
                    Log.v("Call Logout by Service");
//                LoginShared.autologout(getApplicationContext());
//                AppController.sessionStop();

                    // Code for Logout
                    cancelTimer();
                    timer = null;
                    stopSelf();

                    Intent i = new Intent(CommonData.BROADCAST_ACTIVITY_LOGOUT);
                    sendBroadcast(i);

                }
            };
        }
    }

    public void cancelTimer() {
        if(LogoutService.timer != null) {
            LogoutService.timer.cancel();
        }
    }

    public static void cancel() {
        if(LogoutService.timer != null) {
            LogoutService.timer.cancel();
        }
        timer = null;
        if(instance != null) {
            instance.stopSelf();
            instance = null;
        }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
