package it.triviapatente.android.http.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.SharedTPPreferences;
import it.triviapatente.android.http.modules.auth.HTTPAuthEndpoint;
import it.triviapatente.android.http.modules.base.HTTPBaseEndpoint;
import it.triviapatente.android.http.modules.game.HTTPGameEndpoint;
import it.triviapatente.android.http.modules.message.HTTPMessageEndpoint;
import it.triviapatente.android.http.modules.preferences.HTTPPreferencesEndpoint;
import it.triviapatente.android.http.modules.purchases.HTTPPurchasesEndpoint;
import it.triviapatente.android.http.modules.rank.HTTPRankEndpoint;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import it.triviapatente.android.http.modules.stats.HTTPStatsEndpoint;
import it.triviapatente.android.http.modules.training.HTTPTrainingEndpoint;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Antonio on 22/10/16.
 */
public class RetrofitManager {
    public static Gson gson = null;
    private static Retrofit retrofit = null;
    public static Context context = null;

    public static final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    public static final X509TrustManager trustAll = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };
    public static final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
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
    public static SSLContext getSSLContext() {
        try {
            SSLContext mySSLContext = SSLContext.getInstance("SSL");
            mySSLContext.init(null, RetrofitManager.trustAllCerts, new SecureRandom());
            return mySSLContext;
        } catch (Exception e) {
            return null;
        }
    }

    // method called only one time on mApplication on create
    public static void init(Context c) {
        context = c;
        gson = new GsonBuilder().create();
        GsonConverterFactory factory = GsonConverterFactory.create(gson);
        // interceptor which adds token in header
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.hostnameVerifier(hostnameVerifier);
        httpClient.sslSocketFactory(getSSLContext().getSocketFactory(), trustAll);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header(context.getString(R.string.shared_token_key), SharedTPPreferences.getToken())
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpClient.readTimeout(5, TimeUnit.SECONDS).connectTimeout(5, TimeUnit.SECONDS).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.baseUrl))
                .addConverterFactory(factory)
                .client(client)
                .build();
    }

    private static HTTPAuthEndpoint httpAuthEndpoint;
    public static HTTPAuthEndpoint getHTTPAuthEndpoint() {
        if (httpAuthEndpoint == null) { httpAuthEndpoint = retrofit.create(HTTPAuthEndpoint.class); }
        return httpAuthEndpoint;
    }

    private static HTTPGameEndpoint httpGameEndpoint;
    public static HTTPGameEndpoint getHTTPGameEndpoint() {
        if (httpGameEndpoint == null) { httpGameEndpoint = retrofit.create(HTTPGameEndpoint.class); }
        return httpGameEndpoint;
    }

    private static HTTPMessageEndpoint httpMessageEndpoint;
    public static HTTPMessageEndpoint getHTTPMessageEndpoint() {
        if (httpMessageEndpoint == null) { httpMessageEndpoint = retrofit.create(HTTPMessageEndpoint.class); }
        return httpMessageEndpoint;
    }

    private static HTTPPreferencesEndpoint httpPreferencesEndpoint;
    public static HTTPPreferencesEndpoint getHTTPPreferencesEndpoint() {
        if (httpPreferencesEndpoint == null) { httpPreferencesEndpoint = retrofit.create(HTTPPreferencesEndpoint.class); }
        return httpPreferencesEndpoint;
    }

    private static HTTPPurchasesEndpoint httpPurchasesEndpoint;
    public static HTTPPurchasesEndpoint getHTTPPurchasesEndpoint() {
        if (httpPurchasesEndpoint == null) { httpPurchasesEndpoint = retrofit.create(HTTPPurchasesEndpoint.class); }
        return httpPurchasesEndpoint;
    }
    private static HTTPStatsEndpoint httpStatsEndpoint;
    public static HTTPStatsEndpoint getHTTPStatsEndpoint() {
        if (httpStatsEndpoint == null) { httpStatsEndpoint = retrofit.create(HTTPStatsEndpoint.class); }
        return httpStatsEndpoint;
    }

    private static HTTPRankEndpoint httpRankEndpoint;
    public static HTTPRankEndpoint getHTTPRankEndpoint() {
        if (httpRankEndpoint == null) { httpRankEndpoint = retrofit.create(HTTPRankEndpoint.class); }
        return httpRankEndpoint;
    }

    private static HTTPBaseEndpoint httpBaseEndpoint;
    public static HTTPBaseEndpoint getHTTPBaseEndpoint() {
        if (httpBaseEndpoint == null) { httpBaseEndpoint = retrofit.create(HTTPBaseEndpoint.class); }
        return httpBaseEndpoint;
    }
    private static HTTPTrainingEndpoint httpTrainingEndpoint;
    public static HTTPTrainingEndpoint getHttpTrainingEndpoint() {
        if (httpTrainingEndpoint == null) { httpTrainingEndpoint = retrofit.create(HTTPTrainingEndpoint.class); }
        return httpTrainingEndpoint;
    }
}
