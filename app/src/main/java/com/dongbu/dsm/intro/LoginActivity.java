package com.dongbu.dsm.intro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.R;
import com.dongbu.dsm.base.IntroBaseActivity;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.CustomAlertDialog;
import com.dongbu.dsm.common.CustomAlertDialogInterface;
import com.dongbu.dsm.common.CustomAsyncListener;
import com.dongbu.dsm.common.NetworkConst;
import com.dongbu.dsm.util.DSMUtil;
import com.dongbu.dsm.util.HandlerUtils;
import com.dongbu.dsm.util.KeyboardUtil;
import com.dongbu.dsm.util.Log;
import com.dongbu.dsm.util.Regex;
import com.dongbu.dsm.util.TransKeyUtil;
import com.dongbu.dsm.widget.BackKeyEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.firebase.perf.metrics.Trace;
import com.softsecurity.transkey.Global;
import com.softsecurity.transkey.TransKeyActivity;
import com.softsecurity.transkey.TransKeyCipher;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Landon on 2017-09-06
 * 로그인 화면
 * @since 0, 1
 */
public class LoginActivity extends IntroBaseActivity implements View.OnClickListener, CheckBox.OnCheckedChangeListener, View.OnFocusChangeListener {

    private TextView mSearchPwTv;
    private TextView mUserDeviceType;
    // 로그인
    private Button mLoginBtn;
    private BackKeyEditText id_edit;
    private EditText passwd_edit;
    private ImageButton mIdDelBtn, mPasswdDelBtn;
    private RelativeLayout mLoginScreen, mOutLoginLayout;
    private HorizontalScrollView keyscroll;
    private String mPlainText = "";
    private String mCipherText = "";
    private CheckBox mSaveId;
    private TextView mSaveIdTv;
    private TransKeyUtil transKeyUtil;
    // 테스트 도메인 선택용 버튼 레이아웃
    private LinearLayout mTestDomainBtnLayout;
    private Button mTestDomainBtn1;
    private Button mTestDomainBtn2;

    private boolean bFirstLoad = true;
    private boolean bPWClicked = false;
    private boolean bIDFocus = false;

    private View decorView;
    private int uiOption;

    public FirebaseCrashlytics crashlytics;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Trace mTrace ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_login_new);
        commonData.setStep(CommonData.ACTIVITY_LOGIN);
        mContext = this;
        hideNavigationBar();
        crashlytics = FirebaseCrashlytics.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        crashlytics.setUserId("init_Login");//앱크러쉬 사용자별 추적을 위한 셋팅

        init();
        setEvent();
        if(getIntent().getBooleanExtra("moveBack", false)) {
        DSMUtil.BackAnimationEnd(LoginActivity.this);
    } else {
        DSMUtil.BackAnimationFadeStart(LoginActivity.this);
    }

}

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.i("onStart() getClass().getSimpleName() = " + getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    /**
     * 초기화
     */
    public void init(){

        mUserDeviceType =   (TextView)        findViewById(R.id.user_device_type);
        mIdDelBtn	    =	(ImageButton)	  findViewById(R.id.id_delete_btn);
        mPasswdDelBtn	=	(ImageButton)	  findViewById(R.id.passwd_delete_btn);
        id_edit 		=	(BackKeyEditText) findViewById(R.id.id_edit);
        passwd_edit 	=	(EditText)		  findViewById(R.id.passwd_edit);

        mLoginBtn	    =	(Button)	 	  findViewById(R.id.login_btn);
        mSearchPwTv     =   (TextView)        findViewById(R.id.search_id_tv);

        mSaveId         =   (CheckBox)        findViewById(R.id.save_id_cb);
        mSaveIdTv       =   (TextView)        findViewById(R.id.save_id_tv);

        if(!Config.IS_SERVER_HOST_REAL) {
            mTestDomainBtnLayout = (LinearLayout) findViewById(R.id.test_btn_layout);
            mTestDomainBtn1 = (Button) findViewById(R.id.test_btn_1);
            mTestDomainBtn2 = (Button) findViewById(R.id.test_btn_2);

            mTestDomainBtnLayout.setVisibility(View.VISIBLE);
            mTestDomainBtn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setupTestDomainBtn(true);
                }
            });

            mTestDomainBtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setupTestDomainBtn(false);
                }
            });

            setupTestDomainBtn(true);
        }

        mOutLoginLayout =   (RelativeLayout)    findViewById(R.id.out_login_layout);
        mLoginScreen    =   (RelativeLayout)    findViewById(R.id.login_screen);
        mLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 배경 터치 시 키보드 내리기
                KeyboardUtils.hideSoftInput((LoginActivity)mContext);
//                if(!TransKeyCtrlViewUtil.getInstance().isShown()) {
//                    bPWClicked = false;
//                }
                hideNavigationBar();
            }
        });

        passwd_edit.setFocusable(false);
        passwd_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bPWClicked = true;
                HandlerUtils.post(new Runnable() {
                    @Override
                    public void run() {
                        showTranskey();
                        setOutLayoutUpScroll(true);
                    }
                });
            }
        });

        mLoginBtn.setEnabled(false);   // 로그인 버튼 비활성화

        id_edit.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        id_edit.addTextChangedListener(new MyTextWatcher());
        id_edit.setKeyImeChangeListener(new BackKeyEditText.KeyImeChange(){
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
                    bPWClicked = false;
                }
            }
        });
        id_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bPWClicked = false;
            }
        });

        mSaveIdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSaveId.setChecked(!mSaveId.isChecked());
            }
        });

        if(Config.ENABLE_DISPLAY_APP_VERSION_IN_LOGIN) {
            if(DSMUtil.isTablet()) {
                mSearchPwTv.setText("앱 버전 : " + AppUtils.getAppVersionName() + " " + (Config.IS_SERVER_HOST_REAL ? "" : "테스트"));
            } else {
                mSearchPwTv.setText(AppUtils.getAppVersionName() + " " + (Config.IS_SERVER_HOST_REAL ? "" : "테스트"));
            }

        }
        CommonData.getInstance().setLogined(false);

        if(Config.ENABLE_LOGIN_TEMP_DATA) {
            id_edit.setText(Config.TEST_LOGIN_ID);
            //passwd_edit.setText(Config.TEST_LOGIN_PW);
            if(commonData.isSaveId()) {
                mSaveId.setChecked(true);
            } else {
                mSaveId.setChecked(false);
            }
        } else {
            if(commonData.isSaveId()) {
                mSaveId.setChecked(true);
                id_edit.setText(commonData.getId());
            } else {
                mSaveId.setChecked(false);
                id_edit.setText("");
            }
        }

        KeyboardUtil.addKeyboardToggleListener(this, new KeyboardUtil.SoftKeyboardToggleListener()
        {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible)
            {
                Log.d("keyboard visible: "+isVisible);
                if(isVisible) {
                    setOutLayoutUpScroll(true);
                } else {
                    if(!bPWClicked)
                        setOutLayoutUpScroll(false);

//                    if(TransKeyCtrlViewUtil.getInstance().isShown()) {
//                        setOutLayoutUpScroll(true);
//                    }
                }
            }
        });

        //#1.폰-태블릿 상태 표시
        if (DSMUtil.getUserDeviceType("userDeviceType") == null || DSMUtil.getUserDeviceType("userDeviceType").equals("")){
            mUserDeviceType.setText("자동");
        } else {
            if (DSMUtil.isTablet()) {mUserDeviceType.setText("태블릿");}
            else {mUserDeviceType.setText("폰");}
        }
        //#2. 터치하면서 폰-태블릿 변경기능
        mUserDeviceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDeviceTypeChoice();
            }
        });
    }

    @SuppressLint("InvalidAnalyticsName")
    public void userDeviceTypeChoice()
    {
        mTrace = FirebasePerformance.getInstance().newTrace("DSM_userDeviceTypeChoice");
        mTrace.start();
        Bundle params = new Bundle();
        params.putString("Method","userDeviceTypeChoice()");
        mFirebaseAnalytics.logEvent("userDeviceTypeChoice",params);
        String message = "";
        String sub_message = "\n*해당 기능은 안드로이드의 다양한 해상도를 일부 중촉시켜드리고자 제공하는 것으로 "
                            +"\n해상도가 맞지 않는 경우 단말종류를 최대한 맞게 변경하여 사용해 주시기 바랍니다. ";
        if(DSMUtil.isTablet()){ message = "현재 단말 상태는 태블릿을 설정되어 있습니다. 폰으로 변경하시겠습니까?"+ "\n" + sub_message; }
        else{ message = "현재 단말 상태는 폰으로 설정되어 있습니다. 태블릿으로 변경하시겠습니까?"+ "\n" + sub_message; }

        CustomAlertDialog userDeviceTypeDialog = new CustomAlertDialog(mContext, CustomAlertDialog.TYPE_B);
        userDeviceTypeDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
        userDeviceTypeDialog.setContent(message);
        userDeviceTypeDialog.setCanceledOnTouchOutside(false);
        userDeviceTypeDialog.setPositiveButton("변경", new CustomAlertDialogInterface.OnClickListener() {
            @Override
            public void onClick(CustomAlertDialog dialog, Button button) {
                if (DSMUtil.isTablet())
                {
                    mUserDeviceType.setText("폰");
                    DSMUtil.setUserDeviceType("userDeviceType", "P");
                    userDeviceTypeDialog.dismiss();
                }
                else
                {
                    mUserDeviceType.setText("태블릿");
                    DSMUtil.setUserDeviceType("userDeviceType", "T");
                    userDeviceTypeDialog.dismiss();
                }/*
                else {
                    mUserDeviceType.setText("자동");
                    DSMUtil.setUserDeviceType("userDeviceType", "");
                    userDeviceTypeDialog.dismiss();
                }*/
            }
        });
        userDeviceTypeDialog.setNegativeButton("유지", new CustomAlertDialogInterface.OnClickListener() {
            @Override
            public void onClick(CustomAlertDialog dialog, Button button) {
                userDeviceTypeDialog.dismiss();
            }
        });
        mTrace.stop();
        userDeviceTypeDialog.show();
    }

    private void setupTestDomainBtn(boolean isTest1BtnEnable) {
        if(isTest1BtnEnable) {
            mTestDomainBtn1.setBackgroundResource(R.drawable.background_2_5_009881);
            mTestDomainBtn1.setTextColor(getResources().getColorStateList(R.color.color_ffffff_btn));
            mTestDomainBtn2.setBackgroundResource(R.drawable.background_2_5_leftbottom_50d3d2cc);
            mTestDomainBtn2.setTextColor(getResources().getColor(R.color.color_50ffffff));

            Config.SERVER_HOST.TEST_DOMAIN = Config.SERVER_HOST.TEST_DOMAIN_1;

        } else {
            mTestDomainBtn2.setBackgroundResource(R.drawable.background_2_5_009881);
            mTestDomainBtn2.setTextColor(getResources().getColorStateList(R.color.color_ffffff_btn));
            mTestDomainBtn1.setBackgroundResource(R.drawable.background_2_5_leftbottom_50d3d2cc);
            mTestDomainBtn1.setTextColor(getResources().getColor(R.color.color_50ffffff));

            Config.SERVER_HOST.TEST_DOMAIN = Config.SERVER_HOST.TEST_DOMAIN_2;
        }

        CommonData.WEB_SERVICE_HOME_URL_PAD = NetworkConst.getInstance().getWebDomain() + "dsm/zcommon/main/home.do";
        //CommonData.WEB_MAIN_URL = "https://naver.com";
        //CommonData.WEB_MAIN_URL_MANUAL = "https://naver.com";
        CommonData.WEB_MAIN_URL = Config.ENABLE_DEMO ?
                CommonData.WEB_DEMO_URL : DSMUtil.isTablet() ?
                NetworkConst.getInstance().getWebDomain() + CommonData.WEB_SERVICE_MAIN_URL_PAD : NetworkConst.getInstance().getWebDomain() + CommonData.WEB_SERVICE_MAIN_URL_PHONE;
        CommonData.WEB_MAIN_URL_MANUAL = DSMUtil.isTablet() ?
                NetworkConst.getInstance().getWebDomain() + CommonData.WEB_SERVICE_MAIN_URL_PAD : NetworkConst.getInstance().getWebDomain() + CommonData.WEB_SERVICE_MAIN_URL_PHONE;

        Toast.makeText(getApplicationContext(), "설정된 도메인 : " + Config.SERVER_HOST.TEST_DOMAIN, Toast.LENGTH_SHORT).show();
    }

    public void setOutLayoutUpScroll(final boolean isUp) {

//        RelativeLayout.LayoutParams layoutParams =
//                (RelativeLayout.LayoutParams) mOutLoginLayout.getLayoutParams();
//        if (!isUp) {
//            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//            mOutLoginLayout.setLayoutParams(layoutParams);
//        } else {
//            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
//            mOutLoginLayout.setLayoutParams(layoutParams);
//        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        hideProgress();
    }

    private void showTranskey() {
        try {
            //if(transKeyUtil == null) {
                transKeyUtil = new TransKeyUtil(this,  this, TransKeyUtil.TRANSKEY_TYPE_TEXT, null, 8);
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        JSONObject jObject = new JSONObject();

        if (requestCode == CommonData.ACTIVITY_TRANS_KEY_CALL) {

            if (resultCode == Activity.RESULT_CANCELED) {
                if (data != null) {
                    String errMsg = data.getStringExtra(TransKeyActivity.mTK_PARAM_ERROR_MESSAGE);
                    if (errMsg == null) errMsg = "";
                    Log.e(errMsg);
                }
                //setData(onClickIndex, "", "", "");
            } else {

                String cipherData = data.getStringExtra(TransKeyActivity.mTK_PARAM_CIPHER_DATA);
                String cipherDataex = data.getStringExtra(TransKeyActivity.mTK_PARAM_CIPHER_DATA_EX);
                String cipherDataexp = data.getStringExtra(TransKeyActivity.mTK_PARAM_CIPHER_DATA_EX_PADDING);
                String dummyData = data.getStringExtra(TransKeyActivity.mTK_PARAM_DUMMY_DATA);

                Log.d("cipherData : " + cipherData);
                Log.d("cipherDataex : " + cipherDataex);
                Log.d("cipherDataexp : " + cipherDataexp);

                byte[] secureKey = data.getByteArrayExtra(TransKeyActivity.mTK_PARAM_SECURE_KEY);
                int iRealDataLength = data.getIntExtra(TransKeyActivity.mTK_PARAM_DATA_LENGTH, 0);

                if (iRealDataLength == 0) {
//                    if(mTransKeyResultListener != null) {
//                        mTransKeyResultListener.cancel();
//                    }
                    return;
                }

                // 비대칭키를 사용할 경우 데이터 포맷
                String encryptedData = data.getStringExtra(TransKeyActivity.mTK_PARAM_RSA_DATA);
                Log.d("encryptedData : " + encryptedData);

                StringBuffer plainData = null;
                try {
                    TransKeyCipher tkc = new TransKeyCipher("SEED");
                    tkc.setSecureKey(secureKey);
                    String test = tkc.getPBKDF2DataEncryptCipherData(cipherData);

// 			String test1 = tkc.getPBKDF2DataEncryptCipherDataEx(cipherDataex);
//			String test2 = tkc.getPBKDF2DataEncryptCipherDataExWithPadding(cipherDataexp);

                    byte pbPlainData[] = new byte[iRealDataLength];
                    if (tkc.getDecryptCipherData(cipherData, pbPlainData)) {
                        plainData = new StringBuffer(new String(pbPlainData));

                        for (int i = 0; i < pbPlainData.length; i++)
                            pbPlainData[i] = 0x01;
                    } else {
                        // 복호화 실패
                        plainData = new StringBuffer("plainData decode fail...");
                    }

                } catch (Exception e) {
                    if (Global.debug) Log.d(e.getStackTrace().toString());
                }

                String encData = data.getStringExtra(TransKeyActivity.mTK_PARAM_SECURE_DATA);
                Log.d("encData : " + encData);

                mPlainText = plainData.toString();
                mCipherText = encData;

                checkLoginBtn();
                setOutLayoutUpScroll(false);
                passwd_edit.setText(mPlainText);
                if (!mPlainText.isEmpty()) {
                    HandlerUtils.post(new Runnable() {
                        @Override
                        public void run() {
                            showProgress();
                            onClick(mLoginBtn);
                        }
                    });
                }
            }
            transKeyUtil = null;
        }
    }

    /**
     * 이벤트 연결
     */
    public void setEvent(){

        mLoginBtn.setOnClickListener(this);
        mIdDelBtn.setOnClickListener(this);
        mPasswdDelBtn.setOnClickListener(this);
        mSearchPwTv.setOnClickListener(this);

        id_edit.setOnFocusChangeListener(this);
        mSaveId.setOnCheckedChangeListener(this);
    }

    /**
     * 로그인 정보 확인
     * @param id 아이디
     * @param passwd 패스워드
     * @return 로그인 정보가 올바른지 확인한다. ( true - 로그인가능, false - 입력오류 )
     */
    private boolean invalidate(String id, String passwd) {

        CustomAlertDialog dialog = new CustomAlertDialog(this, CustomAlertDialog.TYPE_A);

        //if ( email.equals("") || !DSMUtil.checkEmail(email) ) {
        //if(id.equals("") || !Regex.getInstance().IsIDCorrectByPattern(id)) {
        if(id.equals("")) {
            id_edit.requestFocus();
            commonView.setSelectionCursor(id_edit, id_edit.length());
            dialog.setTitle(getString(R.string.login));
            if(id.equals("")) {
                dialog.setContent(getString(R.string.id_input_data_empty));
            } else if(!Regex.getInstance().IsIDCorrectByPattern(id)) {
                dialog.setContent(getString(R.string.account_id_error));
            }

            dialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
            dialog.show();
            hideProgress();
            return false;
        }

        //if ( passwd.equals("") || !Regex.getInstance().IsPasswordCorrectByPattern(passwd)) {
        if ( passwd.equals("")) {
            //passwd_edit.requestFocus();
            commonView.setSelectionCursor(passwd_edit, passwd_edit.length());
            dialog.setTitle(getString(R.string.login));
            if(passwd.equals("")) {
                dialog.setContent(getString(R.string.pw_input_data_empty));
            } else if(!Regex.getInstance().IsPasswordCorrectByPattern(passwd)) {
                dialog.setContent(getString(R.string.account_password_error));
            }

            dialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), null);
            dialog.show();
            hideProgress();
            return false;
        }
        crashlytics.setUserId(id);//앱크러쉬 사용자별 추적을 위한 셋팅
        crashlytics.setCustomKey("PHONEYPE",DSMUtil.isTablet());
        return true;

    }

   @AddTrace(name = "login",enabled = true)
    @Override
    public void onClick(View v) {

        if(getButtonClickEnabled() == false)
            return;

        setButtonClickEnabled(false);

        switch( v.getId() ){
            case R.id.login_btn:    // 로그인

                showProgress();
                String id 	= id_edit.getText().toString();
                //String passwd 	= passwd_edit.getText().toString();
                String passwd = mPlainText;
                String encpasswd = mCipherText;

                if ( invalidate(id, passwd) ) {                                // 입력된 값 체크

                    commonData.setId(id.toUpperCase());
                    //commonData.setPassword(passwd);

                    if(Config.ENABLE_LOGIN_TEMP_DATA) {
                        commonData.setMDMUserId(Config.TEST_MDM_LOGIN_ID);
                    } else {
                        //commonData.setMDMUserId(commonData.getId());
                        if(!Config.ENABLE_MDM_AGENT_CHECK) {
                            commonData.setMDMUserId(id.toUpperCase());
                        } else {
                            commonData.setMDMUserId("");
                        }
                    }

                    if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_SECURITY_KEY) {
                        //passwd = mCipherText;

                        Log.d("---------------------------------------------------------------");
                        Log.d("보안키보드 암호화 결과 데이터");
                        Log.d("---------------------------------------------------------------");
                        Log.d("mPlainText = " + mPlainText);
                        Log.d("---------------------------------------------------------------");
                        Log.d("mCipherText = " + mCipherText);
                        Log.d("---------------------------------------------------------------");
                        Log.d("passwd = " + passwd);
                        Log.d("---------------------------------------------------------------");

                    }
                    mTrace = FirebasePerformance.getInstance().newTrace("DSM_login");
                    mTrace.start();
                    //TODO 로그인 네트워크 연결하기
                    if(Config.ENABLE_LOGIN_BY_DSM_SERVER) {
                        this.login(id, encpasswd, passwd, networkListener);
                    } else {
                        JoinTologinSuccess(null, null);
                    }
                    mTrace.stop();
                                //this.moveActivity();

                } else {
                    hideProgress();
                }
                break;
            case R.id.id_delete_btn:                // 아이디 삭제
                commonView.setClearEditText(id_edit);
                break;
            case R.id.passwd_delete_btn:            // 비밀번호 삭제
                commonView.setClearEditText(passwd_edit);
                //TransKeyCtrlViewUtil.getInstance().clearText();
                break;
            case R.id.passwd_edit:
                //passwd_edit.requestFocus();
                showTranskey();

        }

        setButtonClickEnabled(true);
    }


    @Override
    public void onBackPressed() {
        showPopup(CommonData.POPUP_TYPE_APP_FINISH, CommonData.PARAM_STRING_NONE);
    }

    @Override
    public void finish() {
        DSMUtil.BackAnimationStart(LoginActivity.this);
        super.finish();
    }



    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();

        switch ( id ) {
            case R.id.id_edit:										    // 아이디
                commonView.setClearImageBt(mIdDelBtn, hasFocus);
                if(hasFocus) {
                    bIDFocus = true;
                    if(!bFirstLoad) {
                        //setOutLayoutUpScroll(true);
                    }
                    bFirstLoad = false;
                } else {
                    bIDFocus = false;
                }
                //TransKeyCtrlViewUtil.getInstance().hideTransKey();
                break;
//            case R.id.passwd_edit:										// 패스워드
//                commonView.setClearImageBt(mPasswdDelBtn, hasFocus);
//                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        commonData.setSaveId(isChecked);
    }

    /**
     * 네트워크 리스너
     */
    public CustomAsyncListener networkListener = new CustomAsyncListener() {

        @Override
        public void onPost(Context context, int type, String resultCode, JSONObject resultData, CustomAlertDialog dialog) {
            // TODO Auto-generated method stub

            switch ( type ) {
                case NetworkConst.NET_DSM_LOGIN:				// 로그인
                    switch ( resultCode ) {
                        case CommonData.API_SUCCESS:
                            Log.i("API_SUCCESS");

                            try {
                                if(resultData != null) {
                                    String code = resultData.getString(CommonData.JSON_LOGIN_RES_CODE);
                                    if(Integer.parseInt(code) == 0) {

                                        JoinTologinSuccess(resultData, dialog);
                                    } else {
                                        dialog.show();
                                    }
                                }

                            } catch (JSONException e) {
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

    private void checkLoginBtn() {
        if((id_edit.length() > 0 && passwd_edit.length() > 0) || (id_edit.length() > 0 && mPlainText.length() > 0)){    // 아이디, 패스워드 입력값이 있다면 로그인 버튼 활성화
            mLoginBtn.setBackgroundResource(R.drawable.background_2_5_009881);
            mLoginBtn.setTextColor(getResources().getColorStateList(R.color.color_ffffff_btn));
            mLoginBtn.setEnabled(true);
        }else{
            mLoginBtn.setBackgroundResource(R.drawable.background_2_5_6fac8b);
            mLoginBtn.setTextColor(getResources().getColor(R.color.color_50ffffff));
            mLoginBtn.setEnabled(false);
        }
//        if(id_edit.length() == 8 && mPlainText.isEmpty() && !bIDFocus) {
//            bPWClicked = true;
//            setOutLayoutUpScroll(true);
//            KeyboardUtils.hideSoftInput((LoginActivity)mContext);
//            TransKeyCtrlViewUtil.getInstance().onClick(keyscroll);
//        }
    }

    /**
     * 입력창 리스너
     */
    class MyTextWatcher implements TextWatcher
    {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Log.d("[onTextChanged] start = " + start + " before = " + before + " count = " + count + " s = " + s);
                //id_edit.setText(id_edit.getText().toString().toUpperCase());
                checkLoginBtn();
                if(!bFirstLoad && s.length() == 8 && StringUtils.isEmpty(passwd_edit.getText())) {
                    bPWClicked = true;
                    setOutLayoutUpScroll(true);
                    KeyboardUtils.hideSoftInput((LoginActivity)mContext);
                    //TransKeyCtrlViewUtil.getInstance().onClick(keyscroll);
                    showTranskey();
                } else {
                    bFirstLoad = false;
                }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            Log.d("[beforeTextChanged] start = " + start + " after = " + after + " count = " + count);
        }

        @Override
        public void afterTextChanged(Editable s)
        {
        }

    }

    public void loginFinish() {
        if(!CommonData.isWebViewActivity) {
            Log.d("[loginFinish] CommonData.isWebViewActivity = false");
            JSONObject loginInfo = CacheUtils.getInstance().getJSONObject(CommonData.JSON_LOGIN_RES_INFO);
            String name = "";
            String ip = "";
            String loginTime = "";
            if (loginInfo != null) {
                try {
                    name = loginInfo.getString(CommonData.JSON_LOGIN_RES_USER_NAME);
                    ip = NetworkUtils.getIPAddress(true);
                    loginTime = loginInfo.getString(CommonData.JSON_LOGIN_TIME);
                } catch (JSONException e) {
                    if (Config.DISPLAY_LOG) e.printStackTrace();
                }

            }
            if (name == null || StringUtils.isEmpty(name)) {
                name = "고객";
            }
            final String msg = name + "님 DB스마트포탈에 오신 것을 환영합니다.\n로그인을 진행합니다.\n\n최종 접속 일시 : \n" + loginTime;
            showPopup(CommonData.POPUP_TYPE_LOGIN_FINISH, msg);

        } else {
            Log.d("[loginFinish] CommonData.isWebViewActivity = true");
        }
    }

    public  void hideNavigationBar()
    {
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOption);
    }
}