package it.triviapatente.android.app.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by donadev on 14/02/18.
 */

public class Foreground implements Application.ActivityLifecycleCallbacks {
    private static Foreground instance;

    public static void init(Application app){
        if (instance == null){
            instance = new Foreground();
            app.registerActivityLifecycleCallbacks(instance);
        }
    }

    public static Foreground get(){
        return instance;
    }
    public static boolean isForeground() {
        return instance.foreground;
    }
    public static boolean isBackground(){
        return !isForeground();
    }

    private Foreground(){}

    private boolean foreground;




    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        foreground = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        foreground = false;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
