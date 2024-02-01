package com.dongbu.dsm.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;

import com.blankj.utilcode.util.CacheUtils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.R;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.CustomAlertDialog;
import com.dongbu.dsm.common.CustomAlertDialogInterface;
import com.dongbu.dsm.common.CustomAsyncListener;
import com.dongbu.dsm.common.NetworkConst;
import com.dongbu.dsm.util.DSMUtil;
import com.dongbu.dsm.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by LandonJung on 2017-8-10.
 * 네트워크 요청 쓰레드 클래스
 * @since 0, 1
 */
public class RequestAsyncNetwork extends Thread {
    private Context mContext;
    private int			mType;
    private String mUrl;
    private HttpUtil	mHttpUtil;
    private CustomAsyncListener mAsyncListener;
    private String mParams;
    private ArrayList<NameValuePair> mNameValuePair;
    private HashMap<String, String> fileParams;
    private ArrayList<String> ArrParams;

    private String response	=	"";

    /**
     * HttpURLConnection 에서 사용
     * @param context
     * @param type
     * @param listener
     * @param params
     */
    public RequestAsyncNetwork(Context context , int type , CustomAsyncListener listener, HashMap<String, String> params)
    {
        this.mContext			= context;
        this.mType				= type;
        this.mAsyncListener 	= listener;

        this.mParams			= getParams(params);

        mHttpUtil				= new HttpUtil();
    }

    /**
     * HttpClient 에서 사용
     * @param context
     * @param type
     * @param listener
     * @param params
     */
    /*
    public RequestAsyncNetwork(Context context , int type , CustomAsyncListener listener, ArrayList<NameValuePair> params)
    {
        this.mContext			= context;
        this.mType				= type;
        this.mAsyncListener 	= listener;

        this.mNameValuePair		= getParams(params);

        mHttpUtil				= new HttpUtil();
    }
    */

    /**
     * 비동기 네트워크
     * @param context   컨텍스트
     * @param type  API 구분타입
     * @param url   서버 url
     * @param listener  리스너
     * @param params    파라미터
     */
    public RequestAsyncNetwork(Context context, int type, String url, CustomAsyncListener listener, ArrayList<NameValuePair> params){
        this.mContext			= context;
        this.mType           =  type;
        this.mUrl				= url;
        this.mAsyncListener 	= listener;

        this.mNameValuePair		= getParams(params);

        mHttpUtil				= new HttpUtil();
    }

    /**
     * 비동기 네트워크 ( 파일업로드 )
     * @param context   컨텍스트
     * @param type  API 구분타입
     * @param url   서버 url
     * @param listener  리스너
     * @param params    파라미터
     */
    public RequestAsyncNetwork(Context context, int type, String url, CustomAsyncListener listener, HashMap<String, String> params){
        this.mContext			= context;
        this.mType           =  type;
        this.mUrl				= url;
        this.mAsyncListener 	= listener;
        this.fileParams     =   params;

        mHttpUtil				= new HttpUtil();
    }

    /**
     * 비동기 네트워크 ( 파일업로드 )
     * @param context   컨텍스트
     * @param type  API 구분타입
     * @param url   서버 url
     * @param listener  리스너
     * @param params    파라미터
     * @param flag 임시
     */
    public RequestAsyncNetwork(Context context, int type, String url, CustomAsyncListener listener, ArrayList<String> params, int flag){
        this.mContext			= context;
        this.mType           =  type;
        this.mUrl				= url;
        this.mAsyncListener 	= listener;
        this.ArrParams     =   params;

        mHttpUtil				= new HttpUtil();
    }

    @SuppressLint("HandlerLeak")
//	private final Handler mNetworkHandler = new Handler()
    // UI 작업을 처리하기 위해서는 아래와 같이 UI쓰레드에 바인딩된 Handler를 만들어야 합니다.
    private final Handler mNetworkHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);

            CustomAlertDialog dialog = null;

            Log.i("msg.what = " + msg.what);

            switch ( msg.what ) {

                case -2: // 실행 종료 요청 (인터럽트)
                    mAsyncListener.onStop(mContext, mType);
                    break;

                case 0: // 데이터 가져오기 성공

                    JSONObject resultObject = null;
                    JSONObject dataObject	= null;
                    JSONArray dataArray = null;
                    JSONObject alertObject	= null;

                    try {
                        if(response != null) {
                            Log.d("response = " + response);
                        }
                        resultObject = new JSONObject(response);
                        String resultCode, resultMessage;

                        if(resultObject.has("result") && resultObject.has("success")) {
                            // MDM 오류 메세지
                            resultCode = resultObject.getBoolean("result") ? CommonData.API_SUCCESS : CommonData.API_FAIL;
                            resultMessage = resultObject.getString("message");
                        } else {
                            resultCode = resultObject.has(CommonData.JSON_RESULT_CODE) ? resultObject.getString(CommonData.JSON_RESULT_CODE) : CommonData.API_CODE_NONE;
                            resultMessage = resultObject.has(CommonData.JSON_RESULT_MESSAGE) ? resultObject.getString(CommonData.JSON_RESULT_MESSAGE) : "";
                        }

                        if(resultObject.has(CommonData.JSON_DATA) && !resultObject.isNull(CommonData.JSON_DATA)) {
                            dataObject 	= resultObject.getJSONObject(CommonData.JSON_DATA);

                            if(resultMessage == null || resultMessage.isEmpty() || resultMessage.equals("null")) {
                                if(dataObject.has(CommonData.JSON_LOGIN_RES_MSG)) {
                                    resultMessage = dataObject.getString(CommonData.JSON_LOGIN_RES_MSG);
                                }
                            }
                        } else if(resultObject.has(CommonData.JSON_MDM_DATA) && !resultObject.isNull(CommonData.JSON_MDM_DATA)) {

                            dataObject 	= resultObject.getJSONObject(CommonData.JSON_MDM_DATA);
                        }

                        dialog = new CustomAlertDialog(mContext, CustomAlertDialog.TYPE_A);
                        dialog.setTitle("안내");
                        dialog.setContent(resultMessage);
                        dialog.setPositiveButton(mContext.getResources().getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {
                            @Override
                            public void onClick(CustomAlertDialog dialog, Button button) {
                                dialog.dismiss();
                            }
                        });

                        // result code 정의 작업
                        mAsyncListener.onPost(mContext, mType, resultCode, dataObject, dialog);

                    } catch (JSONException e) {
                        if(Config.DISPLAY_LOG) e.printStackTrace();

                        dialog = new CustomAlertDialog(mContext, CustomAlertDialog.TYPE_A);
                        dialog.setTitle(mContext.getResources().getString(R.string.popup_dialog_data_error_title));
                        dialog.setContent(mContext.getResources().getString(R.string.popup_dialog_server_error_content));
                        dialog.setPositiveButton(mContext.getResources().getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {
                            @Override
                            public void onClick(CustomAlertDialog dialog, Button button) {
                                dialog.dismiss();
                            }
                        });

                        mAsyncListener.onDataError(mContext, mType, response, dialog);
                    }

                    break;

                default: // 네트워크 오류

                    dialog = new CustomAlertDialog(mContext, CustomAlertDialog.TYPE_A);

                    // 와이파이 연결 오류
                    if ( msg.what == -1 ) {
                        dialog.setTitle(mContext.getResources().getString(R.string.popup_dialog_netword_error_title));
                        dialog.setContent(mContext.getResources().getString(R.string.popup_dialog_netword_error_content));
                        dialog.setPositiveButton(mContext.getResources().getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

                            @Override
                            public void onClick(CustomAlertDialog dialog, Button button) {

                                //DSMUtil.sendBroadCast(mContext, CommonData.BROADCAST_ACTIVITY_FINISH);
                                dialog.dismiss();
                            }
                        });
                    }
                    else {

                        dialog.setTitle(mContext.getResources().getString(R.string.popup_dialog_netword_error_title));
                        dialog.setContent(mContext.getResources().getString(R.string.popup_dialog_netword_error_content));
                        dialog.setPositiveButton(mContext.getResources().getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

                            @Override
                            public void onClick(CustomAlertDialog dialog, Button button) {

                                dialog.dismiss();
                            }
                        });

                    }

                    mAsyncListener.onNetworkError(mContext, mType, mHttpUtil.responseResultCode, dialog);

                    break;
            }
        }
    };

    /**
     * HttpUrlConnection 에서 사용
     * @param body  파라미터
     * @return
     */
    public String getParams(HashMap<String, String> body) {

        if ( body == null )
            body = new HashMap<String, String>();


        if ( !body.containsKey("member_id") )
            body.put("member_id", CommonData.getInstance().getMDMUserId()+"");

        if ( !body.containsKey("device_type") )
            body.put("device_type", "A");

        if ( !body.containsKey("session_code") )

            body.put("session_code", CommonData.getInstance().getSessionToken());

//        if ( !body.containsKey("store_id") )
//            body.put("store_id", NetworkConst.getInstance().getMarketId()+"");

        if ( !body.containsKey("app_ver")){	// app_ver 이 없다면
            if(!CommonData.getInstance().getAppVer().equals("")){	// app_ver 이 공백이 아니라면
                body.put("app_ver", CommonData.getInstance().getAppVer());
            }else{													// app_ver 이 공백이라면

                try {
                    PackageInfo pi = mContext.getPackageManager().getPackageInfo( mContext.getPackageName(), 0 );
                    body.put("app_ver", pi.versionName.toString());
                } catch (Exception e) {
                    Log.e(e.toString());
                }

            }

        }

        String result = "?";

        Iterator<String> iterator = body.keySet().iterator();

        while (iterator.hasNext()) {

            String key = (String) iterator.next();

            Log.v("Params Key : " + key + ", Value : " + body.get(key));

            String value = "";

            try {
                value = URLEncoder.encode(body.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {

                if(Config.DISPLAY_LOG) e.printStackTrace();
            }

            result += "&" + key + "=" + value;

        }

        if ( result.length() > 3 ) {
            result = result.replace("?&", "");
        }

        return result;
    }

    /**
     * HttpClient 에서 사용
     * @param body  네트워크 파라미터
     * @return HttpClient request 에 필요한 param 이 누락되어 있으면 추가하여 돌려준다.
     */
    public ArrayList<NameValuePair> getParams(ArrayList<NameValuePair> body) {
        boolean isMember		= false;
        boolean isDeviceType	= false;
        boolean isSessionCode	= false;
        boolean isStoreId		= false;
        boolean isAppver		= false;

        if ( body == null || body.size() == 0 ){
            body = new ArrayList<NameValuePair>();
            body.add(new BasicNameValuePair("member_id", String.valueOf(CommonData.getInstance().getMDMUserId())));
        }

        for(int i=0; i<body.size(); ++i){
            if(!body.get(i).getName().contains("member_id")){
                if(i == (body.size()-1) && isMember == false){
                    body.add(new BasicNameValuePair("member_id", String.valueOf(CommonData.getInstance().getMDMUserId())));
                }
            }else{
                isMember = true;
            }
        }

        for(int i=0; i<body.size(); ++i){
            if(!body.get(i).getName().contains("device_type")){
                if(i == (body.size()-1) && isDeviceType == false){
                    body.add(new BasicNameValuePair("device_type", "A"));
                }
            }else{
                isDeviceType = true;
            }
        }

        for(int i=0; i<body.size(); ++i){
            if(!body.get(i).getName().contains("session_code")){
                if(i == (body.size()-1) && isSessionCode == false){
                    body.add(new BasicNameValuePair("session_code", CommonData.getInstance().getSessionToken()));
                }
            }else{
                isSessionCode = true;
            }
        }

//        for(int i=0; i<body.size(); ++i){
//            if(!body.get(i).getName().contains("store_id")){
//                if(i == (body.size()-1) && isStoreId == false){
//                    body.add(new BasicNameValuePair("store_id", String.valueOf(NetworkConst.getInstance().getMarketId())));
//                }
//            }else{
//                isStoreId = true;
//            }
//        }

        for(int i=0; i<body.size(); ++i){
            if(!body.get(i).getName().contains("app_ver")){
                if(i == (body.size()-1) && isAppver == false){
                    if(!CommonData.getInstance().getAppVer().equals("")){	// app_ver 이 공백이 아니라면
                        body.add(new BasicNameValuePair("app_ver", CommonData.getInstance().getAppVer()));
                    }else{													// app_ver 이 공백이라면
                        try {
                            PackageInfo pi = mContext.getPackageManager().getPackageInfo( mContext.getPackageName(), 0 );
                            body.add(new BasicNameValuePair("app_ver", pi.versionName.toString()));
                        } catch (Exception e) {
                            Log.e(e.toString());
                        }
                    }
                }
            }else{
                isAppver = true;
            }
        }

        return body;
    }

    @Override
    public void run()
    {
        super.run();

        boolean bNetworkRequest = false;

        Message msg = Message.obtain();

        try {

            bNetworkRequest = requestNetwork();

        }
        catch (Exception e) {
            e.getStackTrace();
            Log.e("InterruptedException === " + e.getMessage());
        }

        if ( interrupted() ) {
            msg.what = -2;
        }
        else {

            Log.i("bNetworkRequest = " + bNetworkRequest);

            if ( bNetworkRequest )  {

                // 네트워크 관련 오류가 있는지 확인
                if ( mHttpUtil.responseResultCode == HttpURLConnection.HTTP_OK ) {
                    Log.i("msg.what = 0");
                    msg.what = 0;
                }
                else {
                    Log.i("msg.what = " +mHttpUtil.responseResultCode);
                    msg.what = mHttpUtil.responseResultCode;
                }

            }
            else {
                Log.i("msg.what = -1");
                msg.what = -1;
            }

        }

        mNetworkHandler.sendMessage(msg);


    }

    /**
     * 네트워크 url 세팅
     * @param url api url
     */
    private void setNetworkHttp(String url) {
        switch (mType){
            case NetworkConst.NET_SET_UPLOAD:   // 문자열 + 파일 업로드
                response	=	mHttpUtil.HttpFileUploads(url, ArrParams);
                break;
            case NetworkConst.NET_SET_UPLOAD_PAPERS:
                response	=	mHttpUtil.HttpFileUploadsForPapers(url, ArrParams);
                break;

            case NetworkConst.NET_SET_PHOTO:    // 사진
                response	=	mHttpUtil.HttpFileUpload(url, fileParams);
                break;
            case NetworkConst.NET_DSM_LOGIN:      // 문자열(JSON) in body
                if(Config.ENABLE_LOGIN_HTTPS) {
                    if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_API) {
                        response = mHttpUtil.requestHttpsClientPostBodyJSON_En(url, mNameValuePair);
                    } else {
                        response = mHttpUtil.requestHttpsClientPostBodyJSON(url, mNameValuePair);
                    }

                } else {
                    response = mHttpUtil.requestHttpClientPostBodyJSON(url, mNameValuePair);
                }
                break;
            case NetworkConst.NET_MDM_GET_APP_INFO:      // 문자열(JSON) in body
                response = mHttpUtil.requestHttpsClientPostBodyJSON(url, mNameValuePair);
                break;

            default:    // 그외 네트워크
                response = mHttpUtil.requestHttpClient(url, mNameValuePair);
                break;
        }

        Log.i("Thread type = " +mType +"response = " +response);
    }

    /**
     * 네트워크 체크
     * @return boolean 네트워크 상태 리턴
     */
    private boolean checkNetwork() {

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        boolean isWifiAvail = ni.isAvailable();
        boolean isWifiConn = ni.isConnected();

        ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean isMobileAvail = ni.isAvailable();
        boolean isMobileConn = ni.isConnected();


        String status = "\nWiFi Avail = " + isWifiAvail + ", Conn = "
                + isWifiConn + "\nMobile Avail = " + isMobileAvail
                + ", Conn = " + isMobileConn;

        Log.e("Network Status : " + status);

        if (!isWifiConn && !isMobileConn) {
            return false;
        }

        return true;

    }

    /**
     * 네트워크 활성화 체크
     * @return  boolean ( true - 네트워크 사용중, false - 네트워크 미사용 )
     */
    private boolean isNetworkStat() {
        try{
            ConnectivityManager manager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo lte_4g = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
            boolean blte_4g = false;
            if(lte_4g != null)
                blte_4g = lte_4g.isConnected();
            if( mobile != null ) {
                if (mobile.isConnected() || wifi.isConnected() || blte_4g)
                    return true;
            } else {
                if ( wifi.isConnected() || blte_4g )
                    return true;
            }
        }catch(Exception e){
            Log.e(e.toString());
        }

        return false;
    }

    /**
     * 네트워크 상태 체크
     * @return  boolean ( true - 네트워크 사용중, false - 네트워크 미사용 )
     * @throws InterruptedException
     */
    private boolean requestNetwork() throws InterruptedException {

        this.response	=	"";

        // 네트워크 상태확인
//		if ( !checkNetwork() ){
        if ( !isNetworkStat()){
            return false;
        }

        /* 2015-10-23 api 호출시 네트워크 타입 제거
        this.setNetworkHttp(mType);
        */
        this.setNetworkHttp(mUrl);
        Log.i("return = true ");
        return true;
    }
}
