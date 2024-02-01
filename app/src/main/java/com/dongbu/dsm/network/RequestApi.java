package com.dongbu.dsm.network;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.dongbu.dsm.Config;
import com.dongbu.dsm.common.CustomAsyncListener;
import com.dongbu.dsm.common.MakeProgress;
import com.dongbu.dsm.widget.DSMProgressDialog;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by LandonJung on 2017-8-10.
 * api 요청 클래스
 * @since 0, 1
 */
public class RequestApi {

    /**
     * 모든 API 요청 ( Activity 호출 )
     * @param context   컨텍스트
     * @param networkType   api 요청 타입 ( int )
     * @param url   api 주소
     * @param networkListener   네트워크 응답 리스너
     * @param params    요청 파라미터
     */
    public static void requestApi(Context context, int networkType, String url, CustomAsyncListener networkListener, ArrayList<NameValuePair> params, RelativeLayout progress){
        RequestAsyncNetwork requestAsyncNetwork = new RequestAsyncNetwork(context, networkType, url,  networkListener, params);
        requestAsyncNetwork.start();
        if(progress != null)
            progress.setVisibility(View.VISIBLE);   // api 호출중에 프로그래스바 활성화
    }

    /**
     * 모든 API 요청 ( Activity 호출 )
     * @param context   컨텍스트
     * @param networkType   api 요청 타입 ( int )
     * @param url   api 주소
     * @param networkListener   네트워크 응답 리스너
     * @param params    요청 파라미터
     */
    public static void requestApi(Context context, int networkType, String url, CustomAsyncListener networkListener, ArrayList<NameValuePair> params, DSMProgressDialog progress){
        RequestAsyncNetwork requestAsyncNetwork = new RequestAsyncNetwork(context, networkType, url,  networkListener, params);
        requestAsyncNetwork.start();

        // api 호출중에 프로그래스바 활성화
//        try {
//            if (progress == null) {
//                progress = new DSMProgressDialog(context);
//                progress.show();
//            } else {
//                progress.show();
//            }
//        } catch (Exception e) {
//            if(Config.DISPLAY_LOG) e.printStackTrace();
//        }
    }

    /**
     * 모든 API 요청 ( Fragment 호출 )
     * @param context   컨텍스트
     * @param networkType   api 요청 타입 ( int )
     * @param url   api 주소
     * @param networkListener   네트워크 응답 리스너
     * @param params    요청 파라미터
     */
    public static void requestApi(Context context, int networkType, String url, CustomAsyncListener networkListener, ArrayList<NameValuePair> params, MakeProgress progress){
        RequestAsyncNetwork requestAsyncNetwork = new RequestAsyncNetwork(context, networkType, url,  networkListener, params);
        requestAsyncNetwork.start();
        if(progress != null)
            progress.show();   // api 호출중에 프로그래스바 활성화
    }

    /**
     * 파일 업로드 API ( Activity 호출 )
     * @param context   컨텍스트
     * @param networkType   api 요청 타입 ( int )
     * @param url   api 주소
     * @param networkListener   네트워크 응답 리스너
     * @param params    요청 파라미터
     */
    public static void requestApi(Context context, int networkType, String url, CustomAsyncListener networkListener, HashMap<String, String> params, RelativeLayout progress){
        RequestAsyncNetwork requestAsyncNetwork = new RequestAsyncNetwork(context, networkType, url,  networkListener, params);
        requestAsyncNetwork.start();
        if(progress != null)
            progress.setVisibility(View.VISIBLE);   // api 호출중에 프로그래스바 활성화
    }

    /**
     * 파일 업로드 API ( Activity 호출 )
     * @param context   컨텍스트
     * @param networkType   api 요청 타입 ( int )
     * @param url   api 주소
     * @param networkListener   네트워크 응답 리스너
     * @param params    요청 파라미터
     * @param flag 사용안함
     */
    public static void requestApi(Context context, int networkType, String url, CustomAsyncListener networkListener, ArrayList<String> params, DSMProgressDialog progress, int flag){
        RequestAsyncNetwork requestAsyncNetwork = new RequestAsyncNetwork(context, networkType, url,  networkListener, params, flag);
        requestAsyncNetwork.start();

        // api 호출중에 프로그래스바 활성화
//        try {
//            if (progress == null) {
//                progress = new DSMProgressDialog(context);
//                progress.show();
//            } else {
//                progress.show();
//            }
//        } catch (Exception e) {
//            if(Config.DISPLAY_LOG) e.printStackTrace();
//        }
//        if(progress != null)
//            progress.setVisibility(View.VISIBLE);   // api 호출중에 프로그래스바 활성화
    }
}
