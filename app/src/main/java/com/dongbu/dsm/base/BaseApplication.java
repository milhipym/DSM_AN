package com.dongbu.dsm.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import androidx.multidex.MultiDexApplication;

import com.dongbu.dsm.util.Log;

/**
 * <pre>
 *     author: LandonJung
 *     time  : 2017/08/11
 *     desc  : 기본 어플리케이션 객체 클래스
 * </pre>
 */
public class BaseApplication extends MultiDexApplication {

    private static final String TAG = "BaseApplication";

    private static BaseApplication sInstance;
    private static Context				mContext;
    private static Handler mHandler;
    private static int					mMainThreadId;

    public static Context getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        registerActivityLifecycleCallbacks(mCallbacks);

        mContext = getApplicationContext();
        mMainThreadId = Process.myTid();
    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    private ActivityLifecycleCallbacks mCallbacks = new ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.d("onActivityCreated() called with: activity = [" + activity + "], savedInstanceState = [" + savedInstanceState + "]");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d("onActivityStarted() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.d("onActivityResumed() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.d("onActivityPaused() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.d("onActivityStopped() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.d("onActivitySaveInstanceState() called with: activity = [" + activity + "], outState = [" + outState + "]");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d("onActivityDestroyed() called with: activity = [" + activity + "]");
        }
    };
}
