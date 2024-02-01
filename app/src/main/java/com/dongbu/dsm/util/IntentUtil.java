package com.dongbu.dsm.util;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * Created by LandonJung on 2017-08-16.
 */

public class IntentUtil {

//    private Activity mActivity;
//
//    public IntentUtils(Activity activity) {
//        this.mActivity = activity;
//    }

    /**
     * 갤러리 호출
     */
    public static Intent createAlbumIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        return Intent.createChooser(intent, null);
    }

    /**
     * 사진
     */
    public static Intent createShotIntent(File tempFile) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Uri uri = Uri.fromFile(tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    /**
     * 사진촬영
     * onActivityResult Bitmap 리턴
     */
    public static void capturePhoto(AppCompatActivity activity, Uri mLocationForPhotos, String targetFilename, Integer requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.withAppendedPath(mLocationForPhotos, targetFilename));
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 정지 영상 모드로 카메라 응용 프로그램을 시작합니다
     * onActivityResult
     */
    public static void capturePhoto_static_Image(AppCompatActivity activity, Integer requestCode) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 비디오 모드로 카메라 응용 프로그램을 시작합니다
     * onActivityResult
     */
    public static void capturePhoto_Video(AppCompatActivity activity , int requestCode) {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 연락처 선택
     * onActivityResult uri 리턴
     */
    public static void selectContact(AppCompatActivity activity, int requesetCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, requesetCode);
        }
    }


    /**
     * 문자 메시지
     */
    public static void sendSMS(AppCompatActivity activity, String phone, String content) {
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
                + phone));
        if (activity.getPackageManager().resolveActivity(sendIntent, 0) == null) {
            Toast.makeText(activity, "이 시스템은 이 기능을 지원하지 않습니다", Toast.LENGTH_SHORT).show();
            return;
        }
        sendIntent.putExtra("sms_body", content);
        activity.startActivity(sendIntent);
    }

    /**
     * 이메일
     */
    public static void sendEmail(AppCompatActivity activity, String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    /**
     * 전화
     */
    public static void callPhone(AppCompatActivity activity, String phone) {
        dialPhone(activity, phone, true);
    }


    private static void dialPhone(Context mContext, String phone, boolean isShow) {
        String action = Intent.ACTION_CALL;// 응용 프로그램에서 호출에 결함이 시작하고 비상 전화에 사용할 수 없습니다
        if (isShow) {
            action = Intent.ACTION_DIAL;// 전화 접속 디스플레이 인터페이스를
        }
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        Intent intent = new Intent(action, Uri.parse("tel:" + phone));
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(intent);
        }
    }

    /**
     * 앱 설치되어 있는지 여부
     */
    public static boolean isInstall(AppCompatActivity activity, String packageName) {
        PackageManager pckMan;
        pckMan = activity.getPackageManager();
        List<PackageInfo> packs = pckMan.getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if (packageName.equals(p.packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 알람 시계 설정
     * SET_ALARM 설정 필요
     */
    public static void createAlarm(AppCompatActivity activity, String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }


    /**
     * 타이머 설정
     * SET_ALARM 설정 필요
     */
    public static void startTimer(AppCompatActivity activity, String message, int seconds) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }


//    /**
//     * 캘린더 이벤트를 추가
//     */
//    public void addEvent(String title, String location, Calendar begin, Calendar end) {
//        Intent intent = new Intent(Intent.ACTION_INSERT)
//                .setData(CalendarContract.Events.CONTENT_URI)
//                .putExtra(CalendarContract.Events.TITLE, title)
//                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
//                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
//                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
//        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
//            mActivity.startActivity(intent);
//        }
//    }

    /**
     * 미디어 파일을 재생
     */
    public static void playMedia(AppCompatActivity activity, Uri file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(file);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    /**
     * 웹 검색을 수행
     */
    public static void searchWeb(AppCompatActivity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, url);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

//    public void openWifiSettings() {
//        Intent intent = new Intent(Intent.ACTION_ACCESSIBILITY_SETTINGS);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
//    }

    /**
     * 인터넷 브라우저
     */
    public static void openWebPage(AppCompatActivity activity, String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }


}

