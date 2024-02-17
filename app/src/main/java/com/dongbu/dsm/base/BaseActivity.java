package com.dongbu.dsm.base;

import android.Manifest;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.R;
import com.dongbu.dsm.app.DSMApplication;
import com.dongbu.dsm.camera.activity.CameraGalleryActivity;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.CommonView;
import com.dongbu.dsm.common.CustomAlertDialog;
import com.dongbu.dsm.common.CustomAlertDialogInterface;
import com.dongbu.dsm.common.CustomAsyncListener;
import com.dongbu.dsm.common.NetworkConst;
import com.dongbu.dsm.common.PermissionUtils;
import com.dongbu.dsm.common.SessionMan;
import com.dongbu.dsm.intro.LoginActivity;
import com.dongbu.dsm.intro.PortalActivity;
import com.dongbu.dsm.main.MainBaseWebViewPadActivity;
import com.dongbu.dsm.main.MainBaseWebViewPhoneActivity;
import com.dongbu.dsm.model.web.BCResultWebItem;
import com.dongbu.dsm.model.web.Contract;
import com.dongbu.dsm.model.web.OCRItem;
import com.dongbu.dsm.network.RequestApi;
import com.dongbu.dsm.network.RequestAsyncNetwork;
import com.dongbu.dsm.provider.MPortalP;
import com.dongbu.dsm.service.LogoutService;
import com.dongbu.dsm.share.KakaoTalkShareActivity;
import com.dongbu.dsm.util.Base64Coder;
import com.dongbu.dsm.util.Base64Coder1;
import com.dongbu.dsm.util.CipherUtil;
import com.dongbu.dsm.util.DSMUtil;
import com.dongbu.dsm.util.FontUtil;
import com.dongbu.dsm.util.HandlerUtils;
import com.dongbu.dsm.util.Log;
import com.dongbu.dsm.util.MDMUtils;
import com.dongbu.dsm.util.MVaccine;
import com.dongbu.dsm.util.PopupUtils;
import com.dongbu.dsm.util.RotationUtil;
import com.dongbu.dsm.webview.jsbridge.CallBackFunctionListener;
import com.dongbu.dsm.widget.DSMProgressDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.minerva.magicbcreaderlib.ui.activity.BCCaptureActivity;
import com.minerva.magicbcreaderlib.ui.item.BCResultItem;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import kr.co.minervasoft.lib.magicidr.activity.IDRCaptureActivity;
import minerva.magicidreaderlib.Const;
import minerva.magicidreaderlib.MagicIDReaderCameraMainActivity;
import minerva.magicidreaderlib.card.CardDTO;

/**
 * Created by LandonJung on 2017-8-10.
 * activity 부모 클래스
 * @since 0, 1
 */
public class BaseActivity extends AppCompatActivity {

    public static ArrayList<AppCompatActivity> actList = new ArrayList<AppCompatActivity>();	// 엑티비티 리스트 저장

    public RequestAsyncNetwork mRequestAsyncNetwork	= null; // 네트워크

    private boolean				mButtonClickEnabled		= true;  // 버튼 클릭 유무
    private Handler mButtonClickEnabledHandler = new Handler();
    protected BaseAgentWebViewActivity.WebViewListener mWebViewListener;

    boolean isShow = true;

    public LayoutInflater mLayoutInflater;
    public RelativeLayout mProgressLayout;

    protected Context mContext;

    public CommonData commonData = CommonData.getInstance();
    public CommonView commonView = CommonView.getInstance();
    public CustomAlertDialog mDialog;

    //CustomAlertDialog updateDialog = null;

    IntentFilter mFilter;
    private int MOVE_ACTIVITY_ID = -1;
    private static int mLogoutType = CommonData.LOGOUT_TYPE_NONE;

    private View decorView;
    private int uiOption;

    protected DSMProgressDialog progressDialog;

    public CallBackFunctionListener getCallBackFunctionListener() {
        return mCallBackFunctionListener;
    }

    public void setCallBackFunctionListener(CallBackFunctionListener mCallBackFunctionListener) {
        this.mCallBackFunctionListener = mCallBackFunctionListener;
    }

    public CallBackFunctionListener mCallBackFunctionListener;

    public MDMUtils.MDMCallbackListener mdmLoginCallbackListener = new MDMUtils.MDMCallbackListener() {
        @Override
        public void onBindResult(boolean result) {
            // MDM Init 성공 여부

            Log.d("[onBindResult] 상위 클래스 mContext.getClass().getSimpleName() = " + mContext.getClass().getSimpleName());

            if(result) {
                if(mContext.getClass().getSimpleName().equals(LoginActivity.class.getSimpleName())) {
                    //((IntroActivity)mContext).autoLoginCheckToView();
                    ((IntroBaseActivity)mContext).JoinTologinSuccess(null, null);
                }
            } else {

                //finish();

                if(mContext.getClass().getSuperclass().getSimpleName().equals(IntroBaseActivity.class.getSimpleName())) {
                    //((IntroBaseActivity)mContext)
                }
            }
        }

        @Override
        public void onLoginResult(boolean result) {
//            if(result) {
            Log.d("[onLoginResult] 상위 클래스 mContext.getClass().getSimpleName() = " + mContext.getClass().getSimpleName());

                Log.d("[onLoginResult] 웹뷰로 이동");
            if(mContext.getClass().getSimpleName().equals(LoginActivity.class.getSimpleName())) {
                if(!CommonData.isWebViewActivity) {
                    ((LoginActivity)mContext).loginFinish();
                }
            }



//                if(Config.ENABLE_APP_VERSION_CHECK_UPDATE) {
//                    // MDM Agent Check 가 성공되면 USER ID 를 전달하여 APP Version Check 후 업데이트를 하도록 한다.
//                    MDMUtils.getInstance(mContext, BaseActivity.this).versionCheck();
//                } else {
                    // MDM Agent Check 가 성공되면 단말기 유저정보를 얻어온다.
                  //  loginFinish();

                //((BaseActivity)mContext).moveActivity();
//                    CommonData.isNoBindMDM = false;
//                    MDMUtils.getInstance(mContext, BaseActivity.this).doProc(CommonData.MDM_ACTION_DEVICE_USER_INFO, false);
//                }
//            }
        }

        @Override
        public void onGetInfoResult(JSONObject data) {

            // 모든 로그인 처리 완료 후 메인으로 이동 전 데이터값 저장 및 화면 전환
            Log.d("onGetInfoResult = " + mContext.getClass().getSuperclass().getSimpleName());

            if(mContext.getClass().getSuperclass().getSimpleName().equals(IntroBaseActivity.class.getSimpleName())) {
                Log.d("[onGetInfoResult] 로그인 정보 저장");
                ((IntroBaseActivity)mContext).loginSuccess(data);
                commonData.setLogined(true);
            }

        }

        @Override
        public void onAppVersionCheckResult(boolean result) {
            //if(result) {
                // MDM Agent Check 가 성공되면 단말기 유저정보를 얻어온다.
                Log.d("[onAppVersionCheckResult] 버전 채크 완료");
                //MDMUtils.getInstance(mContext, BaseActivity.this).doProc(CommonData.MDM_ACTION_DEVICE_USER_INFO, false);
                MDMUtils.getInstance(mContext, BaseActivity.this).doProc(CommonData.MDM_ACTION_LOGIN_OFFICE_CHECK, false);
                Log.d("[onAppVersionCheckResult] 로그인 완료 메세지 호출");
                //((IntroBaseActivity)mContext).moveActivity();
            //}
        }

        @Override
        public void onLogoutResult(boolean result) {

            commonData.setLogined(false);
            SessionMan.setbLogin(false);
            CommonData.isNoBindMDM = true;
            // 로그아웃 처리 완료 후 이동
            if(mLogoutType == CommonData.LOGOUT_TYPE_NORMAL) {
                toLogin();
           } else if(mLogoutType == CommonData.LOGOUT_TYPE_APP_FINISH) {
                // 앱 종료
                exitApp();
            }
        }
    };

    public void exitApp() {
        // 앱 종료
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(Config.ENABLE_VACCINE) {
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotificationManager.cancel(MVaccine.MESSAGE_ID);
                    mNotificationManager.cancel(MVaccine.MESSAGE_ID1);
                }

                if(LogoutService.timer != null) {
                    LogoutService.timer.onFinish();
                }

                SessionMan.setbLogin(false);
                commonData.setLogined(false);
                CommonData.isNoBindMDM = true;

                activityClear();
                System.exit(0);
            }
        });
    }

    public void finishActivity(String simpleName) {
        for(int i = 0; i < actList.size(); i++) {
            Log.d("actList [" + i + "]" + actList.get(i).getClass().getSimpleName());
            if(actList.get(i).getClass().getSimpleName().equals(simpleName)) {
                AppCompatActivity activity = actList.get(i);
                if(activity != null && !activity.isDestroyed()) {
                    Log.d("actList finish activity = " + activity.getClass().getSimpleName());
                    activity.finish();
                    actList.remove(i);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//        SSLConnect sslConnect = new SSLConnect();
//        sslConnect.postHttps(NetworkConst.getInstance().getMDMAppInfoUrl(), 10000, 10000);

        actList.add(this);

        if(true) {
            for(int i = 0; i < actList.size(); i++) {
                Log.d("actList [" + i + "]" + actList.get(i).getClass().getSimpleName());
            }
        }

        mContext = this;

        Log.d("상위클래스 이름 = " + mContext.getClass().getSuperclass().getSimpleName());

        // 태블릿 / 폰을 체크해서 폰이면 세로 태블릿이면 가로로 설정
        RotationUtil.CheckRotation(this);

        mLayoutInflater	=	(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 브로드캐스트 등록
        mFilter = new IntentFilter();
        mFilter.addAction(CommonData.BROADCAST_ACTIVITY_FINISH);
        mFilter.addAction(CommonData.BROADCAST_ACTIVITY_LOGOUT);
        mFilter.addAction(CommonData.BROADCAST_ACTIVITY_UPDATE_SESSION_TIME);
        mFilter.addAction(CommonData.BROADCAST_ACTIVITY_WEBVIEW_CHANGE_URL);
        registerReceiver(mBroadcastReceiver, mFilter);
        Log.i("브로드캐스트 등록 (앱종료/로그아웃/세션타임전달)");
    }

    /**
     * 클릭 이벤트 중복 방지
     *
     * @param enabled
     *
     * true = 클릭 가능, false = 클릭 불가
     */
    public void setButtonClickEnabled(boolean enabled) {

        // 클릭을 활성화 할 때 0.5초의 delay가 있도록 한다. (연속클릭 방지 목적)
        if ( enabled )
            mButtonClickEnabledHandler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    Log.i("button click enable");
                    mButtonClickEnabled = true;
                }
            }, 500);
        else {
            Log.i("button click disable");
        }
        mButtonClickEnabled = false;

    }

    /**
     * 클릭 이벤트 활성 여부 가져오기
     *
     * @return
     * true = 클릭 가능, false = 클릭 불가
     */
    public boolean getButtonClickEnabled() {
        return mButtonClickEnabled;
    }

    public RelativeLayout getProgressLayout(){
        if(mProgressLayout != null){
            return mProgressLayout;
        }
        return null;
    }

    @Override
    public void setContentView(int layoutResID) {


        //super.setContentView(layoutResID);
        View view = mLayoutInflater.inflate(layoutResID, null);

        if(false) {
            // 기본 원형 프로그래스바 사용 시
            View loadingView = mLayoutInflater.inflate(R.layout.dialog_progress_layout, null);
            ((ViewGroup) view).addView(loadingView);

            mProgressLayout = (RelativeLayout) loadingView.findViewById(R.id.progressbar_layout);
            // 레이아웃에 클릭 이벤트를 줘서 아래 버튼들이 눌리지 않도록 한다.
            mProgressLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    return;
                }
            });

            mProgressLayout.setVisibility(View.INVISIBLE);
        }

        setContentView(view);

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        FontUtil.getInstance().setGlobalFont(root);						// 커스텀 폰트 적용

    }

    /**
     * 실행된 activity 모두 메모리 해제
     */
    public void activityClear() {
        for(int i = 0; i < actList.size() - 1; i++) {
            AppCompatActivity activity = actList.get(i);

            if ( activity != null )
                activity.finish();
        }
    }

    protected boolean checkIntentData(Intent intent) {
        //if(commonData.isLogined()) {
        Log.d("commonData.isLogined()");
        if(intent != null) {
            Log.d("intent != null");
            // 타 앱에서 호출 -> 앱의 특정화면으로 이동 요청
            Bundle bundleData = intent.getBundleExtra(CommonData.JSON_APP_CALL_EXTERNAL_BUNDLE_DATA);
            if(bundleData != null) {
                String method = bundleData.getString(CommonData.JSON_APP_CALL_EXTERNAL_METHOD);
                String param = bundleData.getString(CommonData.JSON_APP_CALL_EXTERNAL_PARAM);
                if(method.equals(CommonData.JSON_APP_CALL_EXTERNAL_CALL_APP)) {

                    try {
                        JSONObject jsonObject = new JSONObject(param);
                        String packageNm = (String)jsonObject.get(CommonData.JSON_APP_CALL_EXTERNAL_PACKAGE_NAME);
                        launchPortalApp(packageNm);
                    } catch (JSONException e) {
                        if(Config.DISPLAY_LOG) e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            Uri uri = intent.getData();
            if(uri != null) {
                String packageNm = uri.getQueryParameter(CommonData.JSON_APP_CALL_EXTERNAL_PACKAGE_NAME2);
                if(!StringUtils.isEmpty(packageNm)) {
                    try {
                        launchPortalApp(packageNm);
                    } catch (Exception e) {
                        if(Config.DISPLAY_LOG) e.printStackTrace();
                    }

                }

                // 보장분석 연동
                String pgId = uri.getQueryParameter(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_MPGID);
                if(!StringUtils.isEmpty(pgId)) {
                    CommonData.isEnterBojang = true;
                    Log.d("CommonData.isEnterBojang = true;");
                    String menuId = uri.getQueryParameter(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_MENU_ID);
                    String pgNm = uri.getQueryParameter(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_PGNM);
                    String ssoToken = uri.getQueryParameter(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_SSO_TOKEN);
                    String param = uri.getQueryParameter(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_PARAM);
                    String view = uri.getQueryParameter(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_VIEW);
                    String width = uri.getQueryParameter(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_WIDTH);
                    String height = uri.getQueryParameter(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_HEIGHT);
                    String systemId = uri.getQueryParameter(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_SYSTEM_ID);
                    String pageType = uri.getQueryParameter(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_PAGE_TYPE);

                    JSONObject paramMap = new JSONObject();
                    try {
                        paramMap.put(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_MENU_ID, StringUtils.isEmpty(menuId) ? "" : menuId);
                        paramMap.put(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_SSO_TOKEN, StringUtils.isEmpty(ssoToken) ? "" : ssoToken);
                        paramMap.put(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_MPGID, StringUtils.isEmpty(pgId) ? "" : pgId);
                        paramMap.put(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_PGNM, StringUtils.isEmpty(pgNm) ? "" : pgNm);
                        paramMap.put(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_PARAM, StringUtils.isEmpty(param) ? "" : param);
                        paramMap.put(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_VIEW, StringUtils.isEmpty(view) ? "" : view);
                        paramMap.put(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_WIDTH, StringUtils.isEmpty(width) ? "" : width);
                        paramMap.put(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_HEIGHT, StringUtils.isEmpty(height) ? "" : height);
                        paramMap.put(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_SYSTEM_ID, StringUtils.isEmpty(systemId) ? "" : systemId);
                        paramMap.put(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_PAGE_TYPE, StringUtils.isEmpty(pageType) ? "" : pageType);

                        CacheUtils.getInstance().put(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG, paramMap);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("------------------------------------------------");
                    Log.d("보장분석 연동 파라메터");
                    Log.d("------------------------------------------------");
                    Log.d("ssoToken = " + ssoToken + " \n" +
                            "param = " + param + " \n" +
                            "pgId = " + pgId + " \n" +
                            "pgNm = " + pgNm + " \n" +
                            "menuId = " + menuId + " \n" +
                            "view = " + view + " \n" +
                            "width = " + width + " \n" +
                            "height = " + height + " \n" +
                            "systemId = " + systemId + " \n" +
                            "pageType = " + pageType
                    );
                    Log.d("------------------------------------------------");
                    Log.d(paramMap.toString());
                    Log.d("------------------------------------------------");
                } else {
                    CommonData.isEnterBojang = false;
                }
            } else {
                CommonData.isEnterBojang = false;
            }
        }

//        if()
//            moveActivity();
            Log.d("moveActivity");
            intent.setData(null);
        //    return true;
       // } else {
       //     CommonData.isEnterBojang = false;
      //  }

        return false;
        //setIntent(null);
    }

    public void toLogin() {
        // 로그인 페이지로 이동
        startActivity(CommonData.ACTIVITY_LOGIN_BY_LOGOUT, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
        DSMUtil.BackAnimationEnd(BaseActivity.this);
        commonData.setLogined(false);
        CommonData.isNoBindMDM = true;

//        if(mCallBackFunctionListener != null)
//            mCallBackFunctionListener.onResultLogout(CommonData.API_SUCCESS_WEB_BRIDGE);
    }

    public void launchPortalApp(String packageName)
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(CommonData.JSON_APP_CALL_EXTERNAL_PARAM_USER_ID, commonData.getMDMUserId());
            jsonObject.put(CommonData.JSON_APP_CALL_EXTERNAL_PARAM_USER_NAME, commonData.getLoginUserName());
            jsonObject.put(CommonData.JSON_APP_CALL_EXTERNAL_PARAM_USER_PASSWORD, commonData.getPassword());
            jsonObject.put(CommonData.JSON_APP_CALL_EXTERNAL_PARAM_SESSION_ID, CommonData.EXTRA_APP_DEFAULT_SESSION_ID);

            String package_Nm = packageName;

            String cypherType = CipherUtil.CYPHER_TYPE_AES256;
            String cypherKey  = CommonData.EXTRA_APP_SECURITY_KEY;

            String sUrlParam = MPortalP.getInstance().encrypt(cypherType, jsonObject, cypherKey);

            intent.setData(Uri.parse("MPortalP://" + package_Nm + "?" + sUrlParam));

            startActivity(intent);
        }
        catch(Exception ex)
        {;}
    }

    protected void onResume() {

        super.onResume();
        Log.i("onResume");
        hideNavigationBar();
        // 태블릿 / 폰을 체크해서 폰이면 세로 태블릿이면 가로로 설정
        RotationUtil.CheckRotation(this);

        // start 로그인 유지 기능
        String backgroundTime = commonData.getBackgroundTIme();
        commonData.setBackgroundTime("");

        if(!backgroundTime.equals("")){ // 백그라운드 시간이 있다면
            String currentTime = DSMUtil.getKorDateFormat();

            Date backgroundDate = DSMUtil.getDateFormat(backgroundTime, CommonData.PATTERN_TIME);
            Date currentDate = DSMUtil.getDateFormat(currentTime, CommonData.PATTERN_TIME);

            long subtractTime = currentDate.getTime() - backgroundDate.getTime();

            int day = (int) subtractTime/(1000*60*60);
            int hour = (int) (subtractTime/(1000*60*60) % 24);
            int minutes = (int) (subtractTime/(1000*60) % 60);
            int seconds = (int) (subtractTime/(1000) % 60);

            // 로그인 유지 조건
            // TODO 일정 시간이 지나면 다시 MDM 로그인 하도록 API 연동 구현
            if(day >= 1){
                Log.i("autoLoginCheckToView()");
                ArrayList<NameValuePair> params;

                String id 		= CommonData.getInstance().getId();
                //String password		= CommonData.getInstance().getPassword();

                Log.i("id = " + id);
            }

            Log.i("\nday = " +day);
            Log.i("\nhour = " + hour);
            Log.i("\nminutes = " + minutes);
            Log.i("\nseconds = " + seconds);
        }

        // end 로그인 유지 기능

    }

    @Override
    protected void onStop() {

        super.onStop();
        //hideProgress();
        Log.i("onStop");

        // start 로그인 유지 기능
        if(!DSMUtil.isApplicationForeground(this)){    // 어플리케이션이 백그라운드로 빠졌다면
            commonData.setBackgroundTime(DSMUtil.getKorDateFormat());
            Log.i("backgroundTime = " +commonData.getBackgroundTIme());
        }
        // end 로그인 유지 기능
    }

    @Override
    protected void onRestart() {

        super.onRestart();
    }

    public void showProgressOld() {
        if(mProgressLayout != null)
            mProgressLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgressOld() {
        try {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mProgressLayout != null)
                        mProgressLayout.setVisibility(View.INVISIBLE);
                }
            });

        } catch (Exception e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
    }

    public DSMProgressDialog getProgressDialog() {
        return progressDialog;
    }

    /**
     *  로딩 animation 활성화
     */
    public synchronized void showProgress() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if ((mContext != null) && (!((BaseActivity)mContext).isFinishing())) {
                        if (progressDialog == null) {
                            progressDialog = new DSMProgressDialog(mContext);
                            progressDialog.show();
                        } else {
                            progressDialog.show();
                        }
                    }
                }
            });

        } catch (Exception e) {
            if(Config.DISPLAY_LOG) if(Config.DISPLAY_LOG) e.printStackTrace();
        }

        hideStatusBar();
    }

    /**
     *  로딩 animation 비활성화
     */
    public synchronized void hideProgress() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if ((mContext != null) && (!((BaseActivity)mContext).isFinishing())) {

                        if (progressDialog != null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                        }
                    }

                    progressDialog = null;
                }
            });

        } catch (Exception e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
            //progressDialog = null;
        } finally {
            //progressDialog = null;
        }
    }


    @Override
    protected void onPause() {

        super.onPause();
    }


    @Override
    protected void onDestroy() {

        if ( mBroadcastReceiver != null )
            unregisterReceiver(mBroadcastReceiver);
        if ( mProgressLayout != null)
            mProgressLayout = null;

        super.onDestroy();
    }

    public void setContentView(int layoutResID, Context mContext) {

        super.setContentView(layoutResID);

        // 커스텀 폰트 적용
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        FontUtil.getInstance().setGlobalFont(root);

    }

    /**
     * 액티비티 종료 방송 리스너
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //Log.i(action);
            if ( action.equals(CommonData.BROADCAST_ACTIVITY_FINISH)) {
                Log.i("receive Broadcast activity finish");

                finish();
            } else if( action.equals(CommonData.BROADCAST_ACTIVITY_LOGOUT)) {
                Log.i("receive Broadcast activity logout");

                showPopup(CommonData.POPUP_TYPE_LOGOUT, CommonData.PARAM_STRING_NONE);

            } else if( action.equals(CommonData.BROADCAST_ACTIVITY_UPDATE_SESSION_TIME)) {

                if(!Config.ENABLE_DEMO) {
                    StringBuilder strMsg = new StringBuilder();
                    if(SessionMan.isbLogin()) {
                        if(((DSMApplication)getApplicationContext()).isSessionValid(strMsg, "noTimeUpdate")) {
                            if(Config.DISPLAY_LOG && Config.DISPLAY_LOG_SESSION_TIME_CHECK) {
                                Log.i("receive Broadcast activity sessionTime : " + intent.getStringExtra("sessionTime"));
                                if(mCallBackFunctionListener != null) {
                                    mCallBackFunctionListener.onCallSetSessionTime(intent.getStringExtra("sessionTime"));
                                }
                            }
                        } else {
                            ((DSMApplication)getApplicationContext()).stopSessionTimer();
                            Intent i = new Intent(CommonData.BROADCAST_ACTIVITY_LOGOUT);
                            sendBroadcast(i);
                        }
                    }
                }
            } else if( action.equals(CommonData.BROADCAST_ACTIVITY_WEBVIEW_CHANGE_URL)) {
                if(mWebViewListener != null) {
                    mWebViewListener.callPostMain();
                }
            }
        }
    };

    /**
     * 화면 이동
     * @param ACTIVITY_ID    화면 ID
     */
    public void startActivity(int ACTIVITY_ID, Object param1, Object param2, Object param3, Object param4, Object param5, Object param6) {
        Intent intent = null;
        boolean bContinue = false;
        MOVE_ACTIVITY_ID = ACTIVITY_ID;

        if(ACTIVITY_ID == CommonData.ACTIVITY_MAGIC_DR || ACTIVITY_ID == CommonData.ACTIVITY_MAGIC_BC_READER || ACTIVITY_ID == CommonData.ACTIVITY_MAGIC_ID_READER) {
            if (!PermissionUtils.canAccessCamera(BaseActivity.this)) {  // 카메라 정보 권한이 없다면
                HandlerUtils.post(runPermissionCheck);
            } else {
                bContinue = true;
            }
        }

        switch (ACTIVITY_ID) {
            //  명함인식기
            case CommonData.ACTIVITY_MAGIC_BC_READER:
                if(bContinue) {
                    intent = new Intent(BaseActivity.this, BCCaptureActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(intent, CommonData.ACTIVITY_MAGIC_BC_READER);
                }
                break;

            //  A4 처리
            case CommonData.ACTIVITY_MAGIC_DR:
                if(bContinue) {
                    intent = new Intent(BaseActivity.this, IDRCaptureActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(intent, CommonData.ACTIVITY_MAGIC_DR);
                }
                break;

            //  신분증 인식
            case CommonData.ACTIVITY_MAGIC_ID_READER:

                if(bContinue) {
                    intent = new Intent(BaseActivity.this, MagicIDReaderCameraMainActivity.class);
                    intent.putExtra(IDRCaptureActivity.REQ_KEY_JPG_QUALITY, 95);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivityForResult(intent, CommonData.ACTIVITY_MAGIC_ID_READER);
                }
                break;

            //  로그인
            case CommonData.ACTIVITY_LOGIN:

                intent = new Intent(BaseActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("moveBack", false);

                finish();
                activityClear();

                startActivity(intent);

                break;

            //  로그아웃->로그인
            case CommonData.ACTIVITY_LOGIN_BY_LOGOUT:

                intent = new Intent(BaseActivity.this, LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("moveBack", true);
                finish();
                activityClear();

                startActivity(intent);

                break;

            //  카메라 / 갤러리
            case CommonData.ACTIVITY_CAMERA_GALLERY:

                intent = new Intent(BaseActivity.this, CameraGalleryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(CommonData.JSON_CAMERA_GALLERY_TYPE, (Integer)param1);
                startActivityForResult(intent, CommonData.ACTIVITY_CAMERA_GALLERY);

                break;

            // 문자 보내기
            case CommonData.ACTIVITY_SEND_SMS:
                try {
                    intent = new Intent(Intent.ACTION_VIEW);
                    //intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto://" + (String)param1));
                    intent.putExtra("address", (String) param1);
                    intent.putExtra("sms_body", (String) param2);
                    intent.setType("vnd.android-dir/mms-sms");
                    startActivity(intent);
                }catch(Exception e){
                    mDialog = new CustomAlertDialog(BaseActivity.this, CustomAlertDialog.TYPE_A);
                    mDialog.setTitle("발송실패");
                    mDialog.setContent("Wifi버전(Pad)에서는 SMS를 발송할 수 없습니다.");
                    mDialog.setPositiveButton("확인", dialogClickListener);

                    mDialog.setId(CommonData.POPUP_TYPE_DEFAULT_FINISH);
                    mDialog.show();
                    hideProgress();
                }
                break;

            // 카카오톡 공유화면 호출
            case CommonData.ACTIVITY_KAKAOTALK_SHARE:

                intent = new Intent(mContext, KakaoTalkShareActivity.class);
                intent.putExtra(CommonData.KAKAO_CONTENTS,  (String)param1);
                intent.putExtra(CommonData.KAKAO_LINK_URL, (String)param2);
                intent.putExtra(CommonData.KAKAO_PHOTO_URL, (String)param3);
                intent.putExtra(CommonData.KAKAO_TITLE, (String)param4);
//                intent.putExtra(CommonData.KAKAO_PHOTO_URL, NetworkConst.getInstance().getImgDomain() + mDummyStoryItem.getmFoods_Photo());
                intent.putExtra(CommonData.KAKAO_MEMBER_ID,  CommonData.getInstance().getId());
                startActivityForResult(intent, CommonData.ACTIVITY_KAKAOTALK_SHARE);

                break;

            case CommonData.ACTIVITY_CONTRACT:

                intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, CommonData.ACTIVITY_CONTRACT);

                break;

            // PK 시험 앱 호출
            case CommonData.ACTIVITY_PK_TEST:

                try {
                    if(!StringUtils.isEmpty(AppUtils.getAppName(CommonData.APP_PACKAGE_NAME_PK_TEST))) {

                        String user_Id = commonData.getMDMUserId();
                        String user_Pwd = commonData.getPassword();

                        Log.d("PK 시험 ==> user_Id = " + user_Id + " user_Pwd = " + user_Pwd);

                        String url = "dongbu://pk_mobile?u=" + user_Id + "&p=" + Encoder1(user_Pwd);

                        Log.d("[PK 시험 앱 호출] url = " + url);

                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivityForResult(intent, CommonData.ACTIVITY_PK_TEST);
                    } else {
                        showPopup(CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_MDM, CommonData.APP_NAME_PK_TEST);
                    }

                } catch (ActivityNotFoundException e) {

                }
                break;

            // 가상화시스템 앱 호출
            case CommonData.ACTIVITY_VIRTUAL_SYSTEM:

                try {
                    if(!StringUtils.isEmpty(AppUtils.getAppName(CommonData.APP_PACKAGE_NAME_VIRTUAL_SYSTEM))) {

                        String user_Id = commonData.getMDMUserId();
                        String user_Pwd = commonData.getPassword();

                        Log.d("가상화시스템 ==> user_Id = " + user_Id + " user_Pwd = " + user_Pwd);

                        user_Pwd = Encoder(user_Pwd);

                        // 화면 해상도에 대한 변수 sesres 설정
                        // 화면 가로 해상도가 1280 보다 크면 sesres값을 "6"(고해상도)
                        // 아니면 "0"(저해상도)
                        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
                                .getDefaultDisplay();
                        int displayWidth = display.getWidth();
                        String sesres = "0";
                        String model = android.os.Build.MODEL;

                        if (displayWidth > 1280)
                        {
                            sesres = "6";
                        }
                        else
                        {
                            sesres = "0";
                        }

                        //String no = "6";  // 3:고객관리, 4: 활동지원, 5:자동차보험 , 6:장기보험, 7:일반보험
                        String no = (String)param1;

                        String url = "Mdongbureceiver://launchapp?appname="
                                + URLEncoder.encode("동부화재업무시스템")   // 변경하면 호출이 안된다.
                                + "&s=" + "https://dbcloud.mdongbu.com"
                                + "&u=" + user_Id
                                + "&p=" + user_Pwd
                                + "&d=dbins&gw=0&sesres=" + sesres
                                + "&orientation=2&askexit=1&audio=3&awake=1&sdcard=2&target="+ no;

                        Log.d("[가상화시스템 앱 호출] url = " + url);

                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivityForResult(intent, CommonData.ACTIVITY_VIRTUAL_SYSTEM);
                    } else {
                        showPopup(CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_MDM, CommonData.APP_NAME_VIRTUAL_SYSTEM);
                    }


                } catch (ActivityNotFoundException e) {

                }
                break;

            // 전자서명 앱 호출
            case CommonData.ACTIVITY_ESIGN:

                try {
                    if(!StringUtils.isEmpty(AppUtils.getAppName(CommonData.APP_PACKAGE_NAME_ESIGN))) {

                        String user_Id = commonData.getMDMUserId();
                        String user_Pwd = commonData.getPassword();

//                        String td_bonbu_cd = "2345";
//                        String td_jijum_cd = "2345";
//                        String so_jibu_cd  = "2345";

                        String url = "DongbuEssSign://launcherapp?"
                                +"u=" + user_Id
                                + "&p=" + Encoder1(user_Pwd)
                                + "&td_display_name=" + "전자서명"
                                + "&td_bonbu_cd=" + param2
                                + "&td_jijum_cd=" + param3
                                + "&so_jibu_cd=" + param4
                                + "&auth=" + ""
                                + "&subjectDN=" + ""
                                + "&mode=" + param5;

                        if(!StringUtils.isEmpty((String)param6))
                            url += "&AppType=" + param6;

                        Log.d("[전자서명 앱 호출] url = " + url);

                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivityForResult(intent, CommonData.ACTIVITY_ESIGN);
                    } else {
                        showPopup(CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_MDM, CommonData.APP_NAME_ESIGN);
                    }


                } catch (ActivityNotFoundException e) {

                }
                break;

            // 이프로미광장 앱 호출
            case CommonData.ACTIVITY_EPROMY:

                try {
                    String user_Id = commonData.getMDMUserId();
                    String user_Pwd = commonData.getPassword();

                    String url = "DongbuEPromy://launcherapp?u=" + user_Id + "&p=" + Encoder1(user_Pwd);

                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivityForResult(intent, CommonData.ACTIVITY_EPROMY);

                } catch (ActivityNotFoundException e) {

                }
                break;

            // 이메일 보내기 호출
            case CommonData.ACTIVITY_SEND_EMAIL:

                ShareCompat.IntentBuilder.from(this)
                        .setType("message/rfc822")
                        .addEmailTo((String)param1)
                        .setSubject((String)param2)
                        .setText((String)param3)
                        //.setHtmlText(body) //If you are using HTML in your body text
                        .setChooserTitle("타이틀 선택")
                        .startChooser();

                break;

            // DB러닝 앱 호출
            case CommonData.ACTIVITY_DBLEARNING:

                try {
                    if(!StringUtils.isEmpty(AppUtils.getAppName(CommonData.APP_PACKAGE_DB_LEARNING))) {

                        String url = CommonData.APP_CALL_DB_LEARNING;
                        Log.d("[디비러닝 앱 호출] url = " + url);

                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivityForResult(intent, CommonData.ACTIVITY_DBLEARNING);
                    } else {
                        showPopup(CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_WEBINSTALL, CommonData.APP_NAME_DB_LEARNING);
                    }

                } catch (ActivityNotFoundException e) {
                    Log.d("[디비러닝 앱 Exe]" + e.getMessage());
                }
                break;

            // 아보카도 앱실행
            case CommonData.ACTIVITY_DEEPMEDI:

                try {
                    if(!StringUtils.isEmpty(AppUtils.getAppName(CommonData.APP_PACKAGE_DEEP_MEDI))) {

                        String user_Id = commonData.getMDMUserId();
                        String urlId = user_Id+"_deepmedi_dp$$" ;
                        Log.d("[아보카도 앱 호출] tempId = " + urlId);

                        intent = new Intent("android.intent.action.AVOCADO");
                        intent.putExtra("id",urlId);

                        startActivityForResult(intent, CommonData.ACTIVITY_DEEPMEDI);
                    } else {
                        showPopup(CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_MARKET, CommonData.APP_NAME_DEEP_MEDI);
                    }

                } catch (ActivityNotFoundException e) {
                    Log.d("[디비러닝 앱 Exe]" + e.getMessage());
                }
                break;
                
            // 포탈 앱 실행
            case CommonData.ACTIVITY_PORTAL_APP_LAUNCH:

                try {
                    if(SessionMan.isbLogin()) {
                        JSONObject loginInfo = CacheUtils.getInstance().getJSONObject(CommonData.JSON_LOGIN_RES_INFO);
                        String name = "";
                        if(loginInfo != null) {
                            name = loginInfo.getString(CommonData.JSON_LOGIN_RES_USER_NAME);
                        }
                        if(name == null || StringUtils.isEmpty(name)) {
                            name = "";
                        }

                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        String sUser_Id = commonData.getMDMUserId();
                        String sUser_Nm = name;
                        //String sSession_Id = SessionMan.getSessionId(getApplicationContext());
                        String sSession_Id = CommonData.EXTRA_APP_DEFAULT_SESSION_ID;

                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("user_Id", sUser_Id);
                        jsonObj.put("user_Nm", sUser_Nm);
                        jsonObj.put("session_Id", sSession_Id);

                        String package_Nm = (String)param1;

                        String cypherType = CipherUtil.CYPHER_TYPE_AES256;
                        String cypherKey  = CommonData.EXTRA_APP_SECURITY_KEY;

                        String sUrlParam = MPortalP.getInstance().encrypt(cypherType,jsonObj, cypherKey);

                        intent.setData(Uri.parse("MPortalP://" + package_Nm + "?" + sUrlParam));

                        startActivity(intent);
                    } else {
                        PopupUtils.show(this, "로그인을 진행해 주세요.", new CustomAlertDialogInterface.OnClickListener() {
                            @Override
                            public void onClick(CustomAlertDialog dialog, Button button) {
                                dialog.dismiss();
                            }
                        }, CommonData.POPUP_TYPE_MDM_NOT_INSTALLED);
                    }

                } catch (JSONException e) {
                    if(Config.DISPLAY_LOG) e.printStackTrace();
                } catch (Exception e) {
                    if(Config.DISPLAY_LOG) e.printStackTrace();
                }

        }

        hideProgress();
    }

    public void showCameraGalleryPopupFromWebview(CustomAlertDialogInterface.OnClickListener positive, CustomAlertDialogInterface.OnClickListener negative, CustomAlertDialogInterface.OnClickListener cancel) {
        Log.d("[showCameraGalleryPopupFromWebview] start");
        mDialog = new CustomAlertDialog(BaseActivity.this, CustomAlertDialog.TYPE_B);
        mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
        mDialog.setContent(getString(R.string.popup_dialog_select_camera_gallery));
        mDialog.setPositiveButton(getString(R.string.popup_dialog_camera),positive);
        mDialog.setNegativeButton(getString(R.string.popup_dialog_gallery), negative);
        mDialog.setBackButtonClickListener(cancel);
        mDialog.show();
    }

//    // 길라잡이 앱 호출
//    public void callGilajabi()
//    {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//
//        String link_url =
//                "dongbugilajabi://launcherapp?headCd=" + td_bonbu_cd
//                        + "&agencyCd=" + td_jijum_cd
//                        + "&branchCd=" + so_jibu_cd
//                        + "&empNo="    + userId;
//
//        intent.setData(Uri.parse(link_url));
//        startActivityForResult(intent,10);
//    }
//
//    // 이프로미 광장 앱 호출
//    public void callEPromy()
//    {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//
//        String link_url =
//                "DongbuEPromy://launcherapp?td_display_name="+ td_display_name
//                        + "&td_bonbu_cd=" + td_bonbu_cd
//                        + "&agencyCd=" + td_jijum_cd
//                        + "&branchCd=" + so_jibu_cd
//                        + "&empNo=" + userId));
//
//        intent.setData(link_url);
//        startActivityForResult(intent, 10);
//    }
//
//    // PK시험 앱 호출
//    public void callPK()
//    {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        String vsecureUserPW = passDec(strChipherP, nRealLenP);
//
//        String link_url =
//                "dongbu://pk_mobile?u=" + userId
//                        + "&p=" + Encoder1(vsecureUserPW)));
//
//        intent.setData(Uri.parse(link_url));
//        startActivityForResult(intent, 10);
//    }
//
//    // 가상화 시스템 호출
//    public void callVirtualSystem(int no)
//    {
//        Intent intent = new Intent();
//
//        String vsecureUserPW = Encoder(passDec(strChipherP, nRealLenP));
//        String link_url =
//                "Mdongbureceiver://launchapp?appname="+ URLEncoder.encode("동부화재업무시스템")
//                        + "&s=https://dbcloud.mdongbu.com"
//                        + "&u=" + userId
//                        + "&p=" + vsecureUserPW
//                        + "&d=dbins"
//                        + "&gw=0"
//                        + "&sesres=" + sesres
//                        + "&orientation=2"
//                        + "&askexit=1"
//                        + "&audio=3"
//                        + "&awake=1"
//                        + "&sdcard=2"
//                        + "&target=" + no;
//
//        intent.setData(Uri.parse(link_url));
//        startActivityForResult(intent, 9);
//    }
//
//    // 전자서명 앱 호출
//    public void callDigitSign(String modename)
//    {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//
//        String vsecureAuthPW = passDec(strChipherC, nRealLenC);
//        String vsecureUserPW = passDec(strChipherP, nRealLenP);
//
//        String link_url =
//                "DongbuEssSign://launcherapp?u=" + userId
//                        + "&p=" + Encoder1(vsecureUserPW)
//                        + "&td_display_name=" + td_display_name
//                        + "&td_bonbu_cd=" + td_bonbu_cd
//                        + "&td_jijum_cd=" + td_jijum_cd
//                        + "&so_jibu_cd=" + so_jibu_cd
//                        + "&auth=" + Encoder1(vsecureAuthPW)
//                        + "&subjectDN=" + Encoder1(mSubjectDN)
//                        + "&mode=" + modename
//
//        intent.setData(link_url);
//        startActivityForResult(intent, 10);
//    }

    public String Encoder(String data)
    {
        Base64Coder code = new Base64Coder();
        return code.encodeString(data);
    }

    public String Encoder1(String data)
    {
        Base64Coder1 code = new Base64Coder1();
        return code.encodeString(data);
    }

    Runnable runPermissionCheck = new Runnable() {
        @Override
        public void run() {
         /*------------------------- 권한 관련 초기화 처리  ----------------------------*/

            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    startActivity(MOVE_ACTIVITY_ID, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    Log.i("권한 획득 거부 or 취소");
                    mDialog = new CustomAlertDialog(mContext, CustomAlertDialog.TYPE_A);
                    mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                    mDialog.setContent(getString(R.string.popup_dialog_permission_content));
                    mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

                        @Override
                        public void onClick(CustomAlertDialog dialog, Button button) {
                            mDialog.dismiss();
                            //finish();
                        }
                    });
                    mDialog.show();
                }
            };

            new TedPermission(mContext)
                    .setPermissionListener(permissionlistener)
                    .setPermissions(
                            Manifest.permission.CAMERA
                    )
                    .check();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("[BaseActivity] result:" + requestCode + " result:" + resultCode);
                if(requestCode == CommonData.ACTIVITY_MAGIC_ID_READER) {
                    //  신분증 인식
                    if(resultCode == RESULT_OK) {
                        CardDTO dto = Const.resultDTO;
                        OCRItem ocrItem = new OCRItem();
                        String title = dto.getText(Const.ITEM_TYPE.TITLE);
                        ocrItem.setTitle(title);
                        String name = dto.getText(Const.ITEM_TYPE.NAME);
                        ocrItem.setName(name);
                        String regNum = dto.getText(Const.ITEM_TYPE.REGNUM);
                        ocrItem.setRegNum(regNum);
                        String issueDate = dto.getText(Const.ITEM_TYPE.ISSUEDATE);
                        ocrItem.setIssueDate(issueDate);
                        String licenseNum = "";
                        if(!title.equals("주민등록증")) {
                            licenseNum = dto.getText(Const.ITEM_TYPE.LICENSENUM);
                            ocrItem.setLicenseNum(licenseNum);
                        }
                //String imagePath = dto.getImagePath();
                //ocrItem.setImagePath(imagePath);

                Log.d("------------------ 신분증 인식 결과 ------------------------");
                Log.d("title = " + title);
                Log.d("name = " + name);
                Log.d("regNum = " + regNum);
                Log.d("licenseNum = " + licenseNum);
                Log.d("issueDate = " + issueDate);
                //Log.d("imagePath = " + imagePath);
                Log.d("------------------------------------------------------------");

                mCallBackFunctionListener.onResultDoOcrCall(ocrItem, CommonData.API_SUCCESS_WEB_BRIDGE);
            } else {
                Log.d("------------------ 신분증 인식 결과 ------------------------");
                Log.d("인식 취소 / 실패");
                Log.d("------------------------------------------------------------");
                mCallBackFunctionListener.onResultDoOcrCall(null, CommonData.API_FAIL_WEB_BRIDGE);
            }

        } else if(requestCode == CommonData.ACTIVITY_MAGIC_BC_READER) {
            //  명함인식기
            if(resultCode == RESULT_OK) {
                BCResultItem bcResultItem = (BCResultItem) data.getSerializableExtra("BCResult");
                String bcName = bcResultItem.getBcName();
                String bcCompany = bcResultItem.getBcCompany();
                String bcMobile = bcResultItem.getBcMobile();
                String bcDepartment = bcResultItem.getBcDepartment();
                String bcEmail = bcResultItem.getBcEmail();
                String bcComTel = bcResultItem.getBcComTel();
                String bcWebPage = bcResultItem.getBcWebpage();
                String bcBcJotTitle = bcResultItem.getBcJobTitle();
                String bcAddress = bcResultItem.getBcAddress();
                String bcPostCode = bcResultItem.getBcPostCode();
                String bcFax = bcResultItem.getBcFax();
                String bcSNS = bcResultItem.getBcSNS();

                Log.d("------------------ 명함 인식 결과 ------------------------");
                Log.d("bcName = " + bcName);
                Log.d("bcCompany = " + bcCompany);
                Log.d("bcMobile = " + bcMobile);
                Log.d("bcDepartment = " + bcDepartment);
                Log.d("bcEmail = " + bcEmail);
                Log.d("bcComTel = " + bcComTel);
                Log.d("bcWebPage = " + bcWebPage);
                Log.d("bcBcJotTitle = " + bcBcJotTitle);
                Log.d("bcAddress = " + bcAddress);
                Log.d("bcPostCode = " + bcPostCode);
                Log.d("bcFax = " + bcFax);
                Log.d("bcSNS = " + bcSNS);
                Log.d("------------------------------------------------------------");

                BCResultWebItem bcResultWebItem = new BCResultWebItem();
                bcResultWebItem.set(bcResultItem);
                mCallBackFunctionListener.onResultDoIdCall(bcResultWebItem, CommonData.API_SUCCESS_WEB_BRIDGE);

            } else {
                Log.d("------------------ 명함 인식 결과 ------------------------");
                Log.d("인식 취소 / 실패");
                Log.d("------------------------------------------------------------");
                mCallBackFunctionListener.onResultDoIdCall(null, CommonData.API_FAIL_WEB_BRIDGE);
            }


        } else if(requestCode == CommonData.ACTIVITY_MAGIC_DR) {
            //  A4 처리
            if(resultCode == RESULT_OK) {
                ArrayList<String> files = data.getStringArrayListExtra(IDRCaptureActivity.RET_KEY_RESULT_FILES);

                Log.d("------------------ A4 인식 결과 ------------------------");
                Log.d("인식 갯수 : " + files.size());
                for(int i = 0; i < files.size(); i++) {
                    Log.d(files.get(i));
                }
                Log.d("------------------------------------------------------------");

                paperImagesUpload(files, networkListener);
                //mCallBackFunctionListener.onResultDoPaperCall(files, CommonData.API_SUCCESS_WEB_BRIDGE);
            } else {
                Log.d("------------------ A4 인식 결과 ------------------------");
                Log.d("인식 취소 / 실패");
                Log.d("------------------------------------------------------------");
                mCallBackFunctionListener.onResultDoPaperCall(null, CommonData.API_FAIL_WEB_BRIDGE);
            }
        } else if(requestCode == CommonData.ACTIVITY_CAMERA_GALLERY) {
            if(resultCode == RESULT_OK) {
                String imageFilePath = data.getStringExtra(CommonData.JSON_CAMERA_GALLERY_IMAGE_PATH);
                Log.d("------------------ 카메라 / 갤러리 이미지 가져오기 경로 ------------------------");
                Log.d("이미지 경로 : " + imageFilePath);
                Log.d("------------------------------------------------------------");

                mCallBackFunctionListener.onResultDoCameraGalleryCall(imageFilePath, CommonData.API_SUCCESS_WEB_BRIDGE);
            } else {
                Log.d("------------------ 카메라 / 갤러리 이미지 가져오기 경로 ------------------------");
                Log.d("이미지 가져오기 취소 / 실패");
                Log.d("------------------------------------------------------------");
                mCallBackFunctionListener.onResultDoCameraGalleryCall(null, CommonData.API_FAIL_WEB_BRIDGE);

            }
        } else if(requestCode == CommonData.ACTIVITY_CONTRACT) {

            if(resultCode == RESULT_OK) {

                Cursor cursor = getContentResolver().query(data.getData(),
                        new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
                cursor.moveToFirst();

                Contract contract = new Contract();
                contract.setName(cursor.getString(0));      //0은 이름을 얻어옵니다.
                contract.setTel(cursor.getString(1));       //1은 번호를 받아옵니다.

                cursor.close();

                mCallBackFunctionListener.onResultGetContractCall(contract, CommonData.API_SUCCESS_WEB_BRIDGE);
            } else {
                mCallBackFunctionListener.onResultGetContractCall(null, CommonData.API_FAIL_WEB_BRIDGE);
            }
        } else if(requestCode == CommonData.ACTIVITY_SEND_SMS) {
            if(resultCode == RESULT_OK) {
                Log.d("----------------------- 문자 보내기 ------------------------");
                Log.d("문자 보내기 성공");
                Log.d("------------------------------------------------------------");

                mCallBackFunctionListener.onResultDefault(CommonData.API_SUCCESS_WEB_BRIDGE);
            } else {
                Log.d("------------------ 카메라 / 갤러리 이미지 가져오기 경로 ------------------------");
                Log.d("문자 보내기 취소 / 실패");
                Log.d("------------------------------------------------------------");
                mCallBackFunctionListener.onResultDefault(CommonData.API_FAIL_WEB_BRIDGE);

            }
        } else if(requestCode == CommonData.ACTIVITY_KAKAOTALK_SHARE) {
            if(resultCode == RESULT_OK) {
                Log.d("--------------------- 카카오톡 보내기 ----------------------");
                Log.d("카카오톡 보내기 성공");
                Log.d("------------------------------------------------------------");

                mCallBackFunctionListener.onResultDefault(CommonData.API_SUCCESS_WEB_BRIDGE);
            } else {
                Log.d("------------------ 카카오톡 보내기 -------------------------");
                Log.d("카카오톡 보내기 취소 / 실패");
                Log.d("------------------------------------------------------------");
                mCallBackFunctionListener.onResultDefault(CommonData.API_FAIL_WEB_BRIDGE);

            }
        } else if(requestCode == CommonData.ACTIVITY_SEND_EMAIL) {
            if(resultCode == RESULT_OK) {
                Log.d("---------------------- 이메일 보내기 -----------------------");
                Log.d("이메일 보내기 성공");
                Log.d("------------------------------------------------------------");

                mCallBackFunctionListener.onResultDefault(CommonData.API_SUCCESS_WEB_BRIDGE);
            } else {
                Log.d("---------------------- 이메일 보내기 -----------------------");
                Log.d("이메일 보내기 취소 / 실패");
                Log.d("------------------------------------------------------------");
                mCallBackFunctionListener.onResultDefault(CommonData.API_FAIL_WEB_BRIDGE);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showPopup(int popupType, String option) {

        if(mDialog != null && mDialog.isShowing()){
            return;
        }

        switch (popupType) {
            case CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_MARKET:
            {
                mDialog = new CustomAlertDialog(BaseActivity.this, CustomAlertDialog.TYPE_B);
                mDialog.setTitle(getString(R.string.popup_dialog_app_install_title));
                String contents = getString(R.string.popup_dialog_app_install_content);
                if(option.equals(CommonData.APP_NAME_BOHUM)) {
                    contents = option + " " + getString(R.string.popup_dialog_app_install_content);
                    contents += "\n\n" + getString(R.string.popup_dialog_app_install_content_bohum);
                }
                else if (option.equals(CommonData.APP_NAME_DEEP_MEDI))
                {
                    contents = option + " " + getString(R.string.popup_dialog_app_install_content);
                    CommonData.parameter.setParam1(CommonData.APP_PACKAGE_DEEP_MEDI);
                }

                mDialog.setContent(contents);
                mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), dialogClickListener);
                mDialog.setNegativeButton(getString(R.string.cancel), dialogCancelClickListener);

                mDialog.setId(CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_MARKET);
                mDialog.show();
                hideProgress();
                break;
            }
            case CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_WEBINSTALL:
            {
                mDialog = new CustomAlertDialog(BaseActivity.this, CustomAlertDialog.TYPE_B);
                mDialog.setTitle(getString(R.string.popup_dialog_app_install_title));
                String contents = getString(R.string.popup_dialog_app_install_content);
                if(option.equals(CommonData.APP_NAME_DB_LEARNING)) {
                    contents = option + " " + getString(R.string.popup_dialog_app_install_content);
                    contents += "\n\n" + getString(R.string.popup_dialog_app_install_site);
                }

                mDialog.setContent(contents);
                mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), dialogClickListener);
                mDialog.setNegativeButton(getString(R.string.cancel), dialogCancelClickListener);

                mDialog.setId(CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_WEBINSTALL);
                mDialog.show();
                hideProgress();
                break;
            }
            case CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_MDM:
            {
                mDialog = new CustomAlertDialog(BaseActivity.this, CustomAlertDialog.TYPE_B);
                mDialog.setTitle(getString(R.string.popup_dialog_app_install_title));
                String contents = option + " " + getString(R.string.popup_dialog_app_install_content);
                if(option.equals(CommonData.APP_NAME_PK_TEST)) {
                    contents += "\n\n" + getString(R.string.popup_dialog_app_install_content_pk_test);
                }
                mDialog.setContent(contents);
                mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), dialogClickListener);
                mDialog.setNegativeButton(getString(R.string.cancel), dialogCancelClickListener);

                mDialog.setId(CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_MDM);
                mDialog.show();
                hideProgress();
                break;
            }
            case CommonData.POPUP_TYPE_LOGOUT:
            {
                mDialog = new CustomAlertDialog(BaseActivity.this, CustomAlertDialog.TYPE_A);
                mDialog.setTitle(getString(R.string.popup_dialog_logout_title));
                mDialog.setContent(getString(R.string.popup_dialog_logout_content));
                mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), dialogClickListener);

                mDialog.setId(CommonData.POPUP_TYPE_LOGOUT);
                mDialog.show();
                hideProgress();
                break;
            }
            case CommonData.POPUP_TYPE_LOGIN_FINISH:
            {
                mDialog = new CustomAlertDialog(BaseActivity.this, CustomAlertDialog.TYPE_A);
                mDialog.setTitle("로그인 안내");
                mDialog.setContent(option);
                mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), dialogClickListener);

                mDialog.setId(CommonData.POPUP_TYPE_LOGIN_FINISH);
                if(!this.isFinishing())
                    mDialog.show();
                hideProgress();
                break;
            }
            case CommonData.POPUP_TYPE_LOGOUT_MANUAL:
            {
                mDialog = new CustomAlertDialog(BaseActivity.this, CustomAlertDialog.TYPE_B);
                mDialog.setTitle(getString(R.string.popup_dialog_logout_title));
                mDialog.setContent(getString(R.string.popup_dialog_logout_manual_content));
                mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), dialogClickListener);
                mDialog.setNegativeButton(getString(R.string.cancel), dialogCancelClickListener);

                mDialog.setId(CommonData.POPUP_TYPE_LOGOUT_MANUAL);
                mDialog.show();
                hideProgress();
                break;
            }
            case CommonData.POPUP_TYPE_APP_FINISH:
            {
                mDialog = new CustomAlertDialog(BaseActivity.this, CustomAlertDialog.TYPE_B);
                mDialog.setTitle(getString(R.string.popup_dialog_app_finish_title));
                mDialog.setContent(getString(R.string.popup_dialog_app_finish_content));
                mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), dialogClickListener);
                mDialog.setNegativeButton(getString(R.string.cancel), dialogCancelClickListener);

                mDialog.setId(CommonData.POPUP_TYPE_APP_FINISH);
                break;
            }
            case CommonData.POPUP_TYPE_MUST_SECURE_KEYBOARD_COMPLETE:
            {
                mDialog = new CustomAlertDialog(BaseActivity.this, CustomAlertDialog.TYPE_A);
                mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
                mDialog.setContent(getString(R.string.popup_dialog_must_secure_keyboard_complete_content));
                mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), dialogClickListener);

                mDialog.setId(CommonData.POPUP_TYPE_MUST_SECURE_KEYBOARD_COMPLETE);
                break;
            }

        }

        mDialog.show();
        hideProgress();
    }

    /**
     * 팝업 다이얼로그 클릭 리스너 ( 확인버튼 )
     */
    public CustomAlertDialogInterface.OnClickListener dialogClickListener = new CustomAlertDialogInterface.OnClickListener() {

        @Override
        public void onClick(CustomAlertDialog dialog, Button button) {

            dialog.dismiss();
            Intent 	intent = null;
            ApplicationInfo appInfo = null;
            switch ( dialog.getId() ) {
                case CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_MARKET:				        					// 외부 앱 실행 button click
                    // 외부 앱 실행 시 앱이 설치 되어 있지 않은 경우 마켓으로 유도
                    Uri uri 	= Uri.parse( "market://details?id=" + CommonData.parameter.getParam1());        // 패키지명
                    Log.i("uri = " + uri);
                    intent	= new Intent( Intent.ACTION_VIEW, uri );

                    startActivity( intent );

                    if(mCallBackFunctionListener != null)
                        mCallBackFunctionListener.onResultDoStartExternalApp(CommonData.API_SUCCESS_WEB_BRIDGE);

                    break;
                case CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_WEBINSTALL:
                    // 외부 앱 실행 시 앱이 설치 되어 있지 않은 경우 유밥(디비러닝) 설치페이지로 안내
                    //uri 	= Uri.parse( "https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_a3d369a4-951b-4eb6-8cad-a327b2e56f73/app/f2db9d0c-abc9-4f9b-894c-574378766fb9/appdownload/default.htm?1603953793");
                    uri 	= Uri.parse("https://dbins.ubobtls.com/app");
                    Log.i("uri2 = " + uri);
                    intent	= new Intent( Intent.ACTION_VIEW, uri );

                    startActivity( intent );

                    if(mCallBackFunctionListener != null)
                        mCallBackFunctionListener.onResultDoStartExternalApp(CommonData.API_SUCCESS_WEB_BRIDGE);
                    break;
                case CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_MDM:				        					// 외부 앱 실행 button click
                    if(commonData.isLogined() && Config.ENABLE_MDM_AGENT_CHECK) {
                        android.util.Log.e("YYYM", "POPUP_TYPE_INSTALL_FOR_GO_TO_MDM..." );
                        MDMUtils.getInstance(mContext, BaseActivity.this).doProc(CommonData.MDM_ACTION_LAUNCH_APP, false);
                    } else {
                        PopupUtils.show(BaseActivity.this, "MDM 을 지원하지 않는 버전입니다.");
                    }

                    break;
                case CommonData.POPUP_TYPE_LOGOUT:													            // 로그아웃 button click
                case CommonData.POPUP_TYPE_LOGOUT_MANUAL:
                    //TODO 로그아웃 처리

                    logout();
                    break;

                case CommonData.POPUP_TYPE_APP_FINISH:

                    //TODO 앱 종료 시 로그아웃 처리

                    mLogoutType = CommonData.LOGOUT_TYPE_APP_FINISH;
                    if(commonData.isLogined()) {
                        if(Config.ENABLE_MDM_AGENT_CHECK) {
                            MDMUtils.getInstance(mContext, BaseActivity.this).doProc(CommonData.MDM_ACTION_LOGOUT, false);
                        } else {
                            exitApp();
                        }

                    } else {
                        exitApp();
                    }
                    SessionMan.setbLogin(false);
                    commonData.setLogined(false);
                    break;
                case CommonData.POPUP_TYPE_LOGIN_FINISH:

                    moveActivity();
                    break;
            }
        }
    };

    public void logout() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLogoutType = CommonData.LOGOUT_TYPE_NORMAL;
//                SessionMan.setbLogin(false);
//                commonData.setLogined(false);
                ((DSMApplication)getApplicationContext()).stopSessionTimer();

                if(commonData.isLogined()) {
                    if(Config.ENABLE_MDM_AGENT_CHECK) {
                        MDMUtils.getInstance(mContext, BaseActivity.this).doProc(CommonData.MDM_ACTION_LOGOUT, false);
                    } else {
                        toLogin();
                    }
                } else {
                    toLogin();
                }
            }
        });
    }

    public void hideStatusBar(){
        if (Build.VERSION.SDK_INT < 16){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 팝업 다이얼로그 클릭 리스너 ( 취소버튼 )
     */
    public CustomAlertDialogInterface.OnClickListener dialogCancelClickListener = new CustomAlertDialogInterface.OnClickListener() {

        @Override
        public void onClick(CustomAlertDialog dialog, Button button) {

            dialog.dismiss();
            switch ( dialog.getId() ) {
                case CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_MARKET:				        					// 외부 앱 실행 button click
                    if(mCallBackFunctionListener != null)
                        mCallBackFunctionListener.onResultDoStartExternalApp(CommonData.API_FAIL_WEB_BRIDGE);
                    break;
                case CommonData.POPUP_TYPE_LOGOUT_MANUAL:
                    if(mCallBackFunctionListener != null)
                        mCallBackFunctionListener.onResultLogout(CommonData.API_FAIL_WEB_BRIDGE);
                    break;
                case CommonData.POPUP_TYPE_APP_FINISH:
                    if(mCallBackFunctionListener != null)
                        mCallBackFunctionListener.onResultAppExit(CommonData.API_FAIL_WEB_BRIDGE);
                    break;
            }
        }
    };

    /**
     * 로그인 후 앱 화면 이동처리
     */
    public void moveActivity() {
        // TODO 메인 웹뷰로 가는 처리를 여기서 할 지 검토
        Log.d("commonData.getStep() = " + commonData.getStep());
        if(commonData.isLogined()) {
            // 메인 웹뷰로 전환
            HandlerUtils.postDelayed(mStartMainWebViewActivityReoderToFrontRunnable, CommonData.INTRO_POST_DELAYED);

//            Intent i = new Intent(CommonData.BROADCAST_ACTIVITY_WEBVIEW_CHANGE_URL);
//            sendBroadcast(i);

        } else {
            if(commonData.getStep() < CommonData.ACTIVITY_LOGIN) {
                // 로그인 화면으로 전환
                if(Config.ENABLE_LOGIN) {
                    HandlerUtils.postDelayed(mStartLoginActivityRunnable, CommonData.INTRO_POST_DELAYED);
                } else {
                    HandlerUtils.postDelayed(mStartMainWebViewActivityReoderToFrontRunnable, CommonData.INTRO_POST_DELAYED);
                }
            } else if(commonData.getStep() >= CommonData.ACTIVITY_LOGIN && commonData.getStep() < CommonData.ACTIVITY_PUSH_LOGIN) {
                // 푸시 로그인 진행
                if(Config.ENABLE_PUSH) {
                    HandlerUtils.postDelayed(mStartPushLoginActivityRunnable, CommonData.INTRO_POST_DELAYED);
                } else {
                    if(Config.ENABLE_MDM_AGENT_CHECK && Config.ENABLE_PORTAL_MAIN) {
                        if(commonData.isCompanyUser()) {
                            HandlerUtils.postDelayed(mStartMainPortalLinkActivityRunnable, CommonData.INTRO_POST_DELAYED);
                        } else {
                            HandlerUtils.postDelayed(mStartMainWebViewActivityReoderToFrontRunnable, CommonData.INTRO_POST_DELAYED);
                        }
                    } else {
                        HandlerUtils.postDelayed(mStartMainWebViewActivityReoderToFrontRunnable, CommonData.INTRO_POST_DELAYED);
                    }
                }

            } else {
                // 메인 웹뷰로 전환
                HandlerUtils.postDelayed(mStartMainWebViewActivityReoderToFrontRunnable, CommonData.INTRO_POST_DELAYED);
            }
        }

        hideProgress();

    }

    /**
     * 로그인 화면이동 런어블
     */
    public Runnable mStartLoginActivityRunnable = new Runnable() {

        @Override
        public void run() {

            Log.i("mStartLoginActivityRunnable start");
            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            activityClear();
            finishActivity("IntroActivity");
            //BaseActivity.this.finish();
        }
    };

    /**
     * 푸시 로그인 화면이동 런어블
     */
    public Runnable mStartPushLoginActivityRunnable = new Runnable() {

        @Override
        public void run() {

            Log.i("mStartPushLoginActivityRunnable start");
            Intent intent = new Intent(BaseActivity.this, com.dongbu.dsm.push.activity.LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, CommonData.ACTIVITY_PUSH_LOGIN);
            //IntroBaseActivity.this.finish();
        }
    };

    /**
     * 임직원용 메인 포탈 화면이동 런어블
     */
    public Runnable mStartMainPortalLinkActivityRunnable = new Runnable() {

        @Override
        public void run() {

            Log.i("mStartMainPortalLinkActivityRunnable start");
            Intent intent = null;
            intent = new Intent(BaseActivity.this, PortalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            activityClear();
            finishActivity("IntroActivity");
        }
    };

    /**
     * 메인 웹뷰 화면이동 런어블
     */
    public Runnable mStartMainWebViewActivityRunnable = new Runnable() {

        @Override
        public void run() {

            Log.i("mStartMainWebViewActivityRunnable start");
            Intent intent = null;
            String simpleName = "";
            if(DSMUtil.isTablet()) {
                intent = new Intent(BaseActivity.this, MainBaseWebViewPadActivity.class);
                simpleName = "MainBaseWebViewPadActivity";
            } else {
                intent = new Intent(BaseActivity.this, MainBaseWebViewPhoneActivity.class);
                simpleName = "MainBaseWebViewPhoneActivity";
            }

            if(CommonData.isEnterBojang) {
                intent.putExtra(CommonData.EXTRA_URL, CommonData.WEB_SERVICE_HOME_URL_PAD);
            } else {
                intent.putExtra(CommonData.EXTRA_URL, commonData.WEB_MAIN_URL);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            activityClear();
            finishActivity(simpleName);
            //BaseActivity.this.finish();
        }
    };

    /**
     * 메인 웹뷰 화면이동 런어블 (Forground ordering)
     */
    public Runnable mStartMainWebViewActivityReoderToFrontRunnable = new Runnable() {

        @Override
        public void run() {

            Log.i("mStartMainWebViewActivityReoderToFrontRunnable start");
            Intent intent = null;
            if(DSMUtil.isTablet()) {
                intent = new Intent(BaseActivity.this, MainBaseWebViewPadActivity.class);
            } else {
                intent = new Intent(BaseActivity.this, MainBaseWebViewPhoneActivity.class);
            }

            if(CommonData.isEnterBojang) {
                intent.putExtra(CommonData.EXTRA_URL, CommonData.WEB_SERVICE_HOME_URL_PAD);
            } else {
                intent.putExtra(CommonData.EXTRA_URL, commonData.WEB_MAIN_URL);
            }

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            startActivity(intent);
            finishActivity("IntroActivity");
        }
    };

    @Override
    public void onLowMemory() {
        Log.d("-------------------------  onLowMemory ----------------------------");
        super.onLowMemory();
    }

    /**
     * 문서 업로드 API 호출
     */
    public void paperImagesUpload(ArrayList<String> photos, CustomAsyncListener networkListener) {

        showProgress();
        RequestApi.requestApi(this, NetworkConst.NET_SET_UPLOAD_PAPERS, NetworkConst.getInstance().getPaperUploadUrl(), networkListener, photos, progressDialog, 0);
    }

    /**
     * 네트워크 리스너
     */
    public CustomAsyncListener networkListener = new CustomAsyncListener() {

        @Override
        public void onPost(Context context, int type, String resultCode, JSONObject resultData, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub

            switch ( type ) {
                case NetworkConst.NET_SET_UPLOAD_PAPERS:				// 로그인
                    switch ( resultCode ) {
                        case CommonData.API_SUCCESS:
                            Log.i("API_SUCCESS");

                            try {
                                if(resultData != null) {
                                    if(resultData.has(CommonData.JS_DO_PAPER_CALL_PARAM_APDXFLID)) {
                                        Log.d("Image Upload Success");
                                        JSONObject resultJSON = new JSONObject();
                                        resultJSON.put("data", resultData.get(CommonData.JS_DO_PAPER_CALL_PARAM_APDXFLID).toString());
                                        mCallBackFunctionListener.onResultDoPaperCall(resultJSON.toString(), CommonData.API_SUCCESS_WEB_BRIDGE);
                                    }
                                }

                            } catch (Exception e) {
                                if(Config.DISPLAY_LOG) e.printStackTrace();
                            }

                            break;

                        default:
                            dialog.show();
                            break;
                    }

                    break;

            }
            hideProgress();
        }

        @Override
        public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub
            hideProgress();
            dialog.show();
        }

        @Override
        public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub
            // 데이터에 문제가 있는 경우 다이얼로그를 띄우고 인트로에서는 종료하도록 한다.
            hideProgress();
            dialog.show();
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public  void hideNavigationBar()
    {
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOption);
    }
}
