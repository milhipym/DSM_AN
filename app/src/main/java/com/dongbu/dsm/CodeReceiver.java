package com.dongbu.dsm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;


public class CodeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        //
        // "com.TouchEn.mVaccine.b2b2c.FIRE"
        // 수신 된 Intent 처리

        Uri data = intent.getData();

        String type = intent.getStringExtra("type");

        int i = intent.getIntExtra("result", 0);

        Log.e("CodeReceiver", "result = " + i);

        switch (i)
        {
            case com.secureland.smartmedic.core.Constants.EMPTY_VIRUS:				//1000
                Log.e("CodeReceiver", "com.secureland.smartmedic.core.Constants.EMPTY_VIRUS");
                break;

            case com.secureland.smartmedic.core.Constants.EXIST_VIRUS_CASE1:		//1010
                Log.e("CodeReceiver", "com.secureland.smartmedic.core.Constants.EXIST_VIRUS_CASE1");
                break;

            case com.secureland.smartmedic.core.Constants.EXIST_VIRUS_CASE2:		//1100
                Log.e("CodeReceiver", "com.secureland.smartmedic.core.Constants.EXIST_VIRUS_CASE2");
                break;
        }
    }
}

