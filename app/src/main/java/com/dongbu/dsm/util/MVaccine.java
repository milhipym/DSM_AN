package com.dongbu.dsm.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.TouchEn.mVaccine.b2b2c.activity.BackgroundScanActivity;
import com.TouchEn.mVaccine.b2b2c.activity.ScanActivity;
import com.TouchEn.mVaccine.b2b2c.util.CommonUtil;
import com.TouchEn.mVaccine.b2b2c.util.Global;
import com.dongbu.dsm.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by landonjung on 2016. 7. 22..
 */
public class MVaccine {

    Context mContext;
    AppCompatActivity mActivity;

    /*-------------------------- mVaccine RequestCode 정의 -----------------------------------
    mVaccine 제품관련 RequsetCode를 사전에 정의해서 사용합니다.
    API연동가이드 '3.5 RequstCode정의' 참조(page 9)하여 목적에 맞게 추가 선언하여 사용바랍니다.
    ---------------------------------------------------------------------------------------*/
    // mVaccine 제품 RequestCode
    public static final int REQUEST_CODE = 777;
    // UI 간소화 모드 검사진행 Notification
    public final static int MESSAGE_ID = 12345;
    // UI 간소화 모드 검사결과 Notification
    public final static int MESSAGE_ID1 = 123456;

    public MVaccine(AppCompatActivity activity, Context context) {
        mContext = context;
        mActivity = activity;

//        try {
//            SmartMedic.init(mActivity.getApplicationContext());
//        } catch (Exception e) {
//            if(Config.DISPLAY_LOG) e.printStackTrace();
//        }

        /*------------------------ mVaccine 사이트ID, 라이선스key 설정 ------------------------------
      	사이트 인증을 위해 지급받은 사이트 ID, KEY 값을 정확하게 입력해야 합니다.(라온시큐어 담당SE제공)
        ID, KEY 값이 맞지 않을 경우 mVaccine 구동이 정상적으로 되지 않습니다.
    -------------------------------------------------------------------------------------*/
        if(Config.ENABLE_VACCINE_DEBUG) {
            com.secureland.smartmedic.core.Constants.debug = true; // 디버깅 필요 시 true 설정
            Global.debug = true; // 디버깅 필요 시 true 설정
        }

        com.secureland.smartmedic.core.Constants.site_id = Config.MVACCINE_SITE_ID;
        com.secureland.smartmedic.core.Constants.license_key = Config.MVACCINE_LICENSE_KEY;
    }

    /*------------------------ mVaccine 검사하기 옵션 설정 --------------------------------------------
 	검사하기 옵션을 설정합니다. (API연동가이드 '3.8 검사하기 옵션 설정' page 11 참조)
 	각 옵션별 설정값은 아래와 같습니다.

	useBlackAppCheck
	> true : 루팅관련 우회 앱 검사, false : 검사안함, default : true
	scan_rooting
	> true : 단말루팅 여부 검사, false : 검사안함, default : true
	scan_package
	> true : 악성코드 앱 검사, false : 검사안함, default : false
	useDualEngine
	> true : BitDefender 엔진 검사,  false : 검사안함, default : false
	backgroundScan  // mini 전용
	> true : 악성코드 백그라운드 검사, false : 검사완료 시 까지 UI유지 , default : false
	rootingexitapp
	> true: 루팅 단말일 경우 앱 종료, false : 해당옵션 사용 안함
	dualEngineBackground // full 전용
	> true : BitDefender 엔진 백그라운드 검사 , false : 검사완료 시 까지 UI 유지, default : true
	backgroundJobForLongTime // full 전용
	> true : 악성코드 검사 간 5초 경과 90% 미만 검사 시 백그라운드 구동 여부 띄움 , false : 백그라운드 구동 여부 안띄움, default : false
	show_update
	> true : 패턴/엔진 업데이트 시 토스트  보임, false : 안보임, default : true
	show_license
	> true : 라이선스 유효성 토스트 보임, false : 안보임, default : true
	show_notify // mini 전용
	> true : 검사 간 노티피케이션 표시, false : 미표시, default : true
	show_toast
	> true : 토스트  출력, false : 미출력, default : true
	show_warning
	> true : 경고 토스트  출력 , false: 미출력, default : true
	show_scan_ui // mini 전용
	> true : 검사 중 이미지 출력, false : 미출력, default : true
	notifyAutoClear  // mini 전용
	> true : 검사 후 노티피케이션 자동 종료, false : 미종료, default : true
	notifyClearable  // mini 전용
	> true : 검사 후 노티피케이션 사용자 슬라이드 종료, false : 미종료, default : true
	showBlackAppName
	> true : 루팅앱 탐지 시 앱명 출력, false : 미출력, default : true
    ------------------------------------------------------------------------------------------*/

    /*------------------ 권장 옵션 (mini 모드) --------------------
	검사진행 중 UI가 없는 간소화 모드입니다.
	옵션에 따라 백신의 액티비티에서 루팅검사, 악성코드 검사를 실행합니다.
	--------------------------------------------------------*/

    public void mini() {
        Intent i = new Intent(mContext, BackgroundScanActivity.class); // BackgroundScanActivity와 통신할 Intent생성

        //BackgroundScanActivity로 넘길 옵션값 설정
        i.putExtra("useBlackAppCheck", true);  // 루팅 검사를 실시하면 루팅 우회 앱 설치 여부까지 검사
        i.putExtra("scan_rooting", true);     // 루팅 검사
        i.putExtra("scan_package", true);
        i.putExtra("useDualEngine", true);
        i.putExtra("backgroundScan", false);  // mini 전용
        i.putExtra("rootingexitapp", true);
        i.putExtra("show_update", true);
        i.putExtra("show_license", true);
        i.putExtra("show_notify", true);    // mini 전용
        i.putExtra("show_toast", true);
        i.putExtra("show_about", true);     // mini ????
        i.putExtra("show_warning", true);
        i.putExtra("show_scan_ui", true);    // mini 전용 (false 로 해야 백신실행시 조그만한 팝업이 보이지 않음.)
        i.putExtra("notifyAutoClear", false);     // mini 전용 노티피케이션을 띄워놓은채로 유지
        i.putExtra("notifyClearable", false);     // mini 전용
        //i.putExtra("showBlackAppName", true);

        mActivity.startActivityForResult(i, REQUEST_CODE); //Intent를 보내고 결과값을 얻어옴

    }

	/*----------------- 권장 옵션 (full 모드)--------------------
	검사진행 UI를 제공하는 모드입니다.
	옵션에 따라 백신의 액티비티에서 루팅검사, 악성코드 검사를 실행합니다.
	-------------------------------------------------------*/

    public void full() {
        Intent i = new Intent(mContext, ScanActivity.class);
        i.putExtra("useBlackAppCheck", true);
        i.putExtra("scan_rooting", true);
        i.putExtra("scan_package", true);
        i.putExtra("useDualEngine", true);
        i.putExtra("dualEngineBackground", true);     // full 전용
        i.putExtra("backgroundJobForLongTime", true); // full 전용
        i.putExtra("useStopDialog", false); // full 전용
        i.putExtra("rootingexitapp", true);
        i.putExtra("show_update", false);
        i.putExtra("show_license", false);
        i.putExtra("show_toast", false);
        i.putExtra("show_warning", false);

        mActivity.startActivityForResult(i, REQUEST_CODE);
    }

	/*--------------루팅 검사 (별도 API)-------------------
	루팅 여부만 따로 검사해야할 때 사용할 수 있는 루팅체크 별로 API입니다.
	blackAppCheck 값 true/false 로 루팅 우회 앱 설치 여부 검사를 설정 할 수 있습니다.
	아래 샘플 코드를 활용하여 케이스별 분기 처리합니다.
  	--------------------------------------------------------*/

    public boolean rootingCheck() {

        String message[] = new String[2];
        String strIsRooting = CommonUtil.checkRooting(mContext, true, message);
        String blackAppName = message[0];
        Log.d("strIsRooting " + strIsRooting);

        if (strIsRooting.equals("1")) {
            Log.d("Rooting OK !!");
            Toast.makeText(mContext, "루팅 단말 입니다.", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        else if (strIsRooting.equals("6")) {
            Log.d("verify failed");
            Toast.makeText(mContext, "무결성 검증에 실패 하였습니다..", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        else if (strIsRooting.equals("4")) {
            Log.d("BlackApp Installed");
            Toast.makeText(mContext, message[0]+"루팅 우회 앱이 설치되어 있습니다.", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        else {
            Log.d("No Rooting !!");

//            Toast.makeText(mContext, "루팅 단말이 아닙니다.", Toast.LENGTH_SHORT)
//                    .show();
            return true;
        }
    }

    byte[] fileToByte(String fileName)
    {
        byte[] data = null;
        File objFile = new File(fileName);
        FileInputStream is = null;
        int len = 0;
        Log.d("objFile.exists()  "+objFile.exists());
        if ( objFile.exists() )
        {
            try
            {
                is = new FileInputStream(objFile);

            }
            catch (FileNotFoundException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            if ( is == null )
                return null;

            data = new byte[(int)objFile.length()];
            try
            {
                len = is.read(data, 0, data.length);
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                if(Config.DISPLAY_LOG) e.printStackTrace();
            }


        }

        return data;
    }
}
