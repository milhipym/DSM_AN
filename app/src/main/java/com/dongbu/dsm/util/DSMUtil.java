package com.dongbu.dsm.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.Preference;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.R;
import com.dongbu.dsm.app.DSMApplication;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.intro.LoginActivity;
import com.google.android.gms.common.internal.service.Common;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LandonJung on 2017-8-10.
 * 유틸 모음 클래스
 * @since 0, 1
 */
public abstract class DSMUtil {

    public static int mDisWitdh;
    private static Date mFacebookExpiresDate;
    private static Context mContext;
    private static SharedPreferences sp;

    // 마켓 스토어에 따라 변경해줘야 함.
    public final static int MARKET_ID = 1;

    private static final char[] KEY_LIST = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static Random rnd = new Random();

    public static String getRandomStr() {
        char[] rchar = new char[]{KEY_LIST[rnd.nextInt(26)], KEY_LIST[rnd.nextInt(26)], KEY_LIST[rnd.nextInt(26)], KEY_LIST[rnd.nextInt(26)], KEY_LIST[rnd.nextInt(26)], KEY_LIST[rnd.nextInt(26)], KEY_LIST[rnd.nextInt(26)], KEY_LIST[rnd.nextInt(26)]};
        return String.copyValueOf(rchar);
    }

    /**
     * byte -> hex string
     * @param data
     * @return
     */
    public static String bytesToHex(byte[] data) {
        if (data==null) {
            return null;
        }

        int len = data.length;
        String str = "";
        String hexNumber ;
        for (int i=0; i<len ; i ++){

            hexNumber = "0"+ Integer.toHexString(0xFF&data[i]);
            str += hexNumber.substring(hexNumber.length()-2);
        }
        return str;
    }

    /**
     * hex string -> byte
     * @param str
     * @return
     */
    public static byte[] hexToBytes(String str) {

        if (str==null) {
            return null;
        }
        if (str.length() < 2) {
            return null;
        }
        int len = str.length() / 2;
        byte[] buffer = new byte[len];
        for (int i=0; i<len ; i ++){
            buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
        }

        return buffer;
    }

    /**
     * 이메일이 올바른지 확인
     * @param email
     * @return boolean
     */
    public static boolean checkEmail(String email){
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }

    //    private static final String Passwrod_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{6,12}$";
    private static final String Passwrod_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{7,16}$";
    private static final String NAME_PATTERN = "^[가-힣ㄱ-ㅎㅏ-ㅣ+]*$";
    public static final String PHONE_INVALID = "(01[016789])(\\d{3,4})(\\d{4})";

    /**
     * 전화번호 포맷 설정
     * @param phoneNo
     * @return
     */
    public static String[] getSplitPhoneNum(String phoneNo){

        if (phoneNo.length() == 0){
            return null;
        }

        String strTel = phoneNo;
        String[] strDDD = {"02" , "031", "032", "033", "041", "042", "043", "050",
                "051", "052", "053", "054", "055", "061", "062",
                "063", "064", "010", "011", "012", "013", "015",
                "016", "017", "018", "019", "070"};

        String[] strDDDD = {"0502" , "0503", "0504", "0505", "0506", "0303"};

        if (strTel.length() < 9) {
            return null;
        } else if (strTel.substring(0,2).equals(strDDD[0])) {
            strTel = strTel.substring(0, 2) + '-' + strTel.substring(2, strTel.length() - 4)
                    + '-' + strTel.substring(strTel.length() - 4, strTel.length());
        } else {
            if(strTel.substring(0,3).equals("050")) {
                boolean bDDDD = false;
                for(int i=0; i < strDDDD.length; i++) {
                    if (strTel.substring(0,4).equals(strDDDD[i])) {
                        bDDDD = true;
                        strTel = strTel.substring(0,4) + '-' + strTel.substring(4, strTel.length()-4)
                                + '-' + strTel.substring(strTel.length() -4, strTel.length());
                        break;
                    }
                }
                if(!bDDDD) {
                    for(int ii=1; ii < strDDD.length; ii++) {
                        if (strTel.substring(0,3).equals(strDDD[ii])) {
                            strTel = strTel.substring(0,3) + '-' + strTel.substring(3, strTel.length()-4)
                                    + '-' + strTel.substring(strTel.length() -4, strTel.length());
                        }
                    }
                }
            } else {
                for(int i=1; i < strDDD.length; i++) {
                    if (strTel.substring(0,3).equals(strDDD[i])) {
                        strTel = strTel.substring(0,3) + '-' + strTel.substring(3, strTel.length()-4)
                                + '-' + strTel.substring(strTel.length() -4, strTel.length());
                    }
                }
                for(int i=0; i < strDDDD.length; i++) {
                    if (strTel.substring(0,4).equals(strDDDD[i])) {
                        strTel = strTel.substring(0,4) + '-' + strTel.substring(4, strTel.length()-4)
                                + '-' + strTel.substring(strTel.length() -4, strTel.length());
                    }
                }
            }
        }

        String [] arr = strTel.split("-");

        return arr;
    }

    public static String[] getSplitEmail(String email)
    {
        String[] arr = new String[2];
        // 먼저 @ 의 인덱스를 찾는다 - 인덱스 값: 5
        int idx = email.indexOf("@");

        // @ 앞부분을 추출
        // substring은 첫번째 지정한 인덱스는 포함하지 않는다.
        // 아래의 경우는 첫번째 문자열인 a 부터 추출된다.
        arr[0] = email.substring(0, idx);

        // 뒷부분을 추출
        // 아래 substring은 @ 바로 뒷부분인 n부터 추출된다.
        arr[1] = email.substring(idx+1);

        return arr;
    }

    /**
     * 최신 version 체크 ( 소수점 있는 버전명 전용 )
     * @param localVerName 설치된 버전
     * @param lastVerName 서버 최종 버전
     * @return 현재 설치된 버전이 최종 버전인지 체크한다. ( false - 최종버전, true - 업데이트 필요 )
     */
        public static boolean isAppUpdate(String localVerName, String lastVerName)
        {
            // 업데이트 시 버전 확인을 위한 루틴
            String[] localVerArray	= localVerName.split("\\.");
            String[] lastVerArray 	= lastVerName.split("\\.");


            if ( localVerArray.length != 3 || lastVerArray.length != 3 )
            return false;

        int[] localVerIntArray 	= { Integer.valueOf(localVerArray[0]), Integer.valueOf(localVerArray[1]), Integer.valueOf(localVerArray[2])};
        int[] lastVerIntArray 	= { Integer.valueOf(lastVerArray[0]), Integer.valueOf(lastVerArray[1]), Integer.valueOf(lastVerArray[2])};

        int localVer 	= ( localVerIntArray[0] * 1000 ) + ( localVerIntArray[1] * 100 ) + ( localVerIntArray[2] );
        int lastVer 	= ( lastVerIntArray[0] * 1000 ) + ( lastVerIntArray[1] * 100 ) + ( lastVerIntArray[2] );

        Log.d("localVer : " + localVer);
        Log.d("lastVer  : " + lastVer);

        if ( localVer < lastVer )
            return true;

        return false;
    }

    public static boolean isTablet() {
        boolean bTablet = ScreenUtils.isTablet();
        String model = DeviceUtils.getModel();
        //갤럭시폴드 태블릿화면으로 나오는 현상_SM-F900F
        if(model.contains("SM-T255") || model.contains("SM-T251") || model.contains("SHV-E310")
                || model.contains("SM-F907") || model.contains("SM-F907N")) {
            bTablet = false;
        }
        sp = mContext.getSharedPreferences("usrDevice", Context.MODE_PRIVATE);

        if (!getUserDeviceType("userDeviceType").equals("") || getUserDeviceType("userDeviceType") != null)
        {
            if (getUserDeviceType("userDeviceType").equals("T")) { bTablet = true; }
            else if (getUserDeviceType("userDeviceType").equals("P")) { bTablet = false; }
        }
        return bTablet;
    }

    public static void setUserDeviceType(String key, String value)
    {
        //sp = mContext.getSharedPreferences("usrDevice", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getUserDeviceType(String key)
    {
        String userDeviceType = sp.getString(key,"");
        return userDeviceType;
    }


    /**
     * 패스워드가 올바른지 확인
     * @param passwd
     * @return boolean
     */
    public static boolean checkPasswd(String passwd) {
        Pattern pattern = Pattern.compile(Passwrod_PATTERN);
        Matcher matcher = pattern.matcher(passwd);
        return matcher.matches();
    }

    /**
     * 문자열 -> 원화 형식으로 변경
     * @param comma 문자열
     * @return
     */
    public static String setComma(String comma){
        int result = Integer.parseInt(comma);
        return new java.text.DecimalFormat("#,###").format(result);
    }

    /**
     * 숫자를 2자리 수로 표현
     * @param num 숫자
     * @return
     */
    public static String getTwoDateFormat(int num){
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(num);
    };


    /**
     * Dip -> Pixel 변환
     * @param dp  dip
     * @return int pixel
     */
    public static int pixelFromDP(Context context, float dp)
    {
        int pixel;
        pixel = (int) (dp * DSMApplication.mScale + 0.5f);
//        Log.i("mScale = " + CooingStarApplication.mScale);
//        Log.i("pixel = " + pixel);
        return pixel;
    }

    /**
     * Pixel -> Dip 변환
     * @param context       context
     * @param pixelValue    pixel
     * @return
     */
    public static int toDipFromPixel(Context context , float pixelValue)
    {
        int dip= (int) (pixelValue/ DSMApplication.mScale);
        Log.i("mScale = " + DSMApplication.mScale);
        Log.i("dip = " + dip);
        return dip;
    }

    /**
     * 밀도 구하기
     * @param context   context
     * @return
     */
    public static float getDensity(Context context){
        return DSMApplication.mScale;
    }

    /**
     * 키보드 내리기
     * @param context
     * @param et
     */
    public static void hideKeyboard(final Context context, final EditText et){
        et.postDelayed(new Runnable() {                // 키보드 내리기
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
            }
        }, 100);
    }

    /**
     * 키보드 띄우기
     * @param context
     * @param et
     */
    public static void showKeyboard(final Context context, final EditText et){
        et.postDelayed(new Runnable() {
            public void run() {
                InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(et, InputMethodManager.SHOW_FORCED);
            }
        }, 200);
    }

    /**
     * 브로드캐스트 발송
     * @param context context
     * @return action 브로드캐스트 액션
     */
    public static void sendBroadCast(Context context, String action){
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);

    }

    /**
     * 알파 애니메이션 적용 ( 점점 보이는 애니메이션 )
     * @param view 적용될 뷰
     * @param delay 애니메이션 적용 시간
     * @param bool 뷰 사라지도록 설정 유무 ( true - 사라짐, false - 안사라짐 )
     * @param gone_delay 뷰 사라지는 시간
     */
    public static void setAlphaAni(final View view, int delay, boolean bool, int gone_delay){
        view.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(delay);
        view.setAnimation(animation);

        if(bool){	// 일정 시간이 지나면 사라지도록 설정했다면
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.GONE);
                }
            }, gone_delay);
        }
    }


    /**
     * 시간 오전 오후 체크
     * @param context   컨택스트
     * @param time  시간
     * @return string ( am , pm )
     */
    public static String getAmPm(Context context, String time){

        String amPm;

        StringTokenizer st = new StringTokenizer(time, ":");
        if(Integer.valueOf(st.nextToken()) >= 12){
            amPm = context.getString(R.string.pm);
        }else{
            amPm = context.getString(R.string.am);
        }

        return amPm;

    }

    /**
     * 두 날짜의 차이
     * @param nYear1
     * @param nMonth1
     * @param nDate1
     * @param nYear2
     * @param nMonth2
     * @param nDate2
     * @return
     */
    public static int GetDifferenceOfDate ( int nYear1, int nMonth1, int nDate1, int nYear2, int nMonth2, int nDate2 ){
        Calendar cal = Calendar.getInstance ( );
        int nTotalDate1 = 0, nTotalDate2 = 0, nDiffOfYear = 0, nDiffOfDay = 0;

        if ( nYear1 > nYear2 ){
            for ( int i = nYear2; i < nYear1; i++ ) {
                cal.set ( i, 12, 0 );
                nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
            }
            nTotalDate1 += nDiffOfYear;
        }
        else if ( nYear1 < nYear2 ){
            for ( int i = nYear1; i < nYear2; i++ ){
                cal.set ( i, 12, 0 );
                nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
            }
            nTotalDate2 += nDiffOfYear;
        }

        cal.set ( nYear1, nMonth1-1, nDate1 );
        nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
        nTotalDate1 += nDiffOfDay;

        cal.set ( nYear2, nMonth2-1, nDate2 );
        nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
        nTotalDate2 += nDiffOfDay;

        return nTotalDate1-nTotalDate2;
    }

    public static boolean isExpireDay(int year , int month , int day) {

        Date mDate = getDateFormat(getDateFormat(), CommonData.PATTERN_DATE);
        int todayYear = mDate.getYear() + 1900;
        int todayMonth = mDate.getMonth() + 1;
        int todayDay = mDate.getDate();

        Log.d("[TODAY] year = " + todayYear + " month = " + todayMonth + " day = " + todayDay);

        if(year <= todayYear && month <= todayMonth && day <= todayDay ) {
            return true;
        }

        return false;

    }

    /**
     * yyyy-MM-dd 형식의 문자열을 Date 형태로 변환
     * @param date 날짜 문자열
     * @param date_format 패턴
     * @return
     */
    public static Date getDateFormat(String date, String date_format){
        SimpleDateFormat mFormat = new SimpleDateFormat(date_format);
        Date mDate = null;
        try {
            mDate = mFormat.parse(date);
        } catch (ParseException e) {

            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
        return mDate;
    }

    /**
     * 한국 현재시간 가져오기
     * @return 한국 현재시간을 문자열로 돌려준다.
     */
    public static String getKorDateFormat(){
        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(tz);
        Log.i("kor time = " +df.format(date));
        return df.format(date) ;

    }

    /**
     * 현재시간 가져오기
     * @return 한국 현재시간을 문자열로 돌려준다.
     */
    public static String getDateFormat(){
        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(tz);
        Log.i("kor time = " +df.format(date));
        return df.format(date) ;

    }

    public static void PopupAnimationNone(AppCompatActivity activity){
        activity.overridePendingTransition(R.anim.none, R.anim.none);
    }

    /**
     * 팝업 Activity 활성화시 애니메이션
     * @param activity activity
     */
    public static void PopupAnimationStart(AppCompatActivity activity){
        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.none);
    }

    /**
     * 팝업 Activity 종료시 애니메이션
     * @param activity activity
     */
    public static void PopupAnimationEnd(AppCompatActivity activity){
        activity.overridePendingTransition(0, R.anim.slide_in_down);
    }

    /**
     * 뒤로가기 Activity 활성화시 애니메이션
     * @param activity activity
     */
    public static void BackAnimationStart(AppCompatActivity activity){
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 뒤로가기 Activity 활성화시 애니메이션
     * @param activity activity
     */
    public static void BackAnimationFadeStart(AppCompatActivity activity){
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * 뒤로가기 Activity 종료시 애니메이션
     * @param activity activity
     */
    public static void BackAnimationEnd(AppCompatActivity activity){
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * 이미지뷰 비트맵 자원해제
     * @param iv 이미지뷰
     */
    public static void recycleBitmap(ImageView iv) {
        Drawable d = iv.getDrawable();
        if (d instanceof BitmapDrawable) {
            Log.i("d instanceof BitmapDrawable");
            Bitmap b = ((BitmapDrawable)d).getBitmap();
            b.recycle();

            d.setCallback(null);
        } // 현재로서는 BitmapDrawable 이외의 drawable 들에 대한 직접적인 메모리 해제는 불가능하다.

    }

    /**
     * 메모리 해제
     * @param root 상위 view
     */
    public static void recursiveRecycle(View root) {
        if (root == null)
            return;

         root.setBackgroundDrawable(null);		// Deprecated
        if (root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)root;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                recursiveRecycle(group.getChildAt(i));
            }

            if (!(root instanceof AdapterView)) {
                group.removeAllViews();
            }
        }

        if (root instanceof ImageView) {
            ((ImageView)root).setImageDrawable(null);
        }
        root = null;

        return;
    }

    /**
     * 해시태그 문자열 추출
     * @param inputStr  전체 문자열
     * @param separator 구분자 ( 문자열을 구분할 수 있는 문자 )
     */
    public static String getHashTag(String inputStr, String separator){

        StringBuffer sb = new StringBuffer();
        StringTokenizer token = new StringTokenizer(inputStr, separator);

        int strIndex = 0;
        while (token.hasMoreElements()) {
            String tokenStr =  (String) token.nextElement();

            int lastIndex = tokenStr.indexOf(" ");
            if(lastIndex != -1){    // 해시태그 문자열에 공백이 있는경우 공백 앞자리까지 해시태그
                tokenStr = tokenStr.substring(0, lastIndex);

                int blankIndex = tokenStr.indexOf(" ");
                if(blankIndex != -1){
                    sb.append(tokenStr.substring(0, blankIndex) + ",");
                }else{
                    sb.append(tokenStr + ",");
                }
            }else{  // 공백이 없는경우 문자열 전체를 해시태그
                sb.append(tokenStr +",");
            }

            strIndex++;
        }

        if(sb.length() > 0){
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();

    }

    /**
     * 설치된 앱 찾기
     * @param context        context
     * @param packageName		패키지명
     * @return boolean ( true - 설치, false - 미설치 )
     */
    public static boolean isInstalledApp(Context context, String packageName) {
        List<ApplicationInfo> appList = context.getPackageManager()
                .getInstalledApplications(PackageManager.GET_META_DATA);

        Log.i("isInstalledApp start");
        for (ApplicationInfo appInfo : appList) {
            if (appInfo.packageName.equals(packageName)) {
                return true;
            }
        }
        Log.i("isInstalledApp end");

        return false;
    }

    /**
     * BMI 수치 구하기
     * @param height    키
     * @param weight    몸무게
     */
    public static float getBmi(float height, float weight){

        float bmi = 0f;

        bmi = (float) (weight / ( (height * height) * 0.0001 ));

        Log.i("bmi = " + bmi);
        return bmi;
    }

    /**
     * 특정 텍스트 Color Size 변경
     * @param context 객체
     * @param view	텍스트뷰
     * @param fulltext 전체 문구
     * @param subtext 변경할 문구
     * @param subtext2 두번째 문자
     * @param color 변경할 색상
     * @param size 폰트 사이즈
     * @param styleType font style ( 0 - 기본, 1 - bold , 2 - italic )
     */
    public static void setTextViewCustom(Context context, TextView view, String fulltext, String subtext, String subtext2, int color, int size, int styleType, boolean isUnderLine) {
        view.setText(fulltext, TextView.BufferType.SPANNABLE);
        Spannable str = (Spannable) view.getText();
        int i = fulltext.indexOf(subtext);
        int j = fulltext.indexOf(subtext2);
        if(color > 0) {
            str.setSpan(new ForegroundColorSpan(color), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);   // 색상
        }
        if(size > 0) {  // 사이즈
            str.setSpan(new AbsoluteSizeSpan(DSMUtil.pixelFromDP(context, size)), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if(!subtext2.equals("")){   // 두번째 문자가 있다면 적용
                str.setSpan(new AbsoluteSizeSpan(DSMUtil.pixelFromDP(context, size)), j, j + subtext2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        str.setSpan(new StyleSpan(styleType), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);         // font Style
        if(isUnderLine){
            str.setSpan(new UnderlineSpan(), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);         // underLine
        }

    }

    /**
     * 텍스트하나에 특정문자 Color 변경
     * @param cuttext       자를 text
     * @param puttext       setText 할 곳
     * @param tagname       시작하는 곳
     * @param splitname     마지막 자를 곳
     * @param firstcolor    태그 글짜 색깔
     * @param secondcolor   일반 글짜 색깔
     */
    public static void setParticularColor (String cuttext , TextView puttext  , String tagname , String splitname , String firstcolor , String secondcolor) {

        String[] tag = cuttext.split(splitname);
        ArrayList<String> tagList = new ArrayList<String>();
        for (int i = 0; i < tag.length; i++) {
            boolean tagSharp = tag[i].startsWith(tagname);
            String first = "<font color=" + firstcolor +">" + tag[i] + "</font>";
            String second = "<font color=" + secondcolor + ">" + tag[i] + "</font>";
            if (tagSharp) {
                tagList.add(first);
            } else {
                tagList.add(second);
            }
            puttext.setText(Html.fromHtml(tagList.toString().replace(",", "").replace("]", "").replace("[", "")));
        }

    }

    /**
     * 마지막 로그인 시간 가져오기
     * @param mContext  컨텍스트
     * @param time  로그인 시간
     * @param unit  시간 단위
     * @return
     */
    public static String getLastLoginDate(Context mContext, String time, String unit){
        String online_text = "";
        if(unit.equals(CommonData.UNIT_S)){
            online_text = time +mContext.getString(R.string.second) +" " +mContext.getString(R.string.before);
        }else if(unit.equals(CommonData.UNIT_M)){
            online_text = time +mContext.getString(R.string.minute) +" " +mContext.getString(R.string.before);
        }else if(unit.equals(CommonData.UNIT_H)){
            online_text = time +mContext.getString(R.string.hour) +" " +mContext.getString(R.string.before);
        }else if(unit.equals(CommonData.UNIT_D)){
            online_text = time +mContext.getString(R.string.day) +" " +mContext.getString(R.string.before);
        }else if(unit.equals(CommonData.UNIT_W)){
            online_text = time +mContext.getString(R.string.week) +" " +mContext.getString(R.string.before);
        }else if(unit.equals(CommonData.UNIT_X)){
            Date mDate = getDateFormat(time, CommonData.PATTERN_DATE);
            int year = mDate.getYear() + 1900;
            int month = mDate.getMonth() + 1;
            int day = mDate.getDate();

            online_text = year +mContext.getString(R.string.year) + month +mContext.getString(R.string.month) + day +mContext.getString(R.string.day);
        }else{
            online_text = time;
        }
        return online_text;
    }

    /**
     * 마지막 로그인 시간 가져오기
     * @param ampm  am / pm 분류
     * @param hour  시간
     * @return
     */
    public static String getTimeToday(Context mContext, int ampm, int hour){
        String txt = "";
        int time;
        if (ampm == 1){
            time = hour + 12;
        }else {
            time = hour;
        }

        // a. 04~10 (아침)
        // b. 10~16(점심)
        // c. 16~22(저녁)
        // d. 22~04(심야)

        if (3 < time && time <= 9) {
            txt = mContext.getString(R.string.breakfast);
        }else if (9 < time && time <= 15) {
            txt = mContext.getString(R.string.lunch);
        }else if (15 < time && time <= 21) {
            txt = mContext.getString(R.string.dinner);
        }else if (21 < time && time <= 3) {
            txt = mContext.getString(R.string.latenight);
        }

        return txt.toString();
    }

    /**
     * 앱이 포그라운드 상테인지 확인하는 함수
     * @return boolean  ( true - 포그라운드, false - 백그라운드 )
     */
    public static boolean isApplicationForeground(Context context) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> info = am.getRunningTasks(1);

        if ( !info.isEmpty() ) {

            if ( info.get(0).topActivity.getPackageName().equals( context.getPackageName() ) )
                return true;

        }

        return false;

    }

    /**
     * Bitmap 이미지를 BASE64로 인코딩
     * @param drawable 이미지 리소스
     * @return 인코딩된 이미지 스트링
     */
    public static String encodeImage(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        // Bitmap bitmap =
        // ((BitmapDrawable)context.getResources().getDrawable(R.drawable.dot)).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); // bm is the
        // bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.NO_WRAP);
		/*
		 * ByteArrayOutputStream stream = new ByteArrayOutputStream();
		 * bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); byte[]
		 * bitMapData = stream.toByteArray();
		 */
        // Log.e("BEFORE JSON : ", encodedImage);
        return encodedImage;
    }

    public static String getPhoneNum() {
        TelephonyManager telManager = (TelephonyManager) DSMApplication.getInstance().getApplicationContext().getSystemService( Context.TELEPHONY_SERVICE);
        String phoneNum = telManager.getLine1Number();
        if(phoneNum.startsWith("+82")){
            phoneNum = phoneNum.replace("+82", "0");
        }
        return phoneNum;
    }

    public static String getModifiedPhoneNumber(Context context) {
        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tMgr.getLine1Number();
        if(TextUtils.isEmpty(phoneNumber)) {
            return null;
        } else {
            phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
            if(phoneNumber.startsWith("82")) {
                phoneNumber = phoneNumber.substring(2);
                phoneNumber = "0" + phoneNumber;
            }

            Log.d("modified number : " + phoneNumber);
            return phoneNumber;
        }
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
        return "";
    }

    public static void fnLaunchApp_call(Context context, String packageName, String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("MPortalP://" + packageName + "?" + param));
        context.startActivity(intent);
    }

    public static void setContext(Context applicationContext) {
        mContext = applicationContext;
    }
}
