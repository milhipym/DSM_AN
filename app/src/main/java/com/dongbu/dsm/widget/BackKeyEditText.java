package com.dongbu.dsm.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created by 81700905 on 2017-10-11.
 */

public class BackKeyEditText extends androidx.appcompat.widget.AppCompatEditText {
    /* Must use this constructor in order for the layout files to instantiate the class properly */
    public BackKeyEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    private KeyImeChange keyImeChangeListener;

    public void setKeyImeChangeListener(KeyImeChange listener)
    {
        keyImeChangeListener = listener;
    }

    public interface KeyImeChange
    {
        public void onKeyIme(int keyCode, KeyEvent event);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event)
    {
        if (keyImeChangeListener != null)
        {
            keyImeChangeListener.onKeyIme(keyCode, event);
        }
        return false;
    }

}
