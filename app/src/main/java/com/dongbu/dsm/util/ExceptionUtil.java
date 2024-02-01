package com.dongbu.dsm.util;


public class ExceptionUtil 
{
	/**
	 * 에러메시지를 만든다.
	 * @param errMsg 에러 메시지
	 * @param e 익셉션
	 * @return
	 */
	public static String buildMessage(String errMsg,Exception e)
    {
	     return errMsg + "(" + e.getMessage() + ")";
    }
	
	/**
	 * 에러 메시지를 만든다.
	 * @param errMsg  에러 메시지
	 * @param detailMsg 에러 세부 메시지
	 * @return
	 */
	public static String buildMessage(String errMsg,String detailMsg)
    {		
		return errMsg + "(" + detailMsg + ")";
		
    }
	
}
