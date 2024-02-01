package com.dongbu.dsm.push.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dongbu.dsm.Config;
import com.dongbu.dsm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.mtcom.smartmessage.common.BaseError;
import kr.mtcom.smartmessage.data.SmartMessage;
import kr.mtcom.smartmessage.network.SMApiEngine;
import kr.mtcom.smartmessage.network.SMApiEngineListener;

public class MessageDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private long mCurrentMsgSeq;
    private String mMonthlyNo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_push_item_detail);
        getSupportActionBar().setTitle("Message Detail");

        Button button;
        button = (Button) findViewById(R.id.list_detail_close_button);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.list_detail_remove_button);
        button.setOnClickListener(this);

        Intent intent = getIntent();
        mCurrentMsgSeq = intent.getLongExtra("msg_seq", 0);
        mMonthlyNo =  intent.getStringExtra("msg_monthlyNo");

        messageDetailHandler(mCurrentMsgSeq);
    }

    private void messageDetailHandler(Long msgSeq) {
        SMApiEngine.getInstance().smGetMessageRequest(msgSeq, mMonthlyNo,  mContext, new SMApiEngineListener() {
            public void smartMessageResponse(final SmartMessage item) {
                TextView textView = (TextView) findViewById(R.id.list_detail_text);
                textView.setText(
                        "msgResultSeqno : " + String.valueOf(item.msgResultSeqno) + "\n" +
                                "msgSubject : " + String.valueOf(item.msgSubject) + "\n" +
                                "msgContent : " + String.valueOf(item.msgContent) + "\n" +
                                "msgType : " + String.valueOf(item.msgType) + "\n" +
                                "callbackMdn : " + String.valueOf(item.callbackMdn) + "\n" +
                                "msgSenderId : " + String.valueOf(item.msgSenderId) + "\n" +
                                "msgSenderName : " + String.valueOf(item.msgSenderName) + "\n" +
                                "msgSenderUserSeqno : " + String.valueOf(item.msgSenderUserSeqno) + "\n" +
                                "msgSenderProfileImgYn : " + String.valueOf(item.msgSenderProfileImgYn) + "\n" +
                                "msgSendDt : " + String.valueOf(item.msgSendDt) + "\n" +
                                "msgAccessDt : " + String.valueOf(item.msgAccessDt) + "\n" +
                                "msgReplyDt : " + String.valueOf(item.msgReplyDt) + "\n" +
                                "msgAccessed : " + String.valueOf(item.msgAccessed) + "\n" +
                                "msgReplied : " + String.valueOf(item.msgReplied) + "\n" +
                                "msgFileCount : " + String.valueOf(item.msgFileCount) + "\n" +
                                "previewFileSeqno : " + String.valueOf(item.previewFileSeqno) + "\n" +
                                "lmsType : " + String.valueOf(item.lmsType) + "\n" +
                                "callbackUrl : " + String.valueOf(item.callbackUrl)
                );
            }

            public void errorResponse(BaseError error) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.list_detail_close_button) {
            Intent intent = new Intent(mContext, MessageListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        else if(v.getId() == R.id.list_detail_remove_button) {
            JSONArray deleteMessages = new JSONArray();
            JSONObject obj = new JSONObject();
            try {
                obj.put("msgResultSeqno", mCurrentMsgSeq);
                obj.put("monthlyNo", mMonthlyNo);
            } catch (JSONException e) {
                if(Config.DISPLAY_LOG) e.printStackTrace();
            }
            deleteMessages.put(obj);
            SMApiEngine.getInstance().smMessageDeleteRequest(deleteMessages, mContext, new SMApiEngineListener() {
                public void booleanResponse(boolean boolValue) {
                    Intent intent = new Intent(mContext, MessageListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}
