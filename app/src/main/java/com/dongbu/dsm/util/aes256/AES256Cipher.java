package com.dongbu.dsm.util.aes256;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

//고급 암호화 표준(AES, Advanced Encryption Standard)
//암호화와 복호화 과정에서 동일한 키를 사용하는 대칭 키 알고리즘
public class AES256Cipher {
 
	 private static volatile AES256Cipher INSTANCE;
	 
	 static String cypherKey = "aes256-dongbu-sales-mobuile-key-"; //= "DongbuMobileDM15880100!qaz2wsx3e"; //32bit
	 String IV        = "DongbuMobile!@#$"; //16bit
	  
	 public static AES256Cipher getInstance(String cypherKey)
	 {
	     if(INSTANCE==null)
	     {
	         synchronized(AES256Cipher.class)
	         {
	             if(INSTANCE==null)
	                 INSTANCE=new AES256Cipher(cypherKey);
	         }
	     }
	     return INSTANCE;
	 }

	 public static AES256Cipher getInstance()
	 {
	     if(INSTANCE==null)
	     {
	         synchronized(AES256Cipher.class)
	         {
	             if(INSTANCE==null)
	                 INSTANCE=new AES256Cipher(cypherKey);
	         }
	     }
	     return INSTANCE;
	 }

	 private AES256Cipher(String cypherKey)
	 {
		 this.cypherKey = cypherKey;
		 this.IV        = cypherKey.substring(0,16);
	 }

	private AES256Cipher()
	{
	}

	
	/** 암호화해서 base64로 저장한다. */
	public static String encryptBase64Format(String cypherKey,String s)
	{
   	 	try
		{
   	 	    byte [] data = s.getBytes("utf-8");
			byte [] encrypted = AES256Cipher.getInstance(cypherKey).encrypt(data);
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
			byte [] bytesDec = Base64.decode(s, 0);
			byte [] decrypted = AES256Cipher.getInstance(cypherKey).decrypt(bytesDec);
			
			return new String(decrypted,"UTF-8");
		}
		catch(Exception ex)
		{
			return "";
		}
	}	
 
  //암호화
  public byte[] encrypt(byte[] data)
		  throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
  {
      byte[] keyData = cypherKey.getBytes("UTF-8");
  
      SecretKey secureKey = new SecretKeySpec(keyData, "AES");
   
      Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
      c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));
   
      byte[] encrypted = c.doFinal(data);
      return encrypted;
  }
 
  //복호화
  public byte[] decrypt(byte[] data)
		  throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
 {
	  byte[] keyData = cypherKey.getBytes("UTF-8");
	
	  SecretKey secureKey = new SecretKeySpec(keyData, "AES");
	  Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
	  c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes("UTF-8")));
	   
	  byte[] decrypted = c.doFinal(data);
	   
	  return decrypted;
  }

}

