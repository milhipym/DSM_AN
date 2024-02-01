package com.dongbu.dsm.push.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dongbu.dsm.R;
import com.dongbu.dsm.intro.IntroActivity;

import kr.mtcom.smartmessage.common.ApiEngineListener;
import kr.mtcom.smartmessage.common.BaseError;
import kr.mtcom.smartmessage.manager.AccountManager;

public class AddUserActivity extends AppCompatActivity implements OnClickListener {

    private Context mContext;
    private Long mRegisterType;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_push_register);
        getSupportActionBar().setTitle("Add User");

        Button button;
        button = (Button) findViewById(R.id.register_button);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.register_close_button);
        button.setOnClickListener(this);

        Intent intent = getIntent();
        String register_type = intent.getStringExtra("register_type");
        mRegisterType = Long.valueOf(register_type);

        LinearLayout linearLayout;
        TextView textView;
        if(mRegisterType == 1) {
            linearLayout = (LinearLayout) findViewById(R.id.register_id_layout);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout = (LinearLayout) findViewById(R.id.register_pw_layout);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout = (LinearLayout) findViewById(R.id.register_mdn_layout);
            linearLayout.setVisibility(View.VISIBLE);
            textView = (TextView) findViewById(R.id.register_title);
            textView.setText("아이디 비밀번호로 회원가입");
        }else if(mRegisterType == 2){
            linearLayout = (LinearLayout) findViewById(R.id.register_id_layout);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout = (LinearLayout) findViewById(R.id.register_mdn_layout);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout = (LinearLayout) findViewById(R.id.register_pw_layout);
            linearLayout.setVisibility(View.GONE);
            textView = (TextView) findViewById(R.id.register_title);
            textView.setText("아이디로 회원가입");
        }else if (mRegisterType == 3){
            linearLayout = (LinearLayout) findViewById(R.id.register_id_layout);
            linearLayout.setVisibility(View.GONE);
            linearLayout = (LinearLayout) findViewById(R.id.register_mdn_layout);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout = (LinearLayout) findViewById(R.id.register_pw_layout);
            linearLayout.setVisibility(View.GONE);
            textView = (TextView) findViewById(R.id.register_title);
            textView.setText("전화번호로 회원가입");
        }
    }

    private void registerRequest() {

        EditText editText;
        editText = (EditText) findViewById(R.id.register_id);
        String rID = editText.getText().toString();
        editText = (EditText) findViewById(R.id.register_name);
        String rNAME = editText.getText().toString();
        editText = (EditText) findViewById(R.id.register_mdn);
        String rMDN = editText.getText().toString();
        editText = (EditText) findViewById(R.id.register_password);
        String rPASSWORD = editText.getText().toString();

        if(mRegisterType == 1) {
            AccountManager.addUserWithIDAndPASSWORD(rID, rPASSWORD, rNAME, rMDN, mContext, new ApiEngineListener() {
                public void errorResponse(BaseError error) {
                    Log.d("AddUserActivity", error.getErrorCode()+", "+error.getMessage());
                    closeHandler("add error");
                }
                public void networkErrorResponse() {
                    closeHandler("networ error");
                }
                public void voidResponse() {
                    closeHandler("add success");
                }
            });
        }else if(mRegisterType == 2){
            AccountManager.addUserWithID(rID, rNAME, rMDN, mContext, new ApiEngineListener() {
                public void errorResponse(BaseError error) {
                    Log.d("AddUserActivity", error.getErrorCode()+", "+error.getMessage());
                    closeHandler("add error");
                }
                public void networkErrorResponse() {
                    closeHandler("networ error");
                }
                public void voidResponse() {
                    closeHandler("add success");
                }
            });
        }else if (mRegisterType == 3){
            AccountManager.addUserWithMDN(rMDN, rNAME, mContext, new ApiEngineListener() {
                public void errorResponse(BaseError error) {
                    Log.d("AddUserActivity", error.getErrorCode()+", "+error.getMessage());
                    closeHandler("add error");
                }
                public void networkErrorResponse() {
                    closeHandler("networ error");
                }
                public void voidResponse() {
                    closeHandler("add success");
                }
            });
        }
    }

    private void closeHandler(String str){
        Intent intent = new Intent(mContext, IntroActivity.class);
        if(str != null) {
            intent.putExtra("debug_text", str);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_button) {
            registerRequest();
        } else if (v.getId() == R.id.register_close_button) {
            closeHandler(null);
        }
    }
}
