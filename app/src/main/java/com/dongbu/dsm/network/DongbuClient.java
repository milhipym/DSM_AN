package com.dongbu.dsm.network;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.NetworkConst;
import com.dongbu.dsm.util.HandlerUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import kr.mtcom.smartmessage.common.AppBaseUtil;
import kr.mtcom.smartmessage.common.Config;
import kr.mtcom.smartmessage.common.LOG;
import kr.mtcom.smartmessage.manager.AccountManager;

public class DongbuClient {
	
	private static String TAG = "DongbuClient";
    private static final String BASE_URL_CHAT = Config.HOST_NAME_CHAT+"/";
//    private static final String BASE_URL = Config.HOST_NAME+"/";
    
    private static final String ACCESS_TOKEN = "auth_code";

    
//    private static AsyncHttpClient client = new AsyncHttpClient();
    private static AsyncHttpClient client;
    private static AsyncHttpClient chatClient;

    public static void initialize() {
        LOG.d(TAG, "DongbuClient initialize");

        client = new AsyncHttpClient();
        chatClient = new AsyncHttpClient();

    }

    public static void finishClient() {
        client = null;
        chatClient = null;
    }

    public static void getChat(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, Context context, boolean needAccessToken) {
    	LOG.d(TAG, "url : " + url);
        if (params!=null) {
            LOG.d(TAG, "params : " + params);
        }
        chatClient.get(getAbsoluteUrlChat(url), params, responseHandler);
    }
    
    public static void postChat(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, Context context, boolean needAccessToken) {
    	LOG.d(TAG, "url : " + url);
        if (params!=null) {
            LOG.d(TAG, "params : " + params);
        }
//    	if (needAccessToken && !CarusoAccountManager.getInstance().isAuthenticated(context)) {
//    		return;
//    	}
//
//    	client.addHeader(ACCESS_TOKEN, CarusoAccountManager.getAccessToken(context));

        chatClient.post(getAbsoluteUrlChat(url), params, responseHandler);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, Context context, boolean needAccessToken) {

        LOG.d(TAG, "url : " + url);
        if (params!=null) {
            LOG.d(TAG, "params : " + params);
        }

    	if (needAccessToken && !AccountManager.isAuthenticated(context)) {
    		return;
    	}

    	client.addHeader(ACCESS_TOKEN, AccountManager.getAccessToken(context));

        client.get(getAbsoluteUrl(url, context), params, responseHandler);

    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, Context context, boolean needAccessToken) {

        LOG.d(TAG, "url : " + url);
        if (params!=null) {
            LOG.d(TAG, "params : " + params);
        }

    	if (needAccessToken && !AccountManager.isAuthenticated(context)) {
    		return;
    	}

    	client.addHeader(ACCESS_TOKEN, AccountManager.getAccessToken(context));

        client.post(getAbsoluteUrl(url, context), params, responseHandler);

    }


    
    public static void delete(String url, AsyncHttpResponseHandler responseHandler, Context context, boolean needAccessToken) {
    	
    	LOG.d(TAG, "url : " + url);
    	   	
//    	if (needAccessToken && !CarusoAccountManager.getInstance().isAuthenticated(context)) {
//    		return;
//    	}
//
//    	client.addHeader(ACCESS_TOKEN, CarusoAccountManager.getAccessToken(context));
    	
    	client.delete(getAbsoluteUrl(url, context), responseHandler);
    }

    
    
    private static String getAbsoluteUrl(String relativeUrl, Context context) {
//    	LOG.d(TAG, BASE_URL + relativeUrl );

        String baseUrl = NetworkConst.getInstance().getPushDomain(); //AppBaseUtil.getMetaData(context).getString("kr.mdongbu.smartmessage.base_url");

//        if (AppBaseUtil.getIsDebug(context)) {
//            baseUrl = AppBaseUtil.getMetaData(context).getString("kr.mdongbu.smartmessage.base_url_dev");
//        } else {
//            baseUrl = AppBaseUtil.getMetaData(context).getString("kr.mdongbu.smartmessage.base_url_prod");
//        }

        if (TextUtils.isEmpty(baseUrl)) {
            Toast.makeText(context, "URL 오류. manifest 파일에서 url을 설정하세요.", Toast.LENGTH_SHORT).show();
        }

        return baseUrl + "/" + relativeUrl;
    }

    private static String getAbsoluteUrlChat(String relativeUrl) {
//    	LOG.d(TAG, BASE_URL + relativeUrl );

        return BASE_URL_CHAT + relativeUrl;
    }
  
}