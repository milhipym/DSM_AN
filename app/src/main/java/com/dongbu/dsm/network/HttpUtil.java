package com.dongbu.dsm.network;

import android.net.http.Headers;
import android.os.Build;
import android.webkit.CookieSyncManager;

import com.blankj.utilcode.util.CacheUtils;
import com.dongbu.dsm.Config;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.common.NetworkConst;
import com.dongbu.dsm.util.Log;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cz.msebera.android.httpclient.client.protocol.HttpClientContext;

/**
 * Created by LandonJung on 2017-8-10.
 * 네트워크 클래스
 * @since 0, 1
 */
public class HttpUtil {

    public int responseResultCode = -1;

    public String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";

    /**
     * 이미지 업로드 메소드 ( 여러장 )
     * @param urlString (도메인)
     * @param photoList (업로드 할 파일 절대 경로, 순서)
     * @return
     */
    public String HttpFileUploads(String urlString, HashMap<String, String> photoList)
    {
        String returnString = null;
        HttpURLConnection httpUrlConnection = null;
        try
        {
            DataOutputStream dos = null;
            int fileSize = photoList.size();

            URL mConnectUrl			=	new URL(urlString);


            if (mConnectUrl.getProtocol().toLowerCase().equals("https")) {

                trustAllHosts();
                HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) mConnectUrl.openConnection();
                httpsUrlConnection.setHostnameVerifier(DO_NOT_VERIFY);
                httpUrlConnection = httpsUrlConnection;

            } else {
                httpUrlConnection = (HttpURLConnection) mConnectUrl.openConnection();
            }

            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            dos = new DataOutputStream(httpUrlConnection.getOutputStream());
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"member_id\"\r\n\r\n" + CommonData.getInstance().getMDMUserId());
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"session_code\"\r\n\r\n" + CommonData.getInstance().getSessionCode());
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"device_type\"\r\n\r\n" + "A");
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"app_ver\"\r\n\r\n" + CommonData.getInstance().getAppVer());
            dos.writeBytes("\r\n--" + boundary + "\r\n");

            Iterator<String> iterator = photoList.keySet().iterator();

            while ( iterator.hasNext() ) {

                String key 		= (String) iterator.next();
                String photo 	= photoList.get(key);

                if ( !photo.startsWith("/") ) {
                    dos.writeBytes("\r\n--" + boundary + "\r\n");
//                    dos.writeBytes("Content-Disposition: form-data; name=\"photo_" + key + "\"\r\n\r\n" + photo);
                    dos.writeBytes("Content-Disposition: form-data; name=\"photo \"\r\n\r\n" + photo);
                }

            }

            iterator = photoList.keySet().iterator();

            while ( iterator.hasNext() ) {

                String key = (String) iterator.next();
                String photo 	= photoList.get(key);

                if ( photo.startsWith("/") ) {

                    FileInputStream mFileInputStream	=	new FileInputStream( photo );

                    // 파일 업로드
                    dos.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
//                    dos.writeBytes("Content-Disposition: form-data; name=\"photo_" + key + "\";filename=\"" + photo + "\"" + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"photo[]\";filename=\"" + photo + "\"" + lineEnd);
                    dos.writeBytes("Content-Type: application/octet-stream" + lineEnd + lineEnd);

                    int bytesAvailable = mFileInputStream.available();
                    int maxBufferSize = 1024;
                    int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    byte[] buffer = new byte[bufferSize];
                    int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        DataOutputStream dataWrite = new DataOutputStream(httpUrlConnection.getOutputStream());
                        dataWrite.write(buffer, 0, bufferSize);
                        bytesAvailable = mFileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
                    }
                    mFileInputStream.close();

                }
            }
            dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);

            dos.flush();

            //get response
            responseResultCode = httpUrlConnection.getResponseCode();
            int ch;
            InputStream is = httpUrlConnection.getInputStream();
            StringBuffer b =new StringBuffer();

            while ( ( ch = is.read() ) != -1 ) {
                b.append( (char)ch );
            }

            returnString=b.toString();
            dos.close();
        }
        catch (Exception e)	{
            Log.e(e.toString());
        }
        finally {
            if( httpUrlConnection != null){
                httpUrlConnection.disconnect();
            }
        }

        return returnString.toString();
    }

    /**
     * 이미지 업로드 메소드 ( 여러장 )
     * @param urlString (도메인)
     * @param photoList (업로드 할 파일 절대 경로, 순서)
     * @return
     */
    public String HttpFileUploadsForPapers(String urlString, ArrayList<String> photoList)
    {
        String returnString = null;
        HttpURLConnection httpUrlConnection = null;
        try
        {
            DataOutputStream dos = null;
            int fileSize = photoList.size();

            URL mConnectUrl			=	new URL(urlString);


            if (mConnectUrl.getProtocol().toLowerCase().equals("https")) {

                trustAllHosts();
                HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) mConnectUrl.openConnection();
                httpsUrlConnection.setHostnameVerifier(DO_NOT_VERIFY);
                httpUrlConnection = httpsUrlConnection;

            } else {
                httpUrlConnection = (HttpURLConnection) mConnectUrl.openConnection();
            }

            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            JSONObject jsonObject = CacheUtils.getInstance().getJSONObject(CommonData.JS_DO_PAPER_CALL_PARAM);
            if(jsonObject == null) {
                return "jsonObject == null";
            }

            dos = new DataOutputStream(httpUrlConnection.getOutputStream());
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"" + CommonData.JS_DO_PAPER_CALL_PARAM_UPLOAD_TYPE + "\"\r\n\r\n" + jsonObject.get(CommonData.JS_DO_PAPER_CALL_PARAM_UPLOAD_TYPE));

            for(int i=0; i<photoList.size(); i++) {

                dos.writeBytes("\r\n--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"" + CommonData.JS_DO_PAPER_CALL_PARAM_APDXFLID + (i) + "\"\r\n\r\n" + jsonObject.get(CommonData.JS_DO_PAPER_CALL_PARAM_APDXFLID));
                dos.writeBytes("\r\n--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"" + CommonData.JS_DO_PAPER_CALL_PARAM_FLDVCD + (i) + "\"\r\n\r\n" + jsonObject.get(CommonData.JS_DO_PAPER_CALL_PARAM_FLDVCD));
                dos.writeBytes("\r\n--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"" + CommonData.JS_DO_PAPER_CALL_PARAM_BZDVCD + (i) + "\"\r\n\r\n" + jsonObject.get(CommonData.JS_DO_PAPER_CALL_PARAM_BZDVCD));
                dos.writeBytes("\r\n--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"" + CommonData.JS_DO_PAPER_CALL_PARAM_SQNO + (i) + "\"\r\n\r\n" + jsonObject.get(CommonData.JS_DO_PAPER_CALL_PARAM_SQNO));
                //dos.writeBytes("\r\n--" + boundary + "\r\n");
            }

            for(int i=0; i<photoList.size(); i++){
                String photo 	= photoList.get(i);
                Log.d(photo);
                if ( photo.startsWith("/") ) {

                    FileInputStream mFileInputStream	=	new FileInputStream( photo );

                    // 파일 업로드
                    dos.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
//                    dos.writeBytes("Content-Disposition: form-data; name=\"photo_" + key + "\";filename=\"" + photo + "\"" + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + CommonData.JS_DO_PAPER_CALL_PARAM_UPLOAD_FILE + (i) + "\";filename=\"" + photo + "\"" + lineEnd);
                    dos.writeBytes("Content-Type: application/octet-stream" + lineEnd + lineEnd);

                    int bytesAvailable = mFileInputStream.available();
                    int maxBufferSize = 1024;
                    int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    byte[] buffer = new byte[bufferSize];
                    int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        DataOutputStream dataWrite = new DataOutputStream(httpUrlConnection.getOutputStream());
                        dataWrite.write(buffer, 0, bufferSize);
                        bytesAvailable = mFileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
                    }
                    mFileInputStream.close();
                    Log.d("multipart made");
                }
            }

            dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);
            Log.d("DOS = " + dos.toString());
            dos.flush();

            //get response
            responseResultCode = httpUrlConnection.getResponseCode();
            int ch;
            InputStream is = httpUrlConnection.getInputStream();
            StringBuffer b =new StringBuffer();

            while ( ( ch = is.read() ) != -1 ) {
                b.append( (char)ch );
            }

            returnString=b.toString();
            dos.close();
        }
        catch (Exception e)	{
            Log.e(e.toString());
        }
        finally {
            if( httpUrlConnection != null){
                httpUrlConnection.disconnect();
            }
        }

        return returnString.toString();
    }

    /**
     * 이미지 업로드 메소드 ( 여러장 )
     * @param urlString (도메인)
     * @param photoList (업로드 할 파일 절대 경로, 순서)
     * @return
     */
    public String HttpFileUploads(String urlString, ArrayList<String> photoList)
    {
        String returnString = null;
        HttpURLConnection httpUrlConnection = null;
        try
        {
            DataOutputStream dos = null;
            int fileSize = photoList.size();

            URL mConnectUrl			=	new URL(urlString);


            if (mConnectUrl.getProtocol().toLowerCase().equals("https")) {

                trustAllHosts();
                HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) mConnectUrl.openConnection();
                httpsUrlConnection.setHostnameVerifier(DO_NOT_VERIFY);
                httpUrlConnection = httpsUrlConnection;

            } else {
                httpUrlConnection = (HttpURLConnection) mConnectUrl.openConnection();
            }

            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            dos = new DataOutputStream(httpUrlConnection.getOutputStream());
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadType\"\r\n\r\n" + "B");
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"flDvcd0\"\r\n\r\n" + "I");
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"bzDvcd0\"\r\n\r\n" + "Z");
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"sqno\"\r\n\r\n" + CommonData.getInstance().getAppVer());
            dos.writeBytes("\r\n--" + boundary + "\r\n");

            for(int i=0; i<photoList.size(); i++){
                String photo 	= photoList.get(i);
                if ( photo.startsWith("/") ) {

                    FileInputStream mFileInputStream	=	new FileInputStream( photo );

                    // 파일 업로드
                    dos.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
//                    dos.writeBytes("Content-Disposition: form-data; name=\"photo_" + key + "\";filename=\"" + photo + "\"" + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"photo[]\";filename=\"" + photo + "\"" + lineEnd);
                    dos.writeBytes("Content-Type: application/octet-stream" + lineEnd + lineEnd);

                    int bytesAvailable = mFileInputStream.available();
                    int maxBufferSize = 1024;
                    int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    byte[] buffer = new byte[bufferSize];
                    int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        DataOutputStream dataWrite = new DataOutputStream(httpUrlConnection.getOutputStream());
                        dataWrite.write(buffer, 0, bufferSize);
                        bytesAvailable = mFileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
                    }
                    mFileInputStream.close();

                }
            }

            dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);

            dos.flush();

            //get response
            responseResultCode = httpUrlConnection.getResponseCode();
            int ch;
            InputStream is = httpUrlConnection.getInputStream();
            StringBuffer b =new StringBuffer();

            while ( ( ch = is.read() ) != -1 ) {
                b.append( (char)ch );
            }

            returnString=b.toString();
            dos.close();
        }
        catch (Exception e)	{
            Log.e(e.toString());
        }
        finally {
            if( httpUrlConnection != null){
                httpUrlConnection.disconnect();
            }
        }

        return returnString.toString();
    }

    /**
     * 이미지 업로드 메소드 ( 한장 )
     * @param urlString (도메인)
     * @param photoList (업로드 할 파일 절대 경로)
     * @return
     */
    public String HttpFileUpload(String urlString, HashMap<String, String> photoList) {
        String returnString = null;
        HttpURLConnection httpUrlConnection = null;
        try
        {
            DataOutputStream dos = null;
            int fileSize = photoList.size();

            URL mConnectUrl			=	new URL(urlString);


            if (mConnectUrl.getProtocol().toLowerCase().equals("https")) {

                trustAllHosts();
                HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) mConnectUrl.openConnection();
                httpsUrlConnection.setHostnameVerifier(DO_NOT_VERIFY);
                httpUrlConnection = httpsUrlConnection;

            } else {
                httpUrlConnection = (HttpURLConnection) mConnectUrl.openConnection();
            }

            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            dos = new DataOutputStream(httpUrlConnection.getOutputStream());
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"member_id\"\r\n\r\n" + CommonData.getInstance().getMDMUserId());
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"session_code\"\r\n\r\n" + CommonData.getInstance().getSessionCode());
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"device_type\"\r\n\r\n" + "A");
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"app_ver\"\r\n\r\n" + CommonData.getInstance().getAppVer());
            dos.writeBytes("\r\n--" + boundary + "\r\n");

            Iterator<String> iterator = photoList.keySet().iterator();

            while ( iterator.hasNext() ) {

                String key 		= (String) iterator.next();
                String photo 	= photoList.get(key);

                if ( !photo.startsWith("/") ) {
                    dos.writeBytes("\r\n--" + boundary + "\r\n");
//                    dos.writeBytes("Content-Disposition: form-data; name=\"photo_" + key + "\"\r\n\r\n" + photo);
                    dos.writeBytes("Content-Disposition: form-data; name=\"photo \"\r\n\r\n" + photo);
                }

            }

            iterator = photoList.keySet().iterator();

            while ( iterator.hasNext() ) {

                String key = (String) iterator.next();
                String photo 	= photoList.get(key);

                if ( photo.startsWith("/") ) {

                    FileInputStream mFileInputStream	=	new FileInputStream( photo );

                    // 파일 업로드
                    dos.writeBytes(lineEnd + twoHyphens + boundary + lineEnd);
//                    dos.writeBytes("Content-Disposition: form-data; name=\"photo_" + key + "\";filename=\"" + photo + "\"" + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"photo\";filename=\"" + photo + "\"" + lineEnd);
                    dos.writeBytes("Content-Type: application/octet-stream" + lineEnd + lineEnd);

                    int bytesAvailable = mFileInputStream.available();
                    int maxBufferSize = 1024;
                    int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    byte[] buffer = new byte[bufferSize];
                    int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {
                        DataOutputStream dataWrite = new DataOutputStream(httpUrlConnection.getOutputStream());
                        dataWrite.write(buffer, 0, bufferSize);
                        bytesAvailable = mFileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
                    }
                    mFileInputStream.close();

                }
            }
            dos.writeBytes(lineEnd + twoHyphens + boundary + twoHyphens + lineEnd);

            dos.flush();

            //get response
            responseResultCode = httpUrlConnection.getResponseCode();
            int ch;
            InputStream is = httpUrlConnection.getInputStream();
            StringBuffer b =new StringBuffer();

            while ( ( ch = is.read() ) != -1 ) {
                b.append( (char)ch );
            }

            returnString=b.toString();
            dos.close();
        }
        catch (Exception e)	{
            Log.e(e.toString());
        }
        finally {
            if( httpUrlConnection != null){
                httpUrlConnection.disconnect();
            }
        }

        return returnString.toString();
    }

    /**
     * API 호출
     * @param addr	(도메인)
     * @param param	(Post 파라미터)
     * @return
     */
    public String requestHttpClient(String addr, ArrayList<NameValuePair> param){
        StringBuilder strBuilder = new StringBuilder();

        HttpClient httpClient = getHttpClient();
        httpClient.getParams();
        InputStream is = null;

        try{
            StringBuilder logParam = new StringBuilder();
            for(NameValuePair nvp:param){
                logParam.append("&" +nvp.getName() +"=" +nvp.getValue());
            }
            Log.i("url = " + addr + "?" + logParam);
        }
        catch(Exception e){
            Log.e(e.toString());
        }

        try {

//			URL url = new URL(addr);
            URI uri = new URI(convertDomain(addr));

            HttpPost httpPost = new HttpPost(uri);
            UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(param, "UTF-8");

            httpPost.setEntity(entityRequest);
            // User_agent 사용자 단말 정보 전달
            httpPost.setHeader("USER_AGENT", "Android " + Build.VERSION.RELEASE.toString() +"; "+ Build.MODEL.toString());


            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entityResponse = response.getEntity();
            is = entityResponse.getContent();

            responseResultCode = response.getStatusLine().getStatusCode();
            Log.i("responseResultCode = " +responseResultCode);

            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while((line = bufferReader.readLine()) != null)
            {
                strBuilder.append(line);
                if( line == null) break;
            }
            bufferReader.close();

        }
        catch (Exception e) {
            Log.e(e.toString());
            return "";
        }
        finally {

            httpClient.getConnectionManager().shutdown();
        }

        return strBuilder.toString();
    }

    /**
     * API 호출
     * @param addr	(도메인)
     * @param param	(Post 파라미터)
     * @return
     */
    public String requestHttpClientPostBodyJSON(String addr, ArrayList<NameValuePair> param){
        StringBuilder strBuilder = new StringBuilder();

        HttpClient httpClient = getHttpClient();
        httpClient.getParams();
        InputStream is = null;

        try{
            StringBuilder logParam = new StringBuilder();
            for(NameValuePair nvp:param){
                logParam.append("&" +nvp.getName() +"=" +nvp.getValue());
            }
            Log.i("url = " + addr + "?" + logParam);
        }
        catch(Exception e){
            Log.e(e.toString());
        }

        try {
            //URI uri = new URI(URLEncoder.encode(addr));
            URI uri = new URI(convertDomain(addr));
            //URI uri = new URI("https", "dsm_test.dbins.net", "/dsm/zcommon/callDoAppLogin.do", null, null);

            HttpPost httpPost = new HttpPost(uri);
            JSONObject idsJsonObject = new JSONObject();

            if(param != null && param.size() > 0) {
                for (NameValuePair nvp : param) {
                    try {
                        if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_API) {
                            idsJsonObject.put(nvp.getName(), CommonData.getInstance().encode(nvp.getValue()));
                        } else {
                            idsJsonObject.put(nvp.getName(), nvp.getValue());
                        }

                    } catch (JSONException e) {
                        if(Config.DISPLAY_LOG) e.printStackTrace();
                    }
                }
            }

            String JSONEn = idsJsonObject.toString();
//            if(Config.ENABLE_ENCRYTO) {
//                JSONEn = CommonData.getInstance().encode(idsJsonObject.toString());
//            }
            httpPost.setEntity(new StringEntity(JSONEn, "UTF8"));
            httpPost.setHeader("Content-type", "application/json");

//            StringEntity se = new StringEntity( "JSON: " + json.toString());
//            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//            httpPost.setEntity(se);

            // User_agent 사용자 단말 정보 전달
            httpPost.setHeader("USER_AGENT", "Android " + Build.VERSION.RELEASE.toString() +"; "+ Build.MODEL.toString());

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entityResponse = response.getEntity();
            is = entityResponse.getContent();

            responseResultCode = response.getStatusLine().getStatusCode();
            Log.i("responseResultCode = " +responseResultCode);

            //Header headerJsessionID = response.getFirstHeader("Set-Cookie");
            Header [] headers = response.getAllHeaders();
            if(headers.length > 0) {
                for(int i = 0; i < headers.length; i++) {
                    Header headerJsessionID = headers[i];
                    if(CommonData.isSaveSession && headerJsessionID != null && headerJsessionID.getValue() != null) {
                        if(headerJsessionID.getValue().contains("JSESSIONID=")) {
                            String hArr [] = headerJsessionID.getValue().split(";");
                            if(hArr != null && hArr.length > 0) {
                                String jSessionID = hArr[0].replace("JSESSIONID=", "");
                                CacheUtils.getInstance().put(CommonData.JSON_LOGIN_RES_JSESSIONID, headerJsessionID.toString().replace("Set-Cookie: ", ""));

                                Log.i("================================================");
                                Log.i("JSessionID header = " + headerJsessionID.getValue());
                                Log.i("JSessionID = " + CacheUtils.getInstance().getString(CommonData.JSON_LOGIN_RES_JSESSIONID));
                                Log.i("================================================");
                            }
                        }
                    }
                }
            }

            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            while((line = bufferReader.readLine()) != null)
            {
                strBuilder.append(line);
                if( line == null) break;
            }
            bufferReader.close();

        }
        catch (Exception e) {
            Log.e(e.toString());
            return "";
        }
        finally {

            httpClient.getConnectionManager().shutdown();
        }

        String dec = strBuilder.toString();
        if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_API) {
            dec = CommonData.getInstance().decode(strBuilder.toString());
        }

        return dec;
    }

    public void saveCookie( HttpURLConnection conn)
    {
        if(CommonData.isSaveSession) {
            String m_cookies = "";
            boolean m_session = false;
            Map<String, List<String>> imap = conn.getHeaderFields( ) ;
            if( imap.containsKey( "Set-Cookie" ) )
            {
                List<String> lString = imap.get( "Set-Cookie" ) ;
                for( int i = 0 ; i < lString.size() ; i++ ) {
                    if(lString.get( i ).contains("JSESSIONID=")) {
                        m_cookies += lString.get( i ) ;
                    }
                }
                Log.e(m_cookies);

                CacheUtils.getInstance().put(CommonData.JSON_LOGIN_RES_JSESSIONID, m_cookies);

                Log.i("================================================");
                Log.i("JSessionID header = " + m_cookies);
                Log.i("JSessionID = " + CacheUtils.getInstance().getString(CommonData.JSON_LOGIN_RES_JSESSIONID));
                Log.i("================================================");

                m_session = true ;
            } else {
                m_session = false ;
            }
        }
    }

    /**
     * HTTPS API 호출
     * @param addr	(도메인)
     * @param param	(Post 파라미터)
     * @return
     */
    public String requestHttpsClientPostBodyJSON(String addr, ArrayList<NameValuePair> param){
        URL url = null;
        HttpURLConnection connection = null;
        String returnString = null;
        StringBuilder responseStringBuilder = new StringBuilder();

        try {
            url = new URL(convertDomain(addr));
        } catch (MalformedURLException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        Log.d(addr);

        try {
            trustAllHosts();
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            connection = httpsURLConnection;

            byte[] bytes = getURLQueryToJSON(param).getBytes("UTF-8");

            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setFixedLengthStreamingMode(bytes.length);
            //connection.setRequestProperty("Authorization", tokenType + " "+ accessToken);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            OutputStream os = connection.getOutputStream();
            os.write(bytes);
            os.close();

            connection.connect();

            //get response
            responseResultCode = connection.getResponseCode();

            // 로그인 호출 후 cookie 저장
            saveCookie(connection);

            int ch;
            InputStream is = connection.getInputStream();
            StringBuffer b =new StringBuffer();

            while ( ( ch = is.read() ) != -1 ) {
                b.append( (char)ch );
            }

            returnString = new String(b.toString().getBytes("ISO-8859-1"), "UTF-8");

        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } finally {
            if( connection != null){
                connection.disconnect();
            }
        }
        return returnString;
    }

    /**
     * HTTPS API 호출
     * @param addr	(도메인)
     * @param param	(Post 파라미터)
     * @return
     */
    public String requestHttpsClientPostBodyJSON_En(String addr, ArrayList<NameValuePair> param){
        URL url = null;
        HttpURLConnection connection = null;
        String returnString = null;
        StringBuilder responseStringBuilder = new StringBuilder();

        try {
            url = new URL(convertDomain(addr));
        } catch (MalformedURLException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        try {
            trustAllHosts();
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            connection = httpsURLConnection;

            byte[] bytes = getURLQueryToJSON_En(param).getBytes("UTF-8");

            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setFixedLengthStreamingMode(bytes.length);
            //connection.setRequestProperty("Authorization", tokenType + " "+ accessToken);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            OutputStream os = connection.getOutputStream();
            os.write(bytes);
            os.close();

            connection.connect();

            //get response
            responseResultCode = connection.getResponseCode();

            // 로그인 호출 후 cookie 저장
            saveCookie(connection);

            int ch;
            InputStream is = connection.getInputStream();
            StringBuffer b =new StringBuffer();

            while ( ( ch = is.read() ) != -1 ) {
                b.append( (char)ch );
            }

            String returnStringEn = b.toString();

            returnString = returnStringEn;
            if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_API) {
                returnString = CommonData.getInstance().decode(returnStringEn);
            }

//            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
//
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                for (;;){
//                    String stringLine = bufferedReader.readLine();
//                    if (stringLine == null ) break;
//                    responseStringBuilder.append(stringLine + '\n');
//                }
//                bufferedReader.close();
//            }
//            connection.disconnect();
//
//            //Log.d(responseStringBuilder.toString());
//            return responseStringBuilder.toString();

        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } finally {
            if( connection != null){
                connection.disconnect();
            }
        }
        return returnString;
    }


    private String getURLQuery(List<BasicNameValuePair> params){

        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;

        for (BasicNameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                stringBuilder.append("&");
            try {
                stringBuilder.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                if(Config.DISPLAY_LOG) e.printStackTrace();
            }
        }
        return stringBuilder.toString();

    }

    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
    }

    private String getURLQueryToJSON(List<NameValuePair> params){

        JSONObject idsJsonObject = new JSONObject();

        if(params != null && params.size() > 0) {
            for (NameValuePair nvp : params) {
                try {
                    idsJsonObject.put(nvp.getName(), nvp.getValue());
                } catch (JSONException e) {
                    if(Config.DISPLAY_LOG) e.printStackTrace();
                }
            }
        }

        return idsJsonObject.toString();
    }

    private String getURLQueryToJSON_En(List<NameValuePair> params){

        JSONObject idsJsonObject = new JSONObject();

        if(params != null && params.size() > 0) {
            for (NameValuePair nvp : params) {
                try {
                    if(Config.ENABLE_ENCRYTO && Config.ENABLE_ENCRYTO_API) {
                        idsJsonObject.put(nvp.getName(), CommonData.getInstance().encode(nvp.getValue()));
                    } else {
                        idsJsonObject.put(nvp.getName(), nvp.getValue());
                    }

                } catch (JSONException e) {
                    if(Config.DISPLAY_LOG) e.printStackTrace();
                }
            }
        }

        String JSONEn = idsJsonObject.toString();
//        if(Config.ENABLE_ENCRYTO) {
//            JSONEn = CommonData.getInstance().encode(idsJsonObject.toString());
//        }

        return JSONEn;

    }

    private HttpClient getHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SFSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            Log.e(e.toString());
            return new DefaultHttpClient();
        }
    }

    public String convertDomain(String domain) {
        if(domain.contains("_")) {
            domain.replace("_", "&#95;");
        }

        return domain;
    }

//    private void trustAllHosts()
//    {
//
//        // 2016-06-15 기존 TrushManager 주석
//        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
//        {
//            public java.security.cert.X509Certificate[] getAcceptedIssuers()
//            {
//                return new java.security.cert.X509Certificate[] {};
//            }
//
//            @Override
//            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
//                    throws java.security.cert.CertificateException {}
//
//            @Override
//            public void checkServerTrusted( java.security.cert.X509Certificate[] chain, String authType)
//                    throws java.security.cert.CertificateException {}
//        } };
//
//        try
//        {
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
//        } catch (Exception e) { if(Config.DISPLAY_LOG) e.printStackTrace(); }
//
//    }
//
//    private final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier()
//    {
//        @Override
//        public boolean verify(String arg0, SSLSession arg1)
//        {
//            return true;
//        }
//    };

}
