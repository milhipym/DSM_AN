package com.dongbu.dsm.webview.jsbridge;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.CacheUtils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.app.DSMApplication;
import com.dongbu.dsm.base.BaseActivity;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.CustomAlertDialog;
import com.dongbu.dsm.util.Log;
import com.dongbu.dsm.webview.agentweb.AgentWebConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by bruce on 10/28/15.
 */
public class BridgeWebViewClient extends WebViewClient {
    private BridgeWebView webView;
    private CustomAlertDialog mDialog;
    private BaseActivity mParent;
    private int mCount = 0;

    public BridgeWebViewClient(BridgeWebView webView, CustomAlertDialog dialog, BaseActivity parent) {
        this.webView = webView;
        this.mDialog = dialog;
        this.mParent = parent;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }

//        if (url.startsWith("newtab:")) {
////            addTab(); //add a new tab or window
////            loadNewURL(url.substring(7)); //strip "newtab:" and load url in the webview of the newly created tab or window
//        } else
        if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) {
            webView.handlerReturnData(url);
            return true;
        } else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
            webView.flushMessageQueue();
            return true;
        } else if(url.contains("callDownloadFile.fdo")) {
            webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            //IntentUtil.openWebPage(mParent, url);
            return true;
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
//        if(mParent != null) {
//            mParent.showProgress();
//        }
        Log.d("20180305 : "+url);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {

        //CSH20180305 변경시 세팅만 남김
        //AgentWebConfig.removeSessionCookies();
        AgentWebConfig.syncCookie(url, CacheUtils.getInstance().getString(CommonData.JSON_LOGIN_RES_JSESSIONID));
        //CSH20180305 변경시 세팅만 남김

        super.onPageFinished(view, url);
        Log.d(url);

        if (BridgeWebView.toLoadJs != null) {
            BridgeUtil.webViewLoadLocalJs(view, BridgeWebView.toLoadJs);
        }
        if (webView.getStartupMessage() != null) {
            for (Message m : webView.getStartupMessage()) {
                webView.dispatchMessage(m);
            }
            webView.setStartupMessage(null);
        }
        if(mParent != null) {
            ((DSMApplication)mParent.getApplicationContext()).updateSessionTimer();
        }

        if(Config.ENABLE_WEBVIEW_CLEAR_CACHE) {
            webView.clearCache(true);
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
        if(handler != null) {
            handler.proceed(); // SSL 에러가 발생해도 계속 진행!
        }
        Log.i("onReceivedSslError()");
//        mDialog = new CustomAlertDialog(mParent, CustomAlertDialog.TYPE_B);
//        mDialog.setTitle(mParent.getResources().getString(R.string.popup_dialog_a_type_title));
//        mDialog.setContent(mParent.getResources().getString(R.string.popup_dialog_serucity_content));
//        mDialog.setPositiveButton(mParent.getResources().getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(CustomAlertDialog dialog, Button button) {
//                handler.proceed(); // SSL 에러가 발생해도 계속 진행!
//                dialog.dismiss();
//            }
//        });
//        mDialog.setNegativeButton(null, new CustomAlertDialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(CustomAlertDialog dialog, Button button) {
//                handler.cancel();    // 취소
//                dialog.dismiss();
//            }
//        });
        //mDialog.show();
    }

}