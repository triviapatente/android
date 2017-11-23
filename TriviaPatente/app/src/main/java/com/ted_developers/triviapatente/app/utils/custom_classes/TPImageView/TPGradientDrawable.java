package com.ted_developers.triviapatente.app.utils.custom_classes.TPImageView;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;

import com.ted_developers.triviapatente.R;

/**
 * Created by donadev on 23/11/17.
 */

public class TPGradientDrawable extends GradientDrawable {
    public TPGradientDrawable(Context ctx) {
        int[] colors = new int[] {ContextCompat.getColor(ctx, R.color.mainColorGradTop), ContextCompat.getColor(ctx, R.color.mainColor)};
        this.setColors(colors);
    }
}