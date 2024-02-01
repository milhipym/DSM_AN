package com.dongbu.dsm.intro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.dongbu.dsm.Config;
import com.dongbu.dsm.R;
import com.dongbu.dsm.base.IntroBaseActivity;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.CustomAlertDialog;
import com.dongbu.dsm.common.CustomAlertDialogInterface;
import com.dongbu.dsm.common.PermissionUtils;
import com.dongbu.dsm.common.SessionMan;
import com.dongbu.dsm.util.DSMUtil;
import com.dongbu.dsm.util.HandlerUtils;
import com.dongbu.dsm.util.Log;
import com.dongbu.dsm.util.MVaccine;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

//import com.google.firebase.perf.metrics.AddTrace;

/**
 * Created by LandonJung on 2017-8-10.
 * 로고 클래스
 * @since 0, 1
 */
public class IntroActivity extends IntroBaseActivity {

	public static final int DIALOG_UPDATE		= 0;
	public static final int DIALOG_DATA_ERROR	= 1;

	private boolean mFlag = true;											// 자동 로그인 플래그

	private MVaccine vaccine = null;

	private PackageManager pm;
	private PackageInfo pi	= null;

	CustomAlertDialog mDialog;

    @Bind(R.id.intro_image)
    ImageView introimg;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
		setContentView(R.layout.intro_activity);
		ButterKnife.bind(this);
		mContext = this;
		commonData.setStep(CommonData.ACTIVITY_LAUNCHER);

		if(Config.IS_SERVER_HOST_REAL && !Config.ENABLE_MDM_AGENT_CHECK && Config.LIMIT_RUN) {
			if(DSMUtil.isExpireDay(Config.LIMIT_RUN_YEAR, Config.LIMIT_RUN_MONTH, Config.LIMIT_RUN_DAY)) {
				activityClear();
				System.exit(0);
				return;
			}
		}

		Log.d("Build.VERSION.RELEASE = " + Build.VERSION.RELEASE);

		if(!checkIntentData(getIntent())) {
			init();

			if(CommonData.isEnterBojang) {
				if(!commonData.isLogined()) {
					CommonData.isEnterBojang = false;
				}
				goToLogin();
			} else {
				commonData.setLogined(false);
				SessionMan.setbLogin(false);
				new Thread(new Runnable() {
					@Override
					public void run() {
						doRunSecurityModulesV1();
					}
				}).start();
			}
		}

		showProgress();
	}
	//@AddTrace(name = "runningVaccine")
	public void doRunSecurityModulesV1() {

		if(Config.ENABLE_VACCINE) {
           	/*------------------------- 백신 관련 초기화 처리  ----------------------------*/

			try {
				if (vaccine == null) {
					vaccine = new MVaccine(IntroActivity.this, mContext);

					if (!vaccine.rootingCheck()) {

						finish();

					} else {

						vaccine.mini(); // 백신 액티비티에서 악성코드,루팅 탐지
					}
				}
			} catch (Exception e) {
				if(Config.DISPLAY_LOG) e.printStackTrace();
				if(vaccine != null) {
					vaccine.mini(); // 백신 액티비티에서 악성코드,루팅 탐지
				}
			}

		} else {

			HandlerUtils.postDelayed(runNext, 100);
		}
	}

	Runnable runNext = new Runnable() {
		@Override
		public void run() {
			if (!PermissionUtils.canAccessPhone(IntroActivity.this) ||
					!PermissionUtils.canAccessStorage(IntroActivity.this)) {  // 폰 정보 권한이 없다면
				HandlerUtils.post(runPermissionCheck);
			} else {
				goToLogin();
				//requestAppInfo();
			}
		}
	};

	@Override
	protected void onNewIntent(Intent intent) {

		Log.d("onNewIntent");
		checkIntentData(intent);
		goToLogin();

		super.onNewIntent(intent);
	}

	Runnable runPermissionCheck = new Runnable() {
		@Override
		public void run() {
         /*------------------------- 권한 관련 초기화 처리  ----------------------------*/

			PermissionListener permissionlistener = new PermissionListener() {
				@Override
				public void onPermissionGranted() {
					//requestAppInfo();

					goToLogin();
				}

				@Override
				public void onPermissionDenied(ArrayList<String> deniedPermissions) {
					Log.i("권한 획득 거부 or 취소");
					mDialog = new CustomAlertDialog(mContext, CustomAlertDialog.TYPE_A);
					mDialog.setTitle(getString(R.string.popup_dialog_a_type_title));
					mDialog.setContent(getString(R.string.popup_dialog_permission_content));
					mDialog.setPositiveButton(getString(R.string.popup_dialog_button_confirm), new CustomAlertDialogInterface.OnClickListener() {

						@Override
						public void onClick(CustomAlertDialog dialog, Button button) {
							mDialog.dismiss();
							finish();
						}
					});
					mDialog.show();
				}
			};

			new TedPermission(mContext)
					.setPermissionListener(permissionlistener)
					.setPermissions(
							Manifest.permission.READ_PHONE_STATE,
							Manifest.permission.WRITE_EXTERNAL_STORAGE,
							Manifest.permission.READ_EXTERNAL_STORAGE
					)
					.check();

		}
	};

	/**
	 * 앱 최초 실행시 바로가기 아이콘 추가
	 */
	private void addShortcut() {
		Log.i("addShortcut()");
		//Adding shortcut for MainActivity
		//on Home screen
		Intent shortcutIntent = new Intent(getApplicationContext(), IntroActivity.class);
		shortcutIntent.setAction(Intent.ACTION_MAIN);

		Intent addIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(getApplicationContext(),
						R.mipmap.ic_launcher));
		addIntent.putExtra("duplicate", false);
		addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		getApplicationContext().sendBroadcast(addIntent);

		CommonData.getInstance().setShortCut(true);
	}

	/**
	 * 초기화
	 */
	public void init(){
	}

	/**
	 * 로그인 화면으로 이동
	 */
	public void goToLogin() {

		try {
			moveActivity();

		} catch (Exception e) {
			if(Config.DISPLAY_LOG) e.printStackTrace();
		}
	}

	 /*------------------------- 백신구동 이후 처리  ----------------------------
     백신의 BackgroundScanActivity, ScanActivity 에서
     검사 진행이 완료 된 이후 결과에 따라 처리 할 수 있습니다.
     백신 requestCode, resultCode 값에 따라서 다음과 같이 구현하실 수 있습니다.
     com.secureland.smartmedic.core.Constants.ROOTING_EXIT_APP - 루팅단말 [인텐트에 rootingexitapp-true로 백신 액티비티를 실행 했을 때]
     com.secureland.smartmedic.core.Constants.ROOTING_YES_OR_NO - 루팅단말 [인텐트에 rootingyesorno-true로 백신 액티비티를 실행 하고 사용자가 yes를 눌렀을 때]
     com.secureland.smartmedic.core.Constants.EMPTY_VIRUS - 악성코드, 루팅여부 모두 정상
     com.secureland.smartmedic.core.Constants.EXIST_VIRUS_CASE1 - 악성코드 탐지 후 사용자가 해당 악성코드 앱을 삭제
     com.secureland.smartmedic.core.Constants.EXIST_VIRUS_CASE2 - 악성코드 탐지 후 사용자가 해당 악성코드 앱을 미삭제
     -------------------------------------------------------------------*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == CommonData.ACTIVITY_MDM_APK_INSTALL) {
			moveActivity();
		} else if (requestCode == 777) {
			if (resultCode == com.secureland.smartmedic.core.Constants.ROOTING_EXIT_APP
					|| resultCode == com.secureland.smartmedic.core.Constants.ROOTING_YES_OR_NO) {
				this.finish();
			} else if (resultCode == com.secureland.smartmedic.core.Constants.EMPTY_VIRUS) {

				HandlerUtils.postDelayed(runNext, 10);

			} else if (resultCode == com.secureland.smartmedic.core.Constants.EXIST_VIRUS_CASE1) {

				HandlerUtils.postDelayed(runNext, 10);

			} else if (resultCode == com.secureland.smartmedic.core.Constants.EXIST_VIRUS_CASE2) {

				finish();
			}
		}
	}
}
