package com.dongbu.dsm.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.StringUtils;
import com.dongbu.dsm.R;
import com.dongbu.dsm.util.FontUtil;
import com.dongbu.dsm.util.Log;

/**
 * Created by LandonJung on 2017-8-10.
 * 커스텀 팝업 다이얼로그
 * @since 0, 1
 */
public class CustomAlertDialog extends Dialog {

    public final static int TYPE_A	= 1;    // 버튼1개 팝업
    public final static int TYPE_B	= 2;    // 버튼 2개 팝업
    public final static int TYPE_C	= 3;    // 버튼 2개 팝업

    public static final int TYPE_RATING =   10; // 별점평가
    public static final int TYPE_PHOTO = 11;    // 사진 업로드

    public final static int TYPE_EDIT_PROFILE = 94;
    public final static int TYPE_HOME_DATE = 95;


    public final static int TYPE_NEW_1 = 101;


    public CustomAlertDialogInterface.OnClickListener positiveButtonClickListener = null;
    public CustomAlertDialogInterface.OnClickListener negativeButtonClickListener = null;
    public CustomAlertDialogInterface.OnClickListener thirdButtonClickListener = null;
    public CustomAlertDialogInterface.OnClickListener fourthButtonClickListener = null;
    public CustomAlertDialogInterface.OnClickListener fifthButtonClickListener = null;
    public CustomAlertDialogInterface.OnClickListener sixthButtonClickListener = null;
    public CustomAlertDialogInterface.OnClickListener backButtonClickListener = null;
    public CustomAlertDialogInterface.OnCheckedChangeListener checkedChangeListner = null;
    public CustomAlertDialogInterface.OnRatingBarChangeListener ratingChangeListener = null;

    public int mType = 1;

    private int id = -1;
    private int memberId = -1;
    private String mToast;

    private String mCoffeeItemCode;
    private String mBeanType;

    //RelativeLayout mLayout;
    ConstraintLayout mLayout;
    TextView mContentTv;
    Button button;
    Button negaButton;
    CheckBox checkbox;
    Context mContext;
    EditText mContextEt;

    RelativeLayout mPhotoLayout = null;
    ImageView ContentImageView = null;
    ImageView ItemImageView = null;
    TextView mVoiceContentView = null;

    ImageView mPhotoBlur = null;
    ImageView mPhotoBlurWho = null;
    Bitmap mPhotoBlurBmp = null;

    private boolean mOutTouchCancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.i("onCreate()");

        hideStatusBar();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        WindowManager.LayoutParams lpWindow = getWindow().getAttributes();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.0f;
        getWindow().setAttributes(lpWindow);

        //mLayout = (RelativeLayout) getWindow().findViewById(R.id.dialog_layout);
        mLayout = (ConstraintLayout) getWindow().findViewById(R.id.dialog_layout);
        setCanceledOnTouchOutside(false);


    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {

        mOutTouchCancel = cancel;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        switch ( mType ) {
            case TYPE_A :
                button.setWidth(mLayout.getWidth());
                break;
            case TYPE_B :
            case TYPE_C :
                int maxWidth = mLayout.getWidth();
                button.setWidth((int) maxWidth / 2);
                negaButton.setWidth((int) maxWidth / 2);

                Log.i("maxWidth = " +maxWidth);
                Log.i("maxWidth /2 = " +(int) maxWidth / 2);
                break;

        }

    }

    /**
     * 다이얼로그 외 영억 터치시 다이얼로그를 닫는다
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int action = event.getAction();

        switch ( action ) {

            case MotionEvent.ACTION_UP :

                float touchX = event.getX();
                float touchY = event.getY();

                float layoutX = mLayout.getLeft();
                float layoutY = mLayout.getTop();

                float layoutWidth 	= mLayout.getWidth();
                float layoutHeight 	= mLayout.getHeight();

                if ( !(touchX >= layoutX && touchX <= layoutX+layoutWidth
                        && touchY >= layoutY && touchY <= layoutY+layoutHeight)
                        ) {
                    if ( mOutTouchCancel ) {
                        try{
                            // popup type TYPE_A or TYPE_NEW_1 은 onclick 처리
                            if(mType == TYPE_A || mType == TYPE_NEW_1){	// 버튼 1개 dialog는 클릭 처리
                                Log.i("onBackPressed() Type_A ");
                                button.performClick();
                            }else if(mType == TYPE_HOME_DATE){			// 홈에서 데이트 신청 응답 팝업도 클릭 처리
                                Log.i("onBackPressed() TYPE_HOME_DATE ");
                                negaButton.performClick();
                            }else{
                                Log.v("close window");
                                dismiss();
                            }
                        }catch(Exception e){
                            Log.e(e.toString());
                            dismiss();
                        }

                        if(this.backButtonClickListener != null) {
                            this.backButtonClickListener.onClick(null, null);
                        }

                    }
                }

                break;

        }
        //Log.v("Layout getX : " + mLayout.getX() + "Layout getY : " + mLayout.getY());
        //Log.v("touche getX : " + event.getX() + " touch getY : " + event.getY());


        return super.onTouchEvent(event);


    }

    private void hideStatusBar(){
        if (Build.VERSION.SDK_INT < 16){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 뒤로가기 단, 버튼이 1개인 다이얼로그는 button click으로 인지한다.
     */
    @Override
    public void onBackPressed() {

        if(mType == TYPE_A || mType == TYPE_NEW_1){
            Log.i("onBackPressed() Type_A ");
            button.performClick();
        }else if(mType == TYPE_HOME_DATE){
            Log.i("onBackPressed() TYPE_HOME_DATE ");
            negaButton.performClick();
        }else{
            super.onBackPressed();
        }

        if(this.backButtonClickListener != null) {
            this.backButtonClickListener.onClick(this, null);
        }
    }

    /**
     * 커스텀 팝업 다이얼로그
     * @param context   context
     * @param type  타입 ( 1 ~ 3 )
     */
    public CustomAlertDialog(Context context, int type) {
        super(context, R.style.popup_custom_theme);
        mContext = context;
        setSelectView(type);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setToastMsg(String msg){
        this.mToast = msg;
    }

    public String getToastMsg(){
        return this.mToast;
    }

    public void setMemberId(int memberId){
        this.memberId = memberId;
    }

    public int getMemberId(){
        return this.memberId;
    }

    public Button getPosiBtn(){
        return button;
    }

    /**
     * 버틴 클릭 기본 설정
     */
    public View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            dismiss();
        }
    };


    /**
     * 폰트 적용(**레이아웃 설정 후 적용해야 함)
     */
    public void setFont() {
        // 커스텀 폰트 적용
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        FontUtil.getInstance().setGlobalFont(root);
    }

    public void setSelectView(int type) {

        mType = type;

        switch ( type ) {
            case TYPE_A :   // 버튼 1개
                setContentView(R.layout.popup_dialog_a_type_);
                break;
            case TYPE_B :   // 버튼 2개
                setContentView(R.layout.popup_dialog_b_type_);
                break;
            case TYPE_C:   // 에딧텍스트
                setContentView(R.layout.popup_dialog_c_type_);
                break;
        }
        setFont();

    }

    /**
     * 별점평가 버튼 활성 유무 설정
     * @param bool ( true - 활성화 , false - 비활성홯 )
     */
    public void setButtonColor(boolean bool){
        // 활성화
        if(bool){
            button.setBackgroundResource(R.drawable.btn_right);
            button.setTextColor(ContextCompat.getColorStateList(mContext, R.color.color_ffffff_btn));
            button.setEnabled(true);
        }
        // 비활성화
        else{
            button.setBackgroundResource(R.drawable.background_2_5_rightbottom_50f39c89);
            button.setTextColor(ContextCompat.getColor(mContext, R.color.color_50ffffff));
            button.setEnabled(false);
        }

    }



    /*
     * (non-Javadoc)
     * @see android.app.Dialog#setTitle(java.lang.CharSequence)
     */
    @Override
    public void setTitle(CharSequence title) {

        TextView titleTextView = null;
//        switch ( mType ) {
//            case TYPE_A :
//                titleTextView = (TextView) findViewById(R.id.dialog_title);
//                break;
//            case TYPE_B :
//                titleTextView = (TextView) findViewById(R.id.dialog_title);
//                break;
//            case TYPE_SEARCH_PASSWORD:
//                titleTextView = (TextView) findViewById(R.id.dialog_title);
//                break;
//            case TYPE_PHOTO:
//                titleTextView = (TextView) findViewById(R.id.dialog_title);
//                break;
//            case TYPE_STARMARK:
//                titleTextView = (TextView) findViewById(R.id.dialog_title);
//                break;
//
//        }

        titleTextView = (TextView) findViewById(R.id.dialog_title);

        if ( titleTextView != null ){
            titleTextView.setText(title);
        }
    }

    /*
     * (non-Javadoc)
     * @see android.app.Dialog#setTitle(int)
     */
    @Override
    public void setTitle(int titleId) {

        this.setTitle(titleId);
        String title = getContext().getResources().getString(titleId);
        setTitle(title);
    }

    /**
     * 팝업 내용
     * @param content   내용
     */
    public void setContent(CharSequence content) {

        mContentTv = null;
//        switch ( mType ) {
//            case TYPE_A :
//                ContentTextView = (TextView) findViewById(R.id.dialog_content);
//                break;
//            case TYPE_B :
//                ContentTextView = (TextView) findViewById(R.id.dialog_content);
//                break;
//            case TYPE_SEARCH_PASSWORD:
//                ContentTextView = (TextView) findViewById(R.id.dialog_content);
//                break;
//            case TYPE_STARMARK:
//                ContentTextView = (TextView) findViewById(R.id.dialog_content);
//                break;
//
//        }

        mContentTv = (TextView) findViewById(R.id.dialog_content);

        if ( mContentTv != null ){
            mContentTv.setText(content);
            setToastMsg(String.valueOf(content));
        }
    }

    /**
     *  Type_C Content가 직접 입력하는 EditText가 추가될 때.
     */
    public void makeContent(){
        mContextEt = (EditText) findViewById(R.id.dialog_econtent);
    }

    public String getmakeContent(){
        if(mContextEt != null){
            return mContextEt.getText().toString();
        }else{
            return null;
        }

    }

    public void setBackButtonClickListener(CustomAlertDialogInterface.OnClickListener listener) {
        this.backButtonClickListener = listener;
    }
    /**
     * 확인 버튼 설정 ( 버튼이 2개일 경우 오른쪽 )
     * @param text 버튼명
     * @param listener button click event
     */
    public void setPositiveButton(CharSequence text, CustomAlertDialogInterface.OnClickListener listener) {

        this.positiveButtonClickListener = listener;

        button = null;

        switch ( mType ) {
            case TYPE_PHOTO:
//                button = (Button) findViewById(R.id.gallery_btn);
                if ( text != null )	button.setText(text);
                break;
            default:
                button = (Button) findViewById(R.id.confirm_btn);
                if ( text != null )	button.setText(text);
                break;
        }


        if ( button != null && positiveButtonClickListener != null) {

            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    positiveButtonClickListener.onClick(CustomAlertDialog.this, (Button)v);

                }
            });
        }
        else {
            button.setOnClickListener(clickListener);
        }

    }

    /**
     * 취소 버튼 설정 ( 버튼이 2개일 경우 왼쪽 )
     * @param text 버튼명
     * @param listener button click event
     */
    public void setNegativeButton(String text, CustomAlertDialogInterface.OnClickListener listener) {

        this.negativeButtonClickListener = listener;

        negaButton = null;

//        switch ( mType ) {
//            case TYPE_A :
//                break;
//            case TYPE_B :
//                negaButton = (Button) findViewById(R.id.cancel_btn);
////				/* 2015-01-14 네비바 팝업 -> X 팝업 스타일 개선
//				if ( text != null )	negaButton.setText(text);
////				*/
//                break;
//            case TYPE_SEARCH_PASSWORD:
//                negaButton = (Button) findViewById(R.id.cancel_btn);
//                if ( text != null )	negaButton.setText(text);
//                break;
//            case TYPE_PHOTO:
//                negaButton = (Button) findViewById(R.id.cancel_btn);
//                if ( text != null )	negaButton.setText(text);
//                break;
//            case TYPE_STARMARK:
//                negaButton = (Button) findViewById(R.id.cancel_btn);
//                if ( text != null )	negaButton.setText(text);
//                break;
//
//        }


        negaButton = (Button) findViewById(R.id.cancel_btn);


        if ( negaButton != null && negativeButtonClickListener != null) {

            negaButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    negativeButtonClickListener.onClick(CustomAlertDialog.this, (Button)v);

                }
            });

            if(!StringUtils.isEmpty(text)) {
                negaButton.setText(text);
            }
        }
        else {
            negaButton.setOnClickListener(clickListener);
        }

    }

    /**
     * 프로필 정보 수정 버튼 설정
     * @param text 버튼명
     * @param listener button click event
     */
    public void setThirdButton(String text, CustomAlertDialogInterface.OnClickListener listener) {

        this.thirdButtonClickListener = listener;

        Button button = null;

        /* 2016-01-04 작업 후 주석제거
        switch ( mType ) {
            case TYPE_A :
                break;
            case TYPE_B :
                break;
            case TYPE_PHOTO:
                button = (Button) findViewById(R.id.camera_btn);
                if ( text != null )	button.setText(text);
                break;
            case TYPE_FAVORITE:
                button = (Button) findViewById(R.id.third_btn);
                if ( text != null )	button.setText(text);
                break;

        }
        */

        if ( button != null && thirdButtonClickListener != null) {

            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    thirdButtonClickListener.onClick(CustomAlertDialog.this, (Button)v);

                }
            });
        }
        else {
            button.setOnClickListener(clickListener);
        }

    }

    /**
     * 다음에 하기 버튼 설정
     * @param text 버튼명
     * @param listener button click event
     */
    public void setFourthButton(String text, CustomAlertDialogInterface.OnClickListener listener) {

        this.fourthButtonClickListener = listener;

        Button button = null;

        switch ( mType ) {
            case TYPE_A :
                break;
            case TYPE_B :
                break;
            case TYPE_C :
                break;
        }

        if ( button != null && fourthButtonClickListener != null ) {

            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    fourthButtonClickListener.onClick(CustomAlertDialog.this, (Button)v);

                }
            });
        }
        else {
            button.setOnClickListener(clickListener);
        }

    }

    /**
     * 다음에 하기 버튼 설정
     * @param text 버튼명
     * @param listener button click event
     */
    public void setFifthButton(String text, CustomAlertDialogInterface.OnClickListener listener) {

        this.fifthButtonClickListener = listener;

        Button button = null;

        switch ( mType ) {
            case TYPE_A :
                break;
            case TYPE_B :
                break;
            case TYPE_C :
                break;
        }

        if ( button != null && fifthButtonClickListener != null ) {

            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    fifthButtonClickListener.onClick(CustomAlertDialog.this, (Button)v);

                }
            });
        }
        else {
            button.setOnClickListener(clickListener);
        }

    }

    /**
     * 여섯번째 버튼
     * @param text 버튼명
     * @param listener button click event
     */
    public void setSixthButton(String text, CustomAlertDialogInterface.OnClickListener listener) {

        this.sixthButtonClickListener = listener;

        Button button = null;

        switch ( mType ) {
            case TYPE_EDIT_PROFILE:	// 키워드 수정

                break;
        }

        if ( button != null && sixthButtonClickListener != null ) {

            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    sixthButtonClickListener.onClick(CustomAlertDialog.this, (Button)v);

                }
            });
        }
        else {
            button.setOnClickListener(clickListener);
        }

    }

}
