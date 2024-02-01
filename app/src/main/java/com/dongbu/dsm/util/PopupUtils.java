package com.dongbu.dsm.util;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

import com.dongbu.dsm.Config;
import com.dongbu.dsm.R;
import com.dongbu.dsm.base.BaseActivity;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.CustomAlertDialog;
import com.dongbu.dsm.common.CustomAlertDialogInterface;

/**
 * Created by LandonJung on 2017-09-14.
 */

public class PopupUtils {
    public static AppCompatActivity parentActivity = null;
    public static void show(AppCompatActivity activity, String content) {
        show(activity, activity.getString(R.string.popup_dialog_a_type_title), content, dialogClickListener, CommonData.POPUP_TYPE_DEFAULT);
    }

    public static void show(AppCompatActivity activity, String title, String content) {
        show(activity, title, content, dialogClickListener, CommonData.POPUP_TYPE_DEFAULT);
    }

    public static void show(AppCompatActivity activity, String title, String content, int id) {
        show(activity, title, content, dialogClickListener, id);
    }

    public static void show(AppCompatActivity activity, String content, CustomAlertDialogInterface.OnClickListener positiveListener) {
        show(activity, activity.getString(R.string.popup_dialog_a_type_title), content, positiveListener, CommonData.POPUP_TYPE_DEFAULT);
    }

    public static void show(AppCompatActivity activity, String content, CustomAlertDialogInterface.OnClickListener positiveListener, CustomAlertDialogInterface.OnClickListener negativeListener) {
        show(activity, activity.getString(R.string.popup_dialog_a_type_title), content, positiveListener, negativeListener, CommonData.POPUP_TYPE_DEFAULT);
    }

    public static void show(AppCompatActivity activity, String content, CustomAlertDialogInterface.OnClickListener positiveListener, int id) {
        show(activity, activity.getString(R.string.popup_dialog_a_type_title), content, positiveListener, id);
    }

    public static void show(AppCompatActivity activity, String content, CustomAlertDialogInterface.OnClickListener positiveListener, CustomAlertDialogInterface.OnClickListener negativeListener, int id) {
        show(activity, activity.getString(R.string.popup_dialog_a_type_title), content, positiveListener, negativeListener, id);
    }

    public static void show(final AppCompatActivity activity, final String title, final String content, final CustomAlertDialogInterface.OnClickListener positiveListener, final int id) {

        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //if(dialogA == null) {
                    if ((activity != null) && (!activity.isFinishing())) {
                        CustomAlertDialog dialogA = new CustomAlertDialog(activity, CustomAlertDialog.TYPE_A);
                        //}

                        dialogA.setTitle(title);
                        dialogA.setContent(content);
                        dialogA.setPositiveButton(activity.getString(R.string.popup_dialog_button_confirm), positiveListener);

                        dialogA.setId(id);
                        parentActivity = activity;
                        dialogA.show();

                        ((BaseActivity)activity).hideProgress();
                    }

                }
            });

        } catch (Exception e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }

    }

    public static void show(final AppCompatActivity activity, final String title, final String content, final CustomAlertDialogInterface.OnClickListener positiveListener, final CustomAlertDialogInterface.OnClickListener negativeListener, final int id) {

        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if ((activity != null) && (!activity.isFinishing())) {
                        CustomAlertDialog dialogB = new CustomAlertDialog(activity, CustomAlertDialog.TYPE_B);

                        dialogB.setTitle(title);
                        dialogB.setContent(content);
                        dialogB.setPositiveButton(activity.getString(R.string.popup_dialog_button_confirm), positiveListener);
                        dialogB.setNegativeButton(activity.getString(R.string.popup_dialog_button_cancel), negativeListener);

                        dialogB.setId(id);
                        parentActivity = activity;
                        dialogB.show();

                        ((BaseActivity)activity).hideProgress();
                    }

                }
            });

        } catch (Exception e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }

    }

    /**
     * 팝업 다이얼로그 클릭 리스너 ( 확인버튼 )
     */
    public static CustomAlertDialogInterface.OnClickListener dialogClickListener = new CustomAlertDialogInterface.OnClickListener() {

        @Override
        public void onClick(CustomAlertDialog dialog, Button button) {
            dialog.dismiss();
            switch ( dialog.getId() ) {
                case CommonData.POPUP_TYPE_DEFAULT:
                    break;
                case CommonData.POPUP_TYPE_DEFAULT_FINISH:
                    if(parentActivity != null) {
                        parentActivity.finish();
                    }
                    break;
            }
        }
    };

    /**
     * 팝업 다이얼로그 클릭 리스너 ( 취소버튼 )
     */
    public static CustomAlertDialogInterface.OnClickListener dialogCancelClickListener = new CustomAlertDialogInterface.OnClickListener() {

        @Override
        public void onClick(CustomAlertDialog dialog, Button button) {

            dialog.dismiss();
        }
    };

}
