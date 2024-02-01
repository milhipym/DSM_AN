package com.dongbu.dsm.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import androidx.core.app.NotificationCompat;

import com.dongbu.dsm.R;
import com.dongbu.dsm.app.DSMApplication;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.intro.IntroActivity;
import com.dongbu.dsm.network.RequestAsyncNetwork;
import com.dongbu.dsm.util.Log;
import com.google.android.gcm.GCMBaseIntentService;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by LandonJung on 2017-8-10.
 * GCM 클래스
 */
public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    public static final String PROJECT_ID = "556240746504"; // Google API의 project id

    private Notification mNoti;
    Vibrator mVibrator;

    private int notiIndex = 0;

    private RequestAsyncNetwork mRequestAsyncNetwork;

    /**
     * GCM 서비스 생성
     */
    public GCMIntentService() {
        this(PROJECT_ID);
        Log.v("GCMIntentService Start");
    }


    /**
     * GCM 서비스 생성
     * @param senderId 구글 프로젝트 ID
     */
    protected GCMIntentService(String senderId) {
        super(senderId);

    }

    @Override
    protected void onError(Context context, String errorId) {

        Log.v("onError ... errorId : " + errorId);
    }

    @Override
    protected void onRegistered(Context context, String regId) {

        DSMApplication.deviceToken = regId;
        Log.v("onRegistered ... regId : " + regId);
        // 푸시 등록 될 때
        // 등록에 관한 데이터를 서버에 넘겨준다.
		/*
		if ( CommonData.getInstance().getMemberId() != 0 ) {
			NetworkConst.getInstance().setMemberDeviceInfoParam(CommonData.getInstance().getMemberId(), regId);
			RequestNetwork requestNetwork = new RequestNetwork(getApplicationContext(), NetworkConst.NET_SET_MEMBER_DEVICE_INFO, new Handler());
			requestNetwork.start();
		}
		*/
    }

    @Override
    protected void onUnregistered(Context context, String regId) {

        Log.v("onUnregistered ... regId : " + regId);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {


//        CoffingMainActivity.mIsPush = true;

        Bundle b = intent.getExtras();

        Iterator<String> iterator = b.keySet().iterator();

        while ( iterator.hasNext() ) {
            String key = iterator.next();
            String value = b.get(key).toString();
            Log.v("onMessage ... " + key + " : " + value);
        }

        // 현재 로그 아웃 상태이면 푸시를 무시한다.
		/* 로그아웃 해도 푸시는 발송하도록 수정
		int memberId = CommonData.getInstance().getMemberId();

		if ( memberId != 0 ) {
			showPushMessage(context, intent);
		}
		*/
        showPushMessage(context, intent);
    }

    /**
     * 푸시 메시지를 전달 받으면 상태표시바에 표시함
     * @param context   context
     * @param intent    intent
     */
    private void showPushMessage(Context context, Intent intent) {

        String pid = intent.getStringExtra("pid");

        NotificationManager mNM	= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent newIntent = new Intent(context, IntroActivity.class);

        //newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        newIntent.putExtra(CommonData.JSON_PUSH_NOTICE_ID, Integer.parseInt(pid));
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, notiIndex, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String message = "";
        JSONObject obj = null;


        try {   // 푸시 타입 저장
            message = intent.getStringExtra(CommonData.JSON_MESSAGE);
            obj = new JSONObject(message);

            CommonData.getInstance().setPtype(obj.getString(CommonData.JSON_PTYPE));
            CommonData.getInstance().setPushIndex(obj.getInt(CommonData.JSON_INDEX));

            Log.i("obj = " +obj);

//            if (noticeType.equals(CommonData.JSON_NOTICE) || noticeType.equals(CommonData.JSON_CHAT)) {
//                CommonData.getInstance().setNewMessage(true);
//                Util.sendBroadCast(getApplicationContext(), CommonData.BROADCAST_PUSH_MESSAGE);
//            }

            mNoti = new NotificationCompat.Builder(context)
                    .setContentTitle( getString(R.string.app_name) )
                    .setContentText( obj.getString(CommonData.JSON_MESSAGE))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(obj.getString(CommonData.JSON_MESSAGE))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent).getNotification();

            mNM.notify(notiIndex, mNoti);

            // 0.5초동안 알림왔다고 진동 줌
            mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            mVibrator.vibrate(500);

        }catch(Exception e){
            Log.e(e.toString());
        }


        //notiIndex++;
        //setNotiIndex(notiIndex);
    }
}
