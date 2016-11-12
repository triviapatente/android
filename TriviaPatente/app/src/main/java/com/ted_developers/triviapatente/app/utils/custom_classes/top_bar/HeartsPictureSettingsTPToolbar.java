package com.ted_developers.triviapatente.app.utils.custom_classes.top_bar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.ted_developers.triviapatente.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Antonio on 31/10/16.
 */
public class HeartsPictureSettingsTPToolbar extends TPToolbar {

    public HeartsPictureSettingsTPToolbar(Context context) {
        super(context);
        init(context);
    }

    public HeartsPictureSettingsTPToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeartsPictureSettingsTPToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public HeartsPictureSettingsTPToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        // hide back button
        backButton.setVisibility(GONE);
    }

    // todo do better
    private Timer timer;
    public void startTimer(final Activity activity) {
        if(timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private int sec = 300;
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setHeartImage();
                        setLifeCounter("2");
                        setHeartsTimer(sec--);
                    }
                });
            }
        }, 0, 1000);
    }
}
