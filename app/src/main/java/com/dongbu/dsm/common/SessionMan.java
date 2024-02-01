package com.dongbu.dsm.common;

import java.util.Random;


import android.content.Context;
import android.content.SharedPreferences;

import com.dongbu.dsm.util.ExceptionUtil;

/**
 * 세션 매니저
  *
 */
public class SessionMan 
{
	// 세션 체크 시간. 단위 분
	public static int sessionCheckTime = CommonData.LOGOUT_MAX_TIME_MIN;
	
	// 세션 프리퍼런스
	private static String prefNm = "Session";
	private static String prefKey_Session_Id         = "Session_Id";
	private static String prefKey_Session_CreateTime = "Session_CreateTime";
	private static String prefKey_Session_AccessTime = "Session_AccessTime";
	
	// 로그인 여부
	private static boolean bLogin = false;

	public static boolean isInitSession() {
		return bInitSession;
	}

	private static boolean bInitSession = false;
	
    /** 로그인여부를 가져온다. */
	public static boolean isbLogin() {
		return bLogin;
	}

	/** 로그인여부를 세팅한다. */
	public static void setbLogin(boolean bLogin) {
		SessionMan.bLogin = bLogin;
	}

	/** 세션시간을 세팅한다. */
	public static void setSessionTime(int sessionTime)
	{
		sessionCheckTime = sessionTime;
	}
	
	/** 세션을 생성한다. */
	public static boolean createSession(Context context,StringBuilder sbError)
	{
		try
		{
			// 1. 세션 생성
			//String sessionId = makeSessionId();
			String sessionId = CommonData.EXTRA_APP_DEFAULT_SESSION_ID;
			
			// 2. 세션 저장
			SharedPreferences pref = 
				context.getSharedPreferences(prefNm,Context.MODE_PRIVATE); 
			if(pref != null)
			{
				SharedPreferences.Editor ed = pref.edit(); 
				
				long currentTime = System.currentTimeMillis();

				ed.putString(prefKey_Session_Id,sessionId);
				ed.putLong(prefKey_Session_CreateTime,currentTime);
				ed.putLong(prefKey_Session_AccessTime,currentTime);
				
				ed.commit();
			}
			bInitSession = true;
			return true;
		}
		catch(Exception e)
		{
			sbError.append(ExceptionUtil.buildMessage("세션 생성 실패", e));
			return false;
		}
	}
	
	/** 세션 id를  생성한다.(랜덤8자리) */
	private static String makeSessionId()
	{
		Random r = new Random();
		int n = r.nextInt(899999) + 100000;
		return Integer.toString(n);
	}
	
	/** 세션 id를  가져온다.*/
	public static String getSessionId(Context context)
	{
		String sessionId = "";
		SharedPreferences pref = context.getSharedPreferences(prefNm,Context.MODE_PRIVATE); 
		if(pref != null)
		{
			sessionId = pref.getString(prefKey_Session_Id, "");
		}
		
		return sessionId;
	}
	
	/** 세션이 유효한지 체크한다. */
	public static boolean isSessionValid(Context context,String sessionId,StringBuilder sbError)
	{
		
		SharedPreferences pref = 
				context.getSharedPreferences(prefNm,Context.MODE_PRIVATE); 
		if(pref != null)
		{
			String saved_sessionId = pref.getString(prefKey_Session_Id, "");
			long saved_sessionAccessTime = pref.getLong(prefKey_Session_AccessTime, 0);
			
			if(!sessionId.equals(saved_sessionId))
			{
				sbError.append(ExceptionUtil.buildMessage("세션 유효하지 않음", "요청한 세션아이디는 설정되어 있지않습니다."));
				// 시간을 초기화한다.
				SharedPreferences.Editor ed = pref.edit();
				ed.putLong(prefKey_Session_AccessTime,0);
				ed.commit();
				return false;
			}
			
			long currentTime = System.currentTimeMillis();
			// 세션 시간 초과라면
			if(currentTime - saved_sessionAccessTime > sessionCheckTime * 60 * 1000 )
			{
				sbError.append(ExceptionUtil.buildMessage("세션 유효하지 않음", "세션 시간이 초과되었습니다."));

				// 시간을 초기화한다.
				SharedPreferences.Editor ed = pref.edit();
				ed.putLong(prefKey_Session_AccessTime,0);
				ed.commit();

				return false;
			}
			// 세션 시간이 초과되어있지않다면
			else
			{
				// 현재 시간을 저장한다.
				SharedPreferences.Editor ed = pref.edit();
				ed.putLong(prefKey_Session_AccessTime,currentTime);
				ed.commit();
				return true;
			}
		}
		
		return false;
	}

	/** 세션이 유효한지 체크한다. */
	public static boolean isSessionValidJustTimeCheck(Context context,String sessionId,StringBuilder sbError)
	{

		SharedPreferences pref =
				context.getSharedPreferences(prefNm,Context.MODE_PRIVATE);
		if(pref != null)
		{
			String saved_sessionId = pref.getString(prefKey_Session_Id, "");
			long saved_sessionAccessTime = pref.getLong(prefKey_Session_AccessTime, 0);

			if(!sessionId.equals(saved_sessionId))
			{
				sbError.append(ExceptionUtil.buildMessage("세션 유효하지 않음", "요청한 세션아이디는 설정되어 있지않습니다."));
				// 시간을 초기화한다.
				SharedPreferences.Editor ed = pref.edit();
				ed.putLong(prefKey_Session_AccessTime,0);
				ed.commit();
				return false;
			}

			long currentTime = System.currentTimeMillis();
			// 세션 시간 초과라면
			if(currentTime - saved_sessionAccessTime > sessionCheckTime * 60 * 1000 )
			{
				sbError.append(ExceptionUtil.buildMessage("세션 유효하지 않음", "세션 시간이 초과되었습니다."));

				// 시간을 초기화한다.
				SharedPreferences.Editor ed = pref.edit();
				ed.putLong(prefKey_Session_AccessTime,0);
				ed.commit();

				return false;
			}
//			// 세션 시간이 초과되어있지않다면
//			else
//			{
//				// 현재 시간을 저장한다.
//				SharedPreferences.Editor ed = pref.edit();
//				ed.putLong(prefKey_Session_AccessTime,currentTime);
//				ed.commit();
//				return true;
//			}
			return true;
		}

		return false;
	}
	
	/** 세션을 삭제한다. */
	public static boolean deleteSession(Context context,StringBuilder sbError)
	{
		try
		{
			// 세션 지움
			SharedPreferences pref = 
				context.getSharedPreferences(prefNm,Context.MODE_PRIVATE); 
			if(pref != null)
			{
				SharedPreferences.Editor ed = pref.edit(); 
				
				ed.putString(prefKey_Session_Id,"");
				ed.putLong(prefKey_Session_CreateTime,0);
				ed.putLong(prefKey_Session_AccessTime,0);
				
				ed.commit();
			}
			
			return true;
		}
		catch(Exception e)
		{
			sbError.append(ExceptionUtil.buildMessage("세션 삭제 실패", e));
			return false;
		}
	}
			
}
