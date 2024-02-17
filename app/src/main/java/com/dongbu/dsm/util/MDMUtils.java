package com.dongbu.dsm.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.base.BaseActivity;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.CustomAlertDialog;
import com.dongbu.dsm.common.CustomAlertDialogInterface;
import com.dongbu.dsm.common.CustomAsyncListener;
import com.dongbu.dsm.common.NetworkConst;
import com.dongbu.dsm.common.SessionMan;
import com.dongbu.dsm.network.RequestApi;
import com.raonsecure.touchen_mguard_4_0.MDMAPI;
import com.raonsecure.touchen_mguard_4_0.MDMResultCode;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * MDM 연동 유틸리티
 * Created by LandonJung on 2017-09-14.
 */

public class MDMUtils {
    public static MDMUtils instance = null;
    private Context mContext = null;
    private MDMAPI _mdm = null;
    private AppCompatActivity mActivity = null;
    private int initResult = -1;
    private CustomAlertDialog mDialog;
    MDMAPI.MGuardConnectionListener mGuardConnectionListener = null;

    public interface MDMCallbackListener {
        public void onBindResult(boolean result);
        public void onLoginResult(boolean result);
        public void onGetInfoResult(JSONObject data);
        public void onAppVersionCheckResult(boolean result);
        public void onLogoutResult(boolean result);
    }

    public MDMCallbackListener getMdmCallbackListener() {
        return mdmCallbackListener;
    }

    public void setMdmCallbackListener(MDMCallbackListener mdmCallbackListener) {
        this.mdmCallbackListener = mdmCallbackListener;
    }

    private MDMCallbackListener mdmCallbackListener;


    public static MDMUtils getInstance(Context context, AppCompatActivity activity) {
        if(instance == null) {
            instance = new MDMUtils(context, activity);
        }
        instance.setContext(context, activity);
        return instance;
    }

    public MDMUtils(Context context, AppCompatActivity activity) {
        setContext(context, activity);
    }

    public void setContext(Context context, AppCompatActivity activity) {
        mContext = context;
        mActivity = activity;
    }

    public void installMDMfromAsset(AppCompatActivity activity) {
        AssetManager assetManager = mContext.getAssets();

        InputStream in = null;
        OutputStream out = null;

        String copyRootPath = CommonData.SAVE_APK_PATH_ROOT;
        String copyPath = copyRootPath + CommonData.MDM_APK_NAME;

        File destRoot = new File(copyRootPath);
        if(!destRoot.exists()) {
            destRoot.mkdir();
        }

        try {
            in = assetManager.open(CommonData.MDM_APK_NAME);
            out = new FileOutputStream(copyPath);

            byte[] buffer = new byte[1024];

            int read;
            while((read = in.read(buffer)) != -1) {

                out.write(buffer, 0, read);

            }

            in.close();
            in = null;

            out.flush();          out.close();
            out = null;

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.setDataAndType(Uri.fromFile(new File(copyPath)),
                        "application/vnd.android.package-archive");
            } else {
                Uri apkURI = FileProvider.getUriForFile(
                        mContext,
                        mContext.getApplicationContext()
                                .getPackageName() + ".provider", new File(copyPath));
                intent.setDataAndType(apkURI, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            }
            activity.startActivityForResult(intent, CommonData.ACTIVITY_MDM_APK_INSTALL);

        } catch(Exception e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
    }

    public void onResume(AppCompatActivity activity) {
        mActivity = activity;
        mGuardConnectionListener = getMGuardConnectionListener();
        _mdm = MDMAPI.getInstance();
        _mdm.RS_MDM_Init(mContext, NetworkConst.getInstance().getMDMDomain(), mGuardConnectionListener);
    }

    public boolean isBindSuccess() {
        if(initResult == MDMResultCode.ERR_MDM_SUCCESS) {
            return true;
        }
        return false;
    }

    private MDMAPI.MGuardConnectionListener getMGuardConnectionListener(){
        if(mGuardConnectionListener == null){
            return new MDMAPI.MGuardConnectionListener() {
                @Override
                public void onComplete(int resultCode) {
                    initResult = resultCode;
                    switch (resultCode) {
                        case MDMResultCode.ERR_MDM_SUCCESS:
                            Log.d("bind Success");
                            if(mdmCallbackListener != null) {
                                mdmCallbackListener.onBindResult(true);
                            }
                            // 에이전트 체크
                            break;
                        case MDMResultCode.ERR_MDM_FAILED:
                            Log.d("bind fail");
                            if(mdmCallbackListener != null) {
                                mdmCallbackListener.onBindResult(false);
                            }

                            break;
                        case MDMResultCode.ERR_MDM_NOT_INSTALLED:
                            Log.d("MDM is not Installed");
                            if(mdmCallbackListener != null) {
                                mdmCallbackListener.onBindResult(false);
                            }
                            agentNotInstalled();
                            break;
                    }
                }
            };
        }else
            return mGuardConnectionListener;
    }

    public boolean doProc(int MDM_ACTION, final boolean bHidden) {

        String id = CommonData.getInstance().getId();
        String pw = CommonData.getInstance().getPassword();

        if(initResult == MDMResultCode.ERR_MDM_NOT_INSTALLED){
            agentNotInstalled();
            return false;
        }else if(initResult == MDMResultCode.ERR_MDM_FAILED){
            if(MDM_ACTION != CommonData.MDM_ACTION_INIT){
                //PopupUtils.show(mActivity, "BIND 되지 않았습니다.");
                if(mdmCallbackListener != null) {
                    mdmCallbackListener.onLogoutResult(true);
                }
                return false;
            }
        }

        switch (MDM_ACTION) {
            case CommonData.MDM_ACTION_INIT:
                mGuardConnectionListener = getMGuardConnectionListener();
                _mdm = MDMAPI.getInstance();
                _mdm.RS_MDM_Init(mContext, NetworkConst.getInstance().getMDMDomain(), mGuardConnectionListener);
                break;
            case CommonData.MDM_ACTION_LAUNCH_APP:

                HandlerUtils.post(new Runnable() {
                    @Override
                    public void run() { AppUtils.launchApp(CommonData.APP_PACKAGE_NAME_MDM ); }
                });
                break;
            case CommonData.MDM_ACTION_UPMOOAPP_UPDATE:
                android.util.Log.d("YYMY", "MDM_ACTION_UPMOOAPP_UPDATE");
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setData(Uri.parse("mguard://mam/office"));
                mActivity.startActivity(i);
                break;
            case CommonData.MDM_ACTION_CHECK_AGENT:
                _mdm.RS_MDM_CheckAgent(mContext, new MDMAPI.MGuardCallbackListener() {
                    @Override
                    public void onCompleted(int resultCode, String msg) {
                        Log.d("checkagent...resultCode: "+resultCode+", onCompleted :"+msg);

                            if (resultCode == MDMResultCode.ERR_MDM_NOT_INSTALLED){
                                agentNotInstalled();
                            }
                            else if (resultCode == MDMResultCode.ERR_MDM_NOT_LOGIN) {
                                agentNotLogin(msg);
                            }
                            else if (resultCode == MDMResultCode.ERR_MDM_DEVICE_ROOTING) {
                                PopupUtils.show(mActivity, "루팅된 단말기입니다. 이용이 제한됩니다.", dialogClickListener, CommonData.POPUP_TYPE_MDM_ERR_MDM_DEVICE_ROOTING);
                            }
                            else if (resultCode == MDMResultCode.ERR_MDM_AGENT_NOT_LAST_VERSION) {
                                PopupUtils.show(mActivity, "에이전트가 최신 버전이 아닙니다. 에이전트를 업데이트 해 주세요.", dialogClickListener, CommonData.POPUP_TYPE_MDM_AGENT_NOT_LAST_VERSION);
                            }

                            else if (resultCode == MDMResultCode.ERR_MDM_FIND_HACKED_APP) {
                                PopupUtils.show(mActivity, "위변조된 에이전트를 사용중입니다.가 다릅니다. 이용이 제한됩니다.", dialogClickListener, CommonData.POPUP_TYPE_MDM_ERR_MDM_DEVICE_ROOTING);
                            } else if (resultCode == MDMResultCode.ERR_MDM_SUCCESS){
                                agentCheckSuccess(msg, bHidden);
                            }
                            else {
                                PopupUtils.show(mActivity, "에이전트의 동작이 원활하지 않습니다.\n에이전트를 확인해 주세요.\n앱을 종료합니다.", dialogClickListener, CommonData.POPUP_TYPE_MDM_ERR_MDM_CHECKING);
                                Log.d("resultCode ?????  :"+resultCode);
                        }
                    }

                });
                break;
            case CommonData.MDM_ACTION_UPDATE_AUTH_INFO:

                break;
            case CommonData.MDM_ACTION_CHECK_VACCINE:
                _mdm.RS_MDM_CheckVaccine();
            break;
            case CommonData.MDM_ACTION_DEVICE_USER_INFO:
                _mdm.RS_MDM_GetDeviceUserInfo(new MDMAPI.MGuardCallbackListener() {
                    @Override
                    public void onCompleted(int resultCode, String msg) {
                        Log.d("deviceuserinfo...onCompleted :"+msg);
                        try {
                            JSONObject jsonObj = new JSONObject(msg);
                            mdmCallbackListener.onGetInfoResult(jsonObj);
                        } catch (JSONException e) {
                            if(Config.DISPLAY_LOG) e.printStackTrace();
                        }

                    }
                });
                break;

            case CommonData.MDM_ACTION_LOGIN:
                _mdm.RS_MDM_LoginOffice(new MDMAPI.MGuardCallbackListener() {
                    @Override
                    public void onCompleted(int resultCode, String msg) {
                        Log.d("officeLogin...onCompleted resultCode:"+resultCode+", msg: " + msg);
                        if (resultCode == MDMResultCode.ERR_MDM_ALLEADY_OFFICE_MODE) {
                            Log.d("이미 업무앱 로그인 상태입니다.");
                            //PopupUtils.show(mActivity, "이미 업무앱 로그인 상태입니다.");
                            if(mdmCallbackListener != null)
                                mdmCallbackListener.onLoginResult(CommonData.isNoBindMDM);
//                            loginFinish();

                        }
                        else if (resultCode == MDMResultCode.ERR_MDM_SUCCESS) {
                            //PopupUtils.show(mActivity, "오피스 로그인 성공");
                            Log.d("오피스 로그인 성공");
                            if(mdmCallbackListener != null)
                                mdmCallbackListener.onLoginResult(CommonData.isNoBindMDM);
 //                           loginFinish();

                        }
                        else if (resultCode == MDMResultCode.ERR_MDM_NOT_LOGIN) {
                            Log.d("로그인 상태가 아닙니다.");
                            agentNotLogin(msg);
                        }
                    }
                });
                break;
            /*
            * servTrId, trId : 업무앱 <---> 업무서버 연동
            * servId : onePass ServiceCode
            * userId : 통합 UserId (mGuard userId와 동일)
            * */
            case CommonData.MDM_ACTION_ONE_PASS_AUTH: {

            }
            break;
            // 서버통신
            case CommonData.MDM_ACTION_LOGOUT: {
                _mdm.RS_MDM_LogoutOffice(new MDMAPI.MGuardCallbackListener() {
                    @Override
                    public void onCompleted(int resultCode, String msg) {
                        Log.d("officeLogout...onCompleted resultCode:"+resultCode+", msg: " + msg);
                        if (resultCode == MDMResultCode.ERR_MDM_ALLEADY_PERSONAL_MODE) {
                            Log.d("이미 업무앱 로그아웃 상태입니다.");
                            mdmCallbackListener.onLogoutResult(true);
                        }
                        else if (resultCode == MDMResultCode.ERR_MDM_SUCCESS) {
                            Log.d("오피스 로그아웃 성공");
                            mdmCallbackListener.onLogoutResult(true);
                        }
                        else if (resultCode == MDMResultCode.ERR_MDM_NOT_LOGIN) {
                            agentNotLogin(msg);
                        }
                        _mdm.RS_MDM_Release(mContext);
                        instance = null;
                        _mdm = null;
                    }
                });
            }
            break;
            case CommonData.MDM_ACTION_FORGEAPP_CHECK:
                _mdm.RS_MDM_CheckForgeApp(new MDMAPI.MGuardCallbackListener() {
                    @Override
                    public void onCompleted(int resultCode, String s) {
                        if (resultCode == MDMResultCode.ERR_MDM_APP_NOT_LAST_VERSION) {
                            HandlerUtils.post(new Runnable() {
                                @Override
                                public void run() {
                                    AppUtils.launchApp(CommonData.APP_PACKAGE_NAME_MDM);
                                }
                            });
                        };
                    }
                });

            break;
            case CommonData.MDM_ACTION_LOGIN_OFFICE_CHECK:

                Map<String,String> LoginMap = new HashMap<>();
                LoginMap.put("userId",id);//<- 사용자 ID 전달함.

                _mdm.RS_MDM_LoginOfficeCheck(LoginMap,new MDMAPI.MGuardCallbackListener() {
                    @Override
                    public void onCompleted(int resultCode, String msg) {
                        Log.d("officeLogin...onCompleted resultCode:"+resultCode+", msg: " + msg);
                        if (resultCode == MDMResultCode.ERR_MDM_ALLEADY_OFFICE_MODE) {
                            if(mdmCallbackListener != null)
                                mdmCallbackListener.onLoginResult(CommonData.isNoBindMDM);
//                            loginFinish();
//                            PopupUtils.show(mActivity, "이미 업무앱 로그인 상태입니다.", dialogClickListener, CommonData.POPUP_TYPE_MDM_ALLEADY_LOGIN);
                        }
                        else if (resultCode == MDMResultCode.ERR_MDM_SUCCESS) {
                            if(mdmCallbackListener != null)
                                mdmCallbackListener.onLoginResult(CommonData.isNoBindMDM);
//                            loginFinish();
                        }
                        else if (resultCode == MDMResultCode.ERR_MDM_NOT_LOGIN) {
                            agentNotLogin(msg);
                        }else {
                            if(mdmCallbackListener != null)
                                mdmCallbackListener.onLoginResult(CommonData.isNoBindMDM);
                        }
                    }


                });

                break;
            case CommonData.MDM_ACTION_DIRECTLY_LOGIN:

                if(Config.ENABLE_LOGIN_TEMP_DATA) {
                    id = Config.TEST_MDM_LOGIN_ID;
                    pw = Config.TEST_MDM_LOGIN_PW;
                }

                if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(pw) ) {
                    _mdm.RS_MDM_LoginDirectly(id, pw, new MDMAPI.MGuardCallbackListener() {
                        @Override
                        public void onCompleted(int resultCode, String msg) {
                            Toast.makeText(mContext.getApplicationContext(),"resultCode: "+resultCode+", msg: "+msg, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(mContext.getApplicationContext(),"저장된 아이디 / 비밀번호가 없습니다..", Toast.LENGTH_LONG).show();
                }
                break;
            case CommonData.MDM_ACTION_HW_CONTROL_FLAG_POLICY_BLOCK_BROWSER:
                _mdm.RS_MDM_HwContrl(MDMAPI.FLAG_POLICY_BLOCK_BROWSER, new MDMAPI.MGuardCallbackListener() {
                    @Override
                    public void onCompleted(int resultCode, String msg) {
                        Log.d("hwcontrol...onCompleted :" + msg);
                    }
                });
            case CommonData.MDM_ACTION_HW_CONTROL_FLAG_POLICY_BLOCK_CAPTURE:
                _mdm.RS_MDM_HwContrl(MDMAPI.FLAG_POLICY_BLOCK_CAPTURE, new MDMAPI.MGuardCallbackListener() {
                    @Override
                    public void onCompleted(int resultCode, String msg) {
                        Log.d("hwcontrol...onCompleted :" + msg);
                    }
                });

            default:
                break;
        }

        return true;
    }

    /**
     * MDM 을 통한 DSM App Version Check
     */
    public void versionCheck() {

        Log.i("MDM 을 통한 DSM App Version Check");

//        if(Config.ENABLE_PORTAL_MAIN_OFFLINE) {
//            String portalAppInfoJSON = JSONUtils.getJsonFromAsset(mContext, "portalApp");
//        } else {
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(CommonData.JSON_LOGIN_ID, CommonData.getInstance().getId()));

            Log.d("MDM UserID = " + CommonData.getInstance().getId());
            ((BaseActivity)mActivity).showProgress();
            RequestApi.requestApi(mContext, NetworkConst.NET_MDM_GET_APP_INFO, NetworkConst.getInstance().getMDMAppInfoUrl(), networkListener, params, ((BaseActivity)mActivity).getProgressDialog());
//        }
    }

    /**
     * 네트워크 리스너
     */
    // MDM 내 DSM 앱 버전채크
    public CustomAsyncListener networkListener = new CustomAsyncListener() {

        @Override
        public void onPost(Context context, int type, String resultCode, JSONObject resultData, CustomAlertDialog dialog) {
            switch ( type ) {
                case NetworkConst.NET_MDM_GET_APP_INFO:							// VersionCheck
                    Log.i("MDM 내 DSM 앱 버전채크");
                    switch ( resultCode ) {
                        case CommonData.API_CODE_NONE:
                        case CommonData.API_SUCCESS:
                            try {
                                JSONArray dataArr = resultData.getJSONArray(CommonData.JSON_MDM_APP_INFOS);
                                if(dataArr != null) {
                                    int size = dataArr.length();
                                    boolean bExistApp = false;
                                    for(int i = 0; i < size; i++) {
                                        JSONObject data = dataArr.getJSONObject(i);
                                        if(data != null) {

                                            String packageName = data.getString(CommonData.JSON_MDM_APP_PACKAGE_NAME);
                                            //String appP = "com.mdongbu.mobileeclaim";
                                            String appP = AppUtils.getAppPackageName();
                                            if(packageName.equals(appP)) {

                                                bExistApp = true;
                                                CacheUtils.getInstance().put(CommonData.JSON_MDM_APP_INFOS, data);

                                                boolean isAppUpdate = false;

                                                try {
                                                    isAppUpdate = DSMUtil.isAppUpdate(AppUtils.getAppVersionName(), data.getString(CommonData.JSON_MDM_APP_VERSION).toString());
                                                } catch ( Exception e ) {
                                                    if(Config.DISPLAY_LOG) e.printStackTrace();
                                                }

                                                if ( isAppUpdate ) {	// 자동업데이트가 필요하다면

                                                    PopupUtils.show(mActivity, "앱이 업데이트 되었습니다.\n업데이트를 진행해야만 정상적으로 서비스가 가능 합니다.\n업데이트를 진행합니다.", dialogClickListener, CommonData.POPUP_TYPE_MDM_VERSION_CHECK);

                                                    return;
                                                } else {
                                                    Log.d("최신버전 입니다.");
                                                    mdmCallbackListener.onAppVersionCheckResult(true);
                                                }
                                                break;
                                            }

                                        } else {
                                            PopupUtils.show(mActivity, "에이전트와 연결이 되지 않았습니다..\n에이전트를 확인해 주세요.", dialogClickListener, CommonData.POPUP_TYPE_MDM_NOT_LOGIN);
                                            break;
                                        }
                                    }

                                    if(!bExistApp) {
                                        Log.d("MDM 에 DSM 이 등록되어 있지 않습니다.");
                                        mdmCallbackListener.onAppVersionCheckResult(true);
                                    }

                                }

                                break;
                            }catch(Exception e){
                                Log.e(e.toString());
                            }
                        default:
 //                           ((BaseActivity)mActivity).hideProgress();
                            dialog.show();
                            break;
                    }
                    break;
            }
 //           ((BaseActivity)mActivity).hideProgress();
        }

        @Override
        public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {

            ((BaseActivity)mActivity).hideProgress();
            dialog.show();
        }

        @Override
        public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {

            // 데이터에 문제가 있는 경우 다이얼로그를 띄우고 인트로에서는 종료하도록 한다.
            ((BaseActivity)mActivity).hideProgress();
            dialog.show();

        }

    };

    private void agentNotLogin(String msg) {
        PopupUtils.show(mActivity, "에이전트가 로그인되어 있지 않습니다.\n로그인을 먼저 진행 해 주세요.", dialogClickListener, CommonData.POPUP_TYPE_MDM_NOT_LOGIN);
    }

    private void agentNotInstalled() {
        PopupUtils.show(mActivity, "에이전트가 설치되어 있지 않습니다.\n확인버튼 클릭시 설치됩니다", dialogClickListener, CommonData.POPUP_TYPE_MDM_NOT_INSTALLED);
    }

    private void agentCheckSuccess(String msg, boolean bHidden) {

        if (!bHidden) {
            doProc(CommonData.MDM_ACTION_DEVICE_USER_INFO, false);
        }
//        if(Config.ENABLE_APP_VERSION_CHECK_UPDATE) {
//            versionCheck();
//        } else {
//            mdmCallbackListener.onAppVersionCheckResult(true);
//        }

//        if(!bHidden && CommonData.isNoBindMDM) {
//        }
    }

    public void agentLoginCall() {
        if(Config.ENABLE_MDM_AGENT_CHECK) {
            if(_mdm != null) {
                _mdm.RS_MDM_GOMDMLogin(mContext);
            }
        }
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
                case CommonData.POPUP_TYPE_MDM_NOT_LOGIN:				        					// MDM 미 로그인
                    //_mdm.RS_MDM_GOMDMLogin(mContext);
                    doProc(CommonData.MDM_ACTION_LAUNCH_APP, false);
                    break;
                case CommonData.POPUP_TYPE_MDM_NOT_INSTALLED:				        				// MDM 미 설치
                    //MDMAPI.getInstance().RS_MDM_GOMDMLogin(mContext);
                    _mdm.RS_MDM_InstallAgent(mActivity, new MDMAPI.FileDownloadProgressListener() {
                        @Override
                        public void onCompleted() {
                            Log.d("installAgent...onCompleted");
                        }

                        @Override
                        public void onFailed() {
                            Log.d("installAgent...onFailed");
                        }
                    });

                    break;
                case CommonData.POPUP_TYPE_MDM_CHECK_SUCCESS:				        					// MDM 미 로그인
                    ((BaseActivity)mContext).moveActivity();
                    break;

                case CommonData.POPUP_TYPE_MDM_AGENT_NOT_LAST_VERSION:
                    // _mdm.RS_MDM_InstallAgent(null);
                    if ((mActivity != null) && (!mActivity.isFinishing())) {
                        HandlerUtils.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            //MDMAPI.getInstance().RS_MDM_GOMDMLogin(mActivity);
                                            _mdm.RS_MDM_InstallAgent(mActivity, new MDMAPI.FileDownloadProgressListener() {
                                                @Override
                                                public void onCompleted() {
                                                    Log.d("installAgent...onCompleted");
                                                }

                                                @Override
                                                public void onFailed() {
                                                    Log.d("installAgent...onFailed");
                                                }
                                            });
                                        }catch (Exception e) {
                                            if(Config.DISPLAY_LOG) e.printStackTrace();
                                            PopupUtils.show(mActivity, "에이전트 업데이트 설치 시도 중 오류가 발생되었습니다.\n앱을 종료합니다.", dialogClickListener, CommonData.POPUP_TYPE_MDM_ERR_UPDATE_INSTALLED);
                                        }
                                    }
                                });
                            }
                        }, 100);
                    }
                    break;

                case CommonData.POPUP_TYPE_MDM_ERR_MDM_DEVICE_ROOTING:
                case CommonData.POPUP_TYPE_MDM_ERR_UPDATE_INSTALLED:
                case CommonData.POPUP_TYPE_MDM_ERR_MDM_CHECKING:
                case CommonData.POPUP_TYPE_MDM_ERR_MDM_FIND_HACKED_APP:
                    if(mActivity != null) {
                        ((BaseActivity)mActivity).commonData.setLogined(false);
                        SessionMan.setbLogin(false);

                        mActivity.finish();
                        ((BaseActivity)mActivity).activityClear();

                    }
                    break;
                case CommonData.POPUP_TYPE_MDM_VERSION_CHECK:
                    try {
                        Log.d("POPUP_TYPE_MDM_VERSION_CHECK");
                        String downloadUrl = CacheUtils.getInstance().getJSONObject(CommonData.JSON_MDM_APP_INFOS).getString(CommonData.JSON_MDM_APP_INSTALL_URL);

                        Log.d("JSON_MDM_APP_INSTALL_URL"+CommonData.JSON_MDM_APP_INSTALL_URL+" , JSON_MDM_APP_INFOS: "+CommonData.JSON_MDM_APP_INFOS);
                        Log.d("Config.ENABLE_APP_VERSION_CHECK_UPDATE"+Config.ENABLE_APP_VERSION_CHECK_UPDATE+" , ENABLE_APP_VERSION_CHECK_UPDATE_DOWNLOAD_APK_DIRECTLY: "+Config.ENABLE_APP_VERSION_CHECK_UPDATE_DOWNLOAD_APK_DIRECTLY);
                        if(Config.ENABLE_APP_VERSION_CHECK_UPDATE) {
                            if(Config.ENABLE_APP_VERSION_CHECK_UPDATE_DOWNLOAD_APK_DIRECTLY) {
                                UpdateApp atualizaApp = new UpdateApp();
                                atualizaApp.setContext(mActivity);
                                atualizaApp.execute(downloadUrl);
                            } else {
                                //doProc(CommonData.MDM_ACTION_LAUNCH_APP, false);
                                android.util.Log.d("YYMY", "POPUP_TYPE_MDM_VERSION_CHECK");
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.setData(Uri.parse("mguard://mam/office"));
                                mActivity.startActivity(i);
                            }
                        }

                    } catch (JSONException e) {
                        if(Config.DISPLAY_LOG) e.printStackTrace();
                    }
                    break;
                case CommonData.POPUP_TYPE_MDM_ALLEADY_LOGIN:

                    break;
            }
        }
    };

    public class UpdateApp extends AsyncTask<String,Void,Void> {
        private Context context;
        public void setContext(Context contextf){
            context = contextf;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((BaseActivity)mActivity).showProgress();
        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {
                URL url = new URL(arg0[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                String PATH = Environment.getExternalStorageDirectory().toString() + "/";
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, "update.apk");
                if(outputFile.exists()){
                    outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();

            } catch (Exception e) {
                if(Config.DISPLAY_LOG) Log.e("Update error! " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ((BaseActivity)mActivity).hideProgress();

            Intent intent = new Intent(Intent.ACTION_VIEW);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/update.apk")), "application/vnd.android.package-archive");
            } else {
                File file = new File(Environment.getExternalStorageDirectory() + "/update.apk");
                Uri apkUri = FileProvider.getUriForFile(mActivity.getApplicationContext(), mActivity.getApplicationContext().getPackageName() + ".provider", file);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
            mActivity.startActivity(intent);

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
                case CommonData.POPUP_TYPE_MDM_NOT_LOGIN:				        					// MDM 미 로그인
                case CommonData.POPUP_TYPE_MDM_NOT_INSTALLED:                                       // MDM 미 설치
                    if(mActivity != null) {
                        mActivity.finish();
                        ((BaseActivity)mActivity).activityClear();
                    }
                    break;
            }
        }
    };
}
