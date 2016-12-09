package com.ted_developers.triviapatente.app.utils.custom_classes.actionBar;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Antonio on 12/11/16.
 */
public class BackPictureSettingsTPToolbar extends TPToolbar {

    public BackPictureSettingsTPToolbar(Context context) {
        super(context);
        init(context);
    }

    public BackPictureSettingsTPToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BackPictureSettingsTPToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BackPictureSettingsTPToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        // hide heart box
        heartsBox.setVisibility(GONE);
    }
}
