package com.dongbu.dsm.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.model.web.Parameter;
import com.dongbu.dsm.util.DSMUtil;
import com.dongbu.dsm.util.Log;
import com.dongbu.dsm.util.aes256.AES256CipherV1;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by LandonJung on 2017-8-10.
 * 공유 데이터 클래스
 *
 * @since 0, 1
 */
public class CommonData {

    private Context mContext;
    private static CommonData _instance;

    // -----------------------------------------------------------------------------------------------------------------------------------------------
    // 로컬경로관련

    public static final String TEMP_PATH = android.os.Environment.getExternalStorageDirectory() + "/Android/data/com.dongbu.dsm/tmp/";
    public static final String SAVE_PATH_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + "dsm" + File.separatorChar;
    public static final String SAVE_IMAGE_PATH_ROOT = SAVE_PATH_ROOT + "images" + File.separatorChar;
    public static final String SAVE_APK_PATH_ROOT = SAVE_PATH_ROOT + "apk" + File.separatorChar;

    // -----------------------------------------------------------------------------------------------------------------------------------------------
    // 웹 경로 관련
    public static final String WEB_SERVICE_MAIN_URL_PAD = "dsm/zcommon/loadViewPage.do";
    public static final String WEB_SERVICE_MAIN_URL_PHONE = WEB_SERVICE_MAIN_URL_PAD;
    public static final String WEB_SERVICE_MAIN_URL_PARAM = "pgId=DSMTZB10020UM01";

    //public static String WEB_SERVICE_HOME_URL_PAD = NetworkConst.getInstance().getWebDomain() + "dsm/zcommon/main/home.do";
    public static String WEB_SERVICE_HOME_URL_PAD = NetworkConst.getInstance().getWebDomain() + "dsm/zcommon/main/home.do";
    //public static final String WEB_SERVICE_DOCUMENT_IMAGE_UPLOAD_URL_PAD = NetworkConst.getInstance().getWebDomain() + "dsm/zcommon/lib/callFileUpload.fdo";

//    public static final String WEB_SERVICE_MAIN_URL_PAD = NetworkConst.getInstance().getWebDomain() + "dsm/zcommon/loadViewPage.do";
//    public static final String WEB_SERVICE_MAIN_URL_PHONE = WEB_SERVICE_MAIN_URL_PAD;
//    public static final String WEB_SERVICE_MAIN_URL_PARAM = "pgId=DSMTZB10020UM00";

    public static final String WEB_DEMO_URL = "file:///android_asset/www/demo.html";
    //public static final String WEB_DEMO_URL = "file:///android_asset/www/dsm/html/dsm/dvcpad/k/KAAA010UM.html";

    //html 줌인아웃 테스트 소스
    //public static String WEB_MAIN_URL = "https://cdn.mdbins.com/Upload/testScale.htm";
    //public static String WEB_MAIN_URL_MANUAL = "https://cdn.mdbins.com/Upload/testScale.htm";

    public static String WEB_MAIN_URL = Config.ENABLE_DEMO ?
            WEB_DEMO_URL : DSMUtil.isTablet() ?
            NetworkConst.getInstance().getWebDomain() + WEB_SERVICE_MAIN_URL_PAD : NetworkConst.getInstance().getWebDomain() + WEB_SERVICE_MAIN_URL_PHONE;
    public static String WEB_MAIN_URL_MANUAL = DSMUtil.isTablet() ?
            NetworkConst.getInstance().getWebDomain() + WEB_SERVICE_MAIN_URL_PAD : NetworkConst.getInstance().getWebDomain() + WEB_SERVICE_MAIN_URL_PHONE;

    // ----------------------------------------------------------------------------------------------------------------------------m------------------
    // 브로드캐스트 관련

    public static final String BROADCAST_ACTIVITY_FINISH = "com.dongbu.dsm.activity.finish";
    public static final String BROADCAST_ACTIVITY_LOGOUT = "com.dongbu.dsm.activity.logout";
    public static final String BROADCAST_ACTIVITY_UPDATE_SESSION_TIME = "com.dongbu.dsm.activity.update_session_time";
    public static final String BROADCAST_ACTIVITY_WEBVIEW_CHANGE_URL = "com.dongbu.dsm.activity.webview_change_url";

    public static final String BROADCAST_UPLOAD_MISSION = "BROADCAST_UPLOAD_MISSION";
    public static final String BROADCAST_PUSH_MESSAGE = "BROADCAST_PUSH_MESSAGE";

    // -----------------------------------------------------------------------------------------------------------------------------------------------
    // 웹뷰 관련
    // 웹뷰 URL

    // -----------------------------------------------------------------------------------------------------------------------------------------------
    // 각종 상수값 관련

    // MDM
    public static final String MDM_APK_NAME = "DongbuInsurance_mdm4t.apk";

    // 세션 유지 시간 설정
    public static final int LOGOUT_MAX_TIME_MIN = 20;        // 로그아웃 처리시간 (분)
    public static final int LOGOUT_MAX_TIME = 1 * 60 * LOGOUT_MAX_TIME_MIN;

    public static final String DSM_CONTENT_PROVIDER = "content://" + "com.dongbu.dsm";

    public static final int PORTAL_MAIN_LIST_MAX_COUNT = 40;

    public static final int PARAM_VALUE_NONE = -1;
    public static final String PARAM_STRING_NONE = "";

    // 외부 앱 공통 세션 ID
    public static final String EXTRA_APP_SESSION_ID_KEY = "session_Id";
    public static final String EXTRA_APP_DEFAULT_SESSION_ID = "DSMSESSION_DEFAULT_ID_duddjqdydahqkdlf";
    public static final String EXTRA_APP_SECURITY_KEY = "mobilePortalCypherX2015DBinsTeam";

    public static final int FILE_TYPE_IMAGE = 0;
    public static final int FILE_TYPE_VOICE = 1;

    public static final String YES = "Y";
    public static final String NO = "N";

    public static final int INTRO_POST_DELAYED = 300;
    public static final int ANI_DELAY_100 = 100;
    public static final int ANI_DELAY_200 = 200;
    public static final int ANI_DELAY_500 = 500;
    public static final int ANI_DELAY_1000 = 1000;
    public static final int ANI_DELAY_1500 = 1500;
    public static final int ANI_DELAY_2000 = 2000;
    public static final int ANI_DELAY_2500 = 2500;
    public static final int ANI_DELAY_3000 = 3000;

    // 접속일 UNIT TYPE
    public static final String UNIT_S = "s";    // 초
    public static final String UNIT_M = "m";    // 분
    public static final String UNIT_H = "h";    // 시
    public static final String UNIT_D = "d";    // 일
    public static final String UNIT_W = "w";    // 주
    public static final String UNIT_X = "x";    // 1개월전

    // DateFormat 패턴
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "yyyy-MM-dd HH:mm:ss";

    // 문자
    public static final String STRING_COLON = ":";    // 콜론
    public static final String STRING_HYPHEN = "-";    // 하이픈
    public static final String STRING_SHARP = "#";    // 샵
    public static final String STRING_SPACE = " ";    // 공백
    public static final String STRING_DOT = ".";    // 점
    public static final String STRING_SLASH = "/";    // 슬러쉬
    public static final String STRING_REVERS_SLASH = "\\";  // 역슬러쉬

    // -----------------------------------------------------------------------------------------------------------------------------------------------
    // 글로벌 저장 값 관련

    public static Parameter parameter = null;
    public static boolean isSaveSession = false;
    public static boolean isEnterBojang = false;
    public static int mWebviewPageCount = 0;
    public static boolean isGoBack = false;
    public static boolean isNoBindMDM = true;
    public static boolean isWebViewActivity = false;

    // -----------------------------------------------------------------------------------------------------------------------------------------------
    // intent putExtra 데이터

    public static final String EXTRA_URL = "EXTRA_URL";    // 웹뷰 URL

    // KakaoLink
    public static final String KAKAO_CONTENTS = "KAKAO_CONTENTS";
    public static final String KAKAO_PHOTO_URL = "KAKAO_PHOTO_URL";
    public static final String KAKAO_LINK_URL = "KAKAO_LINK_URL";
    public static final String KAKAO_TITLE = "KAKAO_TITLE";
    public static final String KAKAO_MEMBER_ID = "KAKAO_MEMBER_iD";

    // -----------------------------------------------------------------------------------------------------------------------------------------------
    // onActivityResult requestCode

    public static final int ACTIVITY_LAUNCHER = 1;
    public static final int ACTIVITY_LOGIN = 5;                             // 로그인
    public static final int ACTIVITY_LOGIN_BY_LOGOUT = 6;                   // 로그아웃->로그인
    public static final int ACTIVITY_PUSH_LOGIN = 8;                        // 푸시서버 로그인
    public static final int ACTIVITY_PORTAL = 9;                            // 임직원용 포탈 메인
    public static final int ACTIVITY_MAIN_WEBVIEW = 10;                     // 메인 웹뷰

    public static final int ACTIVITY_MAGIC_BC_READER = 100;                 // 명함인식기 라이브러리
    public static final int ACTIVITY_MAGIC_DR = 101;                        // A4 처리 라이브러리
    public static final int ACTIVITY_MAGIC_ID_READER = 102;                 // 신분증 인식 라이브러리
    public static final int ACTIVITY_CAMERA_GALLERY = 110;                  // 카메라 / 갤러리
    public static final int ACTIVITY_MDM_APK_INSTALL = 500;                 // MDM APK Install
    public static final int ACTIVITY_TRANS_KEY_CALL = 550;                  // 보안키보드 호출
    public static final int ACTIVITY_KAKAOTALK_SHARE = 551;                 // 카카오 링크 공유
    public static final int ACTIVITY_SEND_SMS = 555;                        // 문자 보내기
    public static final int ACTIVITY_CONTRACT = 557;                        // 연락처 가져오기
    public static final int ACTIVITY_SEND_EMAIL = 558;                      // 이메일 보내기
    public static final int ACTIVITY_PK_TEST = 600;                         // PK 시험 앱 호출
    public static final int ACTIVITY_EPROMY = 601;                          // 이프로미 앱 호출
    public static final int ACTIVITY_VIRTUAL_SYSTEM = 602;                  // 가상화 앱 호출
    public static final int ACTIVITY_ESIGN = 603;                           // 전자서명 앱 호출
    public static final int ACTIVITY_PORTAL_APP_LAUNCH = 700;               // 포탈 앱 실행
    public static final int ACTIVITY_DBLEARNING = 800;                      // 디비러닝앱 실행
    public static final int ACTIVITY_DEEPMEDI = 900;                        // 아보카도 실행

    public static final String PARAM1 = "param1";
    public static final String PARAM2 = "param2";
    public static final String PARAM3 = "param3";
    public static final String PARAM4 = "param4";
    public static final String PARAM5 = "param5";
    public static final String PARAM6 = "param6";
    public static final String PARAM7 = "param7";

    // -----------------------------------------------------------------------------------------------------------------------------------------------
    // 파라메터 값 관련
    // 앱 호출 Package Name 또는 ID
    public static final String APP_NAME_PK_TEST = "PK자격";                                     // PK 시험 앱 이름
    public static final String APP_PACKAGE_NAME_PK_TEST = "com.dbins.pk_mobile";                // PK 시험 앱 패키지네임
    public static final String APP_NAME_BOHUM = "보험연수원";                                   // 보험연수원 앱 이름
    public static final String APP_PACKAGE_NAME_BOHUM = "com.in.or.kr.kii";                     // 보험연수원 앱 패키지 네임
    public static final String APP_CALL_ID_PK_TEST = "pktest";                                  // PK 시험 앱 호출 용 웹 파라메터
    public static final String APP_NAME_VIRTUAL_SYSTEM = "가상화시스템";                        // 가상화시스템 앱 이름
    public static final String APP_CALL_ID_VIRTUAL_SYSTEM = "vsystem";                          // 가상화시스템 앱 호출 용 웹 파라메터
    public static final String APP_PACKAGE_NAME_VIRTUAL_SYSTEM = "com.Dongbu.MReceiver";        // 가상화시스템 앱 패키지네임
    public static final String APP_CALL_ID_EPROMY = "epromy";                                   // 이프로미광장 앱 호출 용 웹 파라메터
    public static final String APP_PACKAGE_NAME_MDM = "com.raonsecure.touchen_mguard_4_0";      // MDM 앱 패키지네임
    public static final String APP_NAME_ESIGN = "전자서명";                                     // 전자서명 앱 이름
    public static final String APP_CALL_ID_ESIGN = "DongBu-POPESS";                             // 구 전자서명 호출 용 웹 파라메터
    public static final String APP_PACKAGE_NAME_ESIGN = "kr.co.itpop.POPESS.iDongBu";           // 구 전자서명 앱 패키지네임
    public static final String APP_CALL_DB_LEARNING = "ubob://smartlearning";                   // DB러닝앱 스키마
    public static final String APP_NAME_DB_LEARNING = "DB러닝";                                 // DB러닝 앱이름
    public static final String APP_PACKAGE_DB_LEARNING ="com.ubob.android.grpo";                // DB러닝앱 패키지
    public static final String APP_CALL_DEEP_MEDI = "deepmedi://avocado";                       // DB러닝앱 스키마
    public static final String APP_NAME_DEEP_MEDI = "아보카도";                                  // DB러닝 앱이름
    public static final String APP_PACKAGE_DEEP_MEDI ="com.deepmedi.avocado";                   // DB러닝앱 패키지

    // Tag
    public static final int TAG_TRANS_KEY_LOGIN_PASSWORD = 10;              // 로그인 비밀번호 입력 시 보안키보드 입력 컴포넌트

    // JS Bridge Function Name
    // DO
    public static final String JS_FUNC_DO_START_PROGRESS = "doStartProgress";                   // 프로그래스바 시작
    public static final String JS_FUNC_DO_STOP_PROGRESS = "doStopProgress";                     // 프로그래스바 종료
    public static final String JS_FUNC_DO_OCR_CALL = "doOcrCall";                               // 신분증 인식 촬영 액티비티
    public static final String JS_FUNC_DO_ID_CALL = "doIdCall";                                 // 명함 인식 촬영 액티비티
    public static final String JS_FUNC_DO_PAPER_CALL = "doPaperCall";                           // A4 촬영 액티비티
    public static final String JS_FUNC_DO_UPDATE_SESSION_TIME = "doUpdateSessionTime";          // 세션 타임 업데이트
    public static final String JS_FUNC_DO_START_EXTERNAL_APP = "doStartExternalApp";            // 외부 앱 실행
    public static final String JS_FUNC_DO_LOGOUT = "doLogout";                                  // 로그아웃
    public static final String JS_FUNC_GO_MAIN = "goMain";                                      // 메인으로
    public static final String JS_FUNC_APP_FINISH = "appFinish";                                // 앱 종료
    public static final String JS_FUNC_DO_CAMERA_GALLERY_CALL = "doCameraGalleryCall";          // 카메라 / 갤러리 호출
    public static final String JS_FUNC_DO_SMS_CALL = "doSMSCall";                               // 문자 보내기 화면 호출
    public static final String JS_FUNC_DO_KAKAO_SHARE_CALL = "doShareKakaoCall";                // 카카오톡 공유 호출
    public static final String JS_FUNC_GET_CONTRACT_CALL = "getContractCall";                   // 연락처 가져오기 호출
    public static final String JS_FUNC_DO_SEND_EMAIL_CALL = "doSendEmailCall";                  // 이메일 보내기 화면 호출
    // GET
    public static final String JS_FUNC_GET_INFO = "getInfo";                                    // 디바이스 및 앱 정보 얻어오기
    public static final String JS_FUNC_GET_VALUE = "getValue";

    // SET
    public static final String JS_FUNC_SET_ROTATE = "setRotate";                                // 화면 방향 전환
    public static final String JS_FUNC_SET_VALUE = "setValue";

    public static final String JS_FUNC_ENABLE_HW = "enableHW";                                  // 하드웨어 엑셀레이션 활성화
    public static final String JS_FUNC_DISABLE_HW = "disableHW";                                // 하드웨어 엑셀레이션 비활성화

    /*
     * return Network result
     */
    public static final String JSON_RESULT_CODE = "rsCd";
    public static final String JSON_RESULT_MESSAGE = "responseText";
    public static final String JSON_DATA = "jsonDatas";
    public static final String JSON_MDM_DATA = "data";
    public static final String JSON_ID = "id";
    public static final String JSON_LOGIN_ID = "userId";
    public static final String JSON_LOGIN_PW = "passWd";
    public static final String JSON_LOGIN_OSNAME = "osName";
    public static final String JSON_LOGIN_DEVICE_TYPE = "eqpDvcd";
    public static final String JSON_LOGIN_RES_INFO = "loginResInfo";
    public static final String JSON_LOGIN_TIME = "loginTime";
    public static final String JSON_LOGIN_RES_MSG = "ErrorMsg";
    public static final String JSON_LOGIN_RES_CODE = "ErrorCode";
    public static final String JSON_LOGIN_RES_SSO_TOKEN = "ssoToken";
    public static final String JSON_LOGIN_RES_IS_COMPANY_USER_CODE = "z_ormm_ptl_tpcd";
    public static final String JSON_LOGIN_RES_JSESSIONID = "jsessionid";
    public static final String JSON_LOGIN_RES_USER_NAME = "z_user_nm";
    public static final String JSON_USER_PASS = "user_pass";
    public static final String JSON_USER_NAME = "user_name";
    public static final String JSON_STEP = "step";
    public static final String JSON_SESSION_CODE = "session_code";
    public static final String JSON_SESSION_ID = "session_id";
    public static final String JSON_APP_VER = "app_ver";
    public static final String JSON_PHONE = "phone";
    public static final String JSON_JOIN_TYPE = "join_type";
    public static final String JSON_MESSAGE = "message";
    public static final String JSON_PTYPE = "ptype";
    public static final String JSON_IS_FIRST = "is_first";

    public static final String JS_DO_PAPER_CALL_PARAM = "doPaperCallParam";
    public static final String JS_DO_PAPER_CALL_PARAM_UPLOAD_TYPE = "uploadType";
    public static final String JS_DO_PAPER_CALL_PARAM_APDXFLID = "apdxFlId";
    public static final String JS_DO_PAPER_CALL_PARAM_SQNO = "sqno";
    public static final String JS_DO_PAPER_CALL_PARAM_BZDVCD = "bzDvcd";
    public static final String JS_DO_PAPER_CALL_PARAM_FLDVCD = "flDvcd";
    public static final String JS_DO_PAPER_CALL_PARAM_UPLOAD_FILE = "uploadfile";

    public static final String JSON_MDM_DEVICE_USER_INFOS = "deviceUserInfos";
    public static final String JSON_MDM_OS_VERSION = "osVersion";
    public static final String JSON_MDM_OS_TYPE = "osType";
    public static final String JSON_MDM_DEVICE_MODEL = "deviceModel";
    public static final String JSON_MDM_OS_NAME = "osName";
    public static final String JSON_MDM_DEVICE_ID = "deviceId";
    public static final String JSON_MDM_USER_ID = "userId";
    public static final String JSON_MDM_IS_LOGINED = "isLogined";
    public static final String JSON_MDM_APP_INFOS = "appInfos";
    public static final String JSON_MDM_APP_INFOS_ERROR = "";
    public static final String JSON_MDM_APP_VERSION = "appVer";
    public static final String JSON_MDM_APP_PACKAGE_NAME = "appPackage";
    public static final String JSON_MDM_APP_INSTALL_URL = "appInstallUrl";

    public static final String JSON_ARRAYLIST_STRING = "arraylist_string";

    public static final String JSON_IS_SAVE_ID = "is_save_id";
    public static final String JSON_TODAY_POPUP_EVENT = "today_popup_event";
    public static final String JSON_IS_POPUP_EVENT_READ = "is_popup_event_read";
    public static final String JSON_POPUP_EVENT_VIEW_ID = "pupop_event_view_id";
    public static final String JSON_POPUP_EVENT_READ_TIME = "pupop_event_read_time";

    public static final String JSON_IS_PUSH = "is_push";

    public static final String JSON_INDEX = "index";

    public static final String JSON_BACKGROUND_TIME = "background_time";

    // JSON_ARRAY
    public static final String JSON_SHORT_CUT = "shortcut";

    // push
    public static final String JSON_PUSH_REG_FAIL = "push_fail";                      // 푸시 등록 시 오류가 발생하면 IntroActivity 로 플래그 전달
    public static final String JSON_PUSH_LOGIN_RESULT_MESSAGE = "push_login_result_message";      // 푸시 등록 시 오류 메세지

    // CameraGallery
    public static final String JSON_CAMERA_GALLERY_IMAGE_PATH = "camera_gallery_image_path";
    public static final String JSON_CAMERA_GALLERY_TYPE = "camera_gallery_type";

    // APP CALL EXTERNAL PARAM
    public static final String JSON_APP_CALL_EXTERNAL_BUNDLE_DATA = "bundleData";
    public static final String JSON_APP_CALL_EXTERNAL_METHOD = "method";
    public static final String JSON_APP_CALL_EXTERNAL_PARAM = "param";
    public static final String JSON_APP_CALL_EXTERNAL_CALL_APP = "callApp";
    public static final String JSON_APP_CALL_EXTERNAL_PARAM_USER_ID = "user_Id";
    public static final String JSON_APP_CALL_EXTERNAL_PARAM_USER_NAME = "user_Nm";
    public static final String JSON_APP_CALL_EXTERNAL_PARAM_USER_PASSWORD = "user_Pwd";
    public static final String JSON_APP_CALL_EXTERNAL_PARAM_SESSION_ID = "session_Id";

    // APP CALL EXTERNAL PARAM by Push Notification
    public static final String JSON_APP_CALL_EXTERNAL_PACKAGE_NAME = "package_Nm";
    public static final String JSON_APP_CALL_EXTERNAL_PACKAGE_NAME2 = "packageNm";
    public static final String JSON_APP_CALL_EXTERNAL_PAGE = "page";

    // 보장분석 External Param
    public static final String JSON_APP_CALL_EXTERNAL_BOJANG = "bojang";
    public static final String JSON_APP_CALL_EXTERNAL_BOJANG_MPGID = "mpgId";
    public static final String JSON_APP_CALL_EXTERNAL_BOJANG_PGNM = "pgNm";
    public static final String JSON_APP_CALL_EXTERNAL_BOJANG_MENU_ID = "MENU_ID";
    public static final String JSON_APP_CALL_EXTERNAL_BOJANG_SSO_TOKEN = "ssoToken";
    public static final String JSON_APP_CALL_EXTERNAL_BOJANG_PARAM = "param";
    public static final String JSON_APP_CALL_EXTERNAL_BOJANG_VIEW = "_view";
    public static final String JSON_APP_CALL_EXTERNAL_BOJANG_WIDTH = "width";
    public static final String JSON_APP_CALL_EXTERNAL_BOJANG_HEIGHT = "height";
    public static final String JSON_APP_CALL_EXTERNAL_BOJANG_SYSTEM_ID = "SYSTEM_ID";
    public static final String JSON_APP_CALL_EXTERNAL_BOJANG_PAGE_TYPE = "pageType";


    // API result Code
    public static final String API_SUCCESS = "200";    // NETWORK API 성공 ( DSM 서버 간 )
    public static final String API_FAIL = "500";   // NETWORK API 실패 ( DSM 서버 간 )
    public static final String API_CODE_NONE = "99999";   // NETWORK API 실패 ( DSM 서버 간 )

    public static final int API_SUCCESS_WEB_BRIDGE = 0;    // 성공 ( 웹 연동 간 )
    public static final int API_FAIL_WEB_BRIDGE = -1;   // 실패 ( 웹 연동 간 )

    public static final int API_ERROR_ = 1;
    public static final String API_ERROR_SYSTEM_ERROR = "";    // 시스템 오류로 인하여 중단
    public static final String API_ERROR_INPUT_DATA_ERROR = "";    // 입력 데이터가 부족함

    // -----------------------------------------------------------------------------------------------------------------------------------------------
    // 팝업 관련
    // Popup Type
    public static final int POPUP_TYPE_DEFAULT = 1;   // 기본
    public static final int POPUP_TYPE_DEFAULT_FINISH = 2;   // 기본_Activity_finish
    public static final int POPUP_TYPE_INSTALL_FOR_GO_TO_MDM = 9;   // 앱 설치 않되어 있을 경우 (MDM)
    public static final int POPUP_TYPE_INSTALL_FOR_GO_TO_MARKET = 10;   // 앱 설치 않되어 있을 경우 (MARKET)
    public static final int POPUP_TYPE_INSTALL_FOR_GO_TO_WEBINSTALL = 13;   // 앱 설치 않되어 있을 경우 (자체설치페이지)
    public static final int POPUP_TYPE_LOGOUT = 11;   // 로그아웃 시 ( 세션 만료 )
    public static final int POPUP_TYPE_APP_FINISH = 12;   // 앱 종료 시
    public static final int POPUP_TYPE_MUST_SECURE_KEYBOARD_COMPLETE = 14;   // 로그인 시 보안키보드를 반드시 입력완료 하도록
    public static final int POPUP_TYPE_LOGOUT_MANUAL = 17;   // 로그아웃 시 ( 사용자 )
    public static final int POPUP_TYPE_LOGIN_FINISH = 19;   // 로그인 시 메세지
    public static final int POPUP_TYPE_A_FROM_WEB = 100;   // 웹에서 호출된 팝업 ( A )
    public static final int POPUP_TYPE_B_FROM_WEB = 101;   // 웹에서 호출된 팝업 ( B )
    public static final int POPUP_TYPE_MDM_NOT_LOGIN = 150;   // MDM 미 로그인
    public static final int POPUP_TYPE_MDM_NOT_INSTALLED = 151;   // MDM 미 설치
    public static final int POPUP_TYPE_MDM_CHECK_SUCCESS = 152;   // MDM 채크 성공

    public static final int POPUP_TYPE_MDM_AGENT_NOT_LAST_VERSION = 153;
    public static final int POPUP_TYPE_MDM_ERR_MDM_DEVICE_ROOTING = 154;
    public static final int POPUP_TYPE_MDM_ERR_MDM_FIND_HACKED_APP = 155;
    public static final int POPUP_TYPE_MDM_ERR_UPDATE_INSTALLED = 156;   // MDM 업데이트 중 오류발생
    public static final int POPUP_TYPE_MDM_ERR_MDM_CHECKING = 157; // MDM 채크 중 알수없는 오류 발생

    public static final int POPUP_TYPE_MDM_ALLEADY_LOGIN = 158;   // MDM 이미 로그인 상태 오류
    public static final int POPUP_TYPE_MDM_VERSION_CHECK = 170;   // MDM 내 앱 업데이트 실행

    public static final int POPUP_TYPE_PORTAL_APP_RUN = 200;   // 포탈 메인에서 앱 실행
    public static final int POPUP_TYPE_PORTAL_APP_NOT_INSTALL = 201;   // 포탈 메인에서 앱이 설치되어 있지 않음.
    public static final int POPUP_TYPE_PORTAL_APP_NEED_UPDATE = 202;   // 포탈 메인에서 앱이 업데이트가 필요함.

    // -----------------------------------------------------------------------------------------------------------------------------------------------
    // MDM_ACTION 관련

    public static final int MDM_ACTION_INIT = 100;
    public static final int MDM_ACTION_CHECK_AGENT = 101;
    public static final int MDM_ACTION_DEVICE_USER_INFO = 102;
    public static final int MDM_ACTION_LOGIN = 103;
    public static final int MDM_ACTION_ONE_PASS_AUTH = 104;
    public static final int MDM_ACTION_LOGOUT = 105;
    public static final int MDM_ACTION_DIRECTLY_LOGIN = 106;
    public static final int MDM_ACTION_HW_CONTROL = 107;
    public static final int MDM_ACTION_HW_CONTROL_FLAG_POLICY_BLOCK_BROWSER = 200;
    public static final int MDM_ACTION_HW_CONTROL_FLAG_POLICY_BLOCK_CAPTURE = 201;
    public static final int MDM_ACTION_UPDATE_AUTH_INFO = 202;
    public static final int MDM_ACTION_CHECK_VACCINE = 203;
    public static final int MDM_ACTION_LAUNCH_APP = 204;
    public static final int MDM_ACTION_LOGIN_OFFICE_CHECK = 205;
    public static final int MDM_ACTION_FORGEAPP_CHECK = 206;
    public static final int MDM_ACTION_UPMOOAPP_UPDATE = 207;

    // -----------------------------------------------------------------------------------------------------------------------------------------------
    // 로그아웃 관련
    // Logout Type
    public static final int LOGOUT_TYPE_NONE = 1;
    public static final int LOGOUT_TYPE_NORMAL = 2;
    public static final int LOGOUT_TYPE_APP_FINISH = 3;


    public CommonData() {
    }

    public CommonData(Context context) {
        this.mContext = context;
    }

    /**
     * CommonData 인스턴스 리턴
     *
     * @return CommonData
     */
    public static CommonData getInstance() {
        if (_instance == null) {
            synchronized (CommonData.class) {
                if (_instance == null) {
                    _instance = new CommonData();
                }
            }
        }
        return _instance;
    }

    /**
     * CommonData 인스턴스 리턴
     *
     * @param context context
     * @return CommonData
     */
    public static CommonData getInstance(Context context) {
        if (_instance == null) {
            synchronized (CommonData.class) {
                if (_instance == null) {
                    _instance = new CommonData(context);
                }
            }
        }
        return _instance;
    }

    /**
     * preferences 삭제
     */
    public void deletePreferences() {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.clear();
        editor.commit();
    }

    /**
     * preference 권한 얻기
     *
     * @return
     */
    public SharedPreferences getSharedPreference()                                // 데이터 가져오기
    {
        return mContext.getSharedPreferences("DSM", AppCompatActivity.MODE_PRIVATE);
    }

    /**
     * String 타입의 preferencedata 를 저장한다.
     *
     * @param key   키값
     * @param value 저장할 데이터
     */
    private void setSharedPreferenceData(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * int 타입의 preferencedata 를 저장한다.
     *
     * @param key   키값
     * @param value 저장할 데이터
     */
    private void setSharedPreferenceData(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * boolean 타입의 preferencedata 를 저장한다.
     *
     * @param key   키값
     * @param value 저장할 데이터
     */
    private void setSharedPreferenceData(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * float 타입의 preferencedata 를 저장한다
     *
     * @param key   키값
     * @param value 저장할 데이터
     */
    private void setSharedPreferenceData(String key, float value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    private void setSharedPreferenceArrayString(String key, ArrayList<String> stringList) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(key, TextUtils.join("‚‗‚", stringList)).apply();
        editor.commit();
    }

    // MDM userId 저장하기
    public void setMDMUserId(String userId) {
        Log.d("[저장] userId = " + userId);
        setSharedPreferenceData(JSON_MDM_USER_ID, encode(userId));
    }

    // MDM 로그인 여부
    public boolean isLogined() {
        return getSharedPreference().getBoolean(JSON_MDM_IS_LOGINED, false);
    }

    // MDM 로그인 여부
    public void setLogined(boolean logined) {
        Log.d("[저장] logined = " + logined);
        setSharedPreferenceData(JSON_MDM_IS_LOGINED, logined);
    }

    // MDM userId 가져오기
    public String getMDMUserId() {
        return decode(getSharedPreference().getString(JSON_MDM_USER_ID, ""));
    }


    // MDM osType 저장하기
    public void setMDMOsType(String osType) {
        Log.d("[저장] osType = " + osType);
        setSharedPreferenceData(JSON_MDM_OS_TYPE, osType);
    }

    // MDM osType 가져오기
    public String getMDMOsType() {
        return getSharedPreference().getString(JSON_MDM_OS_TYPE, "");
    }

    // MDM deviceModel 저장하기
    public void setMDMDeviceModel(String deviceModel) {
        Log.d("[저장] deviceModel = " + deviceModel);
        setSharedPreferenceData(JSON_MDM_DEVICE_MODEL, deviceModel);
    }

    // MDM deviceModel 가져오기
    public String getMDMDeviceModel() {
        return getSharedPreference().getString(JSON_MDM_DEVICE_MODEL, "");
    }

    // MDM osName 저장하기
    public void setMDMOsName(String osName) {
        Log.d("[저장] osName = " + osName);
        setSharedPreferenceData(JSON_MDM_OS_NAME, osName);
    }

    // MDM osName 가져오기
    public String getMDMOsName() {
        return getSharedPreference().getString(JSON_MDM_OS_NAME, "");
    }

    // MDM deviceId 저장하기
    public void setMDMDeviceId(String deviceId) {
        Log.d("[저장] deviceId = " + deviceId);
        setSharedPreferenceData(JSON_MDM_DEVICE_ID, deviceId);
    }

    // MDM deviceId 가져오기
    public String getMDMDeviceId() {
        return getSharedPreference().getString(JSON_MDM_DEVICE_ID, "");
    }

    // MDM osVersion 저장하기
    public void setMDMOsVersion(String osVersion) {
        Log.d("[저장] osVersion = " + osVersion);
        setSharedPreferenceData(JSON_MDM_OS_VERSION, osVersion);
    }

    // MDM osVersion 가져오기
    public String getMDMOsVersion() {
        return getSharedPreference().getString(JSON_MDM_OS_VERSION, "");
    }

    // 로그인 아이디 저장
    public void setId(String id) {
        Log.d("[저장] id = " + id);
        setSharedPreferenceData(JSON_ID, encode(id));
    }

    public String getId() {
        return decode(getSharedPreference().getString(JSON_ID, ""));
    }

    // 이름 저장
    public void setName(String name) {
        Log.d("[저장] name = " + name);
        setSharedPreferenceData(JSON_USER_NAME, name);
    }

    public String getName() {
        return getSharedPreference().getString(JSON_USER_NAME, "");
    }

    // 세션 ID 저장
    public void setSessionId(String sessionid) {
        Log.d("[저장] sessionid = " + sessionid);
        setSharedPreferenceData(JSON_SESSION_ID, sessionid);
    }

    public String getSessionId() {
        return getSharedPreference().getString(JSON_SESSION_ID, "");
    }

    // 비밀번호 저장하기
    public void setPassword(String password) {
        if(Config.ENABLE_LOGIN_TEMP_DATA) {
            password = Config.TEST_MDM_LOGIN_PW;
        }
        Log.d("[저장] password = " + password);
        String seed = GetDevicesUUID();
        Log.i("setPassword() getDevicesUUid = " + seed);
        try {
            //setSharedPreferenceData(JSON_USER_PASS, SimpleCrypto.encrypt(seed, password));
            //CacheUtils.getInstance().put(JSON_USER_PASS, SimpleCrypto.encrypt(seed, password));
            CacheUtils.getInstance().put(JSON_USER_PASS, encode(password));
        } catch (Exception e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
    }

    // 비밀번호 가져오기
    public String getPassword() {
        //String password = getSharedPreference().getString(JSON_USER_PASS, "");
        String password = CacheUtils.getInstance().getString(JSON_USER_PASS);
        Log.d("password = " + password);
        if (StringUtils.isEmpty(password))
            return "";

        String seed = GetDevicesUUID();
        Log.i("getPassword()  getDevicesUUid = " + seed);
        try {
            //SimpleCrypto.decrypt(seed, password);
            return decode(password);
        } catch (Exception e) {

            if(Config.DISPLAY_LOG) e.printStackTrace();
            return "";
        }
    }

    public String getSessionToken() {
        try {
            JSONObject resInfo =  CacheUtils.getInstance().getJSONObject(CommonData.JSON_LOGIN_RES_INFO);
            if(resInfo != null) {
                return resInfo.getString(CommonData.JSON_LOGIN_RES_SSO_TOKEN);
            }

        } catch (JSONException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
        return null;
    }

    public boolean isCompanyUser() {
        try {
            JSONObject resInfo =  CacheUtils.getInstance().getJSONObject(CommonData.JSON_LOGIN_RES_INFO);
            if(resInfo != null) {
                String companyUserCode = resInfo.getString(CommonData.JSON_LOGIN_RES_IS_COMPANY_USER_CODE);
                if(companyUserCode.equals("01") ||
                        companyUserCode.equals("14") ||
                        companyUserCode.equals("15")) {
                    return true;
                }
            }
        } catch (JSONException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
        return false;
    }

    public String getLoginUserName() {
        try {
            JSONObject resInfo =  CacheUtils.getInstance().getJSONObject(CommonData.JSON_LOGIN_RES_INFO);
            if(resInfo != null) {
                return resInfo.getString(CommonData.JSON_LOGIN_RES_USER_NAME);
            }

        } catch (JSONException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
        return null;
    }

    // 로그인 유지 세션코드 저장하기
    public void setSessionCode(String sessionCode) {
        Log.d("[저장] sessionCode = " + sessionCode);
        setSharedPreferenceData(JSON_SESSION_CODE, sessionCode);
    }

    // 로그인 유지 세션코드 가져오기
    public String getSessionCode() {
        return getSharedPreference().getString(JSON_SESSION_CODE, "");
    }

    // 버전정보 저장
    public void setAppVer(String app_ver) {
        setSharedPreferenceData(JSON_APP_VER, app_ver);
    }

    public String getAppVer() {
        return getSharedPreference().getString(JSON_APP_VER, "");
    }

    public void setPhoneNumber(String phone) {
        setSharedPreferenceData(JSON_PHONE, phone);
    }

    public String getPhoneNumber() {
        return getSharedPreference().getString(JSON_PHONE, "");
    }

    // 최초 실행 체크
    public void setIsFirst(boolean bool) {
        setSharedPreferenceData(JSON_IS_FIRST, bool);
    }

    public boolean isFirst() {
        return getSharedPreference().getBoolean(JSON_IS_FIRST, false);
    }

    // 로그인 타입
    public void setJoinType(String joinType) {
        setSharedPreferenceData(JSON_JOIN_TYPE, joinType);
    }

    public String getJoinType() {
        return getSharedPreference().getString(JSON_JOIN_TYPE, "");
    }

    // 현재 화면 스텝
    public void setStep(int step) {
        setSharedPreferenceData(JSON_STEP, step);
    }

    public int getStep() {
        return getSharedPreference().getInt(JSON_STEP, -1);
    }

    // 푸시에 따른 데이터 저장
    public void setPtype(String ptype) {
        setSharedPreferenceData(JSON_PTYPE, ptype);
    }

    public String getPtype() {
        return getSharedPreference().getString(JSON_PTYPE, "");
    }

    // ID ( 푸시에서 날아오는 값 )
    public void setPushIndex(int index) {
        setSharedPreferenceData(JSON_INDEX, index);
    }

    public int getPushIndex() {
        return getSharedPreference().getInt(JSON_INDEX, -1);
    }

    // 팝업 이벤트 ID
    public void setPopupEventId(int id) {
        setSharedPreferenceData(JSON_POPUP_EVENT_VIEW_ID, id);
    }

    public int getPopupEventId() {
        return getSharedPreference().getInt(JSON_POPUP_EVENT_VIEW_ID, -1);
    }

    // 아이디 저장 여부
    public void setSaveId(boolean isSaved) {
        setSharedPreferenceData(JSON_IS_SAVE_ID, isSaved);
    }

    // 아이디 저장 여부
    public boolean isSaveId() {
        return getSharedPreference().getBoolean(JSON_IS_SAVE_ID, false);
    }

    // 팝업 이벤트 봤는지 유무 ( 로그인 할때마다 1번만 보인다 )
    public void setPopupEventRead(boolean read) {
        setSharedPreferenceData(JSON_IS_POPUP_EVENT_READ, read);
    }

    // 바탕화면 아이콘 설치 유무
    public void setShortCut(boolean bool) {
        setSharedPreferenceData(JSON_SHORT_CUT, bool);
    }

    // 백그라운드 시점 저장
    public void setBackgroundTime(String time) {
        setSharedPreferenceData(JSON_BACKGROUND_TIME, time);
    }

    public String getBackgroundTIme() {
        return getSharedPreference().getString(JSON_BACKGROUND_TIME, "");
    }

    public void setValue(String key, String value) {
        setSharedPreferenceData(key, value);
    }

    public String getValue(String key) {
        return getSharedPreference().getString(key, "");
    }


    /**
     * 유니크한 값 생성하기
     *
     * @return
     */
    public String GetDevicesUUID() {

        String deviceId = "";
        try {
            final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            deviceId = deviceUuid.toString();
        } catch (Exception e) {
            Log.e(e.toString());
        }
        return deviceId;
    }

    public String encode(String data) {
        String key = Config.EN_KEY;
        try {

            String crypto = AES256CipherV1.AES_Base64_Encode(data, key);
            Log.i("암호화테스트(인코딩) ==> " + crypto);

            decode(crypto);

            return crypto;
        } catch (UnsupportedEncodingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (InvalidKeyException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (BadPaddingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
        return "";
    }

    public String decode(String data) {
        String key = Config.EN_KEY;
        try {
            String descrypto = AES256CipherV1.AES_Base64_Decode(data, key);
            Log.i("암호화테스트(디코딩) ==> " + descrypto);

            return descrypto;
        } catch (UnsupportedEncodingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (InvalidKeyException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (BadPaddingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
        return "";
    }

}
