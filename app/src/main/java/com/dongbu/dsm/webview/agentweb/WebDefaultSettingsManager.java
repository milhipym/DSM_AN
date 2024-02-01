package com.dongbu.dsm.webview.agentweb;


import android.os.Build;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * <b>@项目名：</b> agentweb<br>
 * <b>@包名：</b>com.dongbu.dsm.webview.agentweb<br>
 * <b>@创建者：</b> cxz --  just<br>
 * <b>@创建时间：</b> &{DATE}<br>
 * <b>@公司：</b><br>
 * <b>@邮箱：</b> cenxiaozhong.qqcom@qq.com<br>
 * <b>@描述:source CODE  https://github.com/Justson/AgentWeb</b><br>
 */

public class WebDefaultSettingsManager implements AgentWebSettings, WebListenerManager {

    private WebSettings mWebSettings;
    private static final String TAG = WebDefaultSettingsManager.class.getSimpleName();

    public static WebDefaultSettingsManager getInstance() {
        return new WebDefaultSettingsManager();
    }

    protected WebDefaultSettingsManager() {

    }

    @Override
    public AgentWebSettings toSetting(WebView webView) {
        settings(webView);
        return this;
    }

    private void settings(WebView webView) {


        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
        {
            mWebSettings.setSupportZoom(true);
            mWebSettings.setBuiltInZoomControls(true);
            mWebSettings.setDisplayZoomControls(false);
        }
        mWebSettings.setSavePassword(false);
        if (AgentWebUtils.checkNetwork(webView.getContext())) {
            mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mWebSettings.setTextZoom(100);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setSupportMultipleWindows(false);
        mWebSettings.setBlockNetworkImage(false);
        mWebSettings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebSettings.setAllowFileAccessFromFileURLs(false);
        }
        mWebSettings.setAllowUniversalAccessFromFileURLs(false);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        } else {
            mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setNeedInitialFocus(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setDefaultFontSize(16);
        mWebSettings.setMinimumFontSize(5);
        mWebSettings.setGeolocationEnabled(true);

        String dir = AgentWebConfig.getCachePath(webView.getContext());

        LogUtils.i(TAG, "dir:" + dir + "   appcache:" + AgentWebConfig.getCachePath(webView.getContext()));
        mWebSettings.setGeolocationDatabasePath(dir);
        mWebSettings.setDatabasePath(dir);
        mWebSettings.setAppCachePath(dir);

        mWebSettings.setAppCacheMaxSize(Long.MAX_VALUE);


    }

    @Override
    public WebSettings getWebSettings() {
        return mWebSettings;
    }

    @Override
    public WebListenerManager setWebChromeClient(WebView webview, WebChromeClient webChromeClient) {
        webview.setWebChromeClient(webChromeClient);

        return this;
    }

    @Override
    public WebListenerManager setWebViewClient(WebView webView, WebViewClient webViewClient) {
        webView.setWebViewClient(webViewClient);
        return this;
    }

    @Override
    public WebListenerManager setDownLoader(WebView webView, DownloadListener downloadListener) {
        webView.setDownloadListener(downloadListener);
        return this;
    }


}
