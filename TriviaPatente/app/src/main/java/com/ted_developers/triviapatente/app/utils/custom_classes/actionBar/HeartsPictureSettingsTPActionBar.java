package com.ted_developers.triviapatente.app.utils.custom_classes.actionBar;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Antonio on 31/10/16.
 */
public class HeartsPictureSettingsTPActionBar extends TPActionBar {

    public HeartsPictureSettingsTPActionBar(Context context) {
        super(context);
        init(context);
    }

    public HeartsPictureSettingsTPActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeartsPictureSettingsTPActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public HeartsPictureSettingsTPActionBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        // hide back button
        backButton.setVisibility(GONE);
    }
}
