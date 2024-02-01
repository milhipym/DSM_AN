package com.dongbu.dsm.app;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.multidex.MultiDex;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.Utils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.base.BaseApplication;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.NetworkConst;
import com.dongbu.dsm.service.LogoutService;
import com.dongbu.dsm.util.DSMUtil;
import com.dongbu.dsm.util.FontUtil;
import com.dongbu.dsm.util.Log;
import com.dongbu.dsm.util.MVaccine;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

/**
 * Created by LandonJung on 2017-8-10.
 * DSM 어플리케이션 클래스
 * @since 0, 1
 */
public class DSMApplication extends BaseApplication {

    private static volatile DSMApplication instance = null;
    private static volatile AppCompatActivity currentActivity = null;

    public static String deviceToken = "";
    public static float mScale;

    // multidex method 65536 에러 해결
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(getApplicationContext());                                // 유틸라이브러리 설정
        //MDMUtils.getInstance(getApplicationContext());                      // MDM 설정
        Config.PKG = AppUtils.getAppPackageName();
        Log.i("[DSMApplication] onCreate PKG = " + Config.PKG);

        instance = this;
        initImageLoader(getApplicationContext());                           // 이미지로더 Context 설정

        DSMUtil.setContext(getApplicationContext());
        CommonData.getInstance(getApplicationContext());                    // commondata 설정
        NetworkConst.getInstance().setContext(getApplicationContext());     // NetworkConst 설정
        NetworkConst.getInstance().init();                                  // 앱 실행시 개발 or 리얼서버용 설정
        FontUtil.getInstance().setContext(getApplicationContext());         // 폰트 Context 설정

        if(Config.ENABLE_CRASH_REPORT) {
            File dir = new File(Environment.getExternalStorageDirectory() + "/");
            CrashUtils.init(dir);                                              // Crash Report 설정
        }

        if(Config.ENABLE_WEBVIEW_CLEAR_CACHE) {
            clearApplicationData();
        }

        mScale = getResources().getDisplayMetrics().density;

        initDir();                                                          // 파일 저장 폴더 생성

        Log.d("EN_KEY = " + Config.EN_KEY);
        /**
         * GCM Registar
         */
//        if(Config.ENABLE_PUSH) {
//            try {
//                GCMRegistrar.checkDevice(this);
//                GCMRegistrar.checkManifest(this);
//                deviceToken = GCMRegistrar.getRegistrationId(this);
//            }catch(Exception e){
//                Log.e(e.toString());
//            }
//
//            if ( deviceToken == null || deviceToken.equals("") )
//                GCMRegistrar.register(this, GCMIntentService.PROJECT_ID);
//        }

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if(Config.ENABLE_VACCINE) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.cancel(MVaccine.MESSAGE_ID);
            mNotificationManager.cancel(MVaccine.MESSAGE_ID1);
        }
    }

    public void initDir() {
        String savePathRoot = CommonData.SAVE_PATH_ROOT;
        File _savePathRoot = new File(savePathRoot);
        if(_savePathRoot.exists() == false) {
            _savePathRoot.mkdir();
        }

        String saveImagePathRoot = CommonData.SAVE_IMAGE_PATH_ROOT;
        File _saveImagePathRoot = new File(saveImagePathRoot);
        if(_saveImagePathRoot.exists() == false) {
            _saveImagePathRoot.mkdir();
        }
    }

    /**
     * 이미지 로더 init
     * @param context
     */
    public void initImageLoader(Context context) {

        int memoryCacheSize;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory limit
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(memoryCacheSize)
                        //.denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .threadPoolSize(5)
                        //.imageDownloader(secureLoader)
                        //.enableLogging() // Not necessary in common
                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return true;
    }

    public void updateSessionTimer() {

        try {
            if(!Config.ENABLE_DEMO) {
                StringBuilder strMsg = new StringBuilder();
                if(isSessionValid(strMsg, "")) {
                    if(LogoutService.timer != null) {
                        LogoutService.timer.cancel();
                        LogoutService.countDownvalue = LogoutService.MAX_TIME;
                        LogoutService.timer.start();
                    }
                } else {
                    stopSessionTimer();
                    Intent i = new Intent(CommonData.BROADCAST_ACTIVITY_LOGOUT);
                    sendBroadcast(i);
                }

                Log.d("[session] " + strMsg.toString());
            }

        } catch (Exception e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
    }

    public void stopSessionTimer() {
        try {
            if(LogoutService.timer != null) {
                LogoutService.timer.cancel();
                LogoutService.countDownvalue = LogoutService.MAX_TIME;
            }
            LogoutService.cancel();
        }catch (Exception e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    public boolean createSession(StringBuilder errMsg)
    {
        // 컨텐츠 프로바이더 uri
        Uri uri = Uri.parse(CommonData.DSM_CONTENT_PROVIDER);

        Bundle reqBundle = new Bundle();
        reqBundle.putString(CommonData.EXTRA_APP_SESSION_ID_KEY, CommonData.EXTRA_APP_DEFAULT_SESSION_ID);

        Bundle retBundle =	getContentResolver().call(uri
                ,"createSession"
                ,null
                ,reqBundle);

        String resultYN = retBundle.getString("result");
        errMsg.append(retBundle.getString("errMsg"));

        return resultYN.equals("Y") ? true : false;
    }

    @SuppressLint("NewApi")
    public boolean isSessionValid(StringBuilder errMsg, String arg)
    {
        // 컨텐츠 프로바이더 uri
        Uri uri = Uri.parse(CommonData.DSM_CONTENT_PROVIDER);

        Bundle reqBundle = new Bundle();
        reqBundle.putString(CommonData.EXTRA_APP_SESSION_ID_KEY, CommonData.EXTRA_APP_DEFAULT_SESSION_ID);

        Bundle retBundle =	getContentResolver().call(uri
                ,"isSessionValid"
                ,arg
                ,reqBundle);

        String resultYN = retBundle.getString("result");
        errMsg.append(retBundle.getString("errMsg"));

        return resultYN.equals("Y") ? true : false;
    }

    public static AppCompatActivity getCurrentActivity() {
        Log.d("++ currentActivity : " + (currentActivity != null ? currentActivity.getClass().getSimpleName() : ""));
        return currentActivity;
    }

    public static void setCurrentActivity(AppCompatActivity currentActivity) {
        DSMApplication.currentActivity = currentActivity;
    }

    /**
     * singleton 애플리케이션 객체를 얻는다.
     * @return singleton 애플리케이션 객체
     */
    public static DSMApplication getCooingApplicationContext() {
        if(instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }
}
