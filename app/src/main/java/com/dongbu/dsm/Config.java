package com.dongbu.dsm;

import com.blankj.utilcode.util.Utils;
import com.dongbu.dsm.intro.IntroActivity;
import com.dongbu.dsm.util.aes256.AES256CipherV1;

import java.io.File;

/**
 * <pre>
 *     author: LandonJung
 *     time  : 2017/08/11
 *     desc  : 설정 클래스
 * </pre>
 */
public class Config {

    public static String PKG = "";                                                // Application 객체 init 시 설정

    // -----------------------------------------------------------------------------------------------------------------------------------------------
    // 설정값

    /**
     * 디버깅 로그 출력 여부
     */
    public static boolean DISPLAY_LOG = true;                                              // 디버깅 로그 출력 여부
    public static boolean DISPLAY_LOG_SESSION_TIME_CHECK = true;                           // 세션타임 디버깅 로그 출력 여부

    public static boolean LIMIT_RUN = false;                                              // 정해진 시간 이후로는 실행 안되도록.
    public static int LIMIT_RUN_YEAR = 2017;                                              // (운영 / MDM 안하는 버전만 적용됨)
    public static int LIMIT_RUN_MONTH = 12;
    public static int LIMIT_RUN_DAY = 10;

    /**
     * 상용망 / 테스트망 접속 서버 호스트 구분
     */
    public static boolean IS_SERVER_HOST_REAL = true;                                     // 상용망 / 테스트망 접속 서버 호스트 구분

    /**
     * 로그인 후 MDM Agent Check 실행 여부
     */
    public static boolean ENABLE_MDM_AGENT_CHECK = true;                                   // 로그인 후 MDM Agent Check 실행 여부

    /**
     * MDM 을 통한 DSM 앱 업데이트 활성화 여부
     */
    public static boolean ENABLE_APP_VERSION_CHECK_UPDATE = true;                           // MDM 을 통한 DSM 앱 업데이트 활성화 여부
    public static boolean ENABLE_APP_VERSION_CHECK_UPDATE_DOWNLOAD_APK_DIRECTLY = false;    // APK 직접 다운로드 후 설치 활성화 여부
    // false 일 경우에는 MDM 으로 화면 전환 후 사용자가 설치하도록 함.
    /**
     * 크래시 레포트 활성화 여부
     */
    public static boolean ENABLE_CRASH_REPORT = false;                                             // 크래시 레포트 활성화 여부

    /**
     * 푸시 활성화 여부 - 통합푸시 앱으로 대체 --> 영원히 false
     * se
     */
     public static boolean ENABLE_PUSH = false;                                             // 푸시 활성화 여부

     /**
     * 웹뷰 캐시 삭제 활성화 여부
     */
    public static boolean ENABLE_WEBVIEW_CLEAR_CACHE = true;                                             // 웹뷰 캐시 삭제 활성화 여부
    public static boolean ENABLE_WEBVIEW_LOAD_NO_CACHE = true;                                            //캐시 안하는 상태 true

    /**
     * MDM 을 통한 DSM 앱 업데이트 활성화 여부
     */
    public static boolean ENABLE_PORTAL_MAIN = true;                                             // 임직원용 포탈 메인 화면 활성화 여부
    public static boolean ENABLE_PORTAL_MAIN_OFFLINE = false;                                    // MDM 연동이 아닌 로컬 JSON 으로 데이터로 테스트

    /**
     * 데모 페이지 활성화 여부
     */
    public static boolean ENABLE_DEMO = false;                                             // 데모 페이지 활성화 여부
    public static boolean ENABLE_DEMO_VIDEO = false;                                             // 비디오 재생 데모 활성화 여부
    /**
     * 데이터 암호화 활성화 여부
     */
    public static boolean ENABLE_ENCRYTO = true;                                              // 데이터 암호화 활성화 여부
    public static boolean ENABLE_ENCRYTO_SECURITY_KEY = true;                                              // 보안키보드 패스워드 암호화 사용 여부
    public static boolean ENABLE_ENCRYTO_API = false;                                            // 서버 API 암호화 여부
    public static boolean ENABLE_ENCRYTO_BRIDGE = false;                                            // 웹앱 인터페이스 암호화 여부

    /**
     * 백신 실행 활성화 여부
     */
    public static boolean ENABLE_VACCINE = true;                                             // 백신 실행 활성화 여부
    public static boolean ENABLE_VACCINE_DEBUG = false;                                      // 백신 디버그 활성화 여부

    /**
     * 로그인 페이지 활성화 여부
     */
    public static boolean ENABLE_LOGIN = true;                                             // 로그인 페이지 활성화 여부 (false면 로그인 하지 않음)
    public static boolean ENABLE_LOGIN_HTTPS = true;                                              // 로그인 HTTPS 사용
    public static boolean ENABLE_LOGIN_BY_DSM_SERVER = true;                                             // DSM 서버 로그인 활성화 여부
    public static boolean ENABLE_LOGIN_TEMP_DATA = false;                                             // 임시로 로그인 아이디/패스워드 입력해 놓은것
    // true 일 경우 MDM / Push 로그인 시 TEST_MDM_LOGIN_ID 를 사용합니다.
    // DSM 로그인은 TEST_LOGIN_ID 사용
    public static String TEST_MDM_LOGIN_ID = "81700535";       // MDM Test Login ID
    public static String TEST_MDM_LOGIN_PW = "Dsmtest1!";      // MDM Test Login PW
    //public static String TEST_LOGIN_ID          = "";       // Test Login ID
    public static String TEST_LOGIN_ID = "11312470";       // Test Login ID
    //public static String TEST_LOGIN_PW          = "a13579..";       // Test Login PW
    public static String TEST_LOGIN_PW = "";       // Test Login PW
    // 명함인식용 아이디 18965600 / a13579..
    /**
     * 로그인 페이지에 버전 정보 표시
     */
    public static boolean ENABLE_DISPLAY_APP_VERSION_IN_LOGIN = true;                                             // 로그인 페이지에 버전 정보 표시


    public static boolean ENABLE_ASSET = false;
    public static boolean ENABLE_JSON_FORMAT = false;
    public static boolean ENABLE_MDM_INSTALL_FROM_ASSET_APK = false;                                             // Asset 에 있는 APK 로 MDM 설치

    public static boolean ENABLE_PROVIDER_SESSION_TEST = false;                                              // provider 를 이용한 세션 및 포탈 앱 호출 테스트 여부


    // -----------------------------------------------------------------------------------------------------------------------------------------------
    // 상수값 ( 변경 )

    public static String OS_NAME = "Android";                                          // 웹 인터페이스 디바이스 정보 전송 시 OS 이름 설정
    //public static String EN_KEY                         = "mWnfPURC9TioO8n2vgHcH3/qZ1L3BeY=";               // 데이터 암호화 키 - JSBrigde ( App <-> Web )
    public static String EN_KEY = AES256CipherV1.getEnKey();                 // 데이터 암호화 키 - JSBrigde ( App <-> Web )
    public static String EN_KEY_MDM = AES256CipherV1.getKeyForMDM();                      // MDM 용 데이터 암호화 키

    public static String MVACCINE_SITE_ID = "directdongbu-potal";
    public static String MVACCINE_LICENSE_KEY = "8d4021957b0e5e78ce4f91fd29e8bf31d93db60f";

    public static class SERVER_HOST {
        // 끝에 '/' 삽입
        public static String REAL_DOMAIN = "dsm.mdbins.com";

        public static String TEST_DOMAIN_1 = "dsm-test.dbins.net";
        public static String TEST_DOMAIN_2 = "dsm-test.dbins.net:18443";

        public volatile static String TEST_DOMAIN = TEST_DOMAIN_1;

        public static String REAL = (ENABLE_LOGIN_HTTPS ? "https://" : "http://") + REAL_DOMAIN + "/";                        // 상용망 서버 호스트 주소
        //public static String TEST                     = "http://10.88.22.26/";                            // 테스트망 서버 호스트 주소
        //public static String TEST_DOMAIN                = "10.88.22.25";
        public static String SCHEME = (ENABLE_LOGIN_HTTPS ? "https://" : "http://");   // 테스트망 서버 호스트 주소
        public static String DEV = "https://dsm-dev.dbins.net/";                     // 개발망 서버 호스트 주소 (미사용)
        public static String REAL_MDM = "https://mdm4.mdbins.com";                       // 상용망 MDM 서버
        public static String TEST_MDM = "https://mdm4t.mdbins.com";                      // 테스트망 MDM 서버
        public static String REAL_PUSH = "http://mpush.mdongbu.com:8100";                  // 상용망 푸시 서버
        public static String TEST_PUSH = "http://10.88.23.30:8100";                        // 테스트망 푸시 서버
    }

    public static class GCM {

        public static final String TAG = "GCM";
        // google acount
        // id : dongbuins.mobile@gmail.com
        // pw : dongbu12345
        public static final String SENDER_ID = "36299094801";
        public static final String PROJECT_ID = "dongbusm";
        public static final String COMPANY_CODE = "DBINS";
        public static final String SERVER_KEY = "AIzaSyB-rP1vmH44B16J4Z30HwAtDMzMVYsxXyQ";
        public static final String FCM_SERVER_KEY = "AIzaSyAziIMoErxfRdRhDGmGbQtc2SoFoc6usdQ";
        public static final String FCM_NEW_SERVER_KEY = "AAAACHOYOxE:APA91bG2WfRx4VjqtiU8TnpaEL76tlCsZEYfJ12J09BXpWvANUEKa327FxbobXUjWjAfz6ziM9ou-ACI81sAD492Ug4J1c9lrEWpbKV8qFo5Lm7_YQSD70oNJLJCpF0Hgg1meKe1dPkL";
        public static final String ROOT_ACTIVITY = IntroActivity.class.getName(); //"com.dongbu.dsm.intro.IntroActivity";
    }

    public static class KAKAO {
        // id : dongbuins.it@gmail.com
        // pw : dongbu123456

    }

    // MDM 다운로드 URL
    // https://mdm4t.mdongbu.com/resources/html/app.html

    // -----------------------------------------------------------------------------------------------------------------------------------------------
    // 테스트 코드

    private static String testApkPath;

    public static String getTestApkPath() {
        if (testApkPath == null)
            testApkPath = Utils.getContext().getCacheDir().getAbsolutePath() + File.separatorChar + "apk" + File.separatorChar + "test_install.apk";
        return testApkPath;
    }
}