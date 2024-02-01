package com.dongbu.dsm.util;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.R;
import com.dongbu.dsm.intro.LoginActivity;
import com.softsecurity.transkey.Global;
import com.softsecurity.transkey.ITransKeyActionListener;
import com.softsecurity.transkey.ITransKeyActionListenerEx;
import com.softsecurity.transkey.ITransKeyCallbackListener;
import com.softsecurity.transkey.TransKeyActivity;
import com.softsecurity.transkey.TransKeyCipher;
import com.softsecurity.transkey.TransKeyCtrl;

import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class TransKeyCtrlViewUtil implements View.OnClickListener, View.OnTouchListener, ITransKeyActionListener,ITransKeyActionListenerEx,ITransKeyCallbackListener {
	private final String TAG = this.getClass().getSimpleName();
	public static final int TRANSKEY_TYPE_TEXT = 1;
	public static final int TRANSKEY_TYPE_NUM = 2;

	private static TransKeyCtrlViewUtil instance = null;

	AppCompatActivity mActivity;
	Context mContext;

	String[] plainTextBuf = {"", ""};
	String[] cipherTextBuf = {"", ""};
	String[] dummyTextBuf = {"", ""};

	int curViewIndex = 0;

	EditText editText;
	ViewGroup scrollView;
	ViewGroup imgEditContainer;

	ViewGroup transkeyNaviBar = null;
	View trankeyNaviPrevBtn = null;
	View trankeyNaviNextBtn = null;
	View trankeyNaviCompleteBtn = null;

	int mEditTextResId;
	int mDeleteTextBtnResId;

	int onClickIndex = 0;
	boolean isViewCtrlKeypad = false;

	TransKeyCtrl m_tkMngr = null;

	boolean isUseAutoFocusing = false;
//	private String publicKey = "MIGfMA0GCS....."; // 공개키를 설정함
	private String publicKey;		//파일에서 불러올 경우

	public interface TransKeyResultListener {
		public void done(String plainText, String cipherText);
		public void cancel();
	}

	public TransKeyResultListener getTransKeyResultListener() {
		return mTransKeyResultListener;
	}

	public void setTransKeyResultListener(TransKeyResultListener mTransKeyResultListener) {
		this.mTransKeyResultListener = mTransKeyResultListener;
	}

	private TransKeyResultListener mTransKeyResultListener;

	public static TransKeyCtrlViewUtil getInstance() {
		if(instance == null) {
			instance = new TransKeyCtrlViewUtil();
		}
		return instance;
	}

	public void onCreate(AppCompatActivity activity, Context context, ViewGroup rootView, EditText inputEditText, int deleteTextBtnResId, int tag, int type) {

		mActivity = activity;
		mContext = context;

		editText = inputEditText; // (EditText)rootView.findViewById(editTextResId);
		editText.setOnTouchListener(this);
		editText.setOnClickListener(this);
		editText.setTag(tag);

		scrollView = (ViewGroup)rootView.findViewById(R.id.keyscroll);
		scrollView.setOnTouchListener(this);
		scrollView.setOnClickListener(this);
		scrollView.setTag(tag);

		imgEditContainer = (ViewGroup)rootView.findViewById(R.id.keylayout);
		imgEditContainer.setOnTouchListener(this);
		imgEditContainer.setTag(tag);

		transkeyNaviBar = (ViewGroup)rootView.findViewById(R.id.keypadNaviBar);

		mDeleteTextBtnResId = deleteTextBtnResId;

		try {
			m_tkMngr = new TransKeyCtrl(mContext);
		} catch (Exception e) {
			if(Config.DISPLAY_LOG) e.printStackTrace();
		}

		//pem 파일로부터 PublicKey 생성
		try {
			InputStream fin = mContext.getResources().openRawResource(R.raw.server2048);
			CertificateFactory f = CertificateFactory.getInstance("X.509");
			X509Certificate certificate = (X509Certificate) f.generateCertificate(fin);
			PublicKey pk = certificate.getPublicKey();
			publicKey = new String(Base64.encode(pk.getEncoded(), 0));
		}catch(Exception e){
			Log.d("exception : " + e.getMessage());
		}

		if(type == TRANSKEY_TYPE_TEXT) {
			initTransKeyPad(TransKeyActivity.mTK_TYPE_KEYPAD_QWERTY_LOWER,
					TransKeyActivity.mTK_TYPE_TEXT_PASSWORD_EX,
					"텍스트입력 8자",
					"비밀번호를 입력하세요.",
					8,
					"최대 글자 8자를 입력하셨습니다.",
					5,
					true,
					(FrameLayout)rootView.findViewById(R.id.keypadContainer),
					editText,
					(HorizontalScrollView)rootView.findViewById(R.id.keyscroll),
					(LinearLayout)rootView.findViewById(R.id.keylayout),
					(ImageButton)rootView.findViewById(deleteTextBtnResId),
					(RelativeLayout)rootView.findViewById(R.id.keypadBallon),
					null, false);
//					(ImageView)transkeyNaviBar.findViewById(R.id.transkey_navi_last_input),false);
		} else if(type == TRANSKEY_TYPE_NUM) {
			initTransKeyPad(TransKeyActivity.mTK_TYPE_KEYPAD_NUMBER,
					TransKeyActivity.mTK_TYPE_TEXT_PASSWORD_LAST_IMAGE,
					"숫자입력8자",
					"숫자입력8자 입력하세요",
					8,
					"최대 글자 8자를 입력하셨습니다.",
					0,
					true,
					(FrameLayout)rootView.findViewById(R.id.keypadContainer),
					editText,
					(HorizontalScrollView)rootView.findViewById(R.id.keyscroll),
					(LinearLayout)rootView.findViewById(R.id.keylayout),
					(ImageButton)rootView.findViewById(deleteTextBtnResId),
					(RelativeLayout)rootView.findViewById(R.id.keypadBallon),
					null, false);
//					(ImageView)transkeyNaviBar.findViewById(R.id.transkey_navi_last_input), false);
		}

	}

	public boolean isShown() {
		return m_tkMngr.isShown();
	}

	public void initTransKeyPad(int keyPadType, int textType, String label, String hint,
                                int maxLength, String maxLengthMessage, int line3Padding, boolean bReArrange,
                                FrameLayout keyPadView, EditText editView, HorizontalScrollView scrollView,
                                LinearLayout inputView, ImageButton clearView, RelativeLayout ballonView, boolean bUseAtmMode) {
		try {
			if (m_tkMngr == null)
				m_tkMngr = new TransKeyCtrl(mContext);
			//Activity 로 처리할 때 처럼 파라미터를 인텐트에 넣어서 처리.
			Intent newIntent = getIntentParam(keyPadType, textType, label, hint, maxLength, maxLengthMessage, line3Padding, 40,bUseAtmMode);
			m_tkMngr.init(newIntent, keyPadView, editView, scrollView, inputView, clearView, ballonView);
			m_tkMngr.setReArrangeKeapad(bReArrange);
			m_tkMngr.setTransKeyListener(this);
			m_tkMngr.setTransKeyListenerEx(this);
			m_tkMngr.setTransKeyListenerCallback(this);
//			if (index == 0) {
//				m_tkMngr[index].setHeightOnKeypress(300);
//			}

		} catch (Exception e) {
			if(Global.debug) Log.d(e.getStackTrace().toString());
		}
	}

	public void initTransKeyPad(int keyPadType, int textType, String label, String hint,
                                int maxLength, String maxLengthMessage, int line3Padding, boolean bReArrange,
                                FrameLayout keyPadView, EditText editView, HorizontalScrollView scrollView,
                                LinearLayout inputView, ImageButton clearView, RelativeLayout ballonView, ImageView lastInput, boolean bUseAtmMode) {
		try {
			if (m_tkMngr == null)
				m_tkMngr = new TransKeyCtrl(mContext);
			//Activity 로 처리할 때 처럼 파라미터를 인텐트에 넣어서 처리.
			Intent newIntent = getIntentParam(keyPadType, textType, label, hint, maxLength, maxLengthMessage, line3Padding, 40,bUseAtmMode);
			m_tkMngr.init(newIntent, keyPadView, editView, scrollView, inputView, clearView, ballonView, lastInput);
			m_tkMngr.setReArrangeKeapad(bReArrange);
			m_tkMngr.setTransKeyListener(this);
			m_tkMngr.setTransKeyListenerEx(this);
			m_tkMngr.setTransKeyListenerCallback(this);
//			if (index == 0) {
//				m_tkMngr[index].setHeightOnKeypress(-300);
//			}
		} catch (Exception e) {
			if(Global.debug) Log.d(e.getStackTrace().toString());
		}
	}

	public void showTransKeyPad(int keyPadType) {

//		transkeyNaviBar.setVisibility(View.VISIBLE);
//		transkeyNaviBar.findViewById(R.id.transkey_navi_pre_button).setVisibility(View.VISIBLE);
//		transkeyNaviBar.findViewById(R.id.transkey_navi_next_button).setVisibility(View.VISIBLE);
//		transkeyNaviBar.findViewById(R.id.transkey_navi_complete_button).setVisibility(View.VISIBLE);
		transkeyNaviBar.findViewById(R.id.transkey_navi_last_input).setVisibility(View.VISIBLE);

		KeyboardUtils.hideSoftInput((LoginActivity)mContext);
		m_tkMngr.showKeypad(keyPadType);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		int tagValue = 0;
		Object tag = v.getTag();
		if(tag != null)
			tagValue = (Integer)tag;

		if(id == R.id.keyscroll)
		{
			editText.requestFocus();
			((LoginActivity)mActivity).setOutLayoutUpScroll(true);
			showTransKeyPad(TransKeyActivity.mTK_TYPE_KEYPAD_QWERTY_LOWER);
			onClickIndex = 0;
			isViewCtrlKeypad = true;
		}
	}

	public boolean onTouch(View v, MotionEvent event) {

		int id = v.getId();
		int tagValue = 0;
		Object tag = v.getTag();
		if(tag != null)
			tagValue = (Integer)tag;

		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(id == R.id.keylayout || id == R.id.keyscroll || v == editText)
			{
				editText.requestFocus();
				((LoginActivity)mActivity).setOutLayoutUpScroll(true);
				showTransKeyPad(TransKeyActivity.mTK_TYPE_KEYPAD_QWERTY_LOWER);
				isViewCtrlKeypad = true;
				return true;
			}
		}
		return false;
	}

	public void hideTransKey() {
		if(m_tkMngr != null && m_tkMngr.isShown()) {
			m_tkMngr.cancel();
			((LoginActivity)mActivity).setOutLayoutUpScroll(false);
		}
	}

	public Intent getIntentParam(int keyPadType, int textType, String label, String hint,
                                 int maxLength, String maxLengthMessage, int line3Padding, int reduceRate, boolean useAtmMode) {
		Intent newIntent = new Intent(mContext.getApplicationContext(), TransKeyActivity.class);

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
		newIntent.putExtra(TransKeyActivity.mTK_PARAM_DISABLE_BUTTON_EFFECT, true);

		newIntent.putExtra(TransKeyActivity.mTK_PARAM_QWERTY_BUTTON_MARGIN, 0.5f);
		newIntent.putExtra(TransKeyActivity.mTK_PARAM_NUMBER_BUTTON_MARGIN, 0.44f);

		//         드래그기능을 막고 최초 터치값으로 입력되도록 설정
//        newIntent.putExtra(TransKeyActivity.mTK_PARAM_DISABLE_DRAG_EVENT, true);

//         심볼키패드에서 불필요한 키 더미화
        newIntent.putExtra(TransKeyActivity.mTK_PARAM_CUSTOM_DUMMY_STRING, "%<>&()\"'");

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

	public byte[] toByteArray(String hexStr) {
		int len = hexStr.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hexStr.charAt(i), 16) << 4) + Character
					.digit(hexStr.charAt(i + 1), 16));
		}
		return data;
	}

	private void setData(int index, String plainText, String cipherText, String dummyText) {
		plainTextBuf[index] = plainText;
		cipherTextBuf[index] = cipherText;
		dummyTextBuf[index] = dummyText;

		String plainData = (index + 1) + ".PlainText : " + plainText;
		Log.d(plainData);
		String cipherData = (index + 1) + ".CipherText : " + cipherText;
		Log.d(cipherData);

		if(mTransKeyResultListener != null) {
			if(StringUtils.isEmpty(plainData) || StringUtils.isEmpty(cipherData)) {
				mTransKeyResultListener.cancel();
			} else {
				mTransKeyResultListener.done(plainText, cipherText);
			}
		}
	}

	@Override
	public void cancel(Intent data) {
		isViewCtrlKeypad = false;
		transkeyNaviBar.setVisibility(View.GONE);
		m_tkMngr.ClearAllData();
		setData(onClickIndex, "", "", "");
	}

	public void clearText() {
		if(m_tkMngr != null) {
			m_tkMngr.ClearAllData();
		}
	}

	@Override
	public void done(Intent data) {
		isViewCtrlKeypad = false;
		transkeyNaviBar.setVisibility(View.GONE);
		if (data == null) {
			if(mTransKeyResultListener != null) {
				mTransKeyResultListener.cancel();
			}
			return;
		}

		String cipherData = data.getStringExtra(TransKeyActivity.mTK_PARAM_CIPHER_DATA);
		String cipherDataex = data.getStringExtra(TransKeyActivity.mTK_PARAM_CIPHER_DATA_EX);
		String cipherDataexp = data.getStringExtra(TransKeyActivity.mTK_PARAM_CIPHER_DATA_EX_PADDING);
		String dummyData = data.getStringExtra(TransKeyActivity.mTK_PARAM_DUMMY_DATA);

		Log.d("cipherData : " + cipherData);
		Log.d("cipherDataex : " + cipherDataex);
		Log.d("cipherDataexp : " + cipherDataexp);

		byte[] secureKey = data.getByteArrayExtra(TransKeyActivity.mTK_PARAM_SECURE_KEY);
		int iRealDataLength = data.getIntExtra(TransKeyActivity.mTK_PARAM_DATA_LENGTH, 0);

		if (iRealDataLength == 0) {
			if(mTransKeyResultListener != null) {
				mTransKeyResultListener.cancel();
			}
			return;
		}

		// 비대칭키를 사용할 경우 데이터 포맷
		String encryptedData = data.getStringExtra(TransKeyActivity.mTK_PARAM_RSA_DATA);
		Log.d("encryptedData : " + encryptedData);

		StringBuffer plainData = null;
		try {
			TransKeyCipher tkc = new TransKeyCipher("SEED");
			tkc.setSecureKey(secureKey);
			String test = tkc.getPBKDF2DataEncryptCipherData(cipherData);

// 			String test1 = tkc.getPBKDF2DataEncryptCipherDataEx(cipherDataex);
//			String test2 = tkc.getPBKDF2DataEncryptCipherDataExWithPadding(cipherDataexp);

			byte pbPlainData[] = new byte[iRealDataLength];
			if (tkc.getDecryptCipherData(cipherData, pbPlainData)) {
				plainData = new StringBuffer(new String(pbPlainData));

				for(int i=0;i<pbPlainData.length;i++)
					pbPlainData[i]=0x01;
			} else {
				// 복호화 실패
				plainData = new StringBuffer("plainData decode fail...");
			}

		} catch (Exception e) {
			if(Global.debug) Log.d(e.getStackTrace().toString());
		}

		String encData = data.getStringExtra(TransKeyActivity.mTK_PARAM_SECURE_DATA);
		Log.d("encData : " + encData);

		setData(onClickIndex, plainData.toString(), encData, dummyData);

		//오토포커싱 사용할 경우
//		if(isUseAutoFocusing){
//			if(onClickIndex == 0)
//				nextKeyPad(1, TransKeyActivity.mTK_TYPE_KEYPAD_NUMBER);
//		}

	}

	public boolean onBackPressed() {

		if(isViewCtrlKeypad == true){
			m_tkMngr.finishTransKey(true);
			return true;
		}
		return false;
	}

//	private View.OnClickListener mTranskeyPreBtnClick = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//
//			if(m_tkMngr[1]!=null && m_tkMngr[1].isShown()){
//				m_tkMngr[1].finishTransKey(true);
//				editText[0].requestFocus();
//				showTransKeyPad(0, TransKeyActivity.mTK_TYPE_KEYPAD_QWERTY_LOWER);
//				onClickIndex = 0;
//				isViewCtrlKeypad = true;
//			}
//		}
//	};
//
//	private View.OnClickListener mTranskeyNextBtnClick = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//
//			if(m_tkMngr[0]!=null && m_tkMngr[0].isShown()){
//				m_tkMngr[0].finishTransKey(true);
//				editText[1].requestFocus();
//				showTransKeyPad(1, TransKeyActivity.mTK_TYPE_KEYPAD_NUMBER);
//				onClickIndex = 1;
//				isViewCtrlKeypad = true;
//			}
//		}
//	};

	private View.OnClickListener mTranskeyCompleteBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {

			m_tkMngr.finishTransKey(true);
		}
	};

	// Android-ctrl 모드에서 화면변경(가로->세로,세로->가로)시 보안키패드 자판 사라짐
	public void onConfigurationChanged() {

		m_tkMngr.showKeypad_changeOrientation();
	}

	@Override
	public void input(int type) {

		int currentLength = m_tkMngr.getInputLength();

		Log.d("currentLength :"+currentLength);
	}

//	/**
//	 * 다음 키패드 불러오는 함수
//	 * @param index : 불러올 키패드 인덱스
//	 * @param type : 불러올 키패드 타입
//	 */
//	private void nextKeyPad(int index, int type){
//		editText[index].requestFocus();
//		showTransKeyPad(index, type);
//		onClickIndex = index;
//		isViewCtrlKeypad = true;
//	}

	@Override
	public void minTextSizeCallback(){
		Log.d("minTextSizeCallback() call");

	}

	@Override
	public void maxTextSizeCallback(){
		Log.d("maxTextSizeCallback() call");

	}

	public void onDestroy() {
		if(instance != null) {
			instance = null;
		}
	}
}