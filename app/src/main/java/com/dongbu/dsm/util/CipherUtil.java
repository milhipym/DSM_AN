package com.dongbu.dsm.util;

import com.dongbu.dsm.util.aes128.AES128Cipher;
import com.dongbu.dsm.util.aes256.AES256Cipher;
import com.dongbu.dsm.util.seed.SEED128Cipher;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;

import org.json.JSONObject;



/**
 * 암호화 유틸
 * @author 안정진.
 * AES 128  
 * AES 256
 * SEED 128
 */
public class CipherUtil 
{
	
	/** AES128 */
	public static final String CYPHER_TYPE_AES128 = "A";
	
	/** AES256 */
	public static final String CYPHER_TYPE_AES256 = "A2";
	
	/** SEED */
	public static final String CYPHER_TYPE_SEED = "S";

	
	// ---------------------------- 
	// AES128 
	// ----------------------------

	/**  AES암호화 -> base64 -> urlEncode한 값을 가져온다. */
	public static String urlEncode_After_AES128_Base64(String sCypherKey,String s)
	{
		try 
		{
			String enc = AES128Cipher.encryptBase64Format(sCypherKey,s);
			return URLEncoder.encode(enc,"UTF-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			return "";
		}
	}
	
	/**  urlDecode -> base64 Decod ->  AES복호화 한 값을 가져온다. */
	public static String urlDecode_After_AES128_Base64(String sCypherKey,String s)
	{
		try 
		{
			String dec = AES128Cipher.decryptBase64Format(sCypherKey,s);			
			return URLDecoder.decode(dec,"UTF-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			return "";
		}
	}
	
	// ---------------------------- 
	// AES256
	// ----------------------------
		
	/**  AES256 암호화 -> base64 -> urlEncode한 값을 가져온다. */
	public static String urlEncode_After_AES256_Base64(String cypherKey,String s)
	{
		try 
		{
	 		String enc = AES256Cipher.encryptBase64Format(cypherKey, s);
	 		
			return URLEncoder.encode(enc,"UTF-8");
		} 
		catch (Exception e) 
		{
			return "";
		} 
	}
	
	/**  urlDecode -> base64 Decod -> AES256 복호화 한한 값을 가져온다. */
	public static String urlDecode_After_AES256_Base64(String cypherKey,String s)
	{
		try 
		{
	 		String dec = AES256Cipher.decryptBase64Format(cypherKey, s);
	 		
	 		return URLDecoder.decode(dec,"UTF-8");
		} 
		catch (Exception e) 
		{
			return "";
		} 
	}

	// ---------------------------- 
	// SEED128. CBC 모드. PKCS#5 패딩
	// ----------------------------
	
	/**  Seed128 암호화 -> base64 -> urlEncode한 값을 가져온다. */
	public static String urlEncode_After_Seed_Base64(String sCypherKey,String s)
	{
		try 
		{
	 		String enc = SEED128Cipher.encryptBase64Format(sCypherKey,s);
	 		
			return URLEncoder.encode(enc,"UTF-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			return "";
		}
	}
	
	/**  urlDecode -> base64 Decod -> Seed128 복호화한 값을 가져온다. */
	public static String urlDecode_After_Seed_Base64(String sCypherKey,String s)
	{
		try 
		{
	 		String dec = SEED128Cipher.decryptBase64Format(sCypherKey,s);
	 		
	 		return URLDecoder.decode(dec,"UTF-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			return "";
		}
	}
	
	
	/** map 에 잇는 데이타를 암호화 -> base64 -> urlEncode한 값을 param 형식으로 만든다.*/
	public static String buildUrl_CypherEnc_Base64_Param(String cypherType,JSONObject obj,String CypherKey) throws Exception
	{
		StringBuilder sbResult = new StringBuilder();
		if(obj != null)
		{
			if(cypherType.equals(CYPHER_TYPE_AES128))
				sbResult.append("cypher_Type=" + CYPHER_TYPE_AES128);
			else if(cypherType.equals(CYPHER_TYPE_AES256))
				sbResult.append("cypher_Type=" + CYPHER_TYPE_AES256);
			else
				sbResult.append("cypher_Type=" + CYPHER_TYPE_SEED);
			sbResult.append("&");
			
			Iterator < String >	keys = obj.keys();   //map.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				String keyVaule = obj.getString(key);
				
				if(cypherType.equals(CYPHER_TYPE_AES128))
					keyVaule = urlEncode_After_AES128_Base64(CypherKey,keyVaule);
				else if(cypherType.equals(CYPHER_TYPE_AES256))
					keyVaule = urlEncode_After_AES256_Base64(CypherKey,keyVaule);
				else
					keyVaule = urlEncode_After_Seed_Base64(CypherKey,keyVaule);
				
				sbResult.append(key + "=" + keyVaule);	
				sbResult.append("&");
			}
			int len = sbResult.length();
			
			if(len > 0)
				sbResult.delete(len-1, len);
		}

		return sbResult.toString();
	}

}
