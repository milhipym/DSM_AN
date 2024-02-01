package com.dongbu.dsm.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import com.dongbu.dsm.Config;
import com.dongbu.dsm.R;
import com.dongbu.dsm.base.BaseAgentWebViewActivity;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.service.LogoutService;
import com.dongbu.dsm.util.Log;
import com.dongbu.dsm.util.MDMUtils;

/**
 * Created by LandonJung on 2017-08-11.
 */

public class MainBaseWebViewPhoneActivity extends BaseAgentWebViewActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commonData.setStep(CommonData.ACTIVITY_MAIN_WEBVIEW);
        startService(new Intent(this, LogoutService.class));
        mContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWebView().resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWebView().pauseTimers();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        Log.d("[] MainBaseWebViewPadActivity 종료 --> MDM Logout,...");
        if(Config.ENABLE_MDM_AGENT_CHECK) {
            MDMUtils.getInstance(this, this).doProc(CommonData.MDM_ACTION_LOGOUT, false);
        }
        super.onDestroy();
    }
}
