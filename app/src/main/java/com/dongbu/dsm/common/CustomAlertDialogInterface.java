package com.dongbu.dsm.common;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;

/**
 * Created by LandonJung on 2017-8-10.
 * 커스텀 팝업 다이얼로그 인터페이스
 * @since 0, 1
 */
public class CustomAlertDialogInterface {

    public interface OnClickListener {

        public abstract void onClick(CustomAlertDialog dialog, Button button);

    }

    public interface OnCheckedChangeListener {

        public abstract void onCheckedChanged(CustomAlertDialog dialog, CheckBox checkbox, boolean ischeck);
    }

    public interface OnRatingBarChangeListener {

        public abstract void onRatingChanged(CustomAlertDialog dialog, RatingBar ratingbar, float size, boolean flag);
    }

}


