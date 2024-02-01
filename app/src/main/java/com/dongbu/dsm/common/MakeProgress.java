package com.dongbu.dsm.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.dongbu.dsm.R;


/**
 * Created by LandonJung on 2017-8-10.
 * 프로그래스바 클래스
 * @since 0, 1
 */
public class MakeProgress extends ProgressDialog {

    public MakeProgress(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setCanceledOnTouchOutside(false);
        super.setContentView(R.layout.dialog_progress_layout);

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}