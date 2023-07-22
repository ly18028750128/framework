package org.cloud.utils.http;

import com.alibaba.fastjson.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
public final class OKHttpClientBuilder {

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
            sslContext.init(null, trustAllCerts, new SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            return buildOKHttpClient(sslSocketFactory);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            log.error(e.getMessage(), e);
            return new OkHttpClient.Builder();
        }
    }

    private TrustManager[] buildTrustManagers() {
        return new TrustManager[]{new X509TrustManager() {
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
        }};
    }

    public OkHttpClient.Builder buildOKHttpClient(final String trustCertsType, InputStream inputStream, final String trustCertsPassword,
        final String tlsVersion) throws Exception {
        try {
            KeyStore keyStore = KeyStore.getInstance(trustCertsType);
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

    public OkHttpClient.Builder buildOKHttpClient(final String trustCertsType, InputStream inputStream, final String trustCertsPassword) throws Exception {
        return this.buildOKHttpClient(trustCertsType, inputStream, trustCertsPassword, "TLSv1");
    }

    public OkHttpClient.Builder buildOKHttpClient(final String trustCertsType, byte[] bytes, final String trustCertsPassword, final String tlsVersion)
        throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        return this.buildOKHttpClient(trustCertsType, inputStream, trustCertsPassword, tlsVersion);
    }

    public OkHttpClient.Builder buildOKHttpClient(final String trustCertsType, byte[] bytes, final String trustCertsPassword) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        return this.buildOKHttpClient(trustCertsType, bytes, trustCertsPassword, "TLSv1");
    }

    public OkHttpClient.Builder buildOKHttpClient(final String trustCertsType, final String trustCerts, final String trustCertsPassword,
        final String tlsVersion) throws Exception {
        return this.buildOKHttpClient(trustCertsType, trustCerts.getBytes(), trustCertsPassword, tlsVersion);

    }

    public OkHttpClient.Builder buildOKHttpClient(final String trustCertsType, final String trustCerts, final String trustCertsPassword) throws Exception {
        return this.buildOKHttpClient(trustCertsType, trustCerts, trustCertsPassword, "TLSv1");
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
