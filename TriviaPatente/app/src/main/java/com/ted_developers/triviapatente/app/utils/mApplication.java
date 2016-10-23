package com.ted_developers.triviapatente.app.utils;

import android.app.Application;

import com.ted_developers.triviapatente.http.utils.RetrofitManager;

/**
 * Created by Antonio on 22/10/16.
 */
public class mApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitManager.init(getBaseContext());
    }
}
