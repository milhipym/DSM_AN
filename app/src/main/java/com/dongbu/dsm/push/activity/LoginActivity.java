package com.dongbu.dsm.push.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.dongbu.dsm.Config;
import com.dongbu.dsm.R;
import com.dongbu.dsm.base.BaseActivity;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.network.DongbuAccountManager;
import com.dongbu.dsm.util.DSMUtil;
import com.dongbu.dsm.util.HandlerUtils;
import com.dongbu.dsm.util.Log;

import kr.mtcom.smartmessage.common.ApiEngineListener;
import kr.mtcom.smartmessage.common.AppBaseUtil;
import kr.mtcom.smartmessage.common.BaseError;
import kr.mtcom.smartmessage.common.CompanyUser;
import kr.mtcom.smartmessage.manager.AccountManager;
import kr.mtcom.smartmessage.manager.DongbuPushManager;

public class LoginActivity extends BaseActivity {
    private Context mContext;
    private int mLoginType;
    private String id;
    private String pw;
    private String userName;
    private String mdn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        mContext = this;
        commonData.setStep(CommonData.ACTIVITY_PUSH_LOGIN);
        setContentView(R.layout.activity_push_login);

        mLoginType = 2;

        showProgress();

        if(Config.ENABLE_LOGIN_TEMP_DATA) {
            id = Config.TEST_MDM_LOGIN_ID;
        } else {
            id = CommonData.getInstance().getId();
        }
        userName = CommonData.getInstance().getLoginUserName();
        mdn = DSMUtil.getModifiedPhoneNumber(this);
        pw = "";

        HandlerUtils.post(new Runnable() {
            @Override
            public void run() {
                regHandler();
            }
        });
    }

    private void regHandler() {
        Log.d("푸시 계정 생성 시작 | " + id + " | " + userName + " | " + mdn);
        AccountManager.addUserWithID(id, userName, mdn, mContext, new ApiEngineListener() {
            public void errorResponse(BaseError error) {
                Log.d("푸시 계정 생성 실패" + error.getErrorCode() + ", " + error.getMessage());
                if(error.getErrorCode().equals("2212")) { // 고객사 사용자 LOGINID 중복일 경우에는 그냥 로그인 진행
                    loginHandler();
                } else {
                    closeHandler(RESULT_CANCELED, "[" + error.getErrorCode() + "] " + error.getMessage());
                }
            }
            public void networkErrorResponse() {
                closeHandler(RESULT_CANCELED , "네트워크를 확인해 주세요.");
            }
            public void voidResponse() {
                //closeHandler(RESULT_CANCELED, "add success");
                Log.d("푸시 계정 생성 성공");
                loginHandler();
            }
        });
    }

    private void loginHandler() {

        if(mLoginType ==1) {
            AccountManager.authenticateWithUsernameAndPassword(id, pw, getApplicationContext(), new AccountManager.AccountListener() {
                @Override
                public void authorizeFailed(String code, String message) {
                    closeHandler(RESULT_CANCELED, "login failed code : "+code+" message : "+message);
                }
                @Override
                public void authorizeFinished(final CompanyUser user) {
                    loginCompleteHandler(user);
                }

                @Override
                public void authorizeNetworkError() {
                    closeHandler(RESULT_CANCELED, "authorizeNetworkError");
                }

            });
        } else if(mLoginType == 2) {
            AccountManager.authenticateWithID(id, getApplicationContext(), new AccountManager.AccountListener() {
                @Override
                public void authorizeFailed(String code, String message) {
                    closeHandler(RESULT_CANCELED, "[" + code + "] " + message);
                }
                @Override
                public void authorizeFinished(final CompanyUser user) {
                    loginCompleteHandler(user);
                }

                @Override
                public void authorizeNetworkError() {
                    closeHandler(RESULT_CANCELED, "네트워크가 원활하지 않습니다.");
                }
            });
        } else if(mLoginType == 3) {
            AccountManager.authenticateWithMDN(id, getApplicationContext(), new AccountManager.AccountListener() {
                @Override
                public void authorizeFailed(String code, String message) {
                    closeHandler(RESULT_CANCELED, "login failed code : "+code+" message : "+message);
                }
                @Override
                public void authorizeFinished(final CompanyUser user) {
                    loginCompleteHandler(user);
                }

                @Override
                public void authorizeNetworkError() {
                    closeHandler(RESULT_CANCELED, "authorizeNetworkError");
                }
            });
        }
    }

    private void loginCompleteHandler(CompanyUser user){
        DongbuAccountManager.getInstance().setMe(user); // AccountManager에 CompanyUser 등록
        String packageName = getApplicationContext().getPackageName();
        int userKey = AccountManager.getMyUserSeqNo(getApplicationContext());
        String pushSenderId = Config.GCM.SENDER_ID; //AppBaseUtil.getMetaData(getApplicationContext()).getString("kr.mtcom.smartmessage.push_sender_id");
        String baseUrl = AppBaseUtil.getBaseUrl(getApplicationContext());
        if (userKey > 0 && pushSenderId != null) {
            DongbuPushManager.getInstance().setRegisterData(baseUrl, String.valueOf(userKey), packageName, pushSenderId);
            DongbuPushManager.getInstance().registerPushNoti(getApplicationContext());
        }

        closeHandler(RESULT_OK, "Push Login Success");
    }

    private void closeHandler(int resultCode, String message){
        //Intent intent = new Intent(mContext, IntroActivity.class);
        Intent data = new Intent();
        if(message != null) {
            data.putExtra(CommonData.JSON_PUSH_LOGIN_RESULT_MESSAGE, message);
        }
        setResult(resultCode, data);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        hideProgress();
        finish();
    }

}
