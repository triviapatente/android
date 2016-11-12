package com.ted_developers.triviapatente.app.utils.custom_classes.top_bar;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Antonio on 12/11/16.
 */
public class BackPictureTPToolbar extends TPToolbar {
    public BackPictureTPToolbar(Context context) {
        super(context);
        init(context);
    }

    public BackPictureTPToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BackPictureTPToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BackPictureTPToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        // hide heart box
        heartsBox.setVisibility(GONE);
        // hide settings
        settings.setVisibility(GONE);
    }
}
