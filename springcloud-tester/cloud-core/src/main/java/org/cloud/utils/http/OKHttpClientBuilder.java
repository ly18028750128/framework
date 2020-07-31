package org.cloud.utils.http;

import com.alibaba.fastjson.util.IOUtils;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

public final class OKHttpClientBuilder {
    Logger logger = LoggerFactory.getLogger(OKHttpClientBuilder.class);

    private OKHttpClientBuilder() {
    }

    private final static OKHttpClientBuilder instance = new OKHttpClientBuilder();

    public static OKHttpClientBuilder single() {
        return instance;
    }

    public OkHttpClient.Builder buildOKHttpClient() {
        try {
            TrustManager[] trustAllCerts = buildTrustManagers();
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            return buildOKHttpClient(sslSocketFactory);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            logger.error(e.getMessage(), e);
            return new OkHttpClient.Builder();
        }
    }

    private TrustManager[] buildTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
    }

    public OkHttpClient.Builder buildOKHttpClient(final String trustCertsType, final String trustCertsPath, final String trustCertsPassword) throws Exception {
        return this.buildOKHttpClient(trustCertsType, trustCertsPath, trustCertsPassword, "TLSv1");
    }

    public OkHttpClient.Builder buildOKHttpClient(final String trustCertsType, final String trustCertsPath, final String trustCertsPassword, final String tlsVersion) throws Exception {
        InputStream inputStream = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(trustCertsType);
            inputStream = getClass().getClassLoader().getResourceAsStream(trustCertsPath);
            keyStore.load(inputStream, trustCertsPassword.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, trustCertsPassword.toCharArray());
            final SSLContext sslContext = SSLContext.getInstance(tlsVersion);
            sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            return buildOKHttpClient(sslSocketFactory);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            return new OkHttpClient.Builder();
        } finally {
            IOUtils.close(inputStream);
        }
    }

    public OkHttpClient.Builder buildOKHttpClient(SSLSocketFactory sslSocketFactory) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        TrustManager[] trustAllCerts = buildTrustManagers();
        builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        builder.hostnameVerifier((hostname, session) -> true);
        builder.callTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(30L, TimeUnit.SECONDS);//30秒
        builder.readTimeout(10L, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        return builder;
    }
}
