package cn.aulang.office.web.factory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-19 22:12
 */
public class HttpsUrlConnectionFactory {
    private static SSLSocketFactory sslSocketFactory() throws Exception {
        return sslContext().getSocketFactory();
    }

    private static SSLContext sslContext() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("SSL");

        TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
        return sslContext;
    }

    private static HostnameVerifier hostnameVerifier() {
        return (hostname, sslSession) -> true;
    }

    static {
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier());
        try {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HttpURLConnection create(String strUrl) throws IOException {
        URL url = new URL(strUrl);
        return (HttpURLConnection) url.openConnection();
    }
}
