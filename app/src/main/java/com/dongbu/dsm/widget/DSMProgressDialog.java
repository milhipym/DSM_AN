package com.dongbu.dsm.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dongbu.dsm.R;


/**
 * Created by landonjung on 2016. 9. 22..
 */
public class DSMProgressDialog extends Dialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.0f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dsm_progress_dialog);

        setLayout();
        setTitle(mTitle);

        hideStatusBar();
    }

    public DSMProgressDialog(Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context , R.style.Theme_Transparent);
    }

    public DSMProgressDialog(Context context , String title ,
                        View.OnClickListener singleListener) {
        super(context , R.style.Theme_Transparent);
        this.mTitle = title;
        this.mLeftClickListener = singleListener;
    }

    public DSMProgressDialog(Context context , String title , String content ,
                        View.OnClickListener leftListener , View.OnClickListener rightListener) {
        super(context , R.style.Theme_Transparent);
        this.mTitle = title;
        this.mContent = content;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }

    private String mTitle;
    private String mContent;
    private ImageView imageView;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

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

    /*
     * Layout
     */
    private void setLayout(){

        imageView = (ImageView) findViewById(R.id.progress_image);

        //imageView.setBackgroundResource(R.drawable.dsm_progressbar);
        imageView.setImageResource(R.drawable.dsm_progressbar);
        final AnimationDrawable mailAnimation = (AnimationDrawable) imageView.getDrawable();// imageView.getBackground();
        imageView.post(new Runnable() {
            public void run() {
                if ( mailAnimation != null ) mailAnimation.start();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
