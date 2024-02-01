package com.dongbu.dsm.util.aes128;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class AES128Cipher 
{
	
	/** 암호화해서 base64로 저장한다. */
	public static String encryptBase64Format(String cypherKey,String s)
	{
   	 	try
		{
			byte [] encrypted = encrypt(cypherKey,s);
	   	 	return Base64.encodeToString(encrypted, 0);
		}
		catch(Exception ex)
		{
			return "";
		}
	}
	
	/** base64로 저장된 스트링을 복호화한다. */
	public static String decryptBase64Format(String cypherKey,String s)
	{
		try
		{
			byte [] bytesEnc = Base64.decode(s, 0);
			byte[] bytesDec = decrypt(cypherKey,bytesEnc);
			return new String(bytesDec,0,bytesDec.length,"UTF-8");
		}
		catch(Exception ex)
		{
			return "";
		}
	}
	
	/** 암호화한다. */
	public static byte[] encrypt(String cypherKey,String s)
    {
	   	try
	   	{
		    Key secureKey = new SecretKeySpec(cypherKey.getBytes(),"AES");
		         
		    Cipher cipher = Cipher.getInstance("AES");
		    cipher.init(Cipher.ENCRYPT_MODE, secureKey);
		    byte [] encryptedData=cipher.doFinal(s.getBytes());
		         
		    return encryptedData;
	   	}
	   	catch(Exception ex)
	   	{
	   		 return null;
	   	}
    }
    
	/** 복호화한다. */
	public static byte[] decrypt(String cypherKey,byte[] b)
    {
	   	try
	   	{
		    Key secureKey = new SecretKeySpec(cypherKey.getBytes(),"AES");
		         
		    Cipher cipher = Cipher.getInstance("AES");
		    cipher.init(Cipher.DECRYPT_MODE, secureKey);
		               
		    byte [] plainData=cipher.doFinal(b);
		         
		    return plainData;
	   	}
	   	catch(Exception ex)
	   	{
	   		 return null;
	   	}
    }
}
