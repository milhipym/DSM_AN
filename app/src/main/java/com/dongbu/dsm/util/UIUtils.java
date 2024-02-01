package com.dongbu.dsm.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

import com.dongbu.dsm.base.BaseApplication;

public class UIUtils {

	public static Context getContext() {
		return BaseApplication.getContext();
	}

	public static Resources getResources() {
		return getContext().getResources();
	}

	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	public static String getString(int resId, Object... formatArgs) {
		return getResources().getString(resId, formatArgs);
	}

	public static String[] getStringArr(int resId) {
		return getResources().getStringArray(resId);
	}

	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	public static String getPackageName() {
		return getContext().getPackageName();
	}

	public static long getMainThreadId() {
		return BaseApplication.getMainThreadId();
	}

	public static Handler getMainThreadHandler() {
		return BaseApplication.getHandler();
	}

	public static void postTaskSafely(Runnable task) {
		//获得当前线程id
		int curThreadId = android.os.Process.myTid();
		//主线程id
		long mainThreadId = getMainThreadId();
		if (curThreadId == mainThreadId) {
			// 当前线程==主线程,直接执行任务
			task.run();
		} else {
			// 当前线程==子线程,通过消息机制,把任务交给主线程的Handler去执行
			getMainThreadHandler().post(task);
		}
	}

	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
