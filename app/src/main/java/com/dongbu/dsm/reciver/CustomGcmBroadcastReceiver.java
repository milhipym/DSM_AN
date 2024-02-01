package com.dongbu.dsm.reciver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;

import com.dongbu.dsm.push.CustomGcmIntentService;

import kr.mtcom.smartmessage.common.LOG;


public class CustomGcmBroadcastReceiver extends WakefulBroadcastReceiver {
    private String TAG = "CustomGcmBroadcastReceiver";
    public CustomGcmBroadcastReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        LOG.d(this.TAG, " onReceive");
        ComponentName comp = new ComponentName(context.getPackageName(), CustomGcmIntentService.class.getName());
        startWakefulService(context, intent.setComponent(comp));
        this.setResultCode(-1);
    }
}
