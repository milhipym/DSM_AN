package com.dongbu.dsm.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.R;
import com.dongbu.dsm.app.DSMApplication;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.CustomAlertDialog;
import com.dongbu.dsm.common.CustomAlertDialogInterface;
import com.dongbu.dsm.util.IntentUtil;
import com.dongbu.dsm.util.KeyboardUtil;
import com.dongbu.dsm.util.Log;
import com.dongbu.dsm.util.MDMUtils;
import com.dongbu.dsm.util.WebViewKeyboardUtils;
import com.dongbu.dsm.webview.agentweb.AgentWeb;
import com.dongbu.dsm.webview.agentweb.AgentWebSettings;
import com.dongbu.dsm.webview.agentweb.ChromeClientCallbackManager;
import com.dongbu.dsm.webview.agentweb.IEventHandler;
import com.dongbu.dsm.webview.agentweb.WebDefaultSettingsManager;
import com.dongbu.dsm.webview.jsbridge.BridgeWebView;
import com.dongbu.dsm.webview.jsbridge.BridgeWebViewClient;
import com.dongbu.dsm.webview.jsbridge.WebviewCallbackFuncs;
import com.m2soft.bridge.FormFieldBridgeInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by LandonJung on 2017-8-10.
 * DSM 웹뷰 클래스
 * @since 0, 1
 */
public class BaseAgentWebViewActivity extends BaseActivity { //implements View.OnClickListener{

    private BridgeWebView mWebView;
    protected AgentWeb mAgentWeb;
    private LinearLayout mContainer;
    private Intent mIntent;
    private String mUrl;
    private View contentView_;
    private int priorVisibleHeight_;

    private View decorView;
    private int uiOption;

    private FormFieldBridgeInterface formFieldBridgeInterface;

    public interface WebViewListener {
        public void changeUrl(String url);
        public void callPostMain();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.agent_webview_activity);
        mContext = this;

        mIntent		=	getIntent();
        mUrl		=	mIntent.getStringExtra(CommonData.EXTRA_URL);

        Log.d("[onCreate] mUrl = " + mUrl);

        if(mWebView == null) {
            mWebView    =   new BridgeWebView(this);
            if (Build.VERSION.SDK_INT > 20) {
                getWindow().setFlags(
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }

            // yeom : javascript interface 연결
            formFieldBridgeInterface = new FormFieldBridgeInterface(this, mWebView);
            mWebView.addJavascriptInterface(formFieldBridgeInterface, "m2softFormFieldBridge");
    }

    mContainer = (LinearLayout) this.findViewById(R.id.webview_container);
    contentView_ = mContainer;
        if(mAgentWeb == null) {
        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(mContainer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                //.setAgentWebParent(mContainer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, size.y+getSoftMenuHeight()))
                //.useDefaultIndicator().setIndicatorColorWithHeight(getResources().getColor(R.color.color_2a231f), 20)
                .useDefaultIndicator().defaultProgressBarColor()
                .setAgentWebSettings(getSettings())
                .setReceivedTitleCallback(mCallback)
                .setWebView(mWebView)
                .setEventHandler(eventHandler)
                .setWebChromeClient(new TermsWebViewChromeClient())
                .setWebViewClient(new BridgeWebViewClient(mWebView, mDialog, this))
                .setSecutityType(AgentWeb.SecurityType.strict)
                //.setWebLayout(new WebLayout(this))
                .createAgentWeb()//
                .ready()
                .go(Config.ENABLE_DEMO ? mUrl : getUrl(), Config.ENABLE_DEMO ? true : false);
    }

        mWebViewListener = new WebViewListener() {
            @Override
            public void changeUrl(String url) {

            }

            @Override
            public void callPostMain() {
                doPostUrl();
            }
        };

        // 신규 창 호출 시
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setSupportMultipleWindows(true);

        if(Config.ENABLE_WEBVIEW_LOAD_NO_CACHE) {
            mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        WebviewCallbackFuncs.getInstance().setContextAndActivity(this, this);
        WebviewCallbackFuncs.getInstance().regWebView(getWebView(), mAgentWeb);
        WebviewCallbackFuncs.getInstance().initRegisterHandler();

        getWebView().setBackgroundColor(Color.TRANSPARENT);

        WebViewKeyboardUtils.assistActivity(this);

        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);

        //하단의 네비게이션 단말의 경우 하단이 짤리는 것을 대비하여 네비게이션바 내림
        KeyboardUtil.addKeyboardToggleListener(mWebView, new KeyboardUtil.SoftKeyboardToggleListener()
       {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible)
            {
                Log.d("keyboard visible: "+isVisible);
                if(isVisible) {

                } else {
                    hideNavigationBar();
                }
            }


        });

        if(!Config.ENABLE_DEMO)
            doPostUrl();
    }



    public BridgeWebView getWebView() {
        return (BridgeWebView) mAgentWeb.getWebCreator().get();
    }

    private IEventHandler eventHandler = new IEventHandler() {
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            return false;
        }

        @Override
        public boolean back() {

            hideProgress();
            mAgentWeb.getIndicatorController().offerIndicator().hide();

            if(mWebView.canGoBack() && CommonData.mWebviewPageCount > 1) {
                mWebView.goBack();
            }
//            showPopup(CommonData.POPUP_TYPE_APP_FINISH, CommonData.PARAM_STRING_NONE);

            return false;
        }
    };

    public String getUrl() {

        return "";
    }

    public void setupHW(boolean enable) {
        if(enable) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    public String doPostUrl() {
        if(NetworkUtils.isConnected()) {

            if(mAgentWeb != null && mWebView != null) {

                if(Config.ENABLE_DEMO_VIDEO) {
                    String customHtml = "<html><head><title>Sample</title></head><body><video controls><source src='http://www.broken-links.com/tests/media/BigBuck.m4v'></video></body></html>";
                    mAgentWeb.getLoader().loadData(customHtml, "text/html", "UTF-8");

                } else {
                    // TODO 메인 웹 진입 시 파라메터 확인 수정 ( 세션값 전달 )
                    String postData = "?";// + CommonData.WEB_SERVICE_MAIN_URL_PARAM;

                    Log.d("CommonData.isEnterBojang = " + CommonData.isEnterBojang);
                    // 보장분석 파라메터
                    if(CommonData.isEnterBojang) {
                        JSONObject paramMap = CacheUtils.getInstance().getJSONObject(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG);
                        if(paramMap != null) {
                            try {
                                if(paramMap.has(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_MPGID))
                                    postData += CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_MPGID + "=" + URLEncoder.encode(paramMap.getString(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_MPGID), "utf-8");
                                if(paramMap.has(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_SSO_TOKEN))
                                    postData += "&" + CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_SSO_TOKEN + "=" + URLEncoder.encode(paramMap.getString(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_SSO_TOKEN), "utf-8");
                                if(paramMap.has(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_PGNM))
                                    postData += "&" + CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_PGNM + "=" + URLEncoder.encode(paramMap.getString(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_PGNM), "utf-8");
                                if(paramMap.has(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_HEIGHT))
                                    postData += "&" + CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_HEIGHT + "=" + URLEncoder.encode(paramMap.getString(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_HEIGHT), "utf-8");
                                if(paramMap.has(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_MENU_ID))
                                    postData += "&" + CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_MENU_ID + "=" + URLEncoder.encode(paramMap.getString(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_MENU_ID), "utf-8");
                                if(paramMap.has(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_PAGE_TYPE))
                                    postData += "&" + CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_PAGE_TYPE + "=" + URLEncoder.encode(paramMap.getString(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_PAGE_TYPE), "utf-8");
                                if(paramMap.has(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_PARAM))
                                    postData += "&" + CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_PARAM + "=" + URLEncoder.encode(paramMap.getString(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_PARAM), "utf-8");
                                if(paramMap.has(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_SYSTEM_ID))
                                    postData += "&" + CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_SYSTEM_ID + "=" + URLEncoder.encode(paramMap.getString(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_SYSTEM_ID), "utf-8");
                                if(paramMap.has(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_VIEW))
                                    postData += "&" + CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_VIEW + "=" + URLEncoder.encode(paramMap.getString(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_VIEW), "utf-8");
                                if(paramMap.has(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_WIDTH))
                                    postData += "&" + CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_WIDTH + "=" + URLEncoder.encode(paramMap.getString(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG_WIDTH), "utf-8");

                                //postData = URLEncoder.encode(postData, "utf-8");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
//                        CacheUtils.getInstance().put(CommonData.JSON_APP_CALL_EXTERNAL_BOJANG, (JSONObject)null);
                        }
                        CommonData.isEnterBojang = false;
                    } else {
                        postData += CommonData.WEB_SERVICE_MAIN_URL_PARAM;
                    }

//                    if(CommonData.isEnterBojang) {
//                        //mUrl = CommonData.WEB_SERVICE_HOME_URL_PAD;
//                        CommonData.isEnterBojang = false;
//                    }

                    Log.i("Web Param= " + mUrl + postData);

                    //CSH20180305 cookie 세팅 위치 변경, 로그인 요청 결과 성공 메세지 팝업 전으로 이동
                    //AgentWebConfig.removeSessionCookies();
                    //AgentWebConfig.syncCookie(mUrl, CacheUtils.getInstance().getString(CommonData.JSON_LOGIN_RES_JSESSIONID));

                    mAgentWeb.getLoader().postUrl(mUrl + postData, "".getBytes());
                    //mAgentWeb.getLoader().postUrl(mUrl, postData.getBytes());

                    //CSH20180305 cookie 세팅 위치 변경, 로그인 요청 결과 성공 메세지 팝업 전으로 이동
                    //AgentWebConfig.removeSessionCookies();
                    //AgentWebConfig.syncCookie(mUrl, CacheUtils.getInstance().getString(CommonData.JSON_LOGIN_RES_JSESSIONID));

                    Log.i("================  webview header ===============");
                    Log.i(mAgentWeb.getLoader().getHttpHeaders().toString());
                    Log.i("================================================");
                }
            }


        } else {
            // TODO 메인 웹 진입 시 오프라인 상태이면 로컬 404 html 로 연결
        }

        return "";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("[BaseAgentWebViewActivity] result:" + requestCode + " result:" + resultCode);
        mAgentWeb.uploadFileResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // yeom
        if(requestCode == 555 && resultCode == Activity.RESULT_OK) {
            formFieldBridgeInterface.onActivityResult(requestCode, resultCode, data);
        }
    }

    public AgentWebSettings getSettings() {
        return WebDefaultSettingsManager.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("onStart() getClass().getSimpleName() = " + getClass().getSimpleName());
    }

    @Override
    public void onStop() {

        super.onStop();
    }



    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onNewIntent(Intent intent) {

        mUrl		=	intent.getStringExtra(CommonData.EXTRA_URL);

        Log.d("[onNewIntent] mUrl = " + mUrl);
        doPostUrl();

        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        ((DSMApplication)getApplicationContext()).updateSessionTimer();
        mAgentWeb.getWebLifeCycle().onResume();
        Log.e("baseAgentWebView+onResum...");
        if(Config.ENABLE_MDM_AGENT_CHECK) {
            // MDM 이 정상 실행 중인지 채크한다.
            CommonData.isWebViewActivity = true;
            if(MDMUtils.getInstance(this, this).isBindSuccess()) {
                MDMUtils.getInstance(this, this).doProc(CommonData.MDM_ACTION_CHECK_AGENT, true);
            }
//            else {
//                MDMUtils.getInstance(this, this).onResume(this);
//                MDMUtils.getInstance(this, this).setMdmCallbackListener(mdmLoginCallbackListener);
//            }
        }
        hideNavigationBar();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mAgentWeb.destroy();
        Log.e("baseAgentWebView+onDestroy...");
        mAgentWeb.getWebLifeCycle().onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }

        hideProgress();
        mAgentWeb.getIndicatorController().offerIndicator().hide();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        Log.d(mWebView.getUrl());
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            hideProgress();
            mAgentWeb.getIndicatorController().offerIndicator().hide();
            showPopup(CommonData.POPUP_TYPE_APP_FINISH, CommonData.PARAM_STRING_NONE);
        }
    }

    private ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
        }
    };

    private class TermsWebViewChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

            final JsResult finalRes = result;

//            try {
//                message = new String(message.getBytes("euc-kr"), "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }

            CustomAlertDialog dialog = new CustomAlertDialog(BaseAgentWebViewActivity.this, CustomAlertDialog.TYPE_A);
            dialog.setTitle(getString(R.string.popup_dialog_a_type_title));
            dialog.setContent(message);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

                @Override
                public void onClick(CustomAlertDialog dialog, Button button) {

                    dialog.dismiss();
                    finalRes.confirm();
                }
            });

            dialog.setId(CommonData.POPUP_TYPE_A_FROM_WEB);
            dialog.show();

            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {

            final JsResult finalRes = result;

//            try {
//                message = new String(message.getBytes("euc-kr"), "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }

            CustomAlertDialog dialog = new CustomAlertDialog(BaseAgentWebViewActivity.this, CustomAlertDialog.TYPE_B);
            dialog.setTitle(getString(R.string.popup_dialog_a_type_title));
            dialog.setContent(message);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

                @Override
                public void onClick(CustomAlertDialog dialog, Button button) {

                    dialog.dismiss();
                    finalRes.confirm();
                }
            });

            dialog.setPositiveButton(getString(R.string.popup_dialog_button_cancel), new CustomAlertDialogInterface.OnClickListener() {

                @Override
                public void onClick(CustomAlertDialog dialog, Button button) {

                    dialog.dismiss();
                    finalRes.cancel();
                }
            });

            dialog.setId(CommonData.POPUP_TYPE_B_FROM_WEB);
            dialog.show();

            //return super.onJsConfirm(view, url, message, result);
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }


        @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {

                createDialogWithWebView(view, resultMsg);

                return true;
            }

            protected void createDialogWithWebView(WebView webview, Message resultMsg) {

                WebView.HitTestResult result = webview.getHitTestResult();
                int type = result.getType();
                String data = result.getExtra();
                Log.d("URL = " + data);


            if(true) {
//                AlertDialog.Builder alert = new AlertDialog.Builder(webview.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//
//                alert.setView(dialogWebView);
//                loadWebView(dialogWebView, resultMsg);
//                alert.show();
                if(StringUtils.isEmpty(data)) {

                } else {
                    IntentUtil.openWebPage(BaseAgentWebViewActivity.this, data);
                }
            } else {
                WebView dialogWebView = createNewWebView(webview);

//                String cookie = AgentWebConfig.getCookiesByUrl(mUrl);
//                Log.d("***********************");
//                Log.d("cookie = " + cookie);
//                Log.d("CacheUtils JSESSIONID) = " + CacheUtils.getInstance().getString(CommonData.JSON_LOGIN_RES_JSESSIONID));
//                Log.d("***********************");

                Dialog dialog = new Dialog(BaseAgentWebViewActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.dialog_webview);
                BridgeWebView wb = (BridgeWebView) dialog.findViewById(R.id.sub_webview);

                setFusionWebViewSettings(wb);

                wb.setWebChromeClient(this);
                wb.setWebViewClient(new BridgeWebViewClient((BridgeWebView)wb, mDialog, (BaseActivity)mContext));
                wb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                wb.loadUrl(data);
                //AgentWebConfig.removeSessionCookies();
                //AgentWebConfig.createCookiesSyncInstance(webview.getContext());
                //AgentWebConfig.syncCookie(data, CacheUtils.getInstance().getString(CommonData.JSON_LOGIN_RES_JSESSIONID));

                dialog.setCancelable(true);
                dialog.show();
            }
        }

        protected void loadWebView(WebView view, Message resultMsg) {
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(view);
            resultMsg.sendToTarget();
        }

        private WebView createNewWebView(WebView webView) {
            WebView newWebView = new WebView(webView.getContext());

            setFusionWebViewSettings(newWebView);

            newWebView.setWebChromeClient(this);
            newWebView.setWebViewClient(new BridgeWebViewClient((BridgeWebView)webView, mDialog, (BaseActivity)mContext));
            newWebView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            return newWebView;
        }

        protected void setFusionWebViewSettings(WebView newWebView) {
            WebSettings settings = newWebView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setPluginState(WebSettings.PluginState.ON);
            settings.setJavaScriptCanOpenWindowsAutomatically(false);
            settings.setDefaultTextEncodingName("utf-8");
            settings.setSupportMultipleWindows(false);
        }

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
