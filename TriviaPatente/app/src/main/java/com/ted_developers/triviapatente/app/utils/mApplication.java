package com.ted_developers.triviapatente.app.utils;

import android.app.Application;
import android.view.View;

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

    public static boolean isPointInsideView(float x, float y, View view){
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        //point is inside view bounds
        if(( x > viewX && x < (viewX + view.getWidth())) &&
                ( y > viewY && y < (viewY + view.getHeight()))){
            return true;
        } else {
            return false;
        }
    }
}
