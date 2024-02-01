package com.dongbu.dsm.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.dongbu.dsm.Config;
import com.dongbu.dsm.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by LandonJung on 2017-8-10.
 * 네트워크 공유 클래스
 * @since 0, 1
 */
public class NetworkConst
{
    private static NetworkConst	_instance;

    private Context mContext;
    private PackageInfo pi;


    /**
     * 접속 도메인
     */

    // 리눅스 도메인
    private String TEST_DOMAIN						                    =	Config.SERVER_HOST.SCHEME;       // 이미지 url
    private String TEST_DEF_DOMAIN					                    =	TEST_DOMAIN + "dsm/";   // api url

    private String REAL_DOMAIN						                    =	Config.SERVER_HOST.REAL;            // 이미지 url
    private String REAL_DEF_DOMAIN					                    =	REAL_DOMAIN +"api/"; // api url

    private boolean isReal = Config.IS_SERVER_HOST_REAL;					// true = 실서버 , false = 개발서버
    private boolean isDebug = Config.DISPLAY_LOG;				        // false = 로그 안나옴,true = 로그 나옴

    private int market_id = 1;						// 1 = GOOGLE_MARKET, 2 = TSTORE_MARKET, 3 = NAVER_MARKET
    private int payment_id = 1;						// 0 = UPlus , 1 = INISIS

    /**
     * 앱 실행시 개발 or 리얼서버용 설정
     */
    public void init(){
        if(isReal){
            isReal = true;
            isDebug = Config.DISPLAY_LOG;
        }else{
            isReal = false;
            isDebug = Config.DISPLAY_LOG;
        }

    }

    /**
     * API 도메인 주소
     * @return
     */
    public String getAPIDomain() {

        if ( isReal ) {

            try {
                //REAL_DEF_DOMAIN = Config.SERVER_HOST.SCHEME + URLEncoder.encode(Config.SERVER_HOST.REAL_DOMAIN, "UTF-8") + "/dsm/";
                REAL_DEF_DOMAIN = Config.SERVER_HOST.SCHEME + Config.SERVER_HOST.REAL_DOMAIN + "/dsm/";
                Log.i("REAL_DEF_DOMAIN = " + REAL_DEF_DOMAIN);
            } catch (Exception e) {
                if (Config.DISPLAY_LOG) e.printStackTrace();
            }

            return REAL_DEF_DOMAIN;

        } else {
            try {
                //TEST_DEF_DOMAIN = Config.SERVER_HOST.SCHEME + URLEncoder.encode(Config.SERVER_HOST.TEST_DOMAIN, "UTF-8") + "/dsm/";
                TEST_DEF_DOMAIN = Config.SERVER_HOST.SCHEME + Config.SERVER_HOST.TEST_DOMAIN + "/dsm/";
                Log.i("TEST_DEF_DOMAIN = " + TEST_DEF_DOMAIN);
            } catch (Exception e) {
                if(Config.DISPLAY_LOG) e.printStackTrace();
            }

            return TEST_DEF_DOMAIN;
        }

    }

    /**
     * Web 도메인 주소
     * @return
     */
    public String getWebDomain() {

        if ( isReal ) {
            try {
                //REAL_DOMAIN = Config.SERVER_HOST.SCHEME + URLEncoder.encode(Config.SERVER_HOST.REAL_DOMAIN, "UTF-8") + "/";
                REAL_DOMAIN = Config.SERVER_HOST.SCHEME + Config.SERVER_HOST.REAL_DOMAIN + "/";
            } catch (Exception e) {
                if (Config.DISPLAY_LOG) e.printStackTrace();
            }

            return REAL_DOMAIN;
        } else {
            try {
                //TEST_DOMAIN = Config.SERVER_HOST.SCHEME + URLEncoder.encode(Config.SERVER_HOST.TEST_DOMAIN, "UTF-8") + "/";
                TEST_DOMAIN = Config.SERVER_HOST.SCHEME + Config.SERVER_HOST.TEST_DOMAIN + "/";
            } catch (Exception e) {
                if (Config.DISPLAY_LOG) e.printStackTrace();
            }

            return TEST_DOMAIN;
        }
    }

    /**
     * 이미지 도메인 주소
     * @return
     */
    public String getImgDomain() {

        if ( isReal )
            return REAL_DOMAIN;
        else
            return TEST_DOMAIN;
    }

    /**
     * MDM 서버 주소
     * @return
     */
    public String getMDMDomain() {

        if ( isReal )
            return Config.SERVER_HOST.REAL_MDM;
        else
            return Config.SERVER_HOST.TEST_MDM;
    }

    /**
     * 푸시 서버 주소
     * @return
     */
    public String getPushDomain() {

        if ( isReal )
            return Config.SERVER_HOST.REAL_PUSH;
        else
            return Config.SERVER_HOST.TEST_PUSH;
    }


    public boolean isReal(){
        return isReal;
    }
    public boolean isDebug(){
        return isDebug;
    }

    /**
     * API
     */
    private String DEF_GET_APP_INFO							=   "AppInfo/getVer";
    private String DEF_GET_DSM_LOGIN						=   "zcommon/callDoAppLogin.do";
    private String DEF_GET_DSM_LOGIN_SECURITY				=   "zcommon/callDoAppLoginForMtk.do";
    private String DEF_GET_POPUP_NOTICE                     =   "Mission/getPopupNotice";

    private String DEF_MDM_GET_APP_INFO                     =   "/api/mguard/findDeployAppInfo.do";
    private String DEF_MDM_GET_OPEN_APP_INFO                =   "/api/mguard/findOpenAppInfo.do";

    private String DEF_UPLOAD_PAPER_IMAGES                  =   "zcommon/lib/callFileUpload.fdo";
	
    /**
     * handler return ID
     */
    public static final int NET_GET_APP_INFO	        		=   1;
    public static final int NET_MDM_LOGIN                       =   2;
    public static final int NET_SET_UPLOAD                      =   3;
    public static final int NET_SET_PHOTO                       =   4;
    public static final int NET_LOGIN                           =   6;
    public static final int NET_DSM_LOGIN                       =   7;
    public static final int NET_MDM_GET_APP_INFO                =   8;
    public static final int NET_SET_UPLOAD_PAPERS               =   9;

    //	URL
    public String getAppInfoUrl() { return getAPIDomain() + DEF_GET_APP_INFO; }
    public String getDSMLoginUrl() { return getAPIDomain() + DEF_GET_DSM_LOGIN; }
    public String getPaperUploadUrl() { return getAPIDomain() + DEF_UPLOAD_PAPER_IMAGES; }
    public String getDSMLoginSecurityUrl() { return getAPIDomain() + DEF_GET_DSM_LOGIN_SECURITY; }
    public String getPopupNoticeUrl(){  return getAPIDomain() + DEF_GET_POPUP_NOTICE;   }
    public String getMDMAppInfoUrl(){  return getMDMDomain() + DEF_MDM_GET_APP_INFO;   }
    public String getMDMOpenAppInfoUrl(){  return getMDMDomain() + DEF_MDM_GET_OPEN_APP_INFO;   }

    /**
     * 네트워크 구성 인스턴스 가져오기
     * @return NetworkConst
     */
    public static NetworkConst getInstance()
    {
        if (_instance == null)
        {
            synchronized (NetworkConst.class)
            {
                if(_instance == null)
                {
                    _instance = new NetworkConst();
                }
            }

        }
        return _instance;
    }

    public void setContext(Context context)
    {
        this.mContext = context;
        try {
            pi = mContext.getPackageManager().getPackageInfo( mContext.getPackageName(), 0 );
        } catch (PackageManager.NameNotFoundException e) {

            Log.e(e.toString());
        }
    }

}
