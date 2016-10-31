package com.ted_developers.triviapatente.app.utils.custom_classes.top_bar;

import android.content.Context;
import android.util.AttributeSet;

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
}
