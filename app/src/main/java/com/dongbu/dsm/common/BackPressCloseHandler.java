package com.dongbu.dsm.common;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.dongbu.dsm.R;

/**
 * Created by LandonJung on 2017-8-10.
 * 앱 종료 감지 클래스
 * @since 0, 1
 */
public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Toast toast;

    private AppCompatActivity activity;

    /**
     * 앱 종료 감지 클래스 생성자
     * @param context context
     */
    public BackPressCloseHandler(AppCompatActivity context) {
        this.activity = context;
    }

    /**
     * 뒤로가기 클릭 이벤트
     */
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {

            activity.finish();
            toast.cancel();
        }
    }

    /**
     * 안내 토스트 문구
     */
    public void showGuide() {
        toast = Toast.makeText(activity, activity.getString(R.string.toast_app_exit), Toast.LENGTH_SHORT);
        toast.show();
    }
}
