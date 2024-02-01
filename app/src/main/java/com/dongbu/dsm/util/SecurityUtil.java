package com.dongbu.dsm.util;

import android.util.Base64;

import com.dongbu.dsm.Config;
import com.dongbu.dsm.util.aes256.AES256CipherV1;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by landonjung on 2016. 9. 12..
 */
public class SecurityUtil {

    public static String encode(String tx) {
        try {
            return AES256CipherV1.AES_Encode(tx, Config.EN_KEY).trim();
        } catch (UnsupportedEncodingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (InvalidKeyException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (BadPaddingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
        return null;
    }

    public static String decode(String tx) {
        try {
            return AES256CipherV1.AES_Decode(tx, Config.EN_KEY);
        } catch (UnsupportedEncodingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (InvalidKeyException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (BadPaddingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
        return null;
    }

    public static String urlEncode_After_AES256_Base64(String cypherKey, String s) {
        try {
            //byte [] encrypted = AES256CipherV1.AES_Encode(data.toString(), AES256CipherV1.getKey().toString());
            String encrypted = AES256CipherV1.AES_Encode(s, Config.EN_KEY_MDM);
            String enc = Base64.encodeToString(encrypted.getBytes("UTF-8"), 0);
            return URLEncoder.encode(enc, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (InvalidKeyException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        } catch (BadPaddingException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
        return "";
    }
}
