package com.dongbu.dsm.push;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.dongbu.dsm.Config;
import com.dongbu.dsm.intro.IntroActivity;

import kr.mtcom.smartmessage.common.AppBaseUtil;
import kr.mtcom.smartmessage.common.SMConfig;

import static kr.mtcom.smartmessage.service.GcmIntentService.drawableToBitmap;
//GCM API 삭제됨
//import com.google.android.gms.gcm.GoogleCloudMessaging;

public class CustomGcmIntentService extends IntentService {
    private String TAG = "CustomGcmIntentService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;


    public CustomGcmIntentService() {
        super("CustomGcmIntentService");
    }

//    {
//        "data.svc" : "DSM",
//        "data.alertMsg" : "메세지",
//    }
//
//
//    {
//        "data.svc" : "DSM",
//        "data.ip" : "111.111.111.111",
//        "data.msgSeq" : "1",
//        "data.port" : "8080",
//        "data.url" : "http://wwww"
//    }


    @Override
    public void onHandleIntent(Intent intent) {
//        super.onHandleIntent(intent);
        Log.d(TAG, "received");
/*        Bundle extras = intent.getExtras();
        String dataSvc = extras.getString("data.svc");
        if(dataSvc != null && dataSvc.equals("DSM")) {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            String messageType = gcm.getMessageType(intent);
            Log.d(TAG, "messageType : " + messageType);
            if(!extras.isEmpty()) {
                Log.d(TAG, "extras : " + extras.toString());
                if(!"send_error".equals(messageType) && !"deleted_messages".equals(messageType) && "gcm".equals(messageType)) {
                    this.handleRemoteNoti(extras);
                }
            }
        }

        GcmBroadcastReceiver.completeWakefulIntent(intent);*/
    }

    private void handleRemoteNoti(Bundle bundle) {
        String rootClassName = Config.GCM.ROOT_ACTIVITY; //AppBaseUtil.getMetaData(this).getString("kr.mdongbu.smartmessage.root_activity");
        String broadcast = SMConfig.SM_RECEIVED_NOTIFICATION;
        this.mNotificationManager = (NotificationManager) this.getSystemService(getBaseContext().NOTIFICATION_SERVICE);
        int requestID = (int) System.currentTimeMillis();

        try {
            Intent e = new Intent(this, IntroActivity.class);
            e.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if(broadcast != null) {
                e.putExtra("remote_noti", broadcast);
                e.putExtra("data.ip", bundle.getString("data.ip"));
                e.putExtra("data.msgSeq", bundle.getString("data.msgSeq"));
                e.putExtra("data.port", bundle.getString("data.port"));
                e.putExtra("data.url", bundle.getString("data.url"));
            }

            PendingIntent contentIntent = PendingIntent.getActivity(this, requestID, e, PendingIntent.FLAG_UPDATE_CURRENT);
            String soundPath = AppBaseUtil.getResourceFilePath("noti_sm", this);
            PackageManager pm = this.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            int appIconResId = applicationInfo.icon;
            Drawable icon = this.getPackageManager().getApplicationIcon(this.getPackageName());
            Bitmap bitmap = drawableToBitmap(icon);
            NotificationCompat.Builder builder = (new NotificationCompat.Builder(this)).setSmallIcon(appIconResId).setLargeIcon(bitmap).setWhen(System.currentTimeMillis()).setTicker(bundle.getString("data.alertMsg")).setContentTitle("동부영업용모바일").setContentText(bundle.getString("data.alertMsg")).setDefaults(5).setSound(Uri.parse(soundPath)).setAutoCancel(false).setPriority(2);
            builder.setContentIntent(contentIntent);
            Notification noti = builder.build();
            this.mNotificationManager.notify(this.getApplicationContext().getPackageName().hashCode(), noti);
        } catch (PackageManager.NameNotFoundException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
    }
}
