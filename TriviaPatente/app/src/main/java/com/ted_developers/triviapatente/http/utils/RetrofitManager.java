package com.ted_developers.triviapatente.http.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.utils.mApplication;
import com.ted_developers.triviapatente.http.modules.auth.HTTPAuthEndpoint;
import com.ted_developers.triviapatente.http.modules.game.HTTPGameEndpoint;
import com.ted_developers.triviapatente.http.modules.message.HTTPMessageEndpoint;
import com.ted_developers.triviapatente.http.modules.preferences.HTTPPreferencesEndpoint;
import com.ted_developers.triviapatente.http.modules.purchases.HTTPPurchasesEndpoint;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HTTP;

/**
 * Created by Antonio on 22/10/16.
 */
public class RetrofitManager {
    private static Retrofit retrofit = null;
    public static Context context = null;
    // method called only one time on mApplication on create
    public static void init(Context c) {
        context = c;
        // interceptor which adds token in header
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                SharedPreferences auth = RetrofitManager.context.getSharedPreferences(context.getString(R.string.shared_auth), Context.MODE_PRIVATE);
                String token_name = context.getString(R.string.token_name);
                Request request = original.newBuilder()
                        .header(token_name, auth.getString(token_name, ""))
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpClient.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.baseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static HTTPAuthEndpoint getHTTPAuthEndpoint() {
        return retrofit.create(HTTPAuthEndpoint.class);
    }

    public static HTTPGameEndpoint getHTTPGameEndpoint() {
        return retrofit.create(HTTPGameEndpoint.class);
    }

    public static HTTPMessageEndpoint getHTTPMessageEndpoint() {
        return retrofit.create(HTTPMessageEndpoint.class);
    }

    public static HTTPPreferencesEndpoint getHTTPPreferencesEndpoint() {
        return retrofit.create(HTTPPreferencesEndpoint.class);
    }

    public static HTTPPurchasesEndpoint getHTTPPurchasesEndpoint() {
        return retrofit.create(HTTPPurchasesEndpoint.class);
    }


}
