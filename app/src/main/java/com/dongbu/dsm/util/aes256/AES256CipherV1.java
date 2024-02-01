package com.dongbu.dsm.util.aes256;

import android.util.Base64;

import com.dongbu.dsm.Config;
import com.dongbu.dsm.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
//import kr.co.welcomebank.nf.util.Base64;

public class AES256CipherV1 {
	
	//public static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
	// 암호화 키
	public static final String key = "aes256-dongbu-sales-mobuile-key-";
	public static String iv = "dongbumobile!@#$";

	public static byte[] getKey() {
		String key = "DONGBU_DSM_0911";
		SecretKeySpec keySpec = null;
		try {
			keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		} catch (UnsupportedEncodingException e) {
			if(Config.DISPLAY_LOG) e.printStackTrace();
		}
		return keySpec.getEncoded();
	}

	public static String getKeyForMDM() {
		String key = "123456789012";
		return key;
	}

	public static String getEnKey() {
		return key;
	}

	public static String AES_Base64_Encode(String str, String key)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		byte[] textBytes = str.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
		     SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		     Cipher cipher = null;
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

		Log.i("--> " + cipher.doFinal(textBytes));
		return Base64.encodeToString(cipher.doFinal(textBytes), Base64.NO_WRAP);
	}

	public static String AES_Base64_Decode(String str, String key)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		byte[] textBytes = Base64.decode(str, Base64.NO_WRAP);
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
		SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
		return new String(cipher.doFinal(textBytes), "UTF-8");
	}

	public static String AES_Encode(String str, String key)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		byte[] textBytes = str.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
		SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		Cipher cipher = null;
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

		return new String(cipher.doFinal(textBytes), "UTF-8");
	}

	public static String AES_Decode(String str, String key)	throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		byte[] textBytes = str.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
		SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
		return new String(cipher.doFinal(textBytes), "UTF-8");
	}

}