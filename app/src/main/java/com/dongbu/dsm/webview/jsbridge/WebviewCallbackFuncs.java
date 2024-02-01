package com.dongbu.dsm.webview.jsbridge;

import android.content.Context;
import android.os.Build;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.app.DSMApplication;
import com.dongbu.dsm.base.BaseActivity;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.DataManager;
import com.dongbu.dsm.model.web.BCResultWebItem;
import com.dongbu.dsm.model.web.Contract;
import com.dongbu.dsm.model.web.Info;
import com.dongbu.dsm.model.web.OCRItem;
import com.dongbu.dsm.model.web.Parameter;
import com.dongbu.dsm.util.DSMUtil;
import com.dongbu.dsm.util.GSONUtils;
import com.dongbu.dsm.util.Log;
import com.dongbu.dsm.util.SecurityUtil;
import com.dongbu.dsm.webview.agentweb.AgentWeb;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * App <-> Web 인터페이스
 * Created by landonjung on 2017. 8. 14..
 */
public class WebviewCallbackFuncs {

    private final String TAG = "WebviewCallbackFuncs";

    private static WebviewCallbackFuncs instance;
    private BridgeWebView mMainWebView;
    private BridgeWebView mMainWebViewSub;
    private Context mContext;
    private BaseActivity mActivity;
    private AgentWeb mAgentWeb;
//    private TransKeyUtil transKeyUtil;
//    private TransKeyUtil.TransKeyResultListener transKeyResultListener;
    public static String showInputElementID;

//    private int mCurrentTranskeyType = TransKeyUtil.TRANSKEY_TYPE_NONE;

    private CallBackFunction callbackFunctionGetAppInfo;

    private CallBackFunction callbackFunction_DO_OCR_CALL;                      // 신분증 인식 결과 콜백
    private CallBackFunction callbackFunction_DO_ID_CALL;                       // 명함 인식 결과 콜백
    private CallBackFunction callbackFunction_DO_PAPER_CALL;                    // A4 결과 콜백
    private CallBackFunction callbackFunction_DO_START_EXTERNAL_APP;            // 외부 앱 실행
    private CallBackFunction callbackFunction_GET_INFO;                         // 디바이스 및 앱 정보
    private CallBackFunction callbackFunction_GET_VALUE;                        // 설정값 불러오기
    private CallBackFunction callbackFunction_SET_VALUE;                        // 설정값 저장하기
    private CallBackFunction callbackFunction_DO_LOGOUT;                        // 로그아웃 결과 콜백
    private CallBackFunction callbackFunction_DO_APP_EXIT;                      // 앱 종료 결과 콜백
    private CallBackFunction callbackFunction_DO_CAMERA_GALLERY_CALL;           // 카메라 / 갤러리 호출
    private CallBackFunction callbackFunction_DO_SMS_CALL;                      // 문자 보내기 화면 호출
    private CallBackFunction callbackFunction_DO_KAKAO_SHARE_CALL;              // 카카오톡 공유 화면 호출
    private CallBackFunction callbackFunction_DO_SEND_EMAIL_CALL;               // 이메일 보내기 화면 호출
    private CallBackFunction callbackFunction_GET_Contract_CALL;                // 연락처 가져오기 호출
    private CallBackFunction callbackFunction_ENABLE_HW_CALL;                   // 하드웨어 엑셀레이션 활성화 호출
    private CallBackFunction callbackFunction_DISABLE_HW_CALL;                  // 하드웨어 엑셀레이션 비활성화 호출

    CallBackFunctionListener mCallBackFunctionListener = new CallBackFunctionListener() {
        @Override
        public void onResultDoOcrCall(OCRItem data, int resultCode) {
            // 신분증 인식 결과 처리

            String resultJSON = DataManager.genJSONResultDoOcrCall(resultCode, "", data);
            String resultJSONEn = resultJSON;
            if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                resultJSONEn = CommonData.getInstance().encode(resultJSON);
            }

            if(callbackFunction_DO_OCR_CALL != null) {
                callbackFunction_DO_OCR_CALL.onCallBack(resultJSONEn);
            }
        }

        @Override
        public void onResultDoIdCall(BCResultWebItem data, int resultCode) {
            // 명함 인식 결과 처리

            String resultJSON = DataManager.genJSONResultDoIdCall(resultCode, "", data);
            String resultJSONEn = resultJSON;
            if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                resultJSONEn = CommonData.getInstance().encode(resultJSON);
            }

            if(callbackFunction_DO_ID_CALL != null) {
                callbackFunction_DO_ID_CALL.onCallBack(resultJSONEn);
            }
        }

        @Override
        public void onResultDoPaperCall(final String data, final int resultCode) {
            // A4 인식 결과 처리

            if(mActivity != null) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String resultJSON = DataManager.genJSONResultDoPaperCall(resultCode, "", data);
                        String resultJSONEn = resultJSON;
                        if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                            resultJSONEn = CommonData.getInstance().encode(resultJSON);
                        }

                        if(callbackFunction_DO_PAPER_CALL != null) {
                            callbackFunction_DO_PAPER_CALL.onCallBack(resultJSONEn);
                            Log.d("문서 촬영 결과 웹으로 전송 완료");
                        }
                    }
                });
            }
        }

        @Override
        public void onCallSetSessionTime(String sessionTime) {
            // 실시간 남은 세션 타임 웹으로 전송

            Parameter parameter = new Parameter();
            parameter.setParam1(String.valueOf(sessionTime));
            String resultJSON = DataManager.genJSONResultParameter(CommonData.API_SUCCESS_WEB_BRIDGE, "", parameter);
            String resultJSONEn = resultJSON;
            if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                resultJSONEn = CommonData.getInstance().encode(resultJSON);
            }

            CallWebSetSessionTime(resultJSONEn);
        }

        @Override
        public void onResultGetInfo(Info data, int resultCode) {
            // 디바이스 및 앱 정보 전송

            String resultJSON = DataManager.genJSONResultGetInfo(resultCode, "", data);
            String resultJSONEn = resultJSON;
            if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                resultJSONEn = CommonData.getInstance().encode(resultJSON);
            }

            if(callbackFunction_GET_INFO != null) {
                callbackFunction_GET_INFO.onCallBack(resultJSONEn);
            }
        }

        @Override
        public void onResultLogout(int resultCode) {
            // 로그아웃 결과 전송

            String resultJSON = DataManager.genJSONResultDefault(resultCode, "");
            String resultJSONEn = resultJSON;
            if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                resultJSONEn = CommonData.getInstance().encode(resultJSON);
            }

            if(callbackFunction_DO_LOGOUT != null) {
                callbackFunction_DO_LOGOUT.onCallBack(resultJSONEn);
            }
        }

        @Override
        public void onResultAppExit(int resultCode) {
            // 앱 종료 결과 전송

            String resultJSON = DataManager.genJSONResultDefault(resultCode, "");
            String resultJSONEn = resultJSON;
            if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                resultJSONEn = CommonData.getInstance().encode(resultJSON);
            }

            if(callbackFunction_DO_APP_EXIT != null) {
                callbackFunction_DO_APP_EXIT.onCallBack(resultJSONEn);
            }
        }

        @Override
        public void onResultDoStartExternalApp(int resultCode) {
            // 외부 앱 실행 결과 전송

            String resultJSON = DataManager.genJSONResultDefault(resultCode, "");
            String resultJSONEn = resultJSON;
            if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                resultJSONEn = CommonData.getInstance().encode(resultJSON);
            }

            if(callbackFunction_DO_START_EXTERNAL_APP != null) {
                callbackFunction_DO_START_EXTERNAL_APP.onCallBack(resultJSONEn);
            }

        }

        @Override
        public void onResultDoCameraGalleryCall(String data, int resultCode) {
            // 카메라 / 갤러리 이미지 가져오기 결과 전송

            String resultJSON = DataManager.genJSONResultDoCameraGalleryCall(resultCode, "", data);
            String resultJSONEn = resultJSON;
            if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                resultJSONEn = CommonData.getInstance().encode(resultJSON);
            }

            if(callbackFunction_DO_CAMERA_GALLERY_CALL != null) {
                callbackFunction_DO_CAMERA_GALLERY_CALL.onCallBack(resultJSONEn);
            }

        }

        @Override
        public void onResultGetContractCall(Contract data, int resultCode) {
            // 연락처 가져오기 결과 전송

            String resultJSON = DataManager.genJSONResultGetContractCall(resultCode, "", data);
            String resultJSONEn = resultJSON;
            if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                resultJSONEn = CommonData.getInstance().encode(resultJSON);
            }

            if(callbackFunction_GET_Contract_CALL != null) {
                callbackFunction_GET_Contract_CALL.onCallBack(resultJSONEn);
            }
        }

        @Override
        public void onResultDefault(int resultCode) {
            String resultJSON = DataManager.genJSONResultDefault(resultCode, "");

            String resultJSONEn = resultJSON;
            if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                resultJSONEn = CommonData.getInstance().encode(resultJSON);
            }

            if(callbackFunction_DO_SEND_EMAIL_CALL != null) {
                callbackFunction_DO_SEND_EMAIL_CALL.onCallBack(resultJSONEn);
                callbackFunction_DO_SEND_EMAIL_CALL = null;
            } else if(callbackFunction_DO_SMS_CALL != null) {
                callbackFunction_DO_SMS_CALL.onCallBack(resultJSONEn);
                callbackFunction_DO_SMS_CALL = null;
            } else if(callbackFunction_DO_KAKAO_SHARE_CALL != null) {
                callbackFunction_DO_KAKAO_SHARE_CALL.onCallBack(resultJSONEn);
                callbackFunction_DO_KAKAO_SHARE_CALL = null;
            }

        }

        @Override
        public void onResultSetValue(int resultCode) {
            String resultJSON = DataManager.genJSONResultDefault(resultCode, "");

            String resultJSONEn = resultJSON;
            if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                resultJSONEn = CommonData.getInstance().encode(resultJSON);
            }

            if(callbackFunction_SET_VALUE != null) {
                callbackFunction_SET_VALUE.onCallBack(resultJSONEn);
                callbackFunction_SET_VALUE = null;
            }
        }

        @Override
        public void onResultGetValue(String param, int resultCode) {
            String key = "";
            try {
                JSONObject paramJSON = new JSONObject(param);
                key = paramJSON.getString(CommonData.PARAM1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String value = ((BaseActivity)mActivity).commonData.getValue(key);
            String resultJSON = DataManager.genJSONResultGetValueCall(resultCode, "", key, value);
            String resultJSONEn = resultJSON;
            if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                resultJSONEn = CommonData.getInstance().encode(resultJSON);
            }

            if(callbackFunction_GET_VALUE != null) {
                callbackFunction_GET_VALUE.onCallBack(resultJSONEn);
            }
        }
    };

    public static synchronized WebviewCallbackFuncs getInstance(){
        if (instance == null) {
            instance = new WebviewCallbackFuncs();
        }
        return instance;
    }

    public void setContextAndActivity(Context context, BaseActivity activity) {
        mContext = context;
        mActivity = activity;
        mActivity.setCallBackFunctionListener(mCallBackFunctionListener);
    }

    public WebviewCallbackFuncs() {
    }

    public void regWebView(BridgeWebView webView, AgentWeb agentWeb) {
        mMainWebView = webView;
        mAgentWeb = agentWeb;
    }

    public void regWebViewSub(BridgeWebView webView) {
        mMainWebViewSub = webView;
    }

    public void initRegisterHandler() {

        if(mMainWebView != null) {
            ///////////////////////////////////////////////////////////////////////////////////////////////////////
            // DO
            // 프로그래스바 시작
            mMainWebView.registerHandler(CommonData.JS_FUNC_DO_START_PROGRESS, new BridgeHandler() {

                @Override
                public void handler(String data, CallBackFunction function) {

                    //callbackFunctionGetAppInfo = function;
                    mAgentWeb.getIndicatorController().offerIndicator().show();
                    mActivity.showProgress();
                }

            });

            // 프로그래스바 종료
            mMainWebView.registerHandler(CommonData.JS_FUNC_DO_STOP_PROGRESS, new BridgeHandler() {

                @Override
                public void handler(String data, CallBackFunction function) {

                    //callbackFunctionGetAppInfo = function;
                    mAgentWeb.getIndicatorController().offerIndicator().hide();
                    mActivity.hideProgress();
                }

            });

            // 신분증 인식 촬영 액티비티
            mMainWebView.registerHandler(CommonData.JS_FUNC_DO_OCR_CALL, new BridgeHandler() {

                @Override
                public void handler(String data, CallBackFunction function) {

                    callbackFunction_DO_OCR_CALL = function;
                    mActivity.startActivity(CommonData.ACTIVITY_MAGIC_ID_READER, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
                }

            });

            // 명함 인식 촬영 액티비티
            mMainWebView.registerHandler(CommonData.JS_FUNC_DO_ID_CALL, new BridgeHandler() {

                @Override
                public void handler(String data, CallBackFunction function) {

                    callbackFunction_DO_ID_CALL = function;
                    mActivity.startActivity(CommonData.ACTIVITY_MAGIC_BC_READER, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
                }

            });

            // A4 촬영 액티비티
            mMainWebView.registerHandler(CommonData.JS_FUNC_DO_PAPER_CALL, new BridgeHandler() {

                @Override
                public void handler(String data, CallBackFunction function) {

                    callbackFunction_DO_PAPER_CALL = function;

                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        if(jsonObject != null) {
                            CacheUtils.getInstance().put(CommonData.JS_DO_PAPER_CALL_PARAM, jsonObject);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    mActivity.startActivity(CommonData.ACTIVITY_MAGIC_DR, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
                }

            });

            // 세션 타임 업데이트
            mMainWebView.registerHandler(CommonData.JS_FUNC_DO_UPDATE_SESSION_TIME, new BridgeHandler() {

                @Override
                public void handler(String data, CallBackFunction function) {

                    if(mActivity != null) {
                        ((DSMApplication) mActivity.getApplicationContext()).updateSessionTimer();
                    }
                }
            });

            // 외부 앱 실행
            mMainWebView.registerHandler(CommonData.JS_FUNC_DO_START_EXTERNAL_APP, new BridgeHandler() {

                @Override
                public void handler(String dataEn, CallBackFunction function) {

                    callbackFunction_DO_START_EXTERNAL_APP = function;
                    Log.d("외부 앱 실행 : " + dataEn);
                    String data = dataEn;
                    if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                        data = CommonData.getInstance().decode(dataEn);
                    }

                    Parameter parameter = GSONUtils.changeGsonToBean(data, Parameter.class);
                    if(parameter != null) {
                        String log = GSONUtils.modelToJson(parameter);
                        Log.d("------------------ " + Parameter.class.getName() + " 결과 (JSON)------------------------");
                        Log.d(log);
                        Log.d("----------------------------------------------------------------------------------------");

                        CommonData.parameter = parameter;

                        if(parameter.getParam1().equals(CommonData.APP_CALL_ID_PK_TEST)) {
                            mActivity.startActivity(CommonData.ACTIVITY_PK_TEST, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
                        } else if(parameter.getParam1().equals(CommonData.APP_CALL_ID_VIRTUAL_SYSTEM)) {
                            mActivity.startActivity(CommonData.ACTIVITY_VIRTUAL_SYSTEM, parameter.getParam2(), CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
                        } else if(parameter.getParam1().equals(CommonData.APP_CALL_ID_EPROMY)) {
                            mActivity.startActivity(CommonData.ACTIVITY_EPROMY, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
                        } else if(parameter.getParam1().equals(CommonData.APP_CALL_ID_ESIGN)) {
                            mActivity.startActivity(CommonData.ACTIVITY_ESIGN, parameter.getParam2(), parameter.getParam3(), parameter.getParam4(), parameter.getParam5(), parameter.getParam6(), parameter.getParam7());
                        }
                        else if(parameter.getParam1().equals(CommonData.APP_CALL_DB_LEARNING))
                        {
                            if(AppUtils.isInstallApp(CommonData.APP_PACKAGE_DB_LEARNING))
                            {
                                mActivity.startActivity(CommonData.ACTIVITY_DBLEARNING, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
                            }
                            else if(parameter.getParam1().equals(CommonData.APP_CALL_DB_LEARNING)) {
                                String appName = CommonData.APP_NAME_DB_LEARNING;
                                mActivity.showPopup(CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_WEBINSTALL, appName);
                            }
                        }
                        else if(parameter.getParam1().equals(CommonData.APP_CALL_DEEP_MEDI))
                        {
                            if(AppUtils.isInstallApp(CommonData.APP_PACKAGE_DEEP_MEDI))
                            {
                                mActivity.startActivity(CommonData.ACTIVITY_DEEPMEDI, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
                            }
                            else if(parameter.getParam1().equals(CommonData.APP_CALL_DEEP_MEDI)) {
                                String appName = CommonData.APP_NAME_DEEP_MEDI;
                                mActivity.showPopup(CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_MARKET, appName);
                            }
                        }
                        else {
                            if(AppUtils.isInstallApp(parameter.getParam1())) {AppUtils.launchApp(parameter.getParam1());}
                            else
                            {
                                String appName = "";
                                if(parameter.getParam1().equals(CommonData.APP_PACKAGE_NAME_BOHUM)) {
                                    appName = CommonData.APP_NAME_BOHUM;
                                    mActivity.showPopup(CommonData.POPUP_TYPE_INSTALL_FOR_GO_TO_MARKET, appName);
                                }
                            }
                        }
                    }
                }

            });

            // 메인으로
            mMainWebView.registerHandler(CommonData.JS_FUNC_GO_MAIN, new BridgeHandler() {

                @Override
                public void handler(String data, CallBackFunction function) {
                    mMainWebView.clearHistory();
                    mMainWebView.loadUrl(CommonData.WEB_MAIN_URL_MANUAL);
                }

            });

            // 앱 종료
            mMainWebView.registerHandler(CommonData.JS_FUNC_APP_FINISH, new BridgeHandler() {

                @Override
                public void handler(String data, CallBackFunction function) {
                    callbackFunction_DO_APP_EXIT = function;
                    mActivity.showPopup(CommonData.POPUP_TYPE_APP_FINISH, CommonData.PARAM_STRING_NONE);
                }

            });

            // 앱 종료
            mMainWebView.registerHandler(CommonData.JS_FUNC_DO_LOGOUT, new BridgeHandler() {

                @Override
                public void handler(String data, CallBackFunction function) {
                    callbackFunction_DO_LOGOUT = function;
                    mActivity.logout();
                    //mActivity.showPopup(CommonData.POPUP_TYPE_LOGOUT_MANUAL);
                }

            });

            // 카메라 / 갤러리 호출
            mMainWebView.registerHandler(CommonData.JS_FUNC_DO_CAMERA_GALLERY_CALL, new BridgeHandler() {

                @Override
                public void handler(String dataEn, CallBackFunction function) {
                    callbackFunction_DO_CAMERA_GALLERY_CALL = function;

                    String data = dataEn;
                    if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                        data = CommonData.getInstance().decode(dataEn);
                    }

                    Parameter parameter = GSONUtils.changeGsonToBean(data, Parameter.class);
                    if(parameter != null) {
                        String log = GSONUtils.modelToJson(parameter);
                        Log.d("------------------ " + Parameter.class.getName() + " 결과 (JSON)------------------------");
                        Log.d(log);
                        Log.d("----------------------------------------------------------------------------------------");
                    }
                    mActivity.startActivity(CommonData.ACTIVITY_CAMERA_GALLERY, Integer.parseInt(parameter.getParam1()), CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
                }

            });

            // 문자 보내기 화면 호출
            mMainWebView.registerHandler(CommonData.JS_FUNC_DO_SMS_CALL, new BridgeHandler() {

                @Override
                public void handler(String dataEn, CallBackFunction function) {
                    callbackFunction_DO_SMS_CALL = function;

                    String data = dataEn;
                    if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                        data = CommonData.getInstance().decode(dataEn);
                    }

                    Parameter parameter = GSONUtils.changeGsonToBean(data, Parameter.class);
                    if(parameter != null) {
                        String log = GSONUtils.modelToJson(parameter);
                        Log.d("------------------ " + Parameter.class.getName() + " 결과 (JSON)------------------------");
                        Log.d(log);
                        Log.d("----------------------------------------------------------------------------------------");
                    }
                    mActivity.startActivity(CommonData.ACTIVITY_SEND_SMS, parameter.getParam1(), parameter.getParam2(), CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
                }

            });

            // 카카오톡 공유 호출
            mMainWebView.registerHandler(CommonData.JS_FUNC_DO_KAKAO_SHARE_CALL, new BridgeHandler() {

                @Override
                public void handler(String dataEn, CallBackFunction function) {
                    callbackFunction_DO_KAKAO_SHARE_CALL = function;

                    String data = dataEn;
                    if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                        data = CommonData.getInstance().decode(dataEn);
                    }

                    Parameter parameter = GSONUtils.changeGsonToBean(data, Parameter.class);
                    if(parameter != null) {
                        String log = GSONUtils.modelToJson(parameter);
                        Log.d("------------------ " + Parameter.class.getName() + " 결과 (JSON)------------------------");
                        Log.d(log);
                        Log.d("----------------------------------------------------------------------------------------");
                    }
                    mActivity.startActivity(CommonData.ACTIVITY_KAKAOTALK_SHARE, parameter.getParam1(), parameter.getParam2(), parameter.getParam3(), parameter.getParam4(), CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
                }

            });

            // 이메일 보내기 화면 호출
            mMainWebView.registerHandler(CommonData.JS_FUNC_DO_SEND_EMAIL_CALL, new BridgeHandler() {

                @Override
                public void handler(String dataEn, CallBackFunction function) {
                    callbackFunction_DO_SEND_EMAIL_CALL = function;

                    String data = dataEn;
                    if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_BRIDGE) {
                        data = CommonData.getInstance().decode(dataEn);
                    }

                    Parameter parameter = GSONUtils.changeGsonToBean(data, Parameter.class);
                    if(parameter != null) {
                        String log = GSONUtils.modelToJson(parameter);
                        Log.d("------------------ " + Parameter.class.getName() + " 결과 (JSON)------------------------");
                        Log.d(log);
                        Log.d("----------------------------------------------------------------------------------------");
                    }
                    mActivity.startActivity(CommonData.ACTIVITY_SEND_EMAIL, parameter.getParam1(), parameter.getParam2(), parameter.getParam3(), CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
                }

            });


            ///////////////////////////////////////////////////////////////////////////////////////////////////////
            // GET
            mMainWebView.registerHandler(CommonData.JS_FUNC_GET_INFO, new BridgeHandler() {

                @Override
                public void handler(String data, final CallBackFunction function) {

                    callbackFunction_GET_INFO = function;

                    int resultCode = CommonData.API_FAIL_WEB_BRIDGE;

                    try {
                        Info info = new Info();
                        info.setDeviceId(CommonData.getInstance().GetDevicesUUID());
                        info.setAppVer(AppUtils.getAppVersionName());
                        info.setIp(NetworkUtils.getIPAddress(true));    // true : IPV4 ? false : IPV6
                        info.setMacAddr(DeviceUtils.getMacAddress());
                        info.setOsName(Config.OS_NAME);
                        info.setOsVerName(Build.VERSION.RELEASE);
                        info.setOsVer(String.valueOf(DeviceUtils.getSDKVersion()));
                        info.setPhoneModel(DeviceUtils.getModel());
                        info.setPad(DSMUtil.isTablet());
                        info.setPhoneNum(DSMUtil.getModifiedPhoneNumber(mContext));

                        resultCode = CommonData.API_SUCCESS_WEB_BRIDGE;
                        mCallBackFunctionListener.onResultGetInfo(info, resultCode);

                    } catch ( Exception e ) {
                        if(Config.DISPLAY_LOG) e.printStackTrace();
                        mCallBackFunctionListener.onResultGetInfo(null, resultCode);
                    }
                }
            });

            mMainWebView.registerHandler(CommonData.JS_FUNC_GET_VALUE, new BridgeHandler() {

                @Override
                public void handler(String data, final CallBackFunction function) {

                    callbackFunction_GET_VALUE = function;

                    int resultCode = CommonData.API_FAIL_WEB_BRIDGE;

                    try {
                        resultCode = CommonData.API_SUCCESS_WEB_BRIDGE;
                        mCallBackFunctionListener.onResultGetValue(data, resultCode);

                    } catch ( Exception e ) {
                        if(Config.DISPLAY_LOG) e.printStackTrace();
                        mCallBackFunctionListener.onResultGetValue(null, resultCode);
                    }
                }
            });

            // 연락처 가져오기 호출
            mMainWebView.registerHandler(CommonData.JS_FUNC_GET_CONTRACT_CALL, new BridgeHandler() {

                @Override
                public void handler(String data, CallBackFunction function) {
                    callbackFunction_GET_Contract_CALL = function;
                    mActivity.startActivity(CommonData.ACTIVITY_CONTRACT, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
                }

            });


            ///////////////////////////////////////////////////////////////////////////////////////////////////////
            // SET
            mMainWebView.registerHandler(CommonData.JS_FUNC_SET_ROTATE, new BridgeHandler() {

                @Override
                public void handler(String data, CallBackFunction function) {

                    //callbackFunctionGetAppInfo = function;
                }

            });

            mMainWebView.registerHandler(CommonData.JS_FUNC_SET_VALUE, new BridgeHandler() {

                @Override
                public void handler(String data, CallBackFunction function) {

                    callbackFunction_SET_VALUE = function;

                    Parameter parameter = GSONUtils.changeGsonToBean(data, Parameter.class);
                    if(parameter != null) {
                        String log = GSONUtils.modelToJson(parameter);
                        Log.d("------------------ " + Parameter.class.getName() + " 결과 (JSON)------------------------");
                        Log.d(log);
                        Log.d("----------------------------------------------------------------------------------------");
                    }
                    if(mActivity != null) {
                        ((BaseActivity)mActivity).commonData.setValue(parameter.getParam1(), parameter.getParam2());
                    }

                }

            });

            ///////////////////////////////////////////////////////////////////////////////////////////////////////
            // ENABLE / DISABLE
            mMainWebView.registerHandler(CommonData.JS_FUNC_ENABLE_HW, new BridgeHandler() {

                @Override
                public void handler(String data, CallBackFunction function) {
                    callbackFunction_ENABLE_HW_CALL = function;
                    if (Build.VERSION.SDK_INT < 21) {
                        mMainWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                        Log.d("웹뷰 하드웨어 가속 활성");
                    }
                }

            });

            mMainWebView.registerHandler(CommonData.JS_FUNC_DISABLE_HW, new BridgeHandler() {

                @Override
                public void handler(String data, CallBackFunction function) {
                    callbackFunction_DISABLE_HW_CALL = function;
                    if (Build.VERSION.SDK_INT < 21) {
                        mMainWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                        Log.d("웹뷰 하드웨어 가속 비활성");
                    }
                }

            });


        }
    }

    /**
     * 웹으로 현재 남은 세션 타임을 전달
     * @param resultData    세션 타임 정보 JSON 데이터
     */
    public void CallWebSetSessionTime(final String resultData) {
        Log.e("[CallWebSetSessionTime] start ");

//        mActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//                mMainWebView.callHandler("setSessionTime", resultData, new CallBackFunction() {
//                    @Override
//                    public void onCallBack(String data) {
//                        com.dongbu.dsm.util.Log.e("onCallBack = " + data);
//                    }
//                });
//            }
//        });

    }

    public BridgeWebView getMainWebView() {
        return mMainWebView;
    }

    public void setMainWebView(BridgeWebView webview) {
        this.mMainWebView = webview;
    }



    public void CallGetAppInfoCallBackFunc(String resultData) {

        if(callbackFunctionGetAppInfo == null) return;

        callbackFunctionGetAppInfo.onCallBack(SecurityUtil.encode(resultData));
    }

    public void CallBackFunctionForFinishVideoCall(final String resultData) {
        Log.e("[CallBackFunctionForFinishVideoCall] start ");

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mMainWebView.callHandler("videoPhoneResultCall", SecurityUtil.encode(resultData), new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        com.dongbu.dsm.util.Log.e("onCallBack = " + data);
                    }
                });
            }
        });

    }

//    public void CallForSendInputTextCertNumber(final String data) {
//        mActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//                TransKeySecureInputTextToWeb transKeySecureInputTextToWeb = new TransKeySecureInputTextToWeb();
//                transKeySecureInputTextToWeb.setTxt(data);
//                transKeySecureInputTextToWeb.setElementId(WebviewCallbackFuncs.showInputElementID);
//
//                String json = new Gson().toJson(transKeySecureInputTextToWeb);
//
//                mMainWebView.callHandler("doTransKeyCallBack", json, new CallBackFunction() {
//                    @Override
//                    public void onCallBack(String data) {
//                        Logger.getInstance().e("TransKeyUtil", "onCallBack = " + data);
//                    }
//                });
//            }
//        });
//
//    }
}
