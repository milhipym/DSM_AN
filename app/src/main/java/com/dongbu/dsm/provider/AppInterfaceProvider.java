package com.dongbu.dsm.provider;

import java.util.Iterator;

import org.json.JSONObject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.SessionMan;
import com.dongbu.dsm.util.Log;

/**
 * 앱 인터페이스 프로바이더
 *
 */
public class AppInterfaceProvider extends ContentProvider
{

	@Override
	public boolean onCreate() 
	{
		return false;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) 
	{

		return 0;
	}

	@Override
	public String getType(Uri uri) 
	{
		return "";
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) 
	{		
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) 
	{
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,String[] selectionArgs) 
	{
		return 0;
	}

	public boolean createSession(StringBuilder sbError) {
		if(!SessionMan.createSession(getContext(), sbError)) {
			Log.e("Session 생성 실패 : " + sbError.toString());
			return false;
		}
		return true;
	}
	
	/**
	 * 함수 호출. 단 안드로이드 OS 3.0 이상에서만 지원된다.
	 */
	public Bundle call(String method,String arg,Bundle extras)
	{
		Bundle bundle = new Bundle();
		StringBuilder sbError = new StringBuilder();
		
		boolean bRet = false;
		String result = "";
		String errMsg = "";
		
		try
		{
			if(method== null)
				throw new Exception("메서드가 없습니다");
			
			// 메서드가 콜미이면
			if(method.equals("callMe"))
			{
				callMe(extras);
				bRet = true;	
			}
			else if(method.equals("createSession")) {
				if(!SessionMan.createSession(getContext(), sbError)) {
					Log.e("Session 생성 실패 : " + sbError.toString());
					bRet = false;
				} else{
					bRet = true;
				}
			}
			// 메서드가 세션 밸리드이면
			else if(method.equals("isSessionValid"))
			{
				String session_Id = CommonData.EXTRA_APP_DEFAULT_SESSION_ID; //extras.getString("session_Id");
				if(session_Id == null || session_Id.equals(""))
				{
					throw new Exception("세션 아이디가 없습니다.");
				} else if(!SessionMan.isInitSession()) {
					throw new Exception("세션이 초기화되지 않았습니다.");
				}
				else
				{
					if(arg.equals("noTimeUpdate")) {
						if(SessionMan.isSessionValidJustTimeCheck(getContext(), session_Id, sbError))
							bRet = true;

					} else {
						if(SessionMan.isSessionValid(getContext(), session_Id, sbError))
							bRet = true;
					}
				}
			}
			// 메서드가 로그인이면
			else if(method.equals("isLogin"))
			{
				bRet = SessionMan.isbLogin();	
			}
		}
		catch(Exception e)
		{
			sbError.append(e.getMessage());
		}
		finally
		{
			if(bRet)
			{
				result = "Y";
				errMsg = "";
			}
			else
			{
				result = "N";
				errMsg = sbError.toString();
			}
			
			bundle.putString("result", result);
			bundle.putString("errMsg", errMsg);
		}
		
		return bundle;
	}
	
	private void callMe(Bundle extras) throws Exception 
	{
		String package_Nm = extras.getString("package_Nm");
		if(package_Nm.equals("com.mdongbu.csi"))
		{ 
			JSONObject jsonObj = new JSONObject();
			
			jsonObj.put(CommonData.JSON_APP_CALL_EXTERNAL_PACKAGE_NAME, package_Nm);
			
			Iterator< String >	keys = extras.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				jsonObj.put(key, extras.get( key ).toString());
			}
						
			String param = jsonObj.toString();
			
			Intent intent = getContext().getPackageManager().getLaunchIntentForPackage("com.dongbu.dsm");
	 		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );

	 		Bundle bundleData = new Bundle();
	 		bundleData.putSerializable(CommonData.JSON_APP_CALL_EXTERNAL_METHOD, CommonData.JSON_APP_CALL_EXTERNAL_CALL_APP);
	 		bundleData.putSerializable(CommonData.JSON_APP_CALL_EXTERNAL_PARAM, param);
	 		
	 		intent.putExtra(CommonData.JSON_APP_CALL_EXTERNAL_BUNDLE_DATA, bundleData);
	 		
	 		getContext().startActivity(intent);
		}
	}

}

