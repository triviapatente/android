package com.ted_developers.triviapatente.app.utils;

import android.app.Application;

import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.socket.modules.base.BaseSocketManager;

/**
 * Created by Antonio on 22/10/16.
 */
public class mApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // init retrofit manager
        RetrofitManager.init(this);
        BaseSocketManager.init(this);
        SharedTPPreferences.init(this);
    }
}
