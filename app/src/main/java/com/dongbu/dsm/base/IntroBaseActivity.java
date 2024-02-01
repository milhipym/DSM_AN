package com.dongbu.dsm.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;

import com.blankj.utilcode.util.CacheUtils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.app.DSMApplication;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.CustomAlertDialog;
import com.dongbu.dsm.common.CustomAlertDialogInterface;
import com.dongbu.dsm.common.CustomAsyncListener;
import com.dongbu.dsm.common.NetworkConst;
import com.dongbu.dsm.common.SessionMan;
import com.dongbu.dsm.intro.LoginActivity;
import com.dongbu.dsm.network.RequestApi;
import com.dongbu.dsm.util.DSMUtil;
import com.dongbu.dsm.util.HandlerUtils;
import com.dongbu.dsm.util.Log;
import com.dongbu.dsm.util.MDMUtils;
import com.dongbu.dsm.util.PopupUtils;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.firebase.perf.metrics.Trace;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by LandonJung on 2017-8-10.
 * 로그인 부모 클래스
 * @since 0, 1
 */
public class IntroBaseActivity extends BaseActivity {

    public static Context mContext;
    private View  decorView;
    private int   uiOption;
    private Trace mTrace ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    /**
     * 영업용 모바일 시스템 로그인 성공 후 처리
     * @param data 로그인 정보 데이터
     * @param dialog 화면에 띄울 다이얼로그
     */
    public void JoinTologinSuccess(final JSONObject data, CustomAlertDialog dialog) {

        //TODO 로그인 성공 후 MDM 로그인 및 MDM Agent 채크하는 부분 개발

        CommonData.isSaveSession = false;

        if(data != null) {
            CacheUtils.getInstance().put(CommonData.JSON_LOGIN_RES_INFO, data);
        }

        if(Config.ENABLE_MDM_INSTALL_FROM_ASSET_APK) { // 테스트용
            PopupUtils.show(this, "MDM 앱을 설치합니다.", new CustomAlertDialogInterface.OnClickListener() {
                @Override
                public void onClick(CustomAlertDialog dialog, Button button) {
                    dialog.dismiss();
                    MDMUtils.getInstance(mContext.getApplicationContext(), IntroBaseActivity.this).installMDMfromAsset(IntroBaseActivity.this);
                }
            }, new CustomAlertDialogInterface.OnClickListener() {
                @Override
                public void onClick(CustomAlertDialog dialog, Button button) {
                    dialog.dismiss();
                    moveActivity();
                }
            });

        } else {
            if(Config.ENABLE_MDM_AGENT_CHECK) {

                showProgress();
                CommonData.isWebViewActivity = false;

                if(MDMUtils.getInstance(mContext, IntroBaseActivity.this).isBindSuccess()) {
                    MDMUtils.getInstance(this, this).doProc(CommonData.MDM_ACTION_CHECK_AGENT, false);
                } else {
                    MDMUtils.getInstance(this, this).onResume(this);
                    MDMUtils.getInstance(this, this).setMdmCallbackListener(mdmLoginCallbackListener);
                }

            } else {
                // 모든 로그인 처리 완료 후 메인으로 이동 전 데이터값 저장 및 화면 전환
                loginSuccess(data);
            }
        }
    }

    /**
     * MDM 로그인 성공 후 처리
     * @param data
     */
    public void loginSuccess(JSONObject data) {
        // TODO MDM 로그인 후 계정 정보 및 기타 정보 저장처리

        try {
            if(data != null) {
                // mdm 로그인 정보 저장
                if(data.has(CommonData.JSON_MDM_DEVICE_USER_INFOS)) {
                    JSONArray arr = data.getJSONArray(CommonData.JSON_MDM_DEVICE_USER_INFOS);
                    if(arr != null && arr.length() > 0) {
                        JSONObject info = arr.getJSONObject(0);
                        commonData.setMDMDeviceId(info.getString(CommonData.JSON_MDM_DEVICE_ID));
                        commonData.setMDMDeviceModel(info.getString(CommonData.JSON_MDM_DEVICE_MODEL));
                        // Android 단말기에서 Linux 라고 나오네...
                        //commonData.setMDMOsName(info.getString(CommonData.JSON_MDM_OS_NAME));
                        commonData.setMDMOsName(Config.OS_NAME);
                        commonData.setMDMOsType(info.getString(CommonData.JSON_MDM_OS_TYPE));
                        commonData.setMDMOsVersion(info.getString(CommonData.JSON_MDM_OS_VERSION));
                        commonData.setMDMUserId(info.getString(CommonData.JSON_MDM_USER_ID).toUpperCase());

                        // 로그인 아이디와 MDM 아이디가 다를경우 처리
                        if(!commonData.getId().equals(commonData.getMDMUserId())) {
                            hideProgress();
                            PopupUtils.show(this, "에이전트 계정정보와 로그인 계정정보가 다릅니다.\n확인하시고 다시 로그인 해 주세요.", new CustomAlertDialogInterface.OnClickListener() {
                                @Override
                                public void onClick(CustomAlertDialog dialog, Button button) {
                                    dialog.dismiss();
                                    SessionMan.setbLogin(false);
                                    commonData.setLogined(false);
                                    MDMUtils.getInstance(mContext,IntroBaseActivity.this).doProc(CommonData.MDM_ACTION_LAUNCH_APP, false);
                                }
                            });
                            return;
                        }
                        else
                        {
                            MDMUtils.getInstance(mContext,IntroBaseActivity.this).doProc(CommonData.MDM_ACTION_FORGEAPP_CHECK, false);
                        }
                    }
                }
            }

            SessionMan.setbLogin(true);
            commonData.setLogined(true);

            // 세션 생성
            StringBuilder sbError = new StringBuilder();
            if(!((DSMApplication)getApplicationContext()).createSession(sbError)) {
                Log.e("session 생성 실패 : " + sbError.toString());
            }

            //CSH20180305 cookie 세팅 위치 변경, BaseAgentWebViewActivity.doPostUrl() 에서 이동
            CookieManager.getInstance().setCookie(CommonData.WEB_MAIN_URL, CacheUtils.getInstance().getString(CommonData.JSON_LOGIN_RES_JSESSIONID));


            commonData.setPopupEventRead(false);                        // 최초로그인시 팝업이벤트 읽음제거
            commonData.setBackgroundTime("");                           // 최초로그인시 백그라운드 시점 시간 초기화

            try{

                PackageInfo pi = getPackageManager().getPackageInfo( getPackageName(), 0 );
                CommonData.getInstance().setAppVer(pi.versionName.toString());
            }catch(Exception e){
                Log.e(e.toString());
            }

        } catch (JSONException e) { Log.e(e.toString());}

        if(Config.ENABLE_PROVIDER_SESSION_TEST) {
            PopupUtils.show(this, "세션 테스트 앱을 실행합니다.", new CustomAlertDialogInterface.OnClickListener() {
                @Override
                public void onClick(CustomAlertDialog dialog, Button button) {
                    dialog.dismiss();
                    startActivity(CommonData.ACTIVITY_PORTAL_APP_LAUNCH, "com.dongbu.testcontentprovidercall", null, null, null, null, null);
                }
            }, new CustomAlertDialogInterface.OnClickListener() {
                @Override
                public void onClick(CustomAlertDialog dialog, Button button) {
                    dialog.dismiss();
                    moveActivity();
                }
            });
        } else {

            if(Config.ENABLE_MDM_AGENT_CHECK) {
                if(Config.ENABLE_APP_VERSION_CHECK_UPDATE) {
                    MDMUtils.getInstance(mContext, IntroBaseActivity.this).versionCheck();
                } else {
                    if(!CommonData.isWebViewActivity) {
                        ((LoginActivity)mContext).loginFinish();
                    }
                }
            } else {
                if(!CommonData.isWebViewActivity) {
                    ((LoginActivity)mContext).loginFinish();
                }

                //moveActivity();
            }
        }
        hideProgress();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("[IntroBaseActivity] result:" + requestCode + " result:" + resultCode);
        if(requestCode == CommonData.ACTIVITY_PUSH_LOGIN) {
            //TODO 푸시 로그인 결과 로직 구현
            if(data != null) {
                String message = data.getStringExtra(CommonData.JSON_PUSH_LOGIN_RESULT_MESSAGE);
                Log.d("JSON_PUSH_LOGIN_RESULT_MESSAGE = " + message);
                if(resultCode == RESULT_OK) {
                    HandlerUtils.postDelayed(mStartMainWebViewActivityRunnable, CommonData.INTRO_POST_DELAYED);
                } else {
                    PopupUtils.show(this, "푸시서버에 로그인을 실패했습니다.\n" + message, new CustomAlertDialogInterface.OnClickListener() {
                        @Override
                        public void onClick(CustomAlertDialog dialog, Button button) {
                            dialog.dismiss();

                            HandlerUtils.postDelayed(mStartMainWebViewActivityRunnable, CommonData.INTRO_POST_DELAYED);
                        }
                    });
                }
            } else {
                PopupUtils.show(this, "푸시서버에 로그인을 실패했습니다.\n서버가 원활하지 않습니다.", new CustomAlertDialogInterface.OnClickListener() {
                    @Override
                    public void onClick(CustomAlertDialog dialog, Button button) {
                        dialog.dismiss();

                        HandlerUtils.postDelayed(mStartMainWebViewActivityRunnable, CommonData.INTRO_POST_DELAYED);
                    }
                });

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        hideNavigationBar();
        super.onResume();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    /**
     * 로그인
     * @param id 아이디
     * @param password  암호
     * @param networkListener   리스너
     */
    @AddTrace(name = "login_2")
    public void login(String id, String encpasswd, String password, CustomAsyncListener networkListener) {
        mTrace = FirebasePerformance.getInstance().newTrace("DSM_login");
        mTrace.start();
        mTrace.putAttribute("ID",id);
        // 암호저장 이유는 PK시험 및 가상화시스템 / 이프로미 광장 호출 시 계정 암호를 파라메터로 전달하기 때문
        commonData.setPassword(password);

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(CommonData.JSON_LOGIN_ID, id));
        params.add(new BasicNameValuePair(CommonData.JSON_LOGIN_PW, (Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_SECURITY_KEY) ? encpasswd : password));
        params.add(new BasicNameValuePair(CommonData.JSON_LOGIN_OSNAME, Config.OS_NAME));
        params.add(new BasicNameValuePair(CommonData.JSON_LOGIN_DEVICE_TYPE, DSMUtil.isTablet() ? "T" : "P"));


        CommonData.isSaveSession = true;
        if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_SECURITY_KEY) {
            RequestApi.requestApi(this, NetworkConst.NET_DSM_LOGIN, NetworkConst.getInstance().getDSMLoginSecurityUrl(), networkListener, params, getProgressLayout());
        } else {
            RequestApi.requestApi(this, NetworkConst.NET_DSM_LOGIN, NetworkConst.getInstance().getDSMLoginUrl(), networkListener, params, getProgressLayout());
        }
        mTrace.stop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideNavigationBar();
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
