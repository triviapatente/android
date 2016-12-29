package com.ted_developers.triviapatente.app.utils.custom_classes.buttons;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.ted_developers.triviapatente.R;

/**
 * Created by Antonio on 29/12/16.
 */
public enum ui_data {
    _play_now(R.drawable.green_on_white, R.color.green_on_white, R.string.play_now_button_text),
    _new_game(R.drawable.red_on_white_button, R.color.red, R.string.new_game_button_text),
    _summary(R.drawable.red_on_white_button, R.color.red, R.string.summary_button_text),
    _contact(R.drawable.grey_on_white_button, R.color.greyDark, R.string.contact_button_text),
    _details(R.drawable.yellow_on_white_button, R.color.yellow, R.string.details_button_text);

    private static boolean initialized = false;
    private @ColorRes
    int colorRes;
    private @DrawableRes
    int drawableRes;
    private @StringRes
    int stringRes;
    private Drawable drawable;
    private ColorStateList colors;
    private String string;

    ui_data(@DrawableRes int drawableRes, @ColorRes int colorRes, @StringRes int stringRes) {
        this.colorRes = colorRes;
        this.drawableRes = drawableRes;
        this.stringRes = stringRes;
    }

    public static void init(Context context) {
        if(!initialized) {
            for(ui_data data : ui_data.values()) {
                data.string = context.getString(data.stringRes);
                data.drawable = ContextCompat.getDrawable(context, data.drawableRes);
                data.colors = ContextCompat.getColorStateList(context, data.colorRes);
            }
            initialized = true;
        }
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public ColorStateList getColor() {
        return colors;
    }

    public String getString() {
        return string;
    }

}