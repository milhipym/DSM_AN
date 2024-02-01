package com.dongbu.dsm.util;

/**
 * Created by 81700905 on 2017-10-31.
 */

import android.content.Context;
import android.graphics.Rect;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
public class KeyboardUtil implements ViewTreeObserver.OnGlobalLayoutListener
{
    private final static int MAGIC_NUMBER = 200;

    private SoftKeyboardToggleListener mCallback;
    private View mRootView;
    private Boolean prevValue = null;
    private float mScreenDensity = 1;
    private static HashMap<SoftKeyboardToggleListener, KeyboardUtil> sListenerMap = new HashMap<>();

    public interface SoftKeyboardToggleListener
    {
        void onToggleSoftKeyboard(boolean isVisible);
    }


    @Override
    public void onGlobalLayout()
    {
        Rect r = new Rect();
        mRootView.getWindowVisibleDisplayFrame(r);

        int heightDiff = mRootView.getRootView().getHeight() - (r.bottom - r.top);
        float dp = heightDiff/ mScreenDensity;
        boolean isVisible = dp > MAGIC_NUMBER;

        if (mCallback != null && (prevValue == null || isVisible != prevValue)) {
            prevValue = isVisible;
            mCallback.onToggleSoftKeyboard(isVisible);
        }
    }

    /**
     * Add a new keyboard listener
     * @param act calling activity
     * @param listener callback
     */
    public static void addKeyboardToggleListener(AppCompatActivity act, SoftKeyboardToggleListener listener)
    {
        removeKeyboardToggleListener(listener);

        sListenerMap.put(listener, new KeyboardUtil(act, listener));
    }

    /**
     * Add a new keyboard listener
     * @param act calling activity
     * @param listener callback
     */
    public static void addKeyboardToggleListener(View act, SoftKeyboardToggleListener listener)
    {
        removeKeyboardToggleListener(listener);

        sListenerMap.put(listener, new KeyboardUtil(act, listener));
    }

    /**
     * Remove a registered listener
     * @param listener {@link SoftKeyboardToggleListener}
     */
    public static void removeKeyboardToggleListener(SoftKeyboardToggleListener listener)
    {
        if(sListenerMap.containsKey(listener))
        {
            KeyboardUtil k = sListenerMap.get(listener);
            k.removeListener();

            sListenerMap.remove(listener);
        }
    }

    /**
     * Remove all registered keyboard listeners
     */
    public static void removeAllKeyboardToggleListeners()
    {
        for(SoftKeyboardToggleListener l : sListenerMap.keySet())
            sListenerMap.get(l).removeListener();

        sListenerMap.clear();
    }

    /**
     * Manually toggle soft keyboard visibility
     * @param context calling context
     */
    public static void toggleKeyboardVisibility(Context context)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Force closes the soft keyboard
     * @param activeView the view with the keyboard focus
     */
    public static void forceCloseKeyboard(View activeView)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) activeView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activeView.getWindowToken(), 0);
    }

    private void removeListener()
    {
        mCallback = null;

        mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    private KeyboardUtil(AppCompatActivity act, SoftKeyboardToggleListener listener)
    {
        mCallback = listener;

        mRootView = ((ViewGroup) act.findViewById(android.R.id.content)).getChildAt(0);
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        mScreenDensity = act.getResources().getDisplayMetrics().density;
    }

    private KeyboardUtil(View act, SoftKeyboardToggleListener listener)
    {
        mCallback = listener;

        mRootView = act;
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        mScreenDensity = act.getResources().getDisplayMetrics().density;
    }

}

