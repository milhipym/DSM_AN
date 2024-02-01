package com.dongbu.dsm.util;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by LandonJung on 2017-8-10.
 * 폰트 클래스
 * @since 0, 1
 */
public class FontUtil {
    private static FontUtil _instance;
    private static Context mContext;

    public static Typeface typefaceNomal 	= null;

    public void setFont(View view) {

        ((TextView)view).setTypeface(typefaceNomal);
    }


    /**
     * 폰트 설정
     * @param root 뷰그룹
     */
    public void setGlobalFont(ViewGroup root) {

        // 현재 버전이 허니컴 보다 아래면 폰트를 적용하지 않는다.
//		if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB )
//			return;

        /**
         * Custom Font
         */
        if ( typefaceNomal == null )
            typefaceNomal = Typeface.createFromAsset(mContext.getAssets(), "font.n.ttf.mp3");

        for (int i = 0; i < root.getChildCount(); i++) {

            View child = root.getChildAt(i);

            if (child instanceof TextView) {

                Typeface getTypeface = ((TextView)child).getTypeface();

                if ( getTypeface != null )

                    if ( ((TextView)child).getTypeface().isBold() ) {

                        ((TextView)child).setTypeface(typefaceNomal);

                        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
                            ((TextView)child).setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);

                    }

                ((TextView)child).setTypeface(typefaceNomal);


            }
            else if (child instanceof ViewGroup)
                setGlobalFont((ViewGroup)child);
        }
    }

    // Application 생성 시 context를 입력함
    public void setContext(Context context)
    {
        this.mContext = context;
    }

    /**
     * 폰트 적용을 위한 객체 반환
     * @return instance
     */
    public static FontUtil getInstance()
    {
        if (_instance == null)
        {
            synchronized (FontUtil.class)
            {
                if(_instance == null)
                {
                    _instance = new FontUtil();
                }
            }

        }
        return _instance;
    }
}
