package com.dongbu.dsm.network;

/**
 * Created by 81700905 on 2017-10-20.
 */

import com.dongbu.dsm.Config;
import com.dongbu.dsm.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLConnect {
    // always verify the host - dont check for certificate
    final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    };

    /**
     * Trust every server - don't check for any certificate
     */
    private void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.d("[checkClientTrusted] ");
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.d("[checkServerTrusted] ");
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
        }
    }


    public HttpsURLConnection postHttps(String url, int connTimeout, int readTimeout) {
        trustAllHosts();

        HttpsURLConnection https = null;
        try {
            https = (HttpsURLConnection) new URL(url).openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            https.setConnectTimeout(connTimeout);
            https.setReadTimeout(readTimeout);
        } catch (MalformedURLException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
            return null;
        } catch (IOException e) {
            if(Config.DISPLAY_LOG) e.printStackTrace();
            return null;
        }
        return https;
    }
}


