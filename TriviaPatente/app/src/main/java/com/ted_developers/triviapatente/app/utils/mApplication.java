package com.ted_developers.triviapatente.app.utils;

import android.app.Application;
import android.content.Intent;

import com.ted_developers.triviapatente.R;
import com.ted_developers.triviapatente.app.views.access.FirstAccessActivity;
import com.ted_developers.triviapatente.http.utils.RetrofitManager;
import com.ted_developers.triviapatente.socket.modules.base.BaseSocketManager;

/**
 * Created by Antonio on 22/10/16.
 */
public class mApplication extends Application {
    private static mApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        // app
        app = this;
        // fonts
        TPUtils.setDefaultFont(this, "MONOSPACE", "fonts/OpenSans-Regular.ttf");
        // init
        RetrofitManager.init(this);
        TPUtils.initPicasso(this);
        BaseSocketManager.init(this);
        SharedTPPreferences.init(this);
    }

    public static mApplication getInstance() {
        return app;
    }

    public void goToLoginPage() {
        Intent intent = new Intent(this, FirstAccessActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(getResources().getString(R.string.is_old_session), true);
        startActivity(intent);
    }
}
