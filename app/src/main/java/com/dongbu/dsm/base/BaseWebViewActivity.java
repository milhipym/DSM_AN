package com.dongbu.dsm.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.blankj.utilcode.util.NetworkUtils;
import com.dongbu.dsm.R;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.CustomAlertDialog;
import com.dongbu.dsm.common.CustomAlertDialogInterface;
import com.dongbu.dsm.common.NetworkConst;
import com.dongbu.dsm.util.Log;
import com.dongbu.dsm.webview.jsbridge.BridgeWebView;
import com.dongbu.dsm.webview.jsbridge.BridgeWebViewClient;
import com.dongbu.dsm.webview.jsbridge.DefaultHandler;
import com.dongbu.dsm.webview.jsbridge.WebviewCallbackFuncs;

import org.apache.http.util.EncodingUtils;

/**
 * Created by LandonJung on 2017-8-10.
 * DSM 웹뷰 클래스
 * @since 0, 1
 */
public class BaseWebViewActivity extends BaseActivity implements View.OnClickListener{

    private BridgeWebView mWebView;
    private Intent mIntent;
    private String mUrl;

    private ImageView mBackImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);

        mIntent		=	getIntent();
        mUrl		=	mIntent.getStringExtra(CommonData.EXTRA_URL);
        mWebView	=	(BridgeWebView) findViewById(R.id.web_view);

        // 클라이언트 설정
        //mWebView.setWebViewClient(new TermsWebViewClinet());
        mWebView.setWebViewClient(new BridgeWebViewClient(mWebView, mDialog, this));
        mWebView.setWebChromeClient(new TermsWebViewChromeClient());

        // 속성 설정
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setSavePassword(false);
        mWebView.getSettings().setSupportZoom(true);

        //mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setTextZoom(100);
        mWebView.setDefaultHandler(new DefaultHandler());

        // 해더 설정
        //mWebView.get addHttpHeader("X-Requested-With", "My wonderful app");


        WebviewCallbackFuncs.getInstance().setContextAndActivity(this, this);
        //WebviewCallbackFuncs.getInstance().setTransKeyResultListener(transKeyResultListener);
        WebviewCallbackFuncs.getInstance().regWebView(mWebView, null);
        WebviewCallbackFuncs.getInstance().initRegisterHandler();

        mWebView.setBackgroundColor(Color.TRANSPARENT);

        if ( Build.VERSION.SDK_INT >= 11 )
            mWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        //setTitle(mIntent.getStringExtra(CommonData.EXTRA_ACTIVITY_TITLE));

       if(NetworkUtils.isConnected()) {
           // TODO 메인 웹 진입 시 파라메터 확인 수정
           String postData = "member_id=" + CommonData.getInstance().getMDMUserId() +
                   "&device_type=A" +
                   "&app_ver=" + CommonData.getInstance().getAppVer();
           mWebView.postUrl(mUrl, EncodingUtils.getBytes(postData, "BASE64"));
       } else {
           // TODO 메인 웹 진입 시 오프라인 상태이면 로컬 404 html 로 연결
       }

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
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {

            mWebView.goBack();

            return true;

        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onClick(View v) {
        if ( mWebView.canGoBack()) {

            mWebView.goBack();

            return;

        }else{
            finish();
        }
    }

    private class TermsWebViewChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            //return super.onJsAlert(view, url, message, result);
            return true;
        }
    }

    private class TermsWebViewClinet extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            handler.proceed(); // SSL 에러가 발생해도 계속 진행!
            Log.i("onReceivedSslError()");
//            mDialog = new CustomAlertDialog(BaseWebViewActivity.this, CustomAlertDialog.TYPE_B);
//            mDialog.setTitle(getResources().getString(R.string.popup_dialog_a_type_title));
//            mDialog.setContent(getResources().getString(R.string.popup_dialog_serucity_content));
//            mDialog.setPositiveButton(getResources().getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(CustomAlertDialog dialog, Button button) {
//                    handler.proceed(); // SSL 에러가 발생해도 계속 진행!
//                    dialog.dismiss();
//                }
//            });
//            mDialog.setNegativeButton(null, new CustomAlertDialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(CustomAlertDialog dialog, Button button) {
//                    handler.cancel();    // 취소
//                    dialog.dismiss();
//                }
//            });
//            mDialog.show();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            showProgress();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            hideProgress();
            super.onPageFinished(view, url);
        }

    }


}
