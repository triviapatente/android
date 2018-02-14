package it.triviapatente.android.app.utils;

import android.app.Application;
import android.content.Intent;

import com.google.android.gms.ads.MobileAds;
import it.triviapatente.android.R;
import it.triviapatente.android.app.utils.custom_classes.input.LabeledInputError;
import it.triviapatente.android.app.views.access.FirstAccessActivity;
import it.triviapatente.android.http.utils.RetrofitManager;
import it.triviapatente.android.socket.modules.base.BaseSocketManager;

/**
 * Created by Antonio on 22/10/16.
 */
public class mApplication extends Application {
    private static mApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        // ads
        MobileAds.initialize(this, getString(R.string.admob_appid));
        // app
        app = this;
        // fonts
        TPUtils.setDefaultFont(this, "MONOSPACE", "fonts/OpenSans-Regular.ttf");
        // init
        RetrofitManager.init(this);
        TPUtils.initPicasso(this);
        BaseSocketManager.init(this);
        SharedTPPreferences.init(this);
        LabeledInputError.initAll(this);
        Foreground.init(this);
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
