package com.dongbu.dsm.intro;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.AppUtils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.R;
import com.dongbu.dsm.base.IntroBaseActivity;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.CustomAlertDialog;
import com.dongbu.dsm.common.CustomAlertDialogInterface;
import com.dongbu.dsm.common.CustomAsyncListener;
import com.dongbu.dsm.common.NetworkConst;
import com.dongbu.dsm.intro.adapter.PortalListAdapter;
import com.dongbu.dsm.model.api.AppInfos;
import com.dongbu.dsm.network.RequestApi;
import com.dongbu.dsm.util.DSMUtil;
import com.dongbu.dsm.util.JSONUtils;
import com.dongbu.dsm.util.Log;
import com.dongbu.dsm.util.MDMUtils;
import com.dongbu.dsm.util.PopupUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by LandonJung on 2017-10-30.
 * 임직원용 포탈 메인화면 클래스
 * @since 0, 1
 */
public class PortalActivity extends IntroBaseActivity {

    @Bind(R.id.portal_icon_layout)
    RelativeLayout portalIconLayout;

    @Bind(R.id.gridView)
    GridView portalGridView;

    private PortalListAdapter portalListAdapter;
	private AppInfos curItem;

	ArrayList<AppInfos> portalAppList = new ArrayList<AppInfos>();
	boolean bAllListed = true;
    private String NONE = "";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

		setContentView(R.layout.activity_portal_main);
		ButterKnife.bind(this);
		mContext = this;
		commonData.setStep(CommonData.ACTIVITY_PORTAL);
        DSMUtil.BackAnimationFadeStart(PortalActivity.this);
	}

	PortalListAdapter.PortalListListener portalListListener = new PortalListAdapter.PortalListListener() {
		@Override
		public void onItemClick(View view, AppInfos appInfos) {
			curItem = appInfos;
			if(curItem != null) {
				if(view != null) {
					if(view instanceof ImageButton) {
						if(!curItem.isInstalled()) {
							PopupUtils.show(PortalActivity.this, curItem.getAppNm() + "이 설치되어 있지 않습니다.\nMDM 을 실행하여 설치를 먼저 하세요.", dialogClickListener, CommonData.POPUP_TYPE_PORTAL_APP_NOT_INSTALL);
						} else {
							if(curItem.isNeedUpdate()) {
								PopupUtils.show(PortalActivity.this, curItem.getAppNm() + "이 업데이트 되었습니다.\n업데이트를 진행해야만 정상적으로 서비스가 가능 합니다.\nMDM 을 실행하여 업데이트를 진행하세요.", dialogClickListener, CommonData.POPUP_TYPE_PORTAL_APP_NEED_UPDATE);
							} else {
								PopupUtils.show(PortalActivity.this, curItem.getAppNm() + "을 실행하시겠습니까?", dialogClickListener,
										new CustomAlertDialogInterface.OnClickListener() {
											@Override
											public void onClick(CustomAlertDialog dialog, Button button) {
												dialog.dismiss();
											}
										},
										CommonData.POPUP_TYPE_PORTAL_APP_RUN);
							}
						}
					}
				}
			}
		}
	};


	@Override
	public void onBackPressed() {

		hideProgress();
		showPopup(CommonData.POPUP_TYPE_APP_FINISH, CommonData.PARAM_STRING_NONE);
	}

	@Override
	protected void onResume() {
		super.onResume();

		init();
	}

	/**
	 * 초기화
	 */
	public void init(){

		showProgress();

		if(Config.ENABLE_PORTAL_MAIN && Config.ENABLE_PORTAL_MAIN_OFFLINE) {
            String portalAppInfoJSON = JSONUtils.getJsonFromAsset(mContext, "portalApp");
            portalAppList = parseJsonString(portalAppInfoJSON);
            fillDumyItems();
            initPortalIconView();
        } else {
            getFindAppList();
        }
	}

	public void fillDumyItems() {
        if(portalAppList.size() < CommonData.PORTAL_MAIN_LIST_MAX_COUNT) {
            int dumySize = CommonData.PORTAL_MAIN_LIST_MAX_COUNT - portalAppList.size();
            for(int i = 0; i < dumySize; i++) {
                AppInfos item = new AppInfos(true);
                portalAppList.add(item);
            }
        }
    }

    public void checkApps() {
		boolean isExistDSM = false;
		try {
			for(int i = 0; i < portalAppList.size(); i++) {
				AppInfos appInfos = portalAppList.get(i);
				if(appInfos != null) {
					if(appInfos.getAppPackage() == null) Log.d("appInfos.getAppPackage() == null");
					if(AppUtils.getAppPackageName() == null) Log.d("AppUtils.getAppPackageName()");
					if(appInfos.getAppPackage().equals(AppUtils.getAppPackageName())) {
						isExistDSM = true;
						//break;
					}
					if(DSMUtil.isInstalledApp(mContext, appInfos.getAppPackage())) {
						appInfos.setInstalled(true);
						appInfos.setNeedUpdate(DSMUtil.isAppUpdate(AppUtils.getAppVersionName(appInfos.getAppPackage()), appInfos.getAppVer()));
					} else {
						appInfos.setInstalled(false);
					}
				}
			}
			if(!isExistDSM) {
				AppInfos dsmAppInfos = new AppInfos("영업용 모바일", AppUtils.getAppVersionName(), AppUtils.getAppPackageName(), "Android", "", "", "");
				dsmAppInfos.setInstalled(true);
				portalAppList.add(0, dsmAppInfos);

			}
		} catch (Exception e) {
			if(Config.DISPLAY_LOG) e.printStackTrace();
		}
	}

	public void initPortalIconView() {

        portalListAdapter = new PortalListAdapter(this, R.layout.portal_app_item, portalAppList, portalListListener);
        portalGridView.setAdapter(portalListAdapter);

		hideProgress();
    }

	protected ArrayList<AppInfos> parseJsonString(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, new TypeToken<List<AppInfos>>() {
		}.getType());
	}

	public void getFindAppList() {
		if(bAllListed) {
			portalAppList.clear();
			getFindDeployAppList();
		}
	}

	/**
	 * MDM 을 통한 DSM Deploy App List 가져오기
	 */
	public void getFindDeployAppList() {

		bAllListed = false;

		Log.i("[getFindDeployAppList] MDM 을 통한 DSM Deploy App List 가져오기 ");

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(CommonData.JSON_LOGIN_ID, CommonData.getInstance().getMDMUserId()));

		Log.d("MDM UserID = " + CommonData.getInstance().getMDMUserId());

		RequestApi.requestApi(mContext, NetworkConst.NET_MDM_GET_APP_INFO, NetworkConst.getInstance().getMDMAppInfoUrl(), networkListener, params, getProgressDialog());
	}

	/**
	 * MDM 을 통한 DSM Open App List 가져오기
	 */
	public void getFindOpenAppList() {

		bAllListed = true;

		Log.i("[getFindOpenAppList] MDM 을 통한 DSM Open App List 가져오기 ");

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(CommonData.JSON_LOGIN_ID, CommonData.getInstance().getMDMUserId()));

		Log.d("MDM UserID = " + CommonData.getInstance().getMDMUserId());

		RequestApi.requestApi(mContext, NetworkConst.NET_MDM_GET_APP_INFO, NetworkConst.getInstance().getMDMOpenAppInfoUrl(), networkListener, params, getProgressDialog());
	}

	public void addListItem(List<AppInfos> appInfoList) {
        if(appInfoList != null && appInfoList.size() > 0) {
            for (int i = 0; i < appInfoList.size(); i++) {
                AppInfos item = appInfoList.get(i);
                if (item != null) {
					if(bAllListed) {
						item.setAppNm("[공개]" + item.getAppNm());
					} else {
						item.setAppNm("[업무]" + item.getAppNm());
					}
                    portalAppList.add(new AppInfos(item));
                }
            }
        }
    }

	/**
	 * 네트워크 리스너
	 */
	// MDM 내 DSM 앱 버전채크
	public CustomAsyncListener networkListener = new CustomAsyncListener() {

		@Override
		public void onPost(Context context, int type, String resultCode, JSONObject resultData, CustomAlertDialog dialog) {
			try{
				switch ( type ) {
					case NetworkConst.NET_MDM_GET_APP_INFO:							// VersionCheck
						Log.i("MDM 내 DSM 앱 버전채크");
						switch ( resultCode ) {
							case CommonData.API_CODE_NONE:
								try {
									if(resultData.has(CommonData.JSON_MDM_APP_INFOS)) {
										// {"data":{"appInfos":[{"appPackage":"com.mdongbu.mobileeclaim","deviceType":"A","installPlayStore":"false","appVer":"1.2.8","osType":"2","iconFileUrl":"https://mdm4t.mdongbu.com/file/image/icon.do?fileKey=0","fileHash":"47d1ca2c01e57b70a54d3882f1a5059e239900fad2a6e102a019e9ac69ed3ab6","appNm":"보상모바일_AND","appInstallUrl":"https://mdm4t.mdongbu.com/file/app/appStore.do?appStoreKey=47"},{"appPackage":"com.mdongbu.csi","deviceType":"A","installPlayStore":"false","appVer":"1.0.5","osType":"2","iconFileUrl":"https://mdm4t.mdongbu.com/file/image/icon.do?fileKey=0","fileHash":"3a14455a4e5946e967289db8badc6808298e1d05e6070ff36693e7ebeb2dc58d","appNm":"모바일CSI_AND","appInstallUrl":"https://mdm4t.mdongbu.com/file/app/appStore.do?appStoreKey=48"},{"appPackage":"com.dongbu.dbins","deviceType":"A","installPlayStore":"false","appVer":"1.1.4","osType":"2","iconFileUrl":"https://mdm4t.mdongbu.com/file/image/icon.do?fileKey=0","fileHash":"4bfc7f798b5cd943d6c8d47bd405857117e52b62ec12eb0072c832e20b6906e5","appNm":"모바일DBins_AND","appInstallUrl":"https://mdm4t.mdongbu.com/file/app/appStore.do?appStoreKey=49"},{"appPackage":"com.dongbu.mobile.mobileportal","deviceType":"A","installPlayStore":"false","appVer":"1.0.2.1","osType":"2","iconFileUrl":"https://mdm4t.mdongbu.com/file/image/icon.do?fileKey=0","fileHash":"3805d4f23850424547ba637db5a15378ae78eac6600c526217db2ad71edcfca9","appNm":"모바일포탈_AND","appInstallUrl":"https://mdm4t.mdongbu.com/file/app/appStore.do?appStoreKey=51"},{"appPackage":"com.dongbu.mobilepromy","deviceType":"A","installPlayStore":"false","appVer":"1.5.6","osType":"2","iconFileUrl":"https://mdm4t.mdongbu.com/file/image/icon.do?fileKey=0","fileHash":"3dfff4b415acb2350b92dcf0bfeaa92cdbf1141f82b7fb1dcad012812fd66a40","appNm":"프로미카_AND","appInstallUrl":"https://mdm4t.mdongbu.com/file/app/appStore.do?appStoreKey=52"},{"appPackage":"ezq.android.EZQ8_Dongbu","deviceType":"A","installPlayStore":"false","appVer":"1.0.3","osType":"2","iconFileUrl":"https://mdm4t.mdongbu.com/file/image/icon.do?fileKey=0","fileHash":"7d62c2e90db6482a07e1a10681e70ce421775014c92701bc5a420f9d4c1d45d5","appNm":"프로미톡_AND","appInstallUrl":"https://mdm4t.mdongbu.com/file/app/appStore.do?appStoreKey=53"}]},"success":true}
										JSONArray dataArr = resultData.getJSONArray(CommonData.JSON_MDM_APP_INFOS);
										if(dataArr != null) {
											List<AppInfos> itemList = parseJsonString(dataArr.toString());
											addListItem(itemList);
										}
									} else if(resultData.has(CommonData.JSON_MDM_APP_INFOS_ERROR)) {
										// {"data":{"error":"UNKNOWN_ERROR","message":"알수 없는 에러"},"success":false}
									}

									if(bAllListed) {
										checkApps();
										fillDumyItems();
										initPortalIconView();
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												hideProgress();
											}
										});

									} else {
										getFindOpenAppList();
									}

									break;
								}catch(Exception e){
									Log.e(e.toString());
								}
						}
						break;
				}

			}catch (Exception e) {
				if(Config.DISPLAY_LOG) e.printStackTrace();
				hideProgress();

			}

			//hideProgress();
		}

		@Override
		public void onNetworkError(Context context, int type, int httpResultCode, CustomAlertDialog dialog) {

			hideProgress();
			dialog.show();
		}

		@Override
		public void onDataError(Context context, int type, String resultData, CustomAlertDialog dialog) {

			// 데이터에 문제가 있는 경우 다이얼로그를 띄우고 인트로에서는 종료하도록 한다.
			hideProgress();
			dialog.show();

		}
	};

	/**
	 * 팝업 다이얼로그 클릭 리스너
	 */
	public CustomAlertDialogInterface.OnClickListener dialogClickListener = new CustomAlertDialogInterface.OnClickListener() {

		@Override
		public void onClick(CustomAlertDialog dialog, Button button) {

			dialog.dismiss();
			//showProgress();
			Intent 	intent = null;
			ApplicationInfo appInfo = null;
			switch ( dialog.getId() ) {
				case CommonData.POPUP_TYPE_PORTAL_APP_RUN:				        						// 앱 실행
					if(curItem != null) {
						if(curItem.getAppPackage().equals(AppUtils.getAppPackageName())) {
							moveActivity();
						} else {
							if(curItem.getAppPackage().contains(CommonData.APP_CALL_ID_PK_TEST)) {
								startActivity(CommonData.ACTIVITY_PK_TEST, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE, CommonData.PARAM_VALUE_NONE);
							} else {
								launchPortalApp(curItem.getAppPackage());
							}

						}
					}
					break;
				case CommonData.POPUP_TYPE_PORTAL_APP_NOT_INSTALL:				        				// 앱 미 설치
				case CommonData.POPUP_TYPE_PORTAL_APP_NEED_UPDATE:										// 앱 업데이트
					MDMUtils.getInstance(mContext, PortalActivity.this).agentLoginCall();
					hideProgress();
					break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

    @Override
    public void finish() {
        DSMUtil.BackAnimationFadeStart(PortalActivity.this);
        super.finish();
    }

}
