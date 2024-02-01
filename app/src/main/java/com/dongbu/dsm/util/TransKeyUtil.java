package com.dongbu.dsm.util;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.dongbu.dsm.common.CommonData;
import com.softsecurity.transkey.TransKeyActivity;
import com.softsecurity.transkey.TransKeyCipher;

/**
 * Created by landonjung on 2016. 8. 29..
 */
public class TransKeyUtil {

    AppCompatActivity mActivity;
    Context mContext;

    public static int TRANSKEY_TYPE_NONE = 0;
    public static int TRANSKEY_TYPE_TEXT = 1;
    public static int TRANSKEY_TYPE_NUMBER = 2;
    int mTransKeyType = TRANSKEY_TYPE_NONE;

    String plainTextBuf = "";
    String cipherTextBuf = "";
    String dummyTextBuf = "";

    String mSecureKey = "";
    int maxLength = 0;

    boolean isUseAutoFocusing = false;
    boolean isViewCtrlKeypad = false;
    boolean bDetector = false;
    boolean bClickCancelBtn = false;
    boolean bClickCompleteBtn = false;

    public TransKeyUtil(AppCompatActivity activity, Context context, int type, String secureKey, int maxLength) throws Exception {
        mActivity = activity;
        mContext = context;
        mTransKeyType = type;
        mSecureKey = secureKey;
        this.maxLength = maxLength;

        byte[] key = { 'M', 'O', 'B', 'I', 'L', 'E', 'T', 'R', 'A', 'N', 'S', 'K', 'E', 'Y', '1', '0' };

        if(mTransKeyType == TRANSKEY_TYPE_TEXT) {

            showTransKey(TransKeyActivity.mTK_TYPE_KEYPAD_QWERTY_LOWER, TransKeyActivity.mTK_TYPE_TEXT_PASSWORD, "패스워드 입력", maxLength);

        } else if(mTransKeyType == TRANSKEY_TYPE_NUMBER) {

            showTransKey(TransKeyActivity.mTK_TYPE_KEYPAD_NUMBER, TransKeyActivity.mTK_TYPE_TEXT_PASSWORD, "주민등록번호 뒤 7자리", maxLength);
            // 최소 7자리 입력하세요.
        }
    }

    public void showTransKey(final int keyPadType, final int textType, final String label, final int maxLength) {
        //Intent newIntent = new Intent(mActivity.getApplicationContext(), TransKeyActivity.class);

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent newIntent = getIntentParam(keyPadType, textType, label, "패스워드 8자리 입력 후 [입력완료]를 터치해주세요.", maxLength, "최대 " + maxLength + "자리 입력하세요.", 0, 40, false);
                mActivity.startActivityForResult(newIntent, CommonData.ACTIVITY_TRANS_KEY_CALL);
                //mActivity.startActivity(newIntent);
            }
        });
    }

    public Intent getIntentParam(int keyPadType, int textType, String label, String hint,
                                 int maxLength, String maxLengthMessage, int line3Padding, int reduceRate, boolean useAtmMode) {
        Intent newIntent = new Intent(mActivity, TransKeyActivity.class);

        //public static final int mTK_TYPE_KEYPAD_NUMBER				//숫자전용
        //public static final int mTK_TYPE_KEYPAD_QWERTY_LOWER		//소문자 쿼티
        //public static final int mTK_TYPE_KEYPAD_QWERTY_UPPER		//대문자 쿼티
        //public static final int mTK_TYPE_KEYPAD_ABCD_LOWER			//소문자 순열자판
        //public static final int mTK_TYPE_KEYPAD_ABCD_UPPER			//대문자 순열자판
        //public static final int mTK_TYPE_KEYPAD_SYMBOL				//심벌자판

        newIntent.putExtra(TransKeyActivity.mTK_PARAM_KEYPAD_TYPE, keyPadType);

        //키보드가 입력되는 형태
        //TransKeyActivity.mTK_TYPE_TEXT_IMAGE 					- 보안 텍스트 입력
        //TransKeyActivity.mTK_TYPE_TEXT_PASSWORD 				- 패스워드 입력
        //TransKeyActivity.mTK_TYPE_TEXT_PASSWORD_EX 			- 마지막 글자 보여주는 패스워드 입력
        //TransKeyActivity.mTK_TYPE_TEXT_PASSWORD_LAST_IMAGE	- 마지막 글자를 제외한 나머지를 *표시.
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_INPUT_TYPE, textType);

        //키패드입력화면의 입력 라벨
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_NAME_LABEL, label);
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_DISABLE_SPACE, false);

        //최대 입력값 설정 1 - 16
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_INPUT_MAXLENGTH, maxLength);

        //인터페이스 - maxLength시에 메시지 박스 보여주기. 기본은 메시지 안나옴.
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_MAX_LENGTH_MESSAGE, maxLengthMessage);

        //--> SERVER 연동테스트
        if(false){
            byte[] SecureKey = { 'M', 'o', 'b', 'i', 'l', 'e', 'T', 'r', 'a', 'n', 's', 'K', 'e', 'y', '1', '0' };
            newIntent.putExtra(TransKeyActivity.mTK_PARAM_CRYPT_TYPE, TransKeyActivity.mTK_TYPE_CRYPT_SERVER);
            newIntent.putExtra(TransKeyActivity.mTK_PARAM_SECURE_KEY, SecureKey);
        }
        else{
            newIntent.putExtra(TransKeyActivity.mTK_PARAM_CRYPT_TYPE, TransKeyActivity.mTK_TYPE_CRYPT_LOCAL);
        }
        //<-- SERVER 연동테스트

        //해당 Hint 메시지를 보여준다.
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_SET_HINT, hint);

        //Hint 테스트 사이즈를 설정한다.(단위 dip, 0이면 디폴트 크기로 보여준다.)
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_SET_HINT_TEXT_SIZE, 14);

        //커서를 보여준다.
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_SHOW_CURSOR, true);

        //에디트 박스안의 글자 크기를 조절한다.
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_EDIT_CHAR_REDUCE_RATE, reduceRate);

        //심볼 변환 버튼을 비활성화 시킨다.
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_DISABLE_SYMBOL, false);

        //심볼 변환 버튼을 비활성화 시킬 경우 팝업 메시지를 설정한다.
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_DISABLE_SYMBOL_MESSAGE, "심볼키는 사용할 수 없습니다.");

        //////////////////////////////////////////////////////////////////////////////
        //인터페이스 - line3 padding 값 설정 가능 인자. 기본은 0 값이면 아무 설정 안했을 시 원래 transkey에서 제공하던 값을 제공한다..
//        newIntent.putExtra(TransKeyActivity.mTK_PARAM_KEYPAD_MARGIN, line3Padding);
//        newIntent.putExtra(TransKeyActivity.mTK_PARAM_KEYPAD_LEFT_RIGHT_MARGIN, 0);
//        newIntent.putExtra(TransKeyActivity.mTK_PARAM_KEYPAD_HIGHEST_TOP_MARGIN, 2);

        newIntent.putExtra(TransKeyActivity.mTK_PARAM_USE_TALKBACK, true);
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_SUPPORT_ACCESSIBILITY_SPEAK_PASSWORD, true);
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_PREVENT_CAPTURE, false);
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_HIDE_TIMER_DELAY, 1);
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_USE_KEYPAD_ANIMATION, true);
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_USE_ATM_MODE, useAtmMode);

        newIntent.putExtra(TransKeyActivity.mTK_PARAM_SAMEKEY_ENCRYPT_ENABLE, false);

        newIntent.putExtra(TransKeyActivity.mTK_PARAM_NUMPAD_USE_CANCEL_BUTTON, true);

//
//		String originalPassword = "acc4af609fad57f1cd870a5d94092a24be5fd974";
//		byte[] pbkdfKey = toByteArray(originalPassword);
//		byte[] PBKDF2_SALT = {1, 6, 0, 7, 4, 4, 4, 4, 8, 8, 7, 1, 4, 3, 3, 3, 3, 3, 3, 3};
//		int PBKDF2_IT = 512;
//
//        newIntent.putExtra(TransKeyActivity. mTK_PARAM_PBKDF2_RANDKEY, pbkdfKey);
//		newIntent.putExtra(TransKeyActivity. mTK_PARAM_PBKDF2_SALT, PBKDF2_SALT);
//		newIntent.putExtra(TransKeyActivity. mTK_PARAM_PBKDF2_IT, PBKDF2_IT);

//		newIntent.putExtra(TransKeyActivity.mTK_PARAM_MIN_LENGTH_MESSAGE, "최소 글자 2글자 미만입니다");
//		newIntent.putExtra(TransKeyActivity.mTK_PARAM_INPUT_MINLENGTH, 2);
//		newIntent.putExtra(TransKeyActivity.mTK_PARAM_ALERTDIALOG_TITLE, "mTranskey alert");
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_DISABLE_BUTTON_EFFECT, false);

        newIntent.putExtra(TransKeyActivity.mTK_PARAM_QWERTY_BUTTON_MARGIN, 0.5f);
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_NUMBER_BUTTON_MARGIN, 0.44f);

        //         드래그기능을 막고 최초 터치값으로 입력되도록 설정
//        newIntent.putExtra(TransKeyActivity.mTK_PARAM_DISABLE_DRAG_EVENT, true);

//         심볼키패드에서 불필요한 키 더미화
//        newIntent.putExtra(TransKeyActivity.mTK_PARAM_CUSTOM_DUMMY_STRING, "%<>&()\"'");

//         순차적인 더미 이미지 다르게 표현하기위한 옵션
//        newIntent.putExtra(TransKeyActivity.mTK_PARAM_USE_CUSTOM_DUMMY, true);

//         커서를 이미지로 사용하는 옵션
//        newIntent.putExtra(TransKeyActivity.mTK_PARAM_USE_CUSTOM_CURSOR, true);

//         비대칭키 사용시 공개키 설정하는 옵션
        //newIntent.putExtra(TransKeyActivity.mTK_PARAM_RSA_PUBLICK_KEY, publicKey);

//        쿼티 자판 높이 설정
//        newIntent.putExtra(TransKeyActivity.mTK_PARAM_QWERTY_HEIGHT, (float)1.5);

//        넘버 자판 높이 설정
//	    newIntent.putExtra(TransKeyActivity.mTK_PARAM_NUMBER_HEIGHT, (float)0.5);

//		오토포커싱 설정
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_USE_AUTO_FOCUSING, isUseAutoFocusing);

        //시프트옵션
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_USE_SHIFT_OPTION, true);
        return newIntent;
    }

    public static String decrypt(String secureKey,String cipherData,int iRealDataLength){
        StringBuffer plainData = new StringBuffer();
        try {
            TransKeyCipher tkc = new TransKeyCipher("SEED");
            tkc.setSecureKey(DSMUtil.hexToBytes(secureKey));
            byte pbPlainData[] = new byte [iRealDataLength];
            if (tkc.getDecryptCipherData(cipherData, pbPlainData)){
                plainData = new StringBuffer(new String(pbPlainData));
                for(int i=0;i<pbPlainData.length;i++)
                    pbPlainData[i]=0x01;
            }
        } catch(Exception e) {
            return cipherData;
        }
        return plainData.toString();
    }

}
